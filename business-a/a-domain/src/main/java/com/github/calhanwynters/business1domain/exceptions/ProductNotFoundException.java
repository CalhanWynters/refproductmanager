package com.github.calhanwynters.business1domain.exceptions;

import com.github.calhanwynters.business1domain.valueobjects.ProductId;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(ProductId id) {
        super("Product not found with ID: " + id);
    }
}