package com.yrsd.medcheck.utils;

import org.springframework.beans.factory.annotation.Value;

import java.security.SecureRandom;

public class CodeGenerator {

    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    @Value("${VERIFICATION_CODE_LENGTH}")
    private static int VERIFICATION_CODE_LENGTH = 12;
    private static final SecureRandom random = new SecureRandom();

    public static String generateCode() {
        StringBuilder sb = new StringBuilder(VERIFICATION_CODE_LENGTH);

        for (int i = 0; i < VERIFICATION_CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHAR_SET.length());
            sb.append(CHAR_SET.charAt(randomIndex));
        }

        return sb.toString();
    }


    public static String generateDrugCode(String drug){
        StringBuilder sb = new StringBuilder(3);

        for (int i = 0; i < 3; i++) {
            int randomIndex = random.nextInt(drug.length());
            sb.append(drug.charAt(randomIndex));
        }


        return sb.toString().toUpperCase();
    }

}