package com.example.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class with INTENTIONAL FAILURES
 * This will trigger CI failure and activate the Failure Recovery Agent
 */
public class IntentionalFailureTest {
    
    @Test
    public void testThatShouldFail() {
        // INTENTIONAL FAILURE - This will make CI fail
        int expected = 5;
        int actual = 3;
        
        assertEquals(expected, actual, "This test is designed to fail for agent verification");
    }
    
    @Test
    public void testAnotherFailure() {
        // INTENTIONAL FAILURE #2
        String expected = "Hello";
        String actual = "World";
        
        assertEquals(expected, actual, "Another intentional failure");
    }
    
    @Test
    public void testThatPasses() {
        // This one passes - to show mixed results
        assertEquals(2 + 2, 4, "Basic math should work");
    }
    
    @Test
    public void testNullPointerException() {
        // INTENTIONAL ERROR - Will cause exception
        String text = null;
        assertTrue(text.length() > 0, "This will throw NullPointerException");
    }
}
