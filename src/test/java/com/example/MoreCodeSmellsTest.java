package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoreCodeSmellsTest {
    
    private MoreCodeSmells moreCodeSmells;
    
    @BeforeEach
    void setUp() {
        moreCodeSmells = new MoreCodeSmells();
    }
    
    @Test
    void testIsActive() {
        assertTrue(moreCodeSmells.isActive());
    }
    
    @Test
    void testGetStatus() {
        assertEquals("OK", moreCodeSmells.getStatus(200));
        assertEquals("Unauthorized", moreCodeSmells.getStatus(401));
        assertEquals("Forbidden", moreCodeSmells.getStatus(403));
        assertEquals("Not Found", moreCodeSmells.getStatus(404));
        assertEquals("Error", moreCodeSmells.getStatus(500));
        assertEquals("Unknown", moreCodeSmells.getStatus(999));
    }
    
    @Test
    void testProcessCode() {
        assertDoesNotThrow(() -> {
            moreCodeSmells.processCode(1);
            moreCodeSmells.processCode(2);
            moreCodeSmells.processCode(3);
            moreCodeSmells.processCode(999);
        });
    }
    
    @Test
    void testCalculateDiscount() {
        MoreCodeSmells.DiscountRequest request = new MoreCodeSmells.DiscountRequest(
            70, true, true, 600, true
        );
        
        int discount = moreCodeSmells.calculateDiscount(request);
        
        assertTrue(discount > 0);
    }
    
    @Test
    void testCalculateDiscountSenior() {
        MoreCodeSmells.DiscountRequest request = new MoreCodeSmells.DiscountRequest(
            70, false, false, 50, false
        );
        
        int discount = moreCodeSmells.calculateDiscount(request);
        
        assertEquals(10, discount);
    }
    
    @Test
    void testCalculateDiscountMinor() {
        MoreCodeSmells.DiscountRequest request = new MoreCodeSmells.DiscountRequest(
            15, false, false, 50, false
        );
        
        int discount = moreCodeSmells.calculateDiscount(request);
        
        assertEquals(5, discount);
    }
    
    @Test
    void testCalculateDiscountMember() {
        MoreCodeSmells.DiscountRequest request = new MoreCodeSmells.DiscountRequest(
            30, true, false, 50, false
        );
        
        int discount = moreCodeSmells.calculateDiscount(request);
        
        assertEquals(5, discount);
    }
    
    @Test
    void testCalculateDiscountLoyalty() {
        MoreCodeSmells.DiscountRequest request = new MoreCodeSmells.DiscountRequest(
            30, false, false, 50, true
        );
        
        int discount = moreCodeSmells.calculateDiscount(request);
        
        assertEquals(5, discount);
    }
    
    @Test
    void testCalculateDiscountHoliday() {
        MoreCodeSmells.DiscountRequest request = new MoreCodeSmells.DiscountRequest(
            30, false, true, 50, false
        );
        
        int discount = moreCodeSmells.calculateDiscount(request);
        
        assertEquals(15, discount);
    }
    
    @Test
    void testCalculateDiscountHighPurchase() {
        MoreCodeSmells.DiscountRequest request = new MoreCodeSmells.DiscountRequest(
            30, false, true, 200, false
        );
        
        int discount = moreCodeSmells.calculateDiscount(request);
        
        assertTrue(discount >= 25);
    }
    
    @Test
    void testCalculateDiscountVeryHighPurchase() {
        MoreCodeSmells.DiscountRequest request = new MoreCodeSmells.DiscountRequest(
            30, false, true, 600, false
        );
        
        int discount = moreCodeSmells.calculateDiscount(request);
        
        assertTrue(discount >= 35);
    }
    
    @Test
    void testDoSomething() {
        assertDoesNotThrow(() -> {
            moreCodeSmells.doSomething();
        });
    }
    
    @Test
    void testProcess() {
        assertDoesNotThrow(() -> {
            moreCodeSmells.process();
        });
    }
    
    @Test
    void testBuildString() {
        List<String> items = Arrays.asList("a", "b", "c");
        String result = moreCodeSmells.buildString(items);
        
        assertEquals("a, b, c", result);
    }
    
    @Test
    void testBuildStringEmpty() {
        List<String> items = Arrays.asList();
        String result = moreCodeSmells.buildString(items);
        
        assertEquals("", result);
    }
    
    @Test
    void testBuildStringNull() {
        String result = moreCodeSmells.buildString(null);
        
        assertEquals("", result);
    }
    
    @Test
    void testBuildStringSingleItem() {
        List<String> items = Arrays.asList("a");
        String result = moreCodeSmells.buildString(items);
        
        assertEquals("a", result);
    }
    
    @Test
    void testHandleCondition() {
        assertDoesNotThrow(() -> {
            moreCodeSmells.handleCondition(true);
            moreCodeSmells.handleCondition(false);
        });
    }
    
    @Test
    void testDiscountRequest() {
        MoreCodeSmells.DiscountRequest request = new MoreCodeSmells.DiscountRequest(
            25, true, false, 100, true
        );
        
        assertEquals(25, request.age);
        assertTrue(request.isMember);
        assertFalse(request.isHoliday);
        assertEquals(100, request.purchaseAmount);
        assertTrue(request.hasLoyaltyCard);
    }
}
