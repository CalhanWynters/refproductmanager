package com.github.calhanwynters.model.shared.entities;

import com.github.calhanwynters.model.shared.enums.VariantStatusEnums;
import com.github.calhanwynters.model.shared.valueobjects.*;
import com.github.calhanwynters.model.shared.valueobjects.WeightVO.WeightUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VariantTest {

    // Helper fields for common test data
    private VariantId testId;
    private String testSku;
    private MonetaryAmount basePrice;
    private WeightVO weight;
    private CareInstructionVO careInstructions;
    private List<FeatureInterface> features;
    private static final BigDecimal TEST_PRICE_AMOUNT = BigDecimal.valueOf(50.00);

    @BeforeEach
    public void setUp() {
        testId = VariantId.generate();
        testSku = "SKU-" + UUID.randomUUID().toString().substring(0, 8);
        CurrencyUnit usd = Monetary.getCurrency("USD");
        basePrice = Monetary.getDefaultAmountFactory()
                .setCurrency(usd)
                .setNumber(TEST_PRICE_AMOUNT)
                .create();
        BigDecimal weightAmount = BigDecimal.valueOf(1.5);
        WeightUnit unit = WeightUnit.KILOGRAM;
        weight = new WeightVO(weightAmount, unit);
        careInstructions = new CareInstructionVO("Wash cold.");
        features = Collections.emptyList();
    }

    private Variant createTestFeature() {
        return new Variant(testId, testSku, basePrice, basePrice, features, careInstructions, weight, VariantStatusEnums.DRAFT);
    }

    // ------------------------------------------------------------------
    // UPDATED TEST METHOD FOR DEMONSTRATION PRINTOUTS
    // ------------------------------------------------------------------
    @Test
    public void demonstrateVariantDataFormatAndAttributeComparisonAndMocks() {
        System.out.println("--- Demonstration Printouts ---");

        // --- Setup Mocks for this specific demo test ---
        FeatureInterface mockFeatureA = mock(FeatureInterface.class);
        FeatureInterface mockFeatureB = mock(FeatureInterface.class);
        when(mockFeatureA.name()).thenReturn("MOCKED_FEATURE_A");
        when(mockFeatureA.label()).thenReturn("Mock Feature Alpha Label");
        when(mockFeatureB.name()).thenReturn("MOCKED_FEATURE_B");
        when(mockFeatureB.label()).thenReturn("Mock Feature Beta Label");
        List<FeatureInterface> mockedFeaturesList = Arrays.asList(mockFeatureA, mockFeatureB);

        // Create variants using both real data and the mocked feature list
        // We temporarily override the `features` list in the constructor call for this specific test
        Variant variantA = new Variant(
                testId, testSku, basePrice, basePrice,
                mockedFeaturesList, // Inject mocks into Variant A
                careInstructions, weight, VariantStatusEnums.DRAFT
        );
        Variant variantB = Variant.createDraft(basePrice, weight, careInstructions, Collections.emptyList()); // Variant B has no features

        // 1. Print Format of Variant Data (using overridden toString method)
        System.out.println("\nVariant A Data Format (toString) [Has Mocks Injected]:");
        System.out.println(variantA);

        System.out.println("\nVariant B Data Format (toString) [Has No Features]:");
        System.out.println(variantB);

        // 2. Print Details of the Feature Mocks
        System.out.println("\nDetails of Injected Feature Mocks:");
        System.out.println("Mock A Name: " + variantA.features().get(0).name());
        System.out.println("Mock A Label: " + variantA.features().get(0).label());
        System.out.println("Mock B Name: " + variantA.features().get(1).name());
        System.out.println("Mock B Label: " + variantA.features().get(1).label());

        // 3. Print Output of hasSameAttributes()
        System.out.println("\nAttribute Comparison Results:");

        boolean sameAttributesResult = variantA.hasSameAttributes(variantB);
        System.out.printf("Does Variant A have the same attributes as Variant B? %b%n", sameAttributesResult);

        // Assertions for the test to pass
        assertNotNull(variantA);
        assertFalse(sameAttributesResult, "Variants should differ because Variant B has empty features list.");
        // Verify that the names were accessed during the print statements
        verify(mockFeatureA, atLeastOnce()).name();
        verify(mockFeatureB, atLeastOnce()).label();

        System.out.println("--- End Demonstration Printouts ---");
    }
    // ------------------------------------------------------------------

    // --- The rest of your standard unit tests follow (omitted for brevity in this response) ---
    @Test
    public void testConstructorThrowsNpeWhenIdIsNull() { /* ... */ }
    @Test
    public void testCreateDraftFactoryMethod() { /* ... */ }
    // ... all other tests from previous responses
}
