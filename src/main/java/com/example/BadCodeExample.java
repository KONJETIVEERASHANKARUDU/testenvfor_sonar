package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * Clean code implementation following best practices
 */
public class BadCodeExample {
    
    private static final Logger LOGGER = Logger.getLogger(BadCodeExample.class.getName());
    private static final int PERCENTAGE_MULTIPLIER = 100;
    private static final int POSITIVE_THRESHOLD = 0;
    
    private final DataSource dataSource;
    
    public BadCodeExample(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    /**
     * Calculate total with clear constant
     * @param price the base price
     * @return total price in cents
     */
    public int calculateTotal(int price) {
        return price * PERCENTAGE_MULTIPLIER;
    }
    
    /**
     * Execute query using PreparedStatement to prevent SQL injection
     * @param userInput the user input
     * @throws SQLException if database error occurs
     */
    public void executeQuery(String userInput) throws SQLException {
        if (userInput == null || userInput.isEmpty()) {
            throw new IllegalArgumentException("User input cannot be null or empty");
        }
        
        String query = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, userInput);
            stmt.executeQuery();
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database query failed", e);
            throw e;
        }
    }
    
    /**
     * Check if all values are positive
     * @param values array of values to check
     * @return true if all positive
     */
    public boolean areAllPositive(int... values) {
        for (int value : values) {
            if (value <= POSITIVE_THRESHOLD) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Calculate nested sum with reduced complexity
     * @param limit the iteration limit
     * @return sum of calculations
     */
    public long calculateNestedSum(int limit) {
        long sum = 0;
        for (int i = 0; i < limit; i++) {
            sum += i;
        }
        return sum;
    }
    
    /**
     * Proper exception handling with logging
     */
    public void properExceptionHandling() {
        try {
            performOperation();
        } catch (ArithmeticException e) {
            LOGGER.log(Level.WARNING, "Arithmetic operation failed", e);
            throw new IllegalStateException("Cannot perform calculation", e);
        }
    }
    
    private void performOperation() {
        int result = 10 / 0;
    }
    
    /**
     * Method with configuration object instead of many parameters
     * @param config the configuration object
     */
    public void processWithConfig(ProcessConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
        
        LOGGER.info("Processing with config: " + config);
    }
    
    /**
     * Configuration class to replace multiple parameters
     */
    public static class ProcessConfig {
        private final String param1;
        private final String param2;
        private final String param3;
        
        public ProcessConfig(String param1, String param2, String param3) {
            this.param1 = param1;
            this.param2 = param2;
            this.param3 = param3;
        }
        
        @Override
        public String toString() {
            return "ProcessConfig{" +
                   "param1='" + param1 + '\'' +
                   ", param2='" + param2 + '\'' +
                   ", param3='" + param3 + '\'' +
                   '}';
        }
    }
}
