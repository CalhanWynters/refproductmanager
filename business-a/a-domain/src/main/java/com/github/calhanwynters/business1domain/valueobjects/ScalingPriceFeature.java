package com.github.calhanwynters.business1domain.valueobjects;

public record ScalingPriceFeature(
        String name,
        String featureDescription,
        String label,
        String measurementUnit,  // New field for the measuring unit
        double baseAmount,
        double incrementAmount,
        int maxQuantity) implements FeatureInterface {

    /**
     * Calculates the total adjusted price based on the quantity.
     * @param quantity the number of units (e.g., ounces, length)
     * @return the calculated price based on the specified quantity
     */
    public double calculatePrice(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
        if (maxQuantity >= 0 && quantity > maxQuantity) {
            throw new IllegalArgumentException("Quantity exceeds maximum allowed.");
        }

        return baseAmount + (incrementAmount * quantity);
    }
}
