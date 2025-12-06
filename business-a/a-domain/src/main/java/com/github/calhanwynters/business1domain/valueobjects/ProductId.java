package com.github.calhanwynters.business1domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain value object representing the unique identifier for a Product aggregate.
 */
public record ProductId(String value) {
    public ProductId {
        Objects.requireNonNull(value, "ProductId value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("ProductId value cannot be empty or blank");
        }
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}