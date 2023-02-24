package com.project.note.service;

import com.project.note.dto.AccountDto;
import com.project.note.dto.RegisterAccountRequest;
import com.project.note.dto.VerifyAccountRequest;
import com.project.note.entity.Account;
import com.project.note.entity.AccountStatus;
import com.project.note.event.AccountRegisteredEvent;
import com.project.note.exception.InvalidVerificationCodeException;
import com.project.note.exception.ResourceNotFoundException;
import com.project.note.exception.ValidationException;
import com.project.note.listener.AccountRegisteredEventListener;
import com.project.note.repository.AccountRepository;
import com.project.note.util.VerificationCodeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private VerificationCodeUtil verificationCodeUtil;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private AccountRegisteredEventListener accountRegisteredEventListener;

    @BeforeEach
    public void setUp() {
        doAnswer(returnsFirstArg()).when(accountRepository).save(any(Account.class));
    }

    @Test
    public void whenRegisterAccountRequestValid_returnAccountDto() throws ValidationException {

        String firstname = "John";
        String lastname = "Doe";
        String email = "abc@example.com";
        String password = "secret";

        /*
         AccountRegisteredEventListener.sendEmailVerificationCode will capture AccountRegisteredEvent that's send by AccountService.registerAccount.
         use doNothing() to prevent listener from sending userEmail verification code
        */
        doNothing().when(accountRegisteredEventListener).sendEmailVerificationCode(any(AccountRegisteredEvent.class));

        AccountDto accountDto = accountService.registerAccount(
                new RegisterAccountRequest(
                        firstname,
                        lastname,
                        email,
                        password
                )
        );

        assertEquals(firstname, accountDto.firstname());
    }

    @Test
    public void whenRegisterAccountRequestInvalid_throwValidationException() {
        String firstname = "";
        String lastname = "";
        String email = "";
        String password = "";

        doNothing().when(accountRegisteredEventListener).sendEmailVerificationCode(any(AccountRegisteredEvent.class));

        assertThrows(ValidationException.class, () -> {
            accountService.registerAccount(
                    new RegisterAccountRequest(
                            firstname,
                            lastname,
                            email,
                            password
                    )
            );
        });
    }

    @Test
    public void whenRegisterAccountRequestEmailAlreadyUse_throwValidationException() {
        String firstname = "John";
        String lastname = "Doe";
        String email = "abc@example.com";
        String password = "secret";

        // pretend account with {userEmail} already exist
        doReturn(Optional.of(new Account())).when(accountRepository).findByEmail(email);

        doNothing().when(accountRegisteredEventListener).sendEmailVerificationCode(any(AccountRegisteredEvent.class));

        assertThrows(ValidationException.class, () -> {
            accountService.registerAccount(
                    new RegisterAccountRequest(
                            firstname,
                            lastname,
                            email,
                            password
                    )
            );
        });
    }

    @Test
    public void whenVerifyAccountRequestValid_shouldVerifyAccount() throws ValidationException, ResourceNotFoundException, InvalidVerificationCodeException {
        String id = UUID.randomUUID().toString();
        String firstname = "John";
        String lastname = "Doe";
        String email = "abc@example.com";
        String password = "secret";
        String verificationCode = verificationCodeUtil.generateVerificationCode();

        doReturn(Optional.of(
                new Account(
                        id,
                        firstname,
                        lastname,
                        email,
                        password,
                        verificationCode,
                        AccountStatus.UNVERIFIED
                )
        )).when(accountRepository).findById(id);


        AccountDto accountDto = accountService.verifyAccount(
                new VerifyAccountRequest(
                        id,
                        verificationCode
                )
        );

        assertEquals(AccountStatus.VERIFIED.toString(), accountDto.status());
    }

    @Test
    public void whenAccountDidNotExist_throwNotFoundException() {
        String id = UUID.randomUUID().toString();
        String verificationCode = verificationCodeUtil.generateVerificationCode();

        doReturn(Optional.empty()).when(accountRepository).findById(id);

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.verifyAccount(
                    new VerifyAccountRequest(
                            id,
                            verificationCode
                    )
            );
        });
    }

    @Test
    public void whenVerifyAccountRequestVerificationCodeWrong_throwInvalidVerificationCodeException() {
        String id = UUID.randomUUID().toString();
        String firstname = "John";
        String lastname = "Doe";
        String email = "abc@example.com";
        String password = "secret";
        String verificationCode = verificationCodeUtil.generateVerificationCode();

        doReturn(Optional.of(
                new Account(
                        id,
                        firstname,
                        lastname,
                        email,
                        password,
                        verificationCode,
                        AccountStatus.UNVERIFIED
                )
        )).when(accountRepository).findById(id);

        assertThrows(InvalidVerificationCodeException.class, () -> {
            accountService.verifyAccount(
                    new VerifyAccountRequest(
                            id,
                            "wrong"
                    )
            );
        });
    }
}
