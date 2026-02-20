package com.example.good;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for string operations with clean, efficient implementations.
 */
public final class StringUtils {
    
    private StringUtils() {
        // Private constructor to prevent instantiation
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Checks if a string is null or empty.
     * 
     * @param str the string to check
     * @return true if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
    /**
     * Checks if a string is null, empty, or contains only whitespace.
     * 
     * @param str the string to check
     * @return true if string is blank
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Safely trims a string, returning null if input is null.
     * 
     * @param str the string to trim
     * @return trimmed string or null
     */
    public static String safeTrim(String str) {
        return str == null ? null : str.trim();
    }
    
    /**
     * Capitalizes the first letter of a string.
     * 
     * @param str the string to capitalize
     * @return capitalized string
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    /**
     * Reverses a string efficiently.
     * 
     * @param str the string to reverse
     * @return reversed string
     */
    public static String reverse(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        return new StringBuilder(str).reverse().toString();
    }
    
    /**
     * Splits a string by delimiter and trims each part.
     * 
     * @param str the string to split
     * @param delimiter the delimiter
     * @return list of trimmed parts
     */
    public static List<String> splitAndTrim(String str, String delimiter) {
        List<String> result = new ArrayList<>();
        
        if (isEmpty(str) || isEmpty(delimiter)) {
            return result;
        }
        
        String[] parts = str.split(delimiter);
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        
        return result;
    }
    
    /**
     * Checks if a string contains only alphanumeric characters.
     * 
     * @param str the string to check
     * @return true if alphanumeric
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        
        return str.chars().allMatch(Character::isLetterOrDigit);
    }
    
    /**
     * Truncates a string to a maximum length.
     * 
     * @param str the string to truncate
     * @param maxLength the maximum length
     * @return truncated string
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        
        return str.substring(0, maxLength) + "...";
    }
    
    /**
     * Masks sensitive information in a string.
     * 
     * @param str the string to mask
     * @param visibleChars number of characters to keep visible
     * @return masked string
     */
    public static String maskSensitiveData(String str, int visibleChars) {
        if (isEmpty(str) || str.length() <= visibleChars) {
            return "***";
        }
        
        String visible = str.substring(0, visibleChars);
        return visible + "*".repeat(str.length() - visibleChars);
    }
}
