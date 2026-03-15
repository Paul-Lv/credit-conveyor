package com.example.validation;

import java.math.BigDecimal;

public class ValidationConstants {

    private ValidationConstants() {
    }

    public static final BigDecimal MIN_AMOUNT = new BigDecimal("10000");
    public static final Integer MIN_TERM = 6;
    public static final Integer ADULT_AGE = 18;
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]{2,50}@[A-Za-z0-9.-]{2,20}$";
    public static final String NAME_PATTERN = "^[A-Za-z]{2,30}$";
    public static final String PASSPORT_PATTERN = "^\\d{4}$";
    public static final String PASSPORT_NUMBER_PATTERN = "^\\d{6}$";
}