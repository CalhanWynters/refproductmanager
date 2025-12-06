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

/*
* Make sure that any clients consuming this service are prepared to
* handle Optional<Product> and the custom exceptions you’ve defined. This
* will improve the overall robustness and clarity
*/


@Service
public class ProductServiceImpl implements ProductService {

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
        return productQueryRepository.findById(id)
                .or(() -> {
                    throw new ProductNotFoundException(id); // Throwing the exception if not found
                });
    }

    @Override
    public void saveProduct(Product product) {
        try {
            productCommandRepository.save(product);
        } catch (Exception e) {
            throw new ProductOperationException("Failed to save product", e);
        }
    }

    @Override
    public void deleteProduct(ProductId id) {
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
