package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

/**
 * This class contains security vulnerabilities for SonarQube testing
 */
public class SecurityIssues {
    
    // Multiple hardcoded passwords and credentials
    private static final String DATABASE_PASSWORD = "SuperSecret123!";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String ROOT_PASSWORD = "root123";
    private String userPassword = "password123";
    
    // Hardcoded API keys and tokens
    private static final String AWS_SECRET_KEY = "AKIAIOSFODNN7EXAMPLE";
    private static final String STRIPE_API_KEY = "sk_test_1234567890abcdefghijklmno";
    private static final String JWT_SECRET = "myJWTSecretKey123456789";
    
    // Hardcoded database connection string with credentials
    private String connectionString = "mongodb://admin:password@localhost:27017/mydb";
    
    // Weak random number generation
    public int generateSecurityToken() {
        Random random = new Random();  // Predictable random - security issue
        return random.nextInt(1000000);
    }
    
    // Path traversal vulnerability
    public File readFile(String fileName) {
        // No validation - user could pass "../../../etc/passwd"
        return new File("/app/data/" + fileName);
    }
    
    // Command injection vulnerability
    public void executeCommand(String userInput) throws Exception {
        Runtime.getRuntime().exec("ping " + userInput);  // Dangerous!
    }
    
    // Insecure deserialization
    public Object deserialize(FileInputStream fis) throws Exception {
        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(fis);
        return ois.readObject();  // Security risk
    }
    
    // Authentication without password
    public boolean authenticate(String username) {
        // Missing password check!
        return username != null && username.length() > 0;
    }
    
    // Using weak cryptographic algorithm
    public String hashPassword(String password) {
        // MD5 is weak and deprecated
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(password);
    }
    
    // Exposing system information
    public void printSystemInfo() {
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("User: " + System.getProperty("user.name"));
        System.out.println("Home: " + System.getProperty("user.home"));
        System.out.println("Java Version: " + System.getProperty("java.version"));
    }
}
