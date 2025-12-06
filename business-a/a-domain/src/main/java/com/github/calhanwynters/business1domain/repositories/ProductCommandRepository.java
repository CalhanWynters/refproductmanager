package com.github.calhanwynters.business1domain.repositories;

import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.valueobjects.ProductId;

/**
 * Repository interface for managing product state changes for a single business.
 * This interface handles command operations related to products.
 */
public interface ProductCommandRepository {

    /**
     * Saves or updates a product in the repository.
     *
     * @param product the product to be saved or updated
     */
    void save(Product product);

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the unique identifier of the product to delete
     */
    void deleteById(ProductId id);
}
