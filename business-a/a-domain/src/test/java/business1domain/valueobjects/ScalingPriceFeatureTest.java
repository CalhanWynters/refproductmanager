package business1domain.valueobjects;

import com.github.calhanwynters.business1domain.valueobjects.ScalingPriceFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScalingPriceFeatureTest {

    // Helper method to create a standard feature for testing
    private ScalingPriceFeature createTestFeature() {
        // Base: 5.00, Increment: 2.00/unit, Max: 10 units
        return new ScalingPriceFeature(
                "SCALING_PLAN",
                "Price scales by unit count.",
                "Scaling Plan",
                "units",
                5.00,
                2.00,
                10
        );
    }

    @Test
    public void testPriceCalculationForZeroQuantity() {
        ScalingPriceFeature feature = createTestFeature();
        int quantity = 0;

        System.out.println("--- Test: Zero Quantity ---");
        System.out.println("Feature Details: " + feature);
        System.out.println("Quantity: " + quantity);

        // Calculation: 5.00 + (2.00 * 0) = 5.00
        double expectedPrice = 5.00;
        double actualPrice = feature.calculatePrice(quantity);

        System.out.println("Expected Price: " + expectedPrice);
        System.out.println("Actual Price: " + actualPrice);

        assertEquals(expectedPrice, actualPrice, 0.001, "Price with 0 quantity should equal base amount");
    }

    @Test
    public void testPriceCalculationForNormalQuantity() {
        ScalingPriceFeature feature = createTestFeature();
        int quantity = 3;

        System.out.println("--- Test: Normal Quantity ---");
        System.out.println("Feature Details: " + feature);
        System.out.println("Quantity: " + quantity);

        // Calculation: 5.00 + (2.00 * 3) = 11.00
        double expectedPrice = 11.00;
        double actualPrice = feature.calculatePrice(quantity);

        System.out.println("Expected Price: " + expectedPrice);
        System.out.println("Actual Price: " + actualPrice);

        assertEquals(expectedPrice, actualPrice, 0.001, "Price calculation for 3 units is incorrect");
    }

    @Test
    public void testPriceCalculationForMaxQuantity() {
        ScalingPriceFeature feature = createTestFeature();
        int quantity = 10; // Exactly the max quantity

        System.out.println("--- Test: Max Quantity ---");
        System.out.println("Feature Details: " + feature);
        System.out.println("Quantity: " + quantity);

        // Calculation: 5.00 + (2.00 * 10) = 25.00
        double expectedPrice = 25.00;
        double actualPrice = feature.calculatePrice(quantity);

        System.out.println("Expected Price: " + expectedPrice);
        System.out.println("Actual Price: " + actualPrice);

        assertEquals(expectedPrice, actualPrice, 0.001, "Price calculation at max quantity boundary is incorrect");
    }

    @Test
    public void testNegativeQuantityThrowsException() {
        ScalingPriceFeature feature = createTestFeature();
        int quantity = -1;

        System.out.println("--- Test: Negative Quantity Throws Exception ---");
        System.out.println("Feature Details: " + feature);
        System.out.println("Attempting calculation with Quantity: " + quantity);

        // Verify that calling the method with a negative number throws the expected exception
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> feature.calculatePrice(quantity));

        System.out.println("Caught expected exception: " + thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Quantity must be non-negative"));
    }

    @Test
    public void testQuantityExceedingMaxThrowsException() {
        ScalingPriceFeature feature = createTestFeature();
        int quantity = 11; // One more than the max of 10

        System.out.println("--- Test: Quantity Exceeding Max Throws Exception ---");
        System.out.println("Feature Details: " + feature);
        System.out.println("Attempting calculation with Quantity: " + quantity);

        // Verify that exceeding max quantity throws the expected exception
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> feature.calculatePrice(quantity));

        System.out.println("Caught expected exception: " + thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Quantity exceeds maximum allowed."));
    }

    @Test
    public void testNoMaxQuantityConstraintWhenMaxIsNegative() {
        // Create a feature with maxQuantity set to -1 (often used to mean 'unlimited')
        ScalingPriceFeature unlimitedFeature = new ScalingPriceFeature(
                "UNLIMITED_PLAN", "Unlimited usage.", "Unlimited", "units", 10.00, 1.00, -1
        );

        int largeQuantity = 1000;
        System.out.println("--- Test: No Max Quantity Constraint (Max is Negative) ---");
        System.out.println("Feature Details: " + unlimitedFeature);
        System.out.println("Quantity: " + largeQuantity);

        // Calculation: 10.00 + (1.00 * 1000) = 1010.00
        double expectedPrice = 1010.00;
        double actualPrice = unlimitedFeature.calculatePrice(largeQuantity);

        System.out.println("Expected Price: " + expectedPrice);
        System.out.println("Actual Price: " + actualPrice);

        // This should run without throwing an exception because maxQuantity < 0
        assertEquals(expectedPrice, actualPrice, 0.001);
    }
}
