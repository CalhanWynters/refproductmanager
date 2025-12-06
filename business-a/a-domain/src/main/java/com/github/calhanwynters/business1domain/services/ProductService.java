package com.github.calhanwynters.business1domain.services;

import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.valueobjects.ProductId;

import java.util.Optional;

public interface ProductService {
    Optional<Product> findProductById(ProductId id);
    void saveProduct(Product product);
    void deleteProduct(ProductId id);
}
