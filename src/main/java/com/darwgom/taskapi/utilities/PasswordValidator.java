package com.darwgom.taskapi.utilities;

import java.util.regex.Pattern;

import com.darwgom.taskapi.domain.exceptions.InvalidPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class PasswordValidator {

    private final ApplicationContext applicationContext;

    private static String passwordPattern;

    static {
        Environment env = ApplicationContextProvider.getApplicationContext().getBean(Environment.class);
        passwordPattern = env.getRequiredProperty("password.pattern");
    }

    @Autowired
    public PasswordValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    public static boolean isValid(String password) {
        return Pattern.matches(passwordPattern, password);
    }

    public static void validatePassword(String password) {
        if (!Pattern.matches(passwordPattern, password)) {
            throw new InvalidPasswordException("The password must have at least one uppercase letter, one number, one special character, and more than 5 characters.");
        }
    }

}

