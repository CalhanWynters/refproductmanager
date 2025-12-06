package com.github.calhanwynters.business1domain.repositories;

import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.valueobjects.ProductId;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


/**
 * Repository interface for managing product data for a single business.
 * This interface is designed for asynchronous operations and integration
 * with messaging systems like Kafka.
 */
public interface ProductQueryRepository {

    /**
     * Finds a product by its unique identifier.
     *
     * @param id the unique identifier of the product
     * @return an Optional containing the product if found, empty otherwise
     */
    Optional<Product> findById(ProductId id);

    /**
     * Retrieves all products in the repository.
     *
     * @return a collection of all products
     */
    Collection<Product> findAll();

    /**
     * Finds products by their category.
     *
     * @param category the category to filter products
     * @return a collection of products belonging to the specified category
     */
    Collection<Product> findByCategory(String category);

    /**
     * Retrieves all products with pagination support.
     *
     * @param page the page number to retrieve
     * @param size the number of products per page
     * @return a collection of products for the specified page
     */
    Collection<Product> findAll(int page, int size);

    /**
     * Asynchronously finds a product by its unique identifier.
     *
     * @param id the unique identifier of the product
     * @return a CompletableFuture containing an Optional product
     */
    CompletableFuture<Optional<Product>> findByIdAsync(ProductId id);

    /**
     * Takes a product snapshot and sends it to the business core application for further processing.
     *
     * @param product the product to snapshot and send
     * @return a CompletableFuture indicating the status of the snapshot operation
     */
    CompletableFuture<Void> sendProductSnapshot(Product product);
}