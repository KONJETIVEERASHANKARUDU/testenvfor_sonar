package com.example.test;

import java.util.List;
import java.util.ArrayList;

/**
 * INTENTIONAL DUPLICATE CODE - For Agent Detection
 * This class contains massive code duplication that should trigger
 * automatic detection and fix suggestions
 */
public class DuplicateCodeService {
    
    /**
     * DUPLICATE BLOCK #1 - Customer Validation
     */
    public boolean validateCustomer(String name, String email, String phone) {
        // EXACT DUPLICATE - Should be extracted to utility method
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
        System.out.println("Customer validation passed");
        return true;
    }
    
    /**
     * DUPLICATE BLOCK #2 - Vendor Validation (SAME AS ABOVE!)
     */
    public boolean validateVendor(String name, String email, String phone) {
        // EXACT DUPLICATE - Should be extracted to utility method
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
        System.out.println("Vendor validation passed");
        return true;
    }
    
    /**
     * DUPLICATE BLOCK #3 - Employee Validation (SAME AGAIN!)
     */
    public boolean validateEmployee(String name, String email, String phone) {
        // EXACT DUPLICATE - Should be extracted to utility method
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
        System.out.println("Employee validation passed");
        return true;
    }
    
    /**
     * DUPLICATE BLOCK #4 - Data Processing
     */
    public List<String> processCustomerData(List<String> data) {
        // DUPLICATE PROCESSING LOGIC - START
        List<String> processed = new ArrayList<>();
        for (String item : data) {
            if (item != null && !item.isEmpty()) {
                String cleaned = item.trim().toUpperCase();
                if (cleaned.length() > 0) {
                    processed.add(cleaned);
                    System.out.println("Processed item: " + cleaned);
                }
            }
        }
        System.out.println("Total items processed: " + processed.size());
        // DUPLICATE PROCESSING LOGIC - END
        return processed;
    }
    
    /**
     * DUPLICATE BLOCK #5 - Data Processing (SAME AS ABOVE!)
     */
    public List<String> processVendorData(List<String> data) {
        // DUPLICATE PROCESSING LOGIC - START
        List<String> processed = new ArrayList<>();
        for (String item : data) {
            if (item != null && !item.isEmpty()) {
                String cleaned = item.trim().toUpperCase();
                if (cleaned.length() > 0) {
                    processed.add(cleaned);
                    System.out.println("Processed item: " + cleaned);
                }
            }
        }
        System.out.println("Total items processed: " + processed.size());
        // DUPLICATE PROCESSING LOGIC - END
        return processed;
    }
    
    /**
     * DUPLICATE BLOCK #6 - Data Processing (AGAIN!)
     */
    public List<String> processEmployeeData(List<String> data) {
        // DUPLICATE PROCESSING LOGIC - START
        List<String> processed = new ArrayList<>();
        for (String item : data) {
            if (item != null && !item.isEmpty()) {
                String cleaned = item.trim().toUpperCase();
                if (cleaned.length() > 0) {
                    processed.add(cleaned);
                    System.out.println("Processed item: " + cleaned);
                }
            }
        }
        System.out.println("Total items processed: " + processed.size());
        // DUPLICATE PROCESSING LOGIC - END
        return processed;
    }
    
    /**
     * DUPLICATE BLOCK #7 - Error Handling
     */
    public void handleCustomerError(Exception e) {
        // DUPLICATE ERROR HANDLING - START
        System.err.println("=================================================");
        System.err.println("ERROR OCCURRED: " + e.getMessage());
        System.err.println("Error Type: " + e.getClass().getSimpleName());
        if (e.getCause() != null) {
            System.err.println("Root Cause: " + e.getCause().getMessage());
        }
        e.printStackTrace();
        System.err.println("=================================================");
        // DUPLICATE ERROR HANDLING - END
    }
    
    /**
     * DUPLICATE BLOCK #8 - Error Handling (IDENTICAL!)
     */
    public void handleVendorError(Exception e) {
        // DUPLICATE ERROR HANDLING - START
        System.err.println("=================================================");
        System.err.println("ERROR OCCURRED: " + e.getMessage());
        System.err.println("Error Type: " + e.getClass().getSimpleName());
        if (e.getCause() != null) {
            System.err.println("Root Cause: " + e.getCause().getMessage());
        }
        e.printStackTrace();
        System.err.println("=================================================");
        // DUPLICATE ERROR HANDLING - END
    }
    
    /**
     * DUPLICATE BLOCK #9 - Error Handling (ONCE MORE!)
     */
    public void handleEmployeeError(Exception e) {
        // DUPLICATE ERROR HANDLING - START
        System.err.println("=================================================");
        System.err.println("ERROR OCCURRED: " + e.getMessage());
        System.err.println("Error Type: " + e.getClass().getSimpleName());
        if (e.getCause() != null) {
            System.err.println("Root Cause: " + e.getCause().getMessage());
        }
        e.printStackTrace();
        System.err.println("=================================================");
        // DUPLICATE ERROR HANDLING - END
    }
    
    /**
     * DUPLICATE BLOCK #10 - Calculation Logic
     */
    public double calculateCustomerDiscount(double price, int quantity) {
        // DUPLICATE CALCULATION - START
        double baseDiscount = 0.0;
        if (quantity >= 100) {
            baseDiscount = 0.20;
        } else if (quantity >= 50) {
            baseDiscount = 0.15;
        } else if (quantity >= 10) {
            baseDiscount = 0.10;
        } else if (quantity >= 5) {
            baseDiscount = 0.05;
        }
        double discountAmount = price * quantity * baseDiscount;
        double finalPrice = (price * quantity) - discountAmount;
        System.out.println("Applied discount: " + (baseDiscount * 100) + "%");
        System.out.println("Discount amount: $" + discountAmount);
        System.out.println("Final price: $" + finalPrice);
        return finalPrice;
        // DUPLICATE CALCULATION - END
    }
    
    /**
     * DUPLICATE BLOCK #11 - Calculation Logic (SAME!)
     */
    public double calculateVendorDiscount(double price, int quantity) {
        // DUPLICATE CALCULATION - START
        double baseDiscount = 0.0;
        if (quantity >= 100) {
            baseDiscount = 0.20;
        } else if (quantity >= 50) {
            baseDiscount = 0.15;
        } else if (quantity >= 10) {
            baseDiscount = 0.10;
        } else if (quantity >= 5) {
            baseDiscount = 0.05;
        }
        double discountAmount = price * quantity * baseDiscount;
        double finalPrice = (price * quantity) - discountAmount;
        System.out.println("Applied discount: " + (baseDiscount * 100) + "%");
        System.out.println("Discount amount: $" + discountAmount);
        System.out.println("Final price: $" + finalPrice);
        return finalPrice;
        // DUPLICATE CALCULATION - END
    }
}
