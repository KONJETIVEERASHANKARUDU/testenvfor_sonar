package com.example.good;

import java.util.regex.Pattern;

/**
 * Validation utility class with proper regex patterns and error handling.
 */
public final class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_-]{3,20}$");
    
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Validates an email address format.
     * 
     * @param email the email to validate
     * @return true if email is valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validates a phone number format (international format).
     * 
     * @param phone the phone number to validate
     * @return true if phone is valid
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validates a username (3-20 chars, alphanumeric, underscore, hyphen).
     * 
     * @param username the username to validate
     * @return true if username is valid
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * Validates that a string is within length constraints.
     * 
     * @param str the string to validate
     * @param minLength minimum length
     * @param maxLength maximum length
     * @return true if length is valid
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validates that a number is within a range.
     * 
     * @param value the value to check
     * @param min minimum value
     * @param max maximum value
     * @return true if in range
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
}
