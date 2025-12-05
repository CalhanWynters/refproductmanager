package com.github.calhanwynters.repositories;

import com.github.calhanwynters.model.shared.aggregates.Product;
import com.github.calhanwynters.model.shared.valueobjects.ProductId;

import java.util.Collection;
import java.util.Optional;

// Concrete Class belongs in the infrastructure layer.

interface ProductQueryRepository {
    Optional<Product> findById(ProductId id);
    Collection<Product> findAll();
}