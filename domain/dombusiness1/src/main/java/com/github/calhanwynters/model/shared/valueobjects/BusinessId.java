package com.github.calhanwynters.model.shared.valueobjects;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a unique identifier for a business.
 */
public record BusinessId(String value) {
    public BusinessId {
        Objects.requireNonNull(value, "BusinessId value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("BusinessId value cannot be empty or blank");
        }
    }

    /**
     * Generates a new BusinessId using a random UUID.
     * @return A new instance of BusinessId.
     */
    public static BusinessId generate() {

        return new BusinessId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
