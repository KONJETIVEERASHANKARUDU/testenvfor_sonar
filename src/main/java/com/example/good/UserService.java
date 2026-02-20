package com.example.good;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service class for managing user operations.
 * Demonstrates clean code practices and proper error handling.
 */
public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final List<User> users;
    
    public UserService() {
        this.users = new ArrayList<>();
    }
    
    /**
     * Creates a new user with validation.
     * 
     * @param user the user to create
     * @return the created user
     * @throws IllegalArgumentException if user is null or invalid
     */
    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        validateUser(user);
        users.add(user);
        LOGGER.info("User created successfully: " + user.getUsername());
        return user;
    }
    
    /**
     * Finds a user by username.
     * 
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    public Optional<User> findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return users.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }
    
    /**
     * Retrieves all active users.
     * 
     * @return list of active users
     */
    public List<User> getActiveUsers() {
        return users.stream()
                .filter(User::isActive)
                .toList();
    }
    
    /**
     * Updates user information.
     * 
     * @param username the username of the user to update
     * @param updatedUser the updated user information
     * @return true if update was successful
     */
    public boolean updateUser(String username, User updatedUser) {
        Optional<User> existingUser = findUserByUsername(username);
        
        if (existingUser.isEmpty()) {
            LOGGER.warning("User not found for update: " + username);
            return false;
        }
        
        validateUser(updatedUser);
        users.remove(existingUser.get());
        users.add(updatedUser);
        LOGGER.info("User updated successfully: " + username);
        return true;
    }
    
    /**
     * Deletes a user by username.
     * 
     * @param username the username of the user to delete
     * @return true if deletion was successful
     */
    public boolean deleteUser(String username) {
        Optional<User> user = findUserByUsername(username);
        
        if (user.isEmpty()) {
            LOGGER.warning("User not found for deletion: " + username);
            return false;
        }
        
        users.remove(user.get());
        LOGGER.info("User deleted successfully: " + username);
        return true;
    }
    
    /**
     * Validates user data.
     * 
     * @param user the user to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
    
    /**
     * Gets the total number of users.
     * 
     * @return the user count
     */
    public int getUserCount() {
        return users.size();
    }
}
