package com.example.good;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    
    @Test
    void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty("text"));
        assertFalse(StringUtils.isEmpty(" "));
    }
    
    @Test
    void testIsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank("   "));
        assertFalse(StringUtils.isBlank("text"));
    }
    
    @Test
    void testSafeTrim() {
        assertNull(StringUtils.safeTrim(null));
        assertEquals("text", StringUtils.safeTrim("  text  "));
        assertEquals("", StringUtils.safeTrim("   "));
    }
    
    @Test
    void testCapitalize() {
        assertEquals("Hello", StringUtils.capitalize("hello"));
        assertEquals("H", StringUtils.capitalize("h"));
        assertNull(StringUtils.capitalize(null));
        assertEquals("", StringUtils.capitalize(""));
        assertEquals("A", StringUtils.capitalize("A"));
    }
    
    @Test
    void testReverse() {
        assertEquals("olleh", StringUtils.reverse("hello"));
        assertNull(StringUtils.reverse(null));
        assertEquals("", StringUtils.reverse(""));
        assertEquals("a", StringUtils.reverse("a"));
    }
    
    @Test
    void testSplitAndTrim() {
        List<String> result = StringUtils.splitAndTrim("a, b, c", ",");
        
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }
    
    @Test
    void testSplitAndTrimEmpty() {
        List<String> result = StringUtils.splitAndTrim("", ",");
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testSplitAndTrimNull() {
        List<String> result = StringUtils.splitAndTrim(null, ",");
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testIsAlphanumeric() {
        assertTrue(StringUtils.isAlphanumeric("abc123"));
        assertTrue(StringUtils.isAlphanumeric("ABC"));
        assertTrue(StringUtils.isAlphanumeric("123"));
        assertFalse(StringUtils.isAlphanumeric("abc-123"));
        assertFalse(StringUtils.isAlphanumeric("abc 123"));
        assertFalse(StringUtils.isAlphanumeric(""));
        assertFalse(StringUtils.isAlphanumeric(null));
    }
    
    @Test
    void testTruncate() {
        assertEquals("hello...", StringUtils.truncate("hello world", 5));
        assertEquals("hello", StringUtils.truncate("hello", 10));
        assertNull(StringUtils.truncate(null, 5));
        assertEquals("", StringUtils.truncate("", 5));
    }
    
    @Test
    void testMaskSensitiveData() {
        assertEquals("123*******", StringUtils.maskSensitiveData("1234567890", 3));
        assertEquals("***", StringUtils.maskSensitiveData("abc", 5));
        assertEquals("***", StringUtils.maskSensitiveData("", 3));
        assertEquals("***", StringUtils.maskSensitiveData(null, 3));
    }
    
    @Test
    void testConstructorThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var constructor = StringUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
