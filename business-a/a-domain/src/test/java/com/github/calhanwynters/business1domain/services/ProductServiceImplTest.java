package com.github.calhanwynters.business1domain.services;

import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.exceptions.ProductNotFoundException;
import com.github.calhanwynters.business1domain.exceptions.ProductOperationException;
import com.github.calhanwynters.business1domain.repositories.ProductCommandRepository;
import com.github.calhanwynters.business1domain.repositories.ProductQueryRepository;
import com.github.calhanwynters.business1domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductQueryRepository productQueryRepository;
    private ProductCommandRepository productCommandRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productQueryRepository = mock(ProductQueryRepository.class);
        productCommandRepository = mock(ProductCommandRepository.class);
        productService = new ProductServiceImpl(productQueryRepository, productCommandRepository);
    }

    @Test
    void testFindProductById_Success() {
        ProductId productId = new ProductId("123");
        BusinessId businessId = new BusinessId("biz-001");
        ImageUrlVO imageUrl = new ImageUrlVO("https://example.com/image1.jpg"); // Valid image URL
        GalleryVO gallery = new GalleryVO(Set.of(imageUrl)); // At least one image is required
        Product product = new Product(productId, businessId, "Category", new DescriptionVO("A sample product"),
                gallery, new HashSet<>());

        when(productQueryRepository.findById(productId)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.findProductById(productId);

        assertTrue(result.isPresent());
        assertEquals(productId, result.get().id());
    }

    @Test
    void testFindProductById_NotFound() {
        ProductId productId = new ProductId("123");

        when(productQueryRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(productId));
    }

    @Test
    void testSaveProduct_Success() {
        Product product = new Product(new ProductId("123"), new BusinessId("biz-001"),
                "Category", new DescriptionVO("A sample product"),
                new GalleryVO(Set.of(new ImageUrlVO("https://example.com/image1.jpg"))), new HashSet<>());

        productService.saveProduct(product);

        verify(productCommandRepository, times(1)).save(product);
    }

    @Test
    void testSaveProduct_Failure() {
        Product product = new Product(new ProductId("123"), new BusinessId("biz-001"),
                "Category", new DescriptionVO("A sample product"),
                new GalleryVO(Set.of(new ImageUrlVO("https://example.com/image1.jpg"))), new HashSet<>());

        doThrow(new RuntimeException("Database error")).when(productCommandRepository).save(any());

        Exception exception = assertThrows(ProductOperationException.class, () -> productService.saveProduct(product));

        assertEquals("Failed to save product", exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception.getCause());
    }

    @Test
    void testDeleteProduct_Success() {
        ProductId productId = new ProductId("123");
        BusinessId businessId = new BusinessId("biz-001");
        ImageUrlVO imageUrl = new ImageUrlVO("https://example.com/image1.jpg");
        GalleryVO gallery = new GalleryVO(Set.of(imageUrl));
        Product product = new Product(productId, businessId, "Category", new DescriptionVO("A sample product"),
                gallery, new HashSet<>());

        when(productQueryRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        verify(productCommandRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProduct_NotFound() {
        ProductId productId = new ProductId("123");

        when(productQueryRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(productId));
    }

    @Test
    void testDeleteProduct_Failure() {
        ProductId productId = new ProductId("123");
        BusinessId businessId = new BusinessId("biz-001");
        ImageUrlVO imageUrl = new ImageUrlVO("https://example.com/image1.jpg");
        GalleryVO gallery = new GalleryVO(Set.of(imageUrl));
        Product product = new Product(productId, businessId, "Category", new DescriptionVO("A sample product"),
                gallery, new HashSet<>());

        when(productQueryRepository.findById(productId)).thenReturn(Optional.of(product));
        doThrow(new RuntimeException("Database error")).when(productCommandRepository).deleteById(productId);

        Exception exception = assertThrows(ProductOperationException.class, () -> productService.deleteProduct(productId));

        assertEquals("Failed to delete product", exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception.getCause());
    }
}

