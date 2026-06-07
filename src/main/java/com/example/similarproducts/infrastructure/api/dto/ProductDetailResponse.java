package com.example.similarproducts.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductDetailResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("price") double price,
        @JsonProperty("availability") boolean availability
) {
}
