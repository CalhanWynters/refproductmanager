package com.github.calhanwynters.business1domain.repositories;

import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.valueobjects.ProductId;

import java.util.Collection;
import java.util.Optional;

// Concrete Class belongs in the infrastructure layer.

interface ProductQueryRepository {
    Optional<Product> findById(ProductId id);
    Collection<Product> findAll();
}