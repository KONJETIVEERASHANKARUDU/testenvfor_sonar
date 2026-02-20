package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class SecurityIssuesTest {
    
    private SecurityIssues securityIssues;
    
    @BeforeEach
    void setUp() {
        securityIssues = new SecurityIssues();
    }
    
    @Test
    void testGenerateSecurityToken() {
        String token1 = securityIssues.generateSecurityToken();
        String token2 = securityIssues.generateSecurityToken();
        
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
        assertTrue(token1.length() > 0);
    }
    
    @Test
    void testReadFileValid() {
        File file = securityIssues.readFile("test.txt");
        
        assertNotNull(file);
        assertTrue(file.getPath().contains("test.txt"));
    }
    
    @Test
    void testReadFilePathTraversal() {
        assertThrows(SecurityException.class, () -> {
            securityIssues.readFile("../etc/passwd");
        });
    }
    
    @Test
    void testReadFileWithSlash() {
        assertThrows(SecurityException.class, () -> {
            securityIssues.readFile("folder/file.txt");
        });
    }
    
    @Test
    void testReadFileNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            securityIssues.readFile(null);
        });
    }
    
    @Test
    void testReadFileEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            securityIssues.readFile("");
        });
    }
    
    @Test
    void testAuthenticateValidCredentials() {
        assertTrue(securityIssues.authenticate("user", "password"));
    }
    
    @Test
    void testAuthenticateNullUsername() {
        assertFalse(securityIssues.authenticate(null, "password"));
    }
    
    @Test
    void testAuthenticateEmptyUsername() {
        assertFalse(securityIssues.authenticate("", "password"));
    }
    
    @Test
    void testAuthenticateNullPassword() {
        assertFalse(securityIssues.authenticate("user", null));
    }
    
    @Test
    void testAuthenticateEmptyPassword() {
        assertFalse(securityIssues.authenticate("user", ""));
    }
    
    @Test
    void testHashPassword() {
        String hashed = securityIssues.hashPassword("password123");
        
        assertNotNull(hashed);
        assertTrue(hashed.length() > 0);
        assertNotEquals("password123", hashed);
    }
    
    @Test
    void testHashPasswordNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            securityIssues.hashPassword(null);
        });
    }
    
    @Test
    void testHashPasswordEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            securityIssues.hashPassword("");
        });
    }
    
    @Test
    void testLogSystemInfo() {
        assertDoesNotThrow(() -> {
            securityIssues.logSystemInfo();
        });
    }
}
