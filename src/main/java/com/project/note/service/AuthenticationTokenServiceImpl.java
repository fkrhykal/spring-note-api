package com.project.note.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.note.dto.GetAuthenticationTokenRequest;
import com.project.note.dto.UserCredentialDto;
import com.project.note.entity.Account;
import com.project.note.entity.AccountStatus;
import com.project.note.exception.AuthenticationException;
import com.project.note.exception.InvalidAuthenticationTokenException;
import com.project.note.exception.UnverifiedAccountException;
import com.project.note.exception.ValidationException;
import com.project.note.repository.AccountRepository;
import com.project.note.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class AuthenticationTokenServiceImpl implements AuthenticationTokenService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtil validationUtil;
    private final ObjectMapper objectMapper;
    private Algorithm algorithm;
    @Value("${application.secret-key}")
    private String secretKey;

    public AuthenticationTokenServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder, ValidationUtil validationUtil) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.validationUtil = validationUtil;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        this.objectMapper = objectMapper;
    }

    @Override
    public String getAuthenticationToken(GetAuthenticationTokenRequest request) throws AuthenticationException, ValidationException, UnverifiedAccountException {

        validationUtil.validate(request);

        Account account = accountRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("Invalid Credential"));

        if (account.getStatus().equals(AccountStatus.UNVERIFIED)) {
            throw new UnverifiedAccountException();
        }

        if (!passwordEncoder.matches(request.password(), account.getPassword())) {
            throw new AuthenticationException();
        }

        return generateToken(
                new UserCredentialDto(
                        account.getId(),
                        account.getFirstname(),
                        account.getLastname(),
                        account.getEmail()
                )
        );
    }

    @Override
    public UserCredentialDto getUserCredentialFromToken(String token) throws InvalidAuthenticationTokenException {
        JWTVerifier jwtVerifier = JWT.require(getAlgorithm()).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return payloadToUserCredential(decodedJWT.getPayload());
    }

    private String generateToken(UserCredentialDto userCredentialDto) {
        @SuppressWarnings("unchecked")
        Map<String, ?> payload = objectMapper.convertValue(userCredentialDto, Map.class);

        return JWT.create()
                .withPayload(payload)
                .withExpiresAt(Instant.now().plus(Duration.ofDays(30L)))
                .sign(getAlgorithm());
    }

    private UserCredentialDto payloadToUserCredential(String payload) throws InvalidAuthenticationTokenException {
        try {
            String jsonPayload = decodeToJsonPayload(payload);
            return objectMapper.readValue(jsonPayload, UserCredentialDto.class);
        } catch (JsonProcessingException exception) {
            throw new InvalidAuthenticationTokenException("Invalid Authentication Token");
        }
    }

    private Algorithm getAlgorithm() {
        if (algorithm == null) {
            algorithm = Algorithm.HMAC256(secretKey);
        }
        return algorithm;
    }

    private String decodeToJsonPayload(String payload) {
        return new String(Base64.getDecoder().decode(payload));
    }
}