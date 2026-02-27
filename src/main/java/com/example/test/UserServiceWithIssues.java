package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Test class with INTENTIONAL ISSUES for agent verification
 * - Duplicate code blocks
 * - Security vulnerabilities
 * - Code quality issues
 */
public class UserServiceWithIssues {
    
    // SECURITY ISSUE: Hardcoded credentials
    private static final String DB_PASSWORD = "admin123";
    private static final String DB_USERNAME = "root";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    
    // SECURITY ISSUE: Hardcoded API key
    private static final String API_KEY = "sk_live_12345abcdefghijklmnop";
    
    /**
     * DUPLICATE CODE BLOCK #1
     * This same validation appears in multiple methods
     */
    public boolean validateUser(String username, String email) {
        // Duplicate validation logic - START
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
        // Duplicate validation logic - END
        
        return true;
    }
    
    /**
     * DUPLICATE CODE BLOCK #2
     * Same validation logic repeated here
     */
    public boolean registerUser(String username, String email, String password) {
        // Duplicate validation logic - START (SAME AS ABOVE)
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
        // Duplicate validation logic - END
        
        // SECURITY ISSUE: Password stored in plain text
        System.out.println("Storing password: " + password);
        
        return true;
    }
    
    /**
     * DUPLICATE CODE BLOCK #3
     * Same validation again in third method
     */
    public boolean updateUser(String username, String email) {
        // Duplicate validation logic - START (SAME AS ABOVE AGAIN)
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
        // Duplicate validation logic - END
        
        return true;
    }
    
    /**
     * SECURITY ISSUE: SQL Injection vulnerability
     */
    public void getUserByName(String username) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            
            // SQL INJECTION - user input directly in query
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                System.out.println("User: " + rs.getString("username"));
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * SECURITY ISSUE: Command injection vulnerability
     */
    public void executeSystemCommand(String userInput) {
        try {
            // COMMAND INJECTION - user input in system command
            Runtime.getRuntime().exec("ping " + userInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * SECURITY ISSUE: Path traversal vulnerability
     */
    public String readFile(String fileName) {
        try {
            // PATH TRAVERSAL - no validation of file path
            java.io.File file = new java.io.File("/var/data/" + fileName);
            java.util.Scanner scanner = new java.util.Scanner(file);
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
            scanner.close();
            return content.toString();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * CODE QUALITY ISSUE: Method too long (>50 lines)
     */
    public void processOrder(String orderId, String userId, String productId, int quantity) {
        System.out.println("Processing order: " + orderId);
        
        // Line 10
        if (orderId == null) return;
        if (userId == null) return;
        if (productId == null) return;
        if (quantity <= 0) return;
        
        // Line 15
        System.out.println("Validating user");
        boolean userValid = validateUser(userId, "test@example.com");
        if (!userValid) return;
        
        // Line 20
        System.out.println("Checking inventory");
        int available = checkInventory(productId);
        if (available < quantity) {
            System.out.println("Insufficient inventory");
            return;
        }
        
        // Line 27
        System.out.println("Calculating price");
        double price = calculatePrice(productId, quantity);
        double tax = price * 0.1;
        double total = price + tax;
        
        // Line 32
        System.out.println("Applying discount");
        if (quantity > 10) {
            total = total * 0.9;
        }
        if (quantity > 20) {
            total = total * 0.85;
        }
        
        // Line 40
        System.out.println("Creating invoice");
        String invoice = "Order: " + orderId + "\n";
        invoice += "User: " + userId + "\n";
        invoice += "Product: " + productId + "\n";
        invoice += "Quantity: " + quantity + "\n";
        invoice += "Price: " + price + "\n";
        invoice += "Tax: " + tax + "\n";
        invoice += "Total: " + total + "\n";
        
        // Line 49
        System.out.println("Sending notification");
        sendEmail(userId, invoice);
        sendSMS(userId, "Order confirmed");
        
        // Line 53
        System.out.println("Updating database");
        updateInventory(productId, quantity);
        saveOrder(orderId, userId, total);
        
        // Line 57
        System.out.println("Order processed successfully");
    }
    
    // Stub methods
    private int checkInventory(String productId) { return 100; }
    private double calculatePrice(String productId, int quantity) { return 10.0 * quantity; }
    private void sendEmail(String userId, String message) { }
    private void sendSMS(String userId, String message) { }
    private void updateInventory(String productId, int quantity) { }
    private void saveOrder(String orderId, String userId, double total) { }
}
