package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Additional code smells for comprehensive SonarQube testing
 */
public class MoreCodeSmells {
    
    // Code Smell: Public mutable static field
    public static List<String> publicList = new ArrayList<>();
    
    // Code Smell: Dead code
    private void neverCalledMethod() {
        System.out.println("This method is never called");
    }
    
    // Code Smell: Boolean method name doesn't start with is/has/can
    public boolean active() {
        return true;
    }
    
    // Code Smell: Multiple return statements
    public String getStatus(int code) {
        if (code == 200) {
            return "OK";
        }
        if (code == 404) {
            return "Not Found";
        }
        if (code == 500) {
            return "Error";
        }
        if (code == 403) {
            return "Forbidden";
        }
        if (code == 401) {
            return "Unauthorized";
        }
        return "Unknown";
    }
    
    // Code Smell: Switch statement without default
    public void processCode(int code) {
        switch (code) {
            case 1:
                System.out.println("One");
                break;
            case 2:
                System.out.println("Two");
                break;
            case 3:
                System.out.println("Three");
                break;
            // Missing default case
        }
    }
    
    // Code Smell: Cognitive complexity too high
    public int calculateDiscount(int age, boolean isMember, boolean isHoliday, 
                                  int purchaseAmount, boolean hasLoyaltyCard) {
        int discount = 0;
        
        if (age > 65) {
            discount += 10;
            if (isMember) {
                discount += 5;
                if (hasLoyaltyCard) {
                    discount += 5;
                }
            }
        } else if (age < 18) {
            discount += 5;
        }
        
        if (isHoliday) {
            discount += 15;
            if (purchaseAmount > 100) {
                discount += 10;
                if (purchaseAmount > 500) {
                    discount += 20;
                }
            }
        }
        
        if (isMember && hasLoyaltyCard) {
            discount += 5;
        }
        
        return discount;
    }
    
    // Code Smell: Method naming convention violation
    public void DoSomething() {  // Should be doSomething
        System.out.println("Bad naming");
    }
    
    // Code Smell: Commented out code
    public void process() {
        System.out.println("Active code");
        // System.out.println("Old implementation");
        // for (int i = 0; i < 10; i++) {
        //     System.out.println(i);
        // }
        // calculateOldWay();
    }
    
    // Code Smell: String concatenation in loop
    public String buildString(List<String> items) {
        String result = "";
        for (String item : items) {
            result = result + item + ", ";  // Use StringBuilder instead
        }
        return result;
    }
    
    // Code Smell: Identical branches
    public void identicalBranches(boolean condition) {
        if (condition) {
            System.out.println("Same action");
            System.out.println("More same action");
        } else {
            System.out.println("Same action");
            System.out.println("More same action");
        }
    }
}
