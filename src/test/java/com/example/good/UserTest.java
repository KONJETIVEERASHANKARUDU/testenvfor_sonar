package com.example.good;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    
    @Test
    void testUserCreation() {
        User user = new User("john_doe", "john@example.com", true);
        
        assertEquals("john_doe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertTrue(user.isActive());
        assertNotNull(user.getCreatedAt());
    }
    
    @Test
    void testUserEquals() {
        User user1 = new User("john", "john@example.com", true);
        User user2 = new User("john", "john@example.com", true);
        User user3 = new User("jane", "jane@example.com", false);
        
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, "string");
    }
    
    @Test
    void testUserHashCode() {
        User user1 = new User("john", "john@example.com", true);
        User user2 = new User("john", "john@example.com", true);
        
        assertEquals(user1.hashCode(), user2.hashCode());
    }
    
    @Test
    void testUserToString() {
        User user = new User("john", "john@example.com", true);
        String toString = user.toString();
        
        assertTrue(toString.contains("john"));
        assertTrue(toString.contains("john@example.com"));
        assertTrue(toString.contains("active=true"));
    }
    
    @Test
    void testInactiveUser() {
        User user = new User("inactive", "user@example.com", false);
        
        assertFalse(user.isActive());
    }
}
