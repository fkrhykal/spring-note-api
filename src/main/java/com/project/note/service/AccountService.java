package com.project.note.service;

import com.project.note.dto.*;
import com.project.note.exception.InvalidResetPasswordCodeException;
import com.project.note.exception.InvalidVerificationCodeException;
import com.project.note.exception.ResourceNotFoundException;
import com.project.note.exception.ValidationException;

public interface AccountService {
    AccountDto registerAccount(RegisterAccountRequest request) throws ValidationException;

    AccountDto verifyAccount(VerifyAccountRequest request) throws InvalidVerificationCodeException, ResourceNotFoundException, ValidationException;

    AccountDto createResetPasswordCode(CreateResetPasswordCodeRequest request) throws ValidationException, ResourceNotFoundException;

    AccountDto resetPassword(ResetPasswordRequest request) throws ValidationException, InvalidResetPasswordCodeException, ResourceNotFoundException;
}
