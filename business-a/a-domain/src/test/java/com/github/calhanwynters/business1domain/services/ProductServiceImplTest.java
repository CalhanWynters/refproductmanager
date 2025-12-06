package com.github.calhanwynters.business1domain.services;

import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.entities.Variant;
import com.github.calhanwynters.business1domain.enums.VariantStatusEnums;
import com.github.calhanwynters.business1domain.exceptions.ProductNotFoundException;
import com.github.calhanwynters.business1domain.exceptions.ProductOperationException;
import com.github.calhanwynters.business1domain.repositories.ProductCommandRepository;
import com.github.calhanwynters.business1domain.repositories.ProductQueryRepository;
import com.github.calhanwynters.business1domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.github.calhanwynters.business1domain.valueobjects.WeightVO.WeightUnit.KILOGRAM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductQueryRepository productQueryRepository;
    private ProductCommandRepository productCommandRepository;
    private ProductService productService;
    private BusinessId testBusinessId;
    private GalleryVO validGallery;

    @BeforeEach
    void setUp() {
        productQueryRepository = mock(ProductQueryRepository.class);
        productCommandRepository = mock(ProductCommandRepository.class);
        productService = new ProductServiceImpl(productQueryRepository, productCommandRepository);
        testBusinessId = new BusinessId("biz-001");
        validGallery = new GalleryVO(Set.of(new ImageUrlVO("https://example.com/image_placeholder.jpg")));
    }

    private Variant createTestVariant() {
        return new Variant(
                VariantId.generate(),
                "SKU-BLACK-001",
                Monetary.getDefaultAmountFactory().setCurrency("USD").setNumber(new BigDecimal("999.99")).create(),
                Monetary.getDefaultAmountFactory().setCurrency("USD").setNumber(new BigDecimal("999.99")).create(),
                List.of(),
                new CareInstructionVO("Handle with care"),
                new WeightVO(BigDecimal.ONE, KILOGRAM),
                VariantStatusEnums.DRAFT
        );
    }

    @Test
    void testFindProductById_Success() {
        ProductId productId = new ProductId("123");
        Product product = Product.create(testBusinessId, "Category", new DescriptionVO("A sample product"),
                validGallery, new HashSet<>());
        when(productQueryRepository.findById(productId)).thenReturn(Optional.of(product));
        Optional<Product> result = productService.findProductById(productId);
        assertTrue(result.isPresent());
        assertEquals(product.id(), result.get().id());
        assertEquals(testBusinessId, result.get().businessId());
    }

    @Test
    void testFindProductById_NotFound() {
        ProductId productId = new ProductId("123");
        when(productQueryRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(productId));
    }


    @Test
    void testSaveProduct_Success() {
        Product product = Product.create(testBusinessId, "Category", new DescriptionVO("A sample product"),
                validGallery, new HashSet<>());
        when(productCommandRepository.save(any(Product.class))).thenReturn(product);
        productService.saveProduct(product);
        verify(productCommandRepository, times(1)).save(product);
    }

    @Test
    void testSaveProduct_Failure() {
        Product product = Product.create(testBusinessId, "Category", new DescriptionVO("A sample product"),
                validGallery, new HashSet<>());
        when(productCommandRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(ProductOperationException.class, () -> productService.saveProduct(product));
        assertEquals("Failed to save product", exception.getMessage());
    }


    @Test
    void testDeleteProduct_Success() {
        ProductId productId = new ProductId("123");
        Product product = Product.create(testBusinessId, "Category", new DescriptionVO("A sample product"),
                validGallery, new HashSet<>());
        when(productQueryRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productCommandRepository).deleteById(any(ProductId.class));
        productService.deleteProduct(productId);
        verify(productCommandRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProduct_NotFound() {
        ProductId productId = new ProductId("123");
        when(productQueryRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(productId));
    }

    @Test
    void testUpdateProductDescription_Success() {
        // Arrange
        ProductId productId = new ProductId("123");
        String originalDescriptionText = "Original description";
        String updatedDescriptionText = "Updated description";

        Product originalProduct = Product.create(testBusinessId, "Category", new DescriptionVO(originalDescriptionText),
                validGallery, new HashSet<>());

        System.out.println("Original Product ID: " + originalProduct.id().value());
        System.out.println("Original Description: " + originalProduct.description().value());


        when(productQueryRepository.findById(productId)).thenReturn(Optional.of(originalProduct));
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        when(productCommandRepository.save(productCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product updatedProductInstance = originalProduct.changeDescription(new DescriptionVO(updatedDescriptionText));
        productService.saveProduct(updatedProductInstance);

        // Assert
        verify(productCommandRepository, times(1)).save(any(Product.class));
        Product savedProduct = productCaptor.getValue();

        System.out.println("Saved Product ID (Captured): " + savedProduct.id().value());
        System.out.println("New Description (Captured): " + savedProduct.description().value());
        System.out.println("Variants count in saved product: " + savedProduct.variants().size());


        assertNotNull(savedProduct);
        assertNotSame(originalProduct, savedProduct, "Because Product is a record (immutable), a new instance should be saved.");
        assertEquals(updatedDescriptionText, savedProduct.description().value());
    }

    @Test
    void testAddProductVariant_Success() {
        // Arrange
        ProductId productId = new ProductId("prod-456");
        Product existingProduct = Product.create(testBusinessId, "Electronics",
                new DescriptionVO("Laptop base model"), validGallery);
        Variant newVariant = createTestVariant();

        System.out.println("Existing Product ID: " + existingProduct.id().value());
        System.out.println("New Variant ID to add: " + newVariant.id().value());


        when(productQueryRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        when(productCommandRepository.save(productCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product productWithNewVariant = existingProduct.addVariant(newVariant);
        productService.saveProduct(productWithNewVariant);

        // Assert
        verify(productCommandRepository, times(1)).save(any(Product.class));
        Product savedProduct = productCaptor.getValue();

        System.out.println("Saved Product ID (Captured): " + savedProduct.id().value());
        System.out.println("Variants list in saved product: " + savedProduct.variants());

        assertNotNull(savedProduct);
        assertNotSame(existingProduct, savedProduct, "A new Product instance should have been saved due to immutability.");
        assertEquals(1, savedProduct.variants().size(), "Product should have exactly one variant after adding");
        assertTrue(savedProduct.variants().contains(newVariant), "The new variant should be present in the saved product's set");
    }

}
