package com.project.note.entity;

import com.project.note.exception.InvalidResetPasswordCodeException;
import com.project.note.exception.InvalidVerificationCodeException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String verificationCode;
    @Setter
    private String resetPasswordCode;
    private AccountStatus status;

    public Account(String id, String firstname, String lastname, String email, String password, String verificationCode, AccountStatus status) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.status = status;
    }

    public void verify(String verificationCode) throws InvalidVerificationCodeException {
        if (status.equals(AccountStatus.VERIFIED)) {
            throw new IllegalStateException("Account already verified");
        }
        if (!Objects.equals(this.verificationCode, verificationCode)) {
            throw new InvalidVerificationCodeException("Invalid verification code");
        }
        status = AccountStatus.VERIFIED;
    }

    public void resetPassword(String resetPasswordCode, String newPassword) throws InvalidResetPasswordCodeException {
        if (this.resetPasswordCode.isBlank()) {
            throw new IllegalStateException("Reset password token never been created");
        }

        if (!this.resetPasswordCode.equals(resetPasswordCode)) {
            throw new InvalidResetPasswordCodeException("Invalid reset password code");
        }

        password = newPassword;

        this.resetPasswordCode = "";
    }
}
