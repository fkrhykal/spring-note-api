package com.project.note.controller;

import com.project.note.dto.AccountDto;
import com.project.note.dto.RegisterAccountRequest;
import com.project.note.dto.SuccessResponse;
import com.project.note.dto.VerifyAccountRequest;
import com.project.note.exception.ValidationException;
import com.project.note.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<SuccessResponse<AccountDto>> registerAccount(@RequestBody RegisterAccountRequest body) throws ValidationException {
        return SuccessResponse.ok(accountService.registerAccount(body)).toResponseEntity();
    }

    @PostMapping("/verification")
    public ResponseEntity<SuccessResponse<AccountDto>> verifyAccount(@RequestBody VerifyAccountRequest body) throws Exception {
        return SuccessResponse.ok(accountService.verifyAccount(body)).toResponseEntity();
    }
}
