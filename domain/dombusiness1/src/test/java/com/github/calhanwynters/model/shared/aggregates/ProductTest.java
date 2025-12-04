package com.github.calhanwynters.model.shared.aggregates;

import com.github.calhanwynters.model.shared.entities.Variant;
import com.github.calhanwynters.model.shared.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

public class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        // Create sample inputs for product creation
        DescriptionVO description = new DescriptionVO("A great product description.");
        GalleryVO gallery = new GalleryVO(Set.of(new ImageUrlVO("https://example.com/image1.jpg")));

        MonetaryAmount basePrice = Monetary.getDefaultAmountFactory().setNumber(100.00).setCurrency("USD").create();

        // Create a variant with a valid WeightVO
        Variant variant = Variant.createDraft(
                basePrice,
                new WeightVO(BigDecimal.valueOf(1), WeightVO.WeightUnit.KILOGRAM),
                new CareInstructionVO("Wash with cold water"),
                Collections.emptyList()
        );

        // Create a product instance
        product = Product.create("Electronics", description, gallery, Set.of(variant));
    }

    @Test
    void testPrintProductLayout() {
        // Print the product layout in a readable format
        System.out.println("Product Layout:");
        System.out.printf("ID: %s%n", product.id());
        System.out.printf("Category: %s%n", product.category());
        System.out.printf("Description: %s%n", product.description());
        System.out.printf("Gallery: %s%n", product.gallery());
        System.out.println("Variants:");

        for (Variant v : product.variants()) {
            System.out.printf("  - Variant ID: %s, SKU: %s, Base Price: %s, Current Price: %s, Status: %s%n",
                    v.id(), v.sku(), v.basePrice(), v.currentPrice(), v.status());
        }
    }

    // Other test methods...
}
