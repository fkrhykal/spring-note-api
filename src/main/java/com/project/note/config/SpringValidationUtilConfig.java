package com.project.note.config;

import com.project.note.exception.ValidationException;
import com.project.note.validation.Validation;
import com.project.note.validation.ValidationUtil;
import jakarta.validation.Validator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.GenericTypeResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Configuration
public class SpringValidationUtilConfig {

    @Bean
    public ValidationUtil validationUtil(ApplicationContext applicationContext) {
        SpringValidationUtil springValidationUtil = new SpringValidationUtil(applicationContext.getBean(Validator.class));

        applicationContext.getBeansOfType(Validation.class).forEach((s, validation) -> {
            Class<?>[] args = Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(validation.getClass(), Validation.class));
            springValidationUtil.unsafeRegister(args[0], validation);
        });

        return springValidationUtil;
    }

    private static class SpringValidationUtil implements ValidationUtil {
        private final Map<Class<?>, Validation<?>> registry = new HashMap<>();
        private final DefaultValidation defaultValidation;

        public SpringValidationUtil(Validator validator) {
            defaultValidation = new DefaultValidation(validator);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> void validate(T value) throws ValidationException {
            Optional.ofNullable((Validation<T>) registry.get(value.getClass())).orElse((Validation<T>) defaultValidation).validate(value);
        }

        private void unsafeRegister(Class<?> cls, Validation<?> validation) {
            registry.put(cls, validation);
        }
    }

    private record DefaultValidation(Validator validator) implements Validation<Object> {
        @Override
        public void validate(Object value) throws ValidationException {
            Map<String, String> errors = new HashMap<>();

            validator.validate(value).forEach(error -> {
                errors.put(error.getPropertyPath().toString(), error.getMessage());
            });

            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
        }
    }
}
