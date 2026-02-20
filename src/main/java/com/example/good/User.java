package com.example.good;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable user data model following best practices.
 */
public class User {
    private final String username;
    private final String email;
    private final boolean active;
    private final LocalDateTime createdAt;
    
    /**
     * Creates a new user instance.
     * 
     * @param username the username
     * @param email the email address
     * @param active whether the user is active
     */
    public User(String username, String email, boolean active) {
        this.username = username;
        this.email = email;
        this.active = active;
        this.createdAt = LocalDateTime.now();
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return active == user.active &&
               Objects.equals(username, user.username) &&
               Objects.equals(email, user.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username, email, active);
    }
    
    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", active=" + active +
               ", createdAt=" + createdAt +
               '}';
    }
}
