package com.github.calhanwynters.business1domain.valueobjects;

import com.github.calhanwynters.business1domain.valueobjects.FixedPriceFeature;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FixedPriceFeatureTest {

    @Test
    public void testFixedPriceFeatureCreationAndGetters() {
        String name = "PREMIUM_SUPPORT";
        String description = "Feature providing 24/7 support.";
        String label = "Premium Support";
        double price = 99.99;

        System.out.println("--- Test: Feature Creation and Getters ---");
        System.out.printf("Input Data -> Name: %s, Price: $%.2f%n", name, price);

        // 1. Arrange: Create the instance
        FixedPriceFeature feature = new FixedPriceFeature(name, description, label, price);

        System.out.println("Created Feature Instance: " + feature);

        // 2. Assert: Verify all data points are correctly stored and retrieved
        assertNotNull(feature);

        // Test methods inherited from FeatureInterface (via generated record getters)
        assertEquals(name, feature.name());
        assertEquals(description, feature.featureDescription());
        assertEquals(label, feature.label());

        System.out.println("Verified Name: " + feature.name());
        System.out.println("Verified Label: " + feature.label());
        System.out.println("Verified Price (via getFixedPrice): " + feature.getFixedPrice());

        // Test the explicit getFixedPrice method
        assertEquals(price, feature.getFixedPrice(), 0.001); // Use delta for double comparisons

        // Test the implicit record getter name() as well, ensuring consistency
        assertEquals(price, feature.fixedPrice());
        System.out.println("--- Test Passed ---");
    }

    @Test
    public void testZeroPriceIsValid() {
        System.out.println("--- Test: Zero Price Is Valid ---");

        // A common business rule might require prices >= 0. Testing ensures this works.
        FixedPriceFeature feature = new FixedPriceFeature("FREE_PLAN", "Free tier plan", "Free Plan", 0.00);

        System.out.println("Created Feature Instance: " + feature);
        System.out.println("Verified Price: " + feature.getFixedPrice());

        assertEquals(0.00, feature.getFixedPrice(), 0.001);
        assertTrue(feature.getFixedPrice() >= 0.00, "Price should be non-negative.");
        System.out.println("--- Test Passed ---");
    }

    // Note: This record currently allows nulls and negative prices.
    // If your business logic forbids these, you would add constructor validation
    // and corresponding tests (similar to the BasicFeature example).
}
