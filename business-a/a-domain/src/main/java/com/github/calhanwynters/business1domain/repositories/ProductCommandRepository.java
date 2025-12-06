package com.github.calhanwynters.business1domain.repositories;

import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.valueobjects.ProductId;

/**
 * Repository interface for managing product state changes for a single business.
 * This interface handles command operations related to products.
 */
public interface ProductCommandRepository {
    Product save(Product product); // Returns Product
    void deleteById(ProductId id);
}

