package com.project.note.validation;

import com.project.note.dto.RegisterAccountRequest;
import com.project.note.exception.ValidationException;
import com.project.note.repository.AccountRepository;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RegisterAccountRequestValidator implements Validation<RegisterAccountRequest> {
    private final Validator validator;
    private final AccountRepository accountRepository;

    @Override
    public void validate(RegisterAccountRequest value) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        validator.validate(value).forEach(error -> {
            errors.put(error.getPropertyPath().toString(), error.getMessage());
        });

        if (errors.get("email") == null) {
            accountRepository.findByEmail(value.email()).ifPresent(account -> {
                errors.put("email", "Email already used");
            });
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
