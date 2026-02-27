package com.example.utils;

/**
 * Utility class for discount calculation logic
 * EXTRACTED FROM DUPLICATE CODE to eliminate redundancy
 */
public class DiscountCalculator {
    
    private DiscountCalculator() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Calculate discount based on quantity
     * Extracted from calculateCustomerDiscount and calculateVendorDiscount
     * 
     * @param price Unit price
     * @param quantity Quantity purchased
     * @return Final price after discount
     */
    public static double calculateDiscount(double price, int quantity) {
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
    }
}
