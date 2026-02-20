package com.example.good;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {
    
    @Test
    void testIsValidEmail() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"));
        assertTrue(ValidationUtils.isValidEmail("user.name@example.co.uk"));
        assertTrue(ValidationUtils.isValidEmail("test+tag@example.com"));
        
        assertFalse(ValidationUtils.isValidEmail(""));
        assertFalse(ValidationUtils.isValidEmail(null));
        assertFalse(ValidationUtils.isValidEmail("invalid"));
        assertFalse(ValidationUtils.isValidEmail("@example.com"));
        assertFalse(ValidationUtils.isValidEmail("test@"));
        assertFalse(ValidationUtils.isValidEmail("test @example.com"));
    }
    
    @Test
    void testIsValidPhone() {
        assertTrue(ValidationUtils.isValidPhone("+1234567890"));
        assertTrue(ValidationUtils.isValidPhone("+12345678901234"));
        assertTrue(ValidationUtils.isValidPhone("1234567890"));
        
        assertFalse(ValidationUtils.isValidPhone(""));
        assertFalse(ValidationUtils.isValidPhone(null));
        assertFalse(ValidationUtils.isValidPhone("123"));
        assertFalse(ValidationUtils.isValidPhone("+0123456789"));
        assertFalse(ValidationUtils.isValidPhone("abc1234567890"));
    }
    
    @Test
    void testIsValidUsername() {
        assertTrue(ValidationUtils.isValidUsername("user"));
        assertTrue(ValidationUtils.isValidUsername("user123"));
        assertTrue(ValidationUtils.isValidUsername("user_name"));
        assertTrue(ValidationUtils.isValidUsername("user-name"));
        assertTrue(ValidationUtils.isValidUsername("a".repeat(20)));
        
        assertFalse(ValidationUtils.isValidUsername(""));
        assertFalse(ValidationUtils.isValidUsername(null));
        assertFalse(ValidationUtils.isValidUsername("ab"));
        assertFalse(ValidationUtils.isValidUsername("a".repeat(21)));
        assertFalse(ValidationUtils.isValidUsername("user name"));
        assertFalse(ValidationUtils.isValidUsername("user@name"));
    }
    
    @Test
    void testIsValidLength() {
        assertTrue(ValidationUtils.isValidLength("hello", 3, 10));
        assertTrue(ValidationUtils.isValidLength("abc", 3, 3));
        
        assertFalse(ValidationUtils.isValidLength("ab", 3, 10));
        assertFalse(ValidationUtils.isValidLength("hello world", 3, 10));
        assertFalse(ValidationUtils.isValidLength(null, 3, 10));
    }
    
    @Test
    void testIsInRange() {
        assertTrue(ValidationUtils.isInRange(5, 1, 10));
        assertTrue(ValidationUtils.isInRange(1, 1, 10));
        assertTrue(ValidationUtils.isInRange(10, 1, 10));
        
        assertFalse(ValidationUtils.isInRange(0, 1, 10));
        assertFalse(ValidationUtils.isInRange(11, 1, 10));
    }
    
    @Test
    void testConstructorThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var constructor = ValidationUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
