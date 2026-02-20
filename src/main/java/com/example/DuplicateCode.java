package com.example;

/**
 * This class contains duplicate code blocks for SonarQube testing
 */
public class DuplicateCode {
    
    // Duplicate method 1
    public void processUserData(String name, String email) {
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name");
            return;
        }
        if (email == null || email.isEmpty()) {
            System.out.println("Invalid email");
            return;
        }
        System.out.println("Processing user: " + name);
        System.out.println("Email: " + email);
        System.out.println("User data validated successfully");
        System.out.println("Saving to database...");
        System.out.println("Operation completed");
    }
    
    // Duplicate method 2 - almost identical to above
    public void processCustomerData(String name, String email) {
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name");
            return;
        }
        if (email == null || email.isEmpty()) {
            System.out.println("Invalid email");
            return;
        }
        System.out.println("Processing user: " + name);
        System.out.println("Email: " + email);
        System.out.println("User data validated successfully");
        System.out.println("Saving to database...");
        System.out.println("Operation completed");
    }
    
    // Duplicate method 3 - almost identical to above
    public void processEmployeeData(String name, String email) {
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name");
            return;
        }
        if (email == null || email.isEmpty()) {
            System.out.println("Invalid email");
            return;
        }
        System.out.println("Processing user: " + name);
        System.out.println("Email: " + email);
        System.out.println("User data validated successfully");
        System.out.println("Saving to database...");
        System.out.println("Operation completed");
    }
    
    // More duplicate blocks
    public String formatAddress(String street, String city, String zip) {
        String result = "";
        result = result + "Street: " + street + "\n";
        result = result + "City: " + city + "\n";
        result = result + "ZIP: " + zip + "\n";
        result = result + "Country: USA\n";
        return result;
    }
    
    public String formatShippingAddress(String street, String city, String zip) {
        String result = "";
        result = result + "Street: " + street + "\n";
        result = result + "City: " + city + "\n";
        result = result + "ZIP: " + zip + "\n";
        result = result + "Country: USA\n";
        return result;
    }
    
    public String formatBillingAddress(String street, String city, String zip) {
        String result = "";
        result = result + "Street: " + street + "\n";
        result = result + "City: " + city + "\n";
        result = result + "ZIP: " + zip + "\n";
        result = result + "Country: USA\n";
        return result;
    }
}
