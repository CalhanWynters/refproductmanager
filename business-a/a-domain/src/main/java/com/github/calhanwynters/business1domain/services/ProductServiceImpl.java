package com.github.calhanwynters.business1domain.services;

import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.exceptions.ProductNotFoundException;
import com.github.calhanwynters.business1domain.exceptions.ProductOperationException;
import com.github.calhanwynters.business1domain.repositories.ProductCommandRepository;
import com.github.calhanwynters.business1domain.repositories.ProductQueryRepository;
import com.github.calhanwynters.business1domain.valueobjects.ProductId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    // ... (fields and constructor omitted for brevity) ...
    private final ProductQueryRepository productQueryRepository;
    private final ProductCommandRepository productCommandRepository;

    @Autowired
    public ProductServiceImpl(ProductQueryRepository productQueryRepository,
                              ProductCommandRepository productCommandRepository) {
        this.productQueryRepository = productQueryRepository;
        this.productCommandRepository = productCommandRepository;
    }

    @Override
    public Optional<Product> findProductById(ProductId id) {
        // This signature is already correct based on your previous code
        return productQueryRepository.findById(id)
                .or(() -> {
                    throw new ProductNotFoundException(id);
                });
    }

    @Override
    public void saveProduct(Product product) {
        // This signature is already correct based on your previous code
        try {
            productCommandRepository.save(product);
        } catch (Exception e) {
            throw new ProductOperationException("Failed to save product", e);
        }
    }

    @Override
    public void deleteProduct(ProductId id) {
        // This signature is already correct based on your previous code
        Optional<Product> product = productQueryRepository.findById(id);
        if (product.isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        try {
            productCommandRepository.deleteById(id);
        } catch (Exception e) {
            throw new ProductOperationException("Failed to delete product", e);
        }
    }
}
