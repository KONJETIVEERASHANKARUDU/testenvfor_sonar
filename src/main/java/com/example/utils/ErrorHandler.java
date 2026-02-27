package com.example.utils;

/**
 * Utility class for common error handling logic
 * EXTRACTED FROM DUPLICATE CODE to centralize error management
 */
public class ErrorHandler {
    
    private ErrorHandler() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Handle errors with consistent formatting
     * Extracted from handleCustomerError, handleVendorError, handleEmployeeError
     * 
     * @param e Exception to handle
     * @param context Context information (customer/vendor/employee)
     */
    public static void handleError(Exception e, String context) {
        System.err.println("=================================================");
        System.err.println("ERROR OCCURRED: " + e.getMessage());
        System.err.println("Context: " + context);
        System.err.println("Error Type: " + e.getClass().getSimpleName());
        if (e.getCause() != null) {
            System.err.println("Root Cause: " + e.getCause().getMessage());
        }
        e.printStackTrace();
        System.err.println("=================================================");
    }
}
