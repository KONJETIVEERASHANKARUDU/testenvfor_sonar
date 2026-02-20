package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * This class contains intentional code smells for SonarQube testing
 */
public class BadCodeExample {
    
    // Security Issue: Hardcoded password
    private static final String PASSWORD = "admin123";
    private static final String DB_PASSWORD = "MySecretPassword";
    private static final String API_KEY = "sk_live_1234567890abcdef";
    
    // Code Smell: Unused variable
    private String unusedVariable = "This is never used";
    
    // Code Smell: Magic numbers
    public int calculateTotal(int price) {
        return price * 100;  // What is 100?
    }
    
    // Security Issue: SQL Injection vulnerability
    public void executeQuery(String userInput) throws Exception {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String username = "root";
        // Hardcoded credentials
        String password = "password123";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt = conn.createStatement();
        
        // SQL Injection - concatenating user input
        String query = "SELECT * FROM users WHERE username = '" + userInput + "'";
        stmt.executeQuery(query);
    }
    
    // Code Smell: Method too long and complex
    public void complexMethod(int x, int y, int z, int a, int b) {
        if (x > 0) {
            if (y > 0) {
                if (z > 0) {
                    if (a > 0) {
                        if (b > 0) {
                            System.out.println("All positive");
                        }
                    }
                }
            }
        }
        
        // Too many nested loops
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    for (int l = 0; l < 10; l++) {
                        System.out.println(i + j + k + l);
                    }
                }
            }
        }
    }
    
    // Code Smell: Empty catch block
    public void poorExceptionHandling() {
        try {
            int result = 10 / 0;
        } catch (Exception e) {
            // Empty catch block - swallowing exception
        }
    }
    
    // Code Smell: Method with too many parameters
    public void tooManyParameters(String p1, String p2, String p3, String p4, 
                                   String p5, String p6, String p7, String p8) {
        System.out.println(p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8);
    }
}
