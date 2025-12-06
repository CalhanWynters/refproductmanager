package com.github.calhanwynters.business1domain.repositories;

import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.valueobjects.ProductId;

// Concrete Class belongs in the infrastructure layer.

interface ProductCommandRepository {
    void save(Product product);
    void deleteById(ProductId id);
}