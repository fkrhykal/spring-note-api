package com.project.note.util;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

@Component
public class VerificationCodeUtilImpl implements VerificationCodeUtil {
    @Override
    public String generateVerificationCode() {
        return new BigInteger(40, new SecureRandom()).toString(32).toUpperCase();
    }
}
