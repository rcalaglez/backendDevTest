package com.example.similarproducts.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDetailTest {

    @Test
    void shouldCreateProductDetailWithAllFields() {
        ProductDetail product = new ProductDetail("1", "Shirt", 9.99, true);

        assertEquals("1", product.id());
        assertEquals("Shirt", product.name());
        assertEquals(9.99, product.price(), 0.001);
        assertTrue(product.availability());
    }

    @Test
    void shouldCreateProductWithFalseAvailability() {
        ProductDetail product = new ProductDetail("3", "Blazer", 29.99, false);

        assertEquals("3", product.id());
        assertFalse(product.availability());
    }

    @Test
    void shouldBeEqualWhenSameValues() {
        ProductDetail product1 = new ProductDetail("1", "Shirt", 9.99, true);
        ProductDetail product2 = new ProductDetail("1", "Shirt", 9.99, true);

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValues() {
        ProductDetail product1 = new ProductDetail("1", "Shirt", 9.99, true);
        ProductDetail product2 = new ProductDetail("2", "Dress", 19.99, true);

        assertNotEquals(product1, product2);
    }

    @Test
    void shouldHaveReadableToString() {
        ProductDetail product = new ProductDetail("1", "Shirt", 9.99, true);

        String toString = product.toString();

        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Shirt"));
        assertTrue(toString.contains("9.99"));
        assertTrue(toString.contains("true"));
    }
}
