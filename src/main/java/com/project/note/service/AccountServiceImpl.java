package com.project.note.service;

import com.project.note.dto.*;
import com.project.note.entity.Account;
import com.project.note.entity.AccountStatus;
import com.project.note.event.AccountRegisteredEvent;
import com.project.note.event.PasswordChangedEvent;
import com.project.note.event.ResetPasswordCodeCreatedEvent;
import com.project.note.exception.InvalidResetPasswordCodeException;
import com.project.note.exception.InvalidVerificationCodeException;
import com.project.note.exception.ResourceNotFoundException;
import com.project.note.exception.ValidationException;
import com.project.note.repository.AccountRepository;
import com.project.note.util.VerificationCodeUtil;
import com.project.note.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final ValidationUtil validationUtil;
    private final VerificationCodeUtil verificationCodeGenerator;

    @Override
    public AccountDto registerAccount(RegisterAccountRequest request) throws ValidationException {

        validationUtil.validate(request);

        Account account = new Account(
                UUID.randomUUID().toString(),
                request.firstname(),
                request.lastname(),
                request.email(),
                passwordEncoder.encode(request.password()),
                verificationCodeGenerator.generateVerificationCode(),
                AccountStatus.UNVERIFIED
        );

        account = accountRepository.save(account);

        eventPublisher.publishEvent(new AccountRegisteredEvent(
                account.getId(),
                account.getFirstname(),
                account.getLastname(),
                account.getEmail(),
                account.getVerificationCode()
        ));

        return accountToDto(account);
    }

    @Override
    public AccountDto verifyAccount(VerifyAccountRequest request) throws ValidationException, ResourceNotFoundException, InvalidVerificationCodeException {

        validationUtil.validate(request);

        Account account = accountRepository
                .findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("account with id " + request.id() + " not found"));

        account.verify(request.verificationCode());

        return accountToDto(accountRepository.save(account));
    }

    @Override
    public AccountDto createResetPasswordCode(CreateResetPasswordCodeRequest request) throws ValidationException, ResourceNotFoundException {

        validationUtil.validate(request);

        Account account = accountRepository.findByEmail(request.email()).orElseThrow(() -> new ResourceNotFoundException("account with id"));

        account.setResetPasswordCode(verificationCodeGenerator.generateVerificationCode());


        account = accountRepository.save(account);

        eventPublisher.publishEvent(new ResetPasswordCodeCreatedEvent(
                account.getFirstname(),
                account.getEmail(),
                account.getResetPasswordCode()
        ));

        return accountToDto(account);
    }

    @Override
    public AccountDto resetPassword(ResetPasswordRequest request) throws ValidationException, InvalidResetPasswordCodeException, ResourceNotFoundException {

        validationUtil.validate(request);

        Account account = accountRepository.findById(request.id()).orElseThrow(() -> new ResourceNotFoundException(""));

        account.resetPassword(request.resetPasswordCode(), request.newPassword());

        account = accountRepository.save(account);

        eventPublisher.publishEvent(new PasswordChangedEvent(
                account.getFirstname(),
                account.getEmail()
        ));

        return accountToDto(account);
    }

    private AccountDto accountToDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getFirstname(),
                account.getLastname(),
                account.getEmail(),
                account.getStatus().toString()
        );
    }
}
