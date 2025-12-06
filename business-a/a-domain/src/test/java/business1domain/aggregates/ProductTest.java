package business1domain.aggregates;

import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.entities.Variant;
import com.github.calhanwynters.business1domain.valueobjects.*;
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
        // Generate a stable or random business ID for the test context
        // Add a field for the BusinessId needed in the setup
        BusinessId businessId = BusinessId.generate();

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

        // --- FIX ---
        // Create a product instance using the updated 'create' signature
        // which now requires 'businessId' as the first argument.
        product = Product.create(
                businessId, // Pass the required BusinessId
                "Electronics",
                description,
                gallery,
                Set.of(variant)
        );
    }

    @Test
    void testPrintProductLayout() {
        // Print the product layout in a readable format
        System.out.println("Product Layout:");
        System.out.printf("ID: %s%n", product.id());
        // Add printing the Business ID
        System.out.printf("Business ID: %s%n", product.businessId());
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
