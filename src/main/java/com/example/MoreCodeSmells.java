package com.example;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Clean code implementation without code smells
 */
public class MoreCodeSmells {
    
    private static final Logger LOGGER = Logger.getLogger(MoreCodeSmells.class.getName());
    private static final int SENIOR_AGE = 65;
    private static final int MINOR_AGE = 18;
    private static final int SENIOR_DISCOUNT = 10;
    private static final int MINOR_DISCOUNT = 5;
    private static final int MEMBER_DISCOUNT = 5;
    private static final int LOYALTY_DISCOUNT = 5;
    private static final int HOLIDAY_DISCOUNT = 15;
    private static final int HIGH_PURCHASE_DISCOUNT = 10;
    private static final int VERY_HIGH_PURCHASE_DISCOUNT = 20;
    private static final int HIGH_PURCHASE_THRESHOLD = 100;
    private static final int VERY_HIGH_PURCHASE_THRESHOLD = 500;
    
    // Immutable list instead of mutable public static field
    private static final List<String> ITEMS_LIST = Collections.emptyList();
    
    /**
     * Check if active (proper boolean naming)
     * @return true if active
     */
    public boolean isActive() {
        return true;
    }
    
    /**
     * Get status message for HTTP code
     * @param code the HTTP status code
     * @return status message
     */
    public String getStatus(int code) {
        return switch (code) {
            case 200 -> "OK";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Error";
            default -> "Unknown";
        };
    }
    
    /**
     * Process code with default case
     * @param code the code to process
     */
    public void processCode(int code) {
        switch (code) {
            case 1:
                LOGGER.info("One");
                break;
            case 2:
                LOGGER.info("Two");
                break;
            case 3:
                LOGGER.info("Three");
                break;
            default:
                LOGGER.info("Unknown code");
                break;
        }
    }
    
    /**
     * Calculate discount with reduced complexity
     * @param discountRequest the discount parameters
     * @return total discount percentage
     */
    public int calculateDiscount(DiscountRequest discountRequest) {
        int discount = 0;
        
        discount += calculateAgeDiscount(discountRequest);
        discount += calculateMembershipDiscount(discountRequest);
        discount += calculateHolidayDiscount(discountRequest);
        
        return discount;
    }
    
    private int calculateAgeDiscount(DiscountRequest request) {
        if (request.age >= SENIOR_AGE) {
            return SENIOR_DISCOUNT;
        } else if (request.age < MINOR_AGE) {
            return MINOR_DISCOUNT;
        }
        return 0;
    }
    
    private int calculateMembershipDiscount(DiscountRequest request) {
        int discount = 0;
        if (request.isMember) {
            discount += MEMBER_DISCOUNT;
        }
        if (request.hasLoyaltyCard) {
            discount += LOYALTY_DISCOUNT;
        }
        return discount;
    }
    
    private int calculateHolidayDiscount(DiscountRequest request) {
        if (!request.isHoliday) {
            return 0;
        }
        
        int discount = HOLIDAY_DISCOUNT;
        
        if (request.purchaseAmount > VERY_HIGH_PURCHASE_THRESHOLD) {
            discount += VERY_HIGH_PURCHASE_DISCOUNT;
        } else if (request.purchaseAmount > HIGH_PURCHASE_THRESHOLD) {
            discount += HIGH_PURCHASE_DISCOUNT;
        }
        
        return discount;
    }
    
    /**
     * Proper naming convention
     */
    public void doSomething() {
        LOGGER.info("Performing action with proper naming");
    }
    
    /**
     * Process without commented code
     */
    public void process() {
        LOGGER.info("Active code executing");
    }
    
    /**
     * Build string efficiently using StringBuilder
     * @param items the items to concatenate
     * @return concatenated string
     */
    public String buildString(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            result.append(items.get(i));
            if (i < items.size() - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }
    
    /**
     * No identical branches
     */
    public void handleCondition(boolean condition) {
        if (condition) {
            LOGGER.info("Condition is true");
        } else {
            LOGGER.info("Condition is false");
        }
    }
    
    /**
     * Discount request data class
     */
    public static class DiscountRequest {
        public final int age;
        public final boolean isMember;
        public final boolean isHoliday;
        public final int purchaseAmount;
        public final boolean hasLoyaltyCard;
        
        public DiscountRequest(int age, boolean isMember, boolean isHoliday, 
                             int purchaseAmount, boolean hasLoyaltyCard) {
            this.age = age;
            this.isMember = isMember;
            this.isHoliday = isHoliday;
            this.purchaseAmount = purchaseAmount;
            this.hasLoyaltyCard = hasLoyaltyCard;
        }
    }
}
