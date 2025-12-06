package com.github.calhanwynters.business1domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain value object representing the unique identifier for a Variant Entity.
 */
public record VariantId(String value) {
    public VariantId {
        Objects.requireNonNull(value, "VariantId value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("VariantId value cannot be empty or blank");
        }
    }

    public static VariantId generate() {
        return new VariantId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}