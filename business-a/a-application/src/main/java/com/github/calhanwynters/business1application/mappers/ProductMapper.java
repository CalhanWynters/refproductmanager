package com.github.calhanwynters.business1application.mappers;

import com.github.calhanwynters.business1application.dto.ProductDTO;
import com.github.calhanwynters.business1domain.aggregates.Product;
import com.github.calhanwynters.business1domain.valueobjects.GalleryVO;
import com.github.calhanwynters.business1domain.valueobjects.ImageUrlVO;

import java.util.Set;
import java.util.stream.Collectors;

public class ProductMapper {

    /**
     * Converts a rich Product domain aggregate into a lean ProductDTO for API transport.
     * @param domainProduct The source aggregate root.
     * @return The resulting ProductDTO.
     */
    public static ProductDTO toDto(Product domainProduct) {

        return new ProductDTO(
                domainProduct.id(),
                domainProduct.businessId(),
                domainProduct.category(),
                domainProduct.description().value(),
                mapGalleryToUrls(domainProduct.gallery()),
                domainProduct.variants().size()
        );
    }

    // Helper method now uses the correctly imported GalleryVO type
    private static Set<String> mapGalleryToUrls(GalleryVO galleryVO) {
        return galleryVO.images().stream()
                .map(ImageUrlVO::url)
                .collect(Collectors.toSet());
    }
}
