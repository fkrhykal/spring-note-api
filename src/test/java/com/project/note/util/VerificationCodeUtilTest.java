package com.project.note.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Slf4j
@SpringBootTest
class VerificationCodeUtilTest {

    @Autowired
    private VerificationCodeUtil verificationCodeGenerator;

    @Test
    public void shouldGenerateVerificationCode() {
        String verificationCode = verificationCodeGenerator.generateVerificationCode();
        assertNotNull(verificationCode);
    }

    @Test
    public void shouldGenerateUniqueVerificationCode() {
        List<String> verificationCodes = generateMultipleVerificationCode(100);

        for (int i = 0; i < verificationCodes.size(); i++) {
            for (int j = 0; j < verificationCodes.size(); j++) {
                if (j != i) {
                    assertNotEquals(verificationCodes.get(i), verificationCodes.get(j));
                }
            }
        }
    }

    private List<String> generateMultipleVerificationCode(Integer number) {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            list.add(verificationCodeGenerator.generateVerificationCode());
        }

        return list;
    }
}