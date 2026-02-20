package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateCodeTest {
    
    private DuplicateCode duplicateCode;
    
    @BeforeEach
    void setUp() {
        duplicateCode = new DuplicateCode();
    }
    
    @Test
    void testProcessUserData() {
        assertDoesNotThrow(() -> {
            duplicateCode.processUserData("John", "john@example.com");
        });
    }
    
    @Test
    void testProcessUserDataInvalidName() {
        assertDoesNotThrow(() -> {
            duplicateCode.processUserData(null, "john@example.com");
        });
        
        assertDoesNotThrow(() -> {
            duplicateCode.processUserData("", "john@example.com");
        });
    }
    
    @Test
    void testProcessUserDataInvalidEmail() {
        assertDoesNotThrow(() -> {
            duplicateCode.processUserData("John", null);
        });
        
        assertDoesNotThrow(() -> {
            duplicateCode.processUserData("John", "");
        });
    }
    
    @Test
    void testProcessCustomerData() {
        assertDoesNotThrow(() -> {
            duplicateCode.processCustomerData("Jane", "jane@example.com");
        });
    }
    
    @Test
    void testProcessCustomerDataInvalidInput() {
        assertDoesNotThrow(() -> {
            duplicateCode.processCustomerData(null, "jane@example.com");
        });
    }
    
    @Test
    void testProcessEmployeeData() {
        assertDoesNotThrow(() -> {
            duplicateCode.processEmployeeData("Bob", "bob@example.com");
        });
    }
    
    @Test
    void testProcessEmployeeDataInvalidInput() {
        assertDoesNotThrow(() -> {
            duplicateCode.processEmployeeData("Bob", null);
        });
    }
    
    @Test
    void testFormatAddress() {
        String result = duplicateCode.formatAddress("123 Main St", "New York", "10001");
        
        assertNotNull(result);
        assertTrue(result.contains("123 Main St"));
        assertTrue(result.contains("New York"));
        assertTrue(result.contains("10001"));
        assertTrue(result.contains("USA"));
    }
    
    @Test
    void testFormatShippingAddress() {
        String result = duplicateCode.formatShippingAddress("456 Oak Ave", "Boston", "02101");
        
        assertNotNull(result);
        assertTrue(result.contains("456 Oak Ave"));
        assertTrue(result.contains("Boston"));
        assertTrue(result.contains("02101"));
    }
    
    @Test
    void testFormatBillingAddress() {
        String result = duplicateCode.formatBillingAddress("789 Pine Rd", "Chicago", "60601");
        
        assertNotNull(result);
        assertTrue(result.contains("789 Pine Rd"));
        assertTrue(result.contains("Chicago"));
        assertTrue(result.contains("60601"));
    }
    
    @Test
    void testAllFormatMethodsProduceSameFormat() {
        String address = duplicateCode.formatAddress("Street", "City", "ZIP");
        String shipping = duplicateCode.formatShippingAddress("Street", "City", "ZIP");
        String billing = duplicateCode.formatBillingAddress("Street", "City", "ZIP");
        
        assertEquals(address, shipping);
        assertEquals(address, billing);
    }
}
