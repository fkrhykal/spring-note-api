package com.project.note.controller;

import com.project.note.dto.GetAuthenticationTokenRequest;
import com.project.note.dto.SuccessResponse;
import com.project.note.exception.AuthenticationException;
import com.project.note.exception.UnverifiedAccountException;
import com.project.note.exception.ValidationException;
import com.project.note.service.AuthenticationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationTokenController {

    private final AuthenticationTokenService authenticationTokenService;

    @GetMapping("/authentication-token")
    public ResponseEntity<SuccessResponse<String>> getAuthenticationToken(@RequestBody GetAuthenticationTokenRequest request) throws AuthenticationException, ValidationException, UnverifiedAccountException {
        return SuccessResponse.ok(authenticationTokenService.getAuthenticationToken(request)).toResponseEntity();
    }
}
