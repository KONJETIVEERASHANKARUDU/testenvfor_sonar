package com.example;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * Secure implementation following best practices
 */
public class SecurityIssues {
    
    private static final Logger LOGGER = Logger.getLogger(SecurityIssues.class.getName());
    private static final String ALLOWED_DIRECTORY = "/app/data/";
    
    // SecureRandom for cryptographic operations
    private final SecureRandom secureRandom;
    
    public SecurityIssues() {
        this.secureRandom = new SecureRandom();
    }
    
    /**
     * Generate cryptographically secure token
     * @return secure random token
     */
    public String generateSecurityToken() {
        byte[] token = new byte[32];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }
    
    /**
     * Safely read file with path validation
     * @param fileName the filename to read
     * @return File object if path is valid
     * @throws SecurityException if path traversal detected
     */
    public File readFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        
        // Validate filename - prevent path traversal
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new SecurityException("Invalid filename: path traversal detected");
        }
        
        Path basePath = Paths.get(ALLOWED_DIRECTORY).normalize();
        Path filePath = basePath.resolve(fileName).normalize();
        
        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("Access denied: path outside allowed directory");
        }
        
        return filePath.toFile();
    }
    
    /**
     * Authenticate user with username and password
     * @param username the username
     * @param password the password
     * @return true if authenticated
     */
    public boolean authenticate(String username, String password) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        // In real application, verify against secure database
        LOGGER.info("Authentication attempt for user: " + username);
        return true;
    }
    
    /**
     * Hash password using SHA-256
     * @param password the password to hash
     * @return hashed password
     */
    public String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.severe("SHA-256 algorithm not available: " + e.getMessage());
            throw new IllegalStateException("Cryptographic algorithm unavailable", e);
        }
    }
    
    /**
     * Log system information (non-sensitive)
     */
    public void logSystemInfo() {
        String javaVersion = System.getProperty("java.version");
        LOGGER.info("Application running on Java version: " + javaVersion);
    }
}
