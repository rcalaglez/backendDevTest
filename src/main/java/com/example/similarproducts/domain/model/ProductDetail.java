package com.example.similarproducts.domain.model;

public record ProductDetail(
        String id,
        String name,
        double price,
        boolean availability
) {
}
