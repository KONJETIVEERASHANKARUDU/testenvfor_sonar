package com.example.utils;

import java.util.List;
import java.util.ArrayList;

/**
 * Utility class for common data processing logic
 * EXTRACTED FROM DUPLICATE CODE to eliminate redundancy
 */
public class DataProcessor {
    
    private DataProcessor() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Process data by cleaning and normalizing
     * Extracted from processCustomerData, processVendorData, processEmployeeData
     * 
     * @param data Input data list
     * @return Processed and cleaned data
     */
    public static List<String> processData(List<String> data) {
        List<String> processed = new ArrayList<>();
        for (String item : data) {
            if (item != null && !item.isEmpty()) {
                String cleaned = item.trim().toUpperCase();
                if (cleaned.length() > 0) {
                    processed.add(cleaned);
                    System.out.println("Processed item: " + cleaned);
                }
            }
        }
        System.out.println("Total items processed: " + processed.size());
        return processed;
    }
}
