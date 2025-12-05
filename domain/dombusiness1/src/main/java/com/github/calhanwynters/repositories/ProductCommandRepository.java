package com.github.calhanwynters.repositories;

import com.github.calhanwynters.model.shared.aggregates.Product;
import com.github.calhanwynters.model.shared.valueobjects.ProductId;

// Concrete Class belongs in the infrastructure layer.

interface ProductCommandRepository {
    void save(Product product);
    void deleteById(ProductId id);
}