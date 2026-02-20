package com.example.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        userService = new UserService();
    }
    
    @Test
    void testCreateUser() {
        User user = new User("john", "john@example.com", true);
        User created = userService.createUser(user);
        
        assertEquals(user, created);
        assertEquals(1, userService.getUserCount());
    }
    
    @Test
    void testCreateUserNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(null);
        });
    }
    
    @Test
    void testCreateUserInvalidUsername() {
        User user = new User("", "john@example.com", true);
        
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });
    }
    
    @Test
    void testCreateUserInvalidEmail() {
        User user = new User("john", "invalid-email", true);
        
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });
    }
    
    @Test
    void testFindUserByUsername() {
        User user = new User("john", "john@example.com", true);
        userService.createUser(user);
        
        Optional<User> found = userService.findUserByUsername("john");
        
        assertTrue(found.isPresent());
        assertEquals("john", found.get().getUsername());
    }
    
    @Test
    void testFindUserByUsernameNotFound() {
        Optional<User> found = userService.findUserByUsername("nonexistent");
        
        assertFalse(found.isPresent());
    }
    
    @Test
    void testFindUserByUsernameNull() {
        Optional<User> found = userService.findUserByUsername(null);
        
        assertFalse(found.isPresent());
    }
    
    @Test
    void testGetActiveUsers() {
        userService.createUser(new User("john", "john@example.com", true));
        userService.createUser(new User("jane", "jane@example.com", true));
        userService.createUser(new User("bob", "bob@example.com", false));
        
        List<User> activeUsers = userService.getActiveUsers();
        
        assertEquals(2, activeUsers.size());
    }
    
    @Test
    void testUpdateUser() {
        User user = new User("john", "john@example.com", true);
        userService.createUser(user);
        
        User updated = new User("john", "newemail@example.com", true);
        boolean result = userService.updateUser("john", updated);
        
        assertTrue(result);
    }
    
    @Test
    void testUpdateUserNotFound() {
        User updated = new User("nonexistent", "email@example.com", true);
        boolean result = userService.updateUser("nonexistent", updated);
        
        assertFalse(result);
    }
    
    @Test
    void testDeleteUser() {
        User user = new User("john", "john@example.com", true);
        userService.createUser(user);
        
        boolean result = userService.deleteUser("john");
        
        assertTrue(result);
        assertEquals(0, userService.getUserCount());
    }
    
    @Test
    void testDeleteUserNotFound() {
        boolean result = userService.deleteUser("nonexistent");
        
        assertFalse(result);
    }
    
    @Test
    void testGetUserCount() {
        assertEquals(0, userService.getUserCount());
        
        userService.createUser(new User("john", "john@example.com", true));
        userService.createUser(new User("jane", "jane@example.com", true));
        
        assertEquals(2, userService.getUserCount());
    }
}
