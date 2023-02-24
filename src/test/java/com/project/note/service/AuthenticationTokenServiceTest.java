package com.project.note.service;

import com.project.note.dto.GetAuthenticationTokenRequest;
import com.project.note.entity.Account;
import com.project.note.entity.AccountStatus;
import com.project.note.exception.AuthenticationException;
import com.project.note.exception.UnverifiedAccountException;
import com.project.note.exception.ValidationException;
import com.project.note.repository.AccountRepository;
import com.project.note.util.VerificationCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
public class AuthenticationTokenServiceTest {
    @Autowired
    private AuthenticationTokenService authenticationTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationCodeUtil verificationCodeUtil;
    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void shouldGenerateTokenFromUserCredential() throws ValidationException, AuthenticationException, UnverifiedAccountException {

        String firstname = "John";
        String lastname = "Doe";
        String email = "abc@example.com";
        String password = "secret";
        String hashedPassword = passwordEncoder.encode(password);

        when(accountRepository.findByEmail(email)).thenReturn(
                Optional.of(new Account(
                        UUID.randomUUID().toString(),
                        firstname,
                        lastname,
                        email,
                        hashedPassword,
                        verificationCodeUtil.generateVerificationCode(),
                        AccountStatus.VERIFIED
                ))
        );

        String token = authenticationTokenService.getAuthenticationToken(
                new GetAuthenticationTokenRequest(
                        "abc@example.com",
                        "secret"
                )
        );

        assertNotNull(token);
    }

    @Test
    public void whenAccountDidNotExist_throwAuthenticationException() {
        when(accountRepository.findByEmail("abc@example.com")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> {
            authenticationTokenService.getAuthenticationToken(
                    new GetAuthenticationTokenRequest(
                            "abc@example.com",
                            "secret"
                    )
            );
        });
    }

    @Test
    public void whenPasswordIncorrect_throwAuthenticationException() {
        String firstname = "John";
        String lastname = "Doe";
        String email = "abc@example.com";
        String password = "secret";
        String hashedPassword = passwordEncoder.encode(password);


        when(accountRepository.findByEmail("abc@example.com")).thenReturn(
                Optional.of(
                        new Account(
                                UUID.randomUUID().toString(),
                                firstname,
                                lastname,
                                email,
                                hashedPassword,
                                verificationCodeUtil.generateVerificationCode(),
                                AccountStatus.VERIFIED
                        )
                )
        );

        assertThrows(AuthenticationException.class, () -> {
            authenticationTokenService.getAuthenticationToken(
                    new GetAuthenticationTokenRequest(
                            "abc@example.com",
                            "wrong"
                    )
            );
        });
    }

    @Test
    public void whenAccountUnverified_throwUnverifiedAccountException() {
        String firstname = "John";
        String lastname = "Doe";
        String email = "abc@example.com";
        String password = "secret";
        String hashedPassword = passwordEncoder.encode(password);


        when(accountRepository.findByEmail("abc@example.com")).thenReturn(
                Optional.of(
                        new Account(
                                UUID.randomUUID().toString(),
                                firstname,
                                lastname,
                                email,
                                hashedPassword,
                                verificationCodeUtil.generateVerificationCode(),
                                AccountStatus.UNVERIFIED
                        )
                )
        );

        assertThrows(UnverifiedAccountException.class, () -> {
            authenticationTokenService.getAuthenticationToken(
                    new GetAuthenticationTokenRequest(
                            "abc@example.com",
                            "secret"
                    )
            );
        });
    }

    @Test
    public void whenRequestInvalid_throwValidationException() {
        assertThrows(ValidationException.class, () -> {
            authenticationTokenService.getAuthenticationToken(
                    new GetAuthenticationTokenRequest(
                            "",
                            ""
                    )
            );
        });
    }
}
