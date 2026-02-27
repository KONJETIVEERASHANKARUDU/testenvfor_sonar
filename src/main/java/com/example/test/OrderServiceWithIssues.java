package com.example.test;

/**
 * Order service with DUPLICATE CODE from UserServiceWithIssues
 * This demonstrates cross-file duplication
 */
public class OrderServiceWithIssues {
    
    // SECURITY ISSUE: Another hardcoded password
    private static final String ADMIN_PASSWORD = "password123";
    private static final String SECRET_KEY = "my-secret-key-12345";
    
    /**
     * DUPLICATE CODE BLOCK - SAME AS UserServiceWithIssues
     * Agent should detect this as duplicate
     */
    public boolean validateOrder(String username, String email) {
        // Duplicate validation logic - SAME AS UserServiceWithIssues
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
        // End duplicate
        
        return true;
    }
    
    /**
     * SECURITY ISSUE: Weak cryptography
     */
    public String encryptData(String data) {
        // Using weak MD5 hashing
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(data.getBytes());
            return java.util.Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * SECURITY ISSUE: Insecure random number generation
     */
    public String generateToken() {
        // Using insecure Random instead of SecureRandom
        java.util.Random random = new java.util.Random();
        return String.valueOf(random.nextInt());
    }
    
    /**
     * CODE QUALITY ISSUE: Very long method with complex logic
     */
    public double calculateTotalPrice(String[] items, int[] quantities, double[] prices, 
                                     boolean isPremium, String couponCode, boolean isMember) {
        double subtotal = 0;
        
        // Calculate subtotal
        for (int i = 0; i < items.length; i++) {
            subtotal += quantities[i] * prices[i];
        }
        
        // Apply member discount
        if (isMember) {
            subtotal = subtotal * 0.95;
        }
        
        // Apply premium discount
        if (isPremium) {
            subtotal = subtotal * 0.90;
        }
        
        // Apply coupon
        if (couponCode != null) {
            if (couponCode.equals("SAVE10")) {
                subtotal = subtotal * 0.90;
            } else if (couponCode.equals("SAVE20")) {
                subtotal = subtotal * 0.80;
            } else if (couponCode.equals("SAVE30")) {
                subtotal = subtotal * 0.70;
            }
        }
        
        // Calculate tax
        double tax = subtotal * 0.08;
        
        // Calculate shipping
        double shipping = 0;
        if (subtotal < 50) {
            shipping = 10;
        } else if (subtotal < 100) {
            shipping = 5;
        }
        
        // Apply free shipping for premium
        if (isPremium) {
            shipping = 0;
        }
        
        // Calculate total
        double total = subtotal + tax + shipping;
        
        return total;
    }
    
    /**
     * ANOTHER DUPLICATE - Same validation again
     */
    public boolean checkCustomer(String username, String email) {
        // Duplicate validation logic - SAME AGAIN
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
        // End duplicate
        
        return true;
    }
}
