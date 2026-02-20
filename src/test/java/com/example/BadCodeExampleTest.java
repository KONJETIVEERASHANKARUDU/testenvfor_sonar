package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BadCodeExampleTest {
    
    @Mock
    private DataSource dataSource;
    
    @Mock
    private Connection connection;
    
    @Mock
    private PreparedStatement preparedStatement;
    
    private BadCodeExample badCodeExample;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        badCodeExample = new BadCodeExample(dataSource);
    }
    
    @Test
    void testCalculateTotal() {
        assertEquals(100, badCodeExample.calculateTotal(1));
        assertEquals(1000, badCodeExample.calculateTotal(10));
        assertEquals(0, badCodeExample.calculateTotal(0));
    }
    
    @Test
    void testExecuteQuerySuccess() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        assertDoesNotThrow(() -> {
            badCodeExample.executeQuery("testuser");
        });
        
        verify(preparedStatement).setString(1, "testuser");
        verify(preparedStatement).executeQuery();
    }
    
    @Test
    void testExecuteQueryNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            badCodeExample.executeQuery(null);
        });
    }
    
    @Test
    void testExecuteQueryEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            badCodeExample.executeQuery("");
        });
    }
    
    @Test
    void testExecuteQuerySqlException() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));
        
        assertThrows(SQLException.class, () -> {
            badCodeExample.executeQuery("testuser");
        });
    }
    
    @Test
    void testAreAllPositive() {
        assertTrue(badCodeExample.areAllPositive(1, 2, 3));
        assertFalse(badCodeExample.areAllPositive(1, 0, 3));
        assertFalse(badCodeExample.areAllPositive(-1, 2, 3));
        assertTrue(badCodeExample.areAllPositive(1));
        assertTrue(badCodeExample.areAllPositive());
    }
    
    @Test
    void testCalculateNestedSum() {
        assertEquals(0, badCodeExample.calculateNestedSum(0));
        assertEquals(0, badCodeExample.calculateNestedSum(1));
        assertEquals(45, badCodeExample.calculateNestedSum(10));
    }
    
    @Test
    void testProperExceptionHandling() {
        assertThrows(IllegalStateException.class, () -> {
            badCodeExample.properExceptionHandling();
        });
    }
    
    @Test
    void testProcessWithConfig() {
        BadCodeExample.ProcessConfig config = new BadCodeExample.ProcessConfig("p1", "p2", "p3");
        
        assertDoesNotThrow(() -> {
            badCodeExample.processWithConfig(config);
        });
    }
    
    @Test
    void testProcessWithConfigNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            badCodeExample.processWithConfig(null);
        });
    }
    
    @Test
    void testProcessConfigToString() {
        BadCodeExample.ProcessConfig config = new BadCodeExample.ProcessConfig("a", "b", "c");
        String toString = config.toString();
        
        assertTrue(toString.contains("a"));
        assertTrue(toString.contains("b"));
        assertTrue(toString.contains("c"));
    }
}
