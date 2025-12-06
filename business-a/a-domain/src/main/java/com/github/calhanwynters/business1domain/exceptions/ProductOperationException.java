package com.github.calhanwynters.business1domain.exceptions;

public class ProductOperationException extends RuntimeException {
    public ProductOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
