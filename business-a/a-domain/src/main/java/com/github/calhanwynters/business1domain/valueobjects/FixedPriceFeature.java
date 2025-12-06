package com.github.calhanwynters.business1domain.valueobjects;

public record FixedPriceFeature(
        String name,
        String featureDescription,
        String label,
        double fixedPrice) implements FeatureInterface {

    /**
     * Retrieves the fixed price for this feature.
     * @return the fixed price
     */
    public double getFixedPrice() {
        return fixedPrice;
    }
}
