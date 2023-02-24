package com.project.note.service;

import com.project.note.dto.GetAuthenticationTokenRequest;
import com.project.note.dto.UserCredentialDto;
import com.project.note.exception.AuthenticationException;
import com.project.note.exception.InvalidAuthenticationTokenException;
import com.project.note.exception.UnverifiedAccountException;
import com.project.note.exception.ValidationException;

public interface AuthenticationTokenService {
    String getAuthenticationToken(GetAuthenticationTokenRequest request) throws AuthenticationException, ValidationException, UnverifiedAccountException;

    UserCredentialDto getUserCredentialFromToken(String token) throws AuthenticationException, InvalidAuthenticationTokenException;
}
