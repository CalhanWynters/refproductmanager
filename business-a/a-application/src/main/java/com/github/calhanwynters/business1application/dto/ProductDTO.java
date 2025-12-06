package com.github.calhanwynters.business1application.dto;

import com.github.calhanwynters.business1domain.valueobjects.BusinessId;
import com.github.calhanwynters.business1domain.valueobjects.ProductId;

import java.util.Set;

/**
 * A Data Transfer Object representing a simplified view of a Product for API exposure.
 */
public record ProductDTO(
        ProductId id,
        BusinessId businessId,
        String category,
        String descriptionValue, // Renamed parameter for clarity in the DTO
        Set<String> imageUrls,
        int variantCount
) {}
