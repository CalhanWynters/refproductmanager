package com.github.calhanwynters.business1domain.entities;

import com.github.calhanwynters.business1domain.entities.Variant;
import com.github.calhanwynters.business1domain.enums.VariantStatusEnums;
import com.github.calhanwynters.business1domain.valueobjects.CareInstructionVO;
import com.github.calhanwynters.business1domain.valueobjects.FeatureInterface;
import com.github.calhanwynters.business1domain.valueobjects.VariantId;
import com.github.calhanwynters.business1domain.valueobjects.WeightVO;
import com.github.calhanwynters.business1domain.valueobjects.WeightVO.WeightUnit;

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

    // Extracted method to create Variant A
    private Variant createVariantA(List<FeatureInterface> mockedFeaturesList) {
        return new Variant(
                testId,
                testSku,
                basePrice,
                basePrice,
                mockedFeaturesList,
                careInstructions,
                weight,
                VariantStatusEnums.DRAFT
        );
    }

    @Test
    public void demonstrateVariantDataFormatAndAttributeComparisonAndMocks() {
        System.out.println("--- Demonstration Printouts ---");

        // Setup mocks for FeatureInterface
        FeatureInterface mockFeatureA = mock(FeatureInterface.class);
        FeatureInterface mockFeatureB = mock(FeatureInterface.class);
        when(mockFeatureA.name()).thenReturn("MOCKED_FEATURE_A");
        when(mockFeatureA.label()).thenReturn("Mock Feature Alpha Label");
        when(mockFeatureB.name()).thenReturn("MOCKED_FEATURE_B");
        when(mockFeatureB.label()).thenReturn("Mock Feature Beta Label");
        List<FeatureInterface> mockedFeaturesList = Arrays.asList(mockFeatureA, mockFeatureB);

        // Create variants using the extracted variant creation method
        Variant variantA = createVariantA(mockedFeaturesList);
        Variant variantB = Variant.createDraft(basePrice, weight, careInstructions, Collections.emptyList());

        // Print format of Variant Data
        System.out.println("\nVariant A Data Format (toString) [Has Mocks Injected]:");
        System.out.println(variantA);
        System.out.println("\nVariant B Data Format (toString) [Has No Features]:");
        System.out.println(variantB);

        // Print details of the feature mocks
        System.out.println("\nDetails of Injected Feature Mocks:");
        System.out.println("Mock A Name: " + variantA.features().get(0).name());
        System.out.println("Mock A Label: " + variantA.features().get(0).label());
        System.out.println("Mock B Name: " + variantA.features().get(1).name());
        System.out.println("Mock B Label: " + variantA.features().get(1).label());

        // Print output of hasSameAttributes()
        System.out.println("\nAttribute Comparison Results:");
        boolean sameAttributesResult = variantA.hasSameAttributes(variantB);
        System.out.printf("Does Variant A have the same attributes as Variant B? %b%n", sameAttributesResult);

        // Assertions for the test to pass
        assertNotNull(variantA);
        assertFalse(sameAttributesResult, "Variants should differ because Variant B has an empty features list.");
        // Verify that the names were accessed during the print statements
        verify(mockFeatureA, atLeastOnce()).name();
        verify(mockFeatureB, atLeastOnce()).label();

        System.out.println("--- End Demonstration Printouts ---");
    }

    // Additional test methods
    @Test
    public void testConstructorThrowsNpeWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> new Variant(null, testSku, basePrice, basePrice, features, careInstructions, weight, VariantStatusEnums.DRAFT));
    }

    @Test
    public void testCreateDraftFactoryMethod() {
        Variant draftVariant = Variant.createDraft(basePrice, weight, careInstructions, Collections.emptyList());

        assertNotNull(draftVariant);
        assertEquals(basePrice, draftVariant.basePrice());
        assertEquals(careInstructions, draftVariant.careInstructions());
        assertTrue(draftVariant.features().isEmpty(), "Draft variant should have empty features list.");
    }

}

