package com.example.utils;

/**
 * Utility class for common validation logic
 * EXTRACTED FROM DUPLICATE CODE to improve maintainability
 */
public class ValidationUtils {
    
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Validates entity with name, email, and phone
     * Extracted from validateCustomer, validateVendor, validateEmployee
     * 
     * @param name Entity name
     * @param email Entity email
     * @param phone Entity phone
     * @return true if valid, false otherwise
     */
    public static boolean validateEntity(String name, String email, String phone) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("ERROR: Name cannot be empty");
            return false;
        }
        if (name.length() < 2) {
            System.err.println("ERROR: Name too short");
            return false;
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            System.err.println("ERROR: Invalid email format");
            return false;
        }
        if (phone == null || phone.length() < 10) {
            System.err.println("ERROR: Invalid phone number");
            return false;
        }
        return true;
    }
    
    /**
     * Validates user with username and email
     * Extracted from duplicate validation blocks in UserServiceWithIssues
     * 
     * @param username Username to validate
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateUser(String username, String email) {
        if (username == null || username.isEmpty()) {
            System.out.println("Username is required");
            return false;
        }
        if (username.length() < 3) {
            System.out.println("Username must be at least 3 characters");
            return false;
        }
        if (email == null || email.isEmpty()) {
            System.out.println("Email is required");
            return false;
        }
        if (!email.contains("@")) {
            System.out.println("Invalid email format");
            return false;
        }
        return true;
    }
}
