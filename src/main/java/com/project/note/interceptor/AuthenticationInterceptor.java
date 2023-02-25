package com.project.note.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.project.note.dto.UserCredentialDto;
import com.project.note.exception.AuthenticationException;
import com.project.note.exception.InvalidAuthenticationTokenException;
import com.project.note.service.AuthenticationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationTokenService authenticationTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws AuthenticationException, InvalidAuthenticationTokenException, Exception {
        String token = Optional
                .ofNullable(request.getHeader("X-Api-Key"))
                .orElseThrow(() -> new AuthenticationException("X-Api-Key header required"));
        try {
            UserCredentialDto userCredential = authenticationTokenService.getUserCredentialFromToken(token);
            request.setAttribute("User-Credential", userCredential);
        } catch (JWTVerificationException exception) {
            log.error(exception.getMessage());
            throw new AuthenticationException();
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
