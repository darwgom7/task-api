package com.darwgom.taskapi.utilities;

import com.darwgom.taskapi.domain.exceptions.InvalidPasswordException;

import java.util.regex.Pattern;

public class EmailFormatValidator {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    public static boolean isValidEmailFormat(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    public static void validEmailFormat(String email) {
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            throw new InvalidPasswordException("The email format is invalid");
        }
    }
}