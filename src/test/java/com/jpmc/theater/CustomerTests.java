package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerTests {

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer("Rajesh", "1");
        assertEquals("Rajesh", customer.getName());
        assertEquals("1", customer.getId());
    }

    @Test
    void testCreateCustomerWithEmptyName() {
        assertThrows(IllegalStateException.class, () -> {
            new Customer("", "1");
        });
    }

    @Test
    void testCreateCustomerWithMissingName() {
        assertThrows(IllegalStateException.class, () -> {
            new Customer(null, "1");
        });
    }

    @Test
    void testCreateCustomerWithEmptyId() {
        assertThrows(IllegalStateException.class, () -> {
            new Customer("Rajesh", "  ");
        });
    }

    @Test
    void testCreateCustomerWithMissingId() {
        assertThrows(IllegalStateException.class, () -> {
            new Customer("Rajesh", null);
        });
    }

}
