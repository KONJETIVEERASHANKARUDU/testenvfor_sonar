package com.example;

import java.util.logging.Logger;

/**
 * Refactored class with no duplicate code
 */
public class DuplicateCode {
    
    private static final Logger LOGGER = Logger.getLogger(DuplicateCode.class.getName());
    private static final String DEFAULT_COUNTRY = "USA";
    
    /**
     * Common method to process any type of data
     * @param name the name
     * @param email the email
     * @param type the data type (user/customer/employee)
     */
    private void processData(String name, String email, String type) {
        if (name == null || name.isEmpty()) {
            LOGGER.warning("Invalid name for " + type);
            return;
        }
        if (email == null || email.isEmpty()) {
            LOGGER.warning("Invalid email for " + type);
            return;
        }
        
        LOGGER.info("Processing " + type + ": " + name);
        LOGGER.info("Email: " + email);
        LOGGER.info(type + " data validated successfully");
        LOGGER.info("Saving to database...");
        LOGGER.info("Operation completed");
    }
    
    /**
     * Process user data
     */
    public void processUserData(String name, String email) {
        processData(name, email, "user");
    }
    
    /**
     * Process customer data
     */
    public void processCustomerData(String name, String email) {
        processData(name, email, "customer");
    }
    
    /**
     * Process employee data
     */
    public void processEmployeeData(String name, String email) {
        processData(name, email, "employee");
    }
    
    /**
     * Common method to format any address
     * @param street the street address
     * @param city the city
     * @param zip the ZIP code
     * @return formatted address string
     */
    private String formatAddressCommon(String street, String city, String zip) {
        StringBuilder result = new StringBuilder();
        result.append("Street: ").append(street).append("\n");
        result.append("City: ").append(city).append("\n");
        result.append("ZIP: ").append(zip).append("\n");
        result.append("Country: ").append(DEFAULT_COUNTRY).append("\n");
        return result.toString();
    }
    
    /**
     * Format standard address
     */
    public String formatAddress(String street, String city, String zip) {
        return formatAddressCommon(street, city, zip);
    }
    
    /**
     * Format shipping address
     */
    public String formatShippingAddress(String street, String city, String zip) {
        return formatAddressCommon(street, city, zip);
    }
    
    /**
     * Format billing address
     */
    public String formatBillingAddress(String street, String city, String zip) {
        return formatAddressCommon(street, city, zip);
    }
}
