package com.github.calhanwynters.model.shared.aggregates;

import com.github.calhanwynters.model.shared.entities.Variant; // Import the shared interface
import com.github.calhanwynters.model.shared.valueobjects.*;
import java.util.*;

/*** Aggregate Root representing a Product in the domain.
 * An immutable record that controls access to its internal components
 * and enforces business invariants.
 */
public record Product(
        ProductId id,
        BusinessId businessId, // <-- New field
        String category,
        DescriptionVO description,
        GalleryVO gallery,
        Set<Variant> variants
) {
    public Product {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(businessId, "businessId must not be null"); // <-- Null check for BusinessId
        Objects.requireNonNull(category, "category must not be null");
        Objects.requireNonNull(description, "featureDescription must not be null");
        Objects.requireNonNull(gallery, "gallery must not be null");
        Objects.requireNonNull(variants, "variants must not be null");
        // Ensure the internal collection is deeply immutable
        variants = Set.copyOf(variants);
    }

    // --- Factory Methods ---

    /**
     * Updated Factory method: Now accepts initial variants as the generic interface
     * and requires the category and businessId to be passed in.
     */
    public static Product create(
            BusinessId businessId,
            String category,
            DescriptionVO description,
            GalleryVO gallery,
            Set<Variant> initialVariants
    ) {
        // Updated constructor call with businessId as the second argument:
        return new Product(
                ProductId.generate(),
                businessId,
                category,
                description,
                gallery,
                initialVariants
        );
    }

    /**
     * Factory method for creating an item with no variants initially,
     * requires the category and businessId to be passed in.
     */
    public static Product create(
            BusinessId businessId, // <-- Requires BusinessId here too
            String category,
            DescriptionVO description,
            GalleryVO gallery
    ) {
        // Delegates to the main create method, passing the new businessId argument:
        return create(businessId, category, description, gallery, Collections.emptySet());
    }

    // --- Behavior Methods ---

    public Product changeDescription(DescriptionVO newDescription) {
        // Corrected to return a new instance with the updated description
        return new Product(this.id, this.businessId, this.category, newDescription, this.gallery, this.variants);
    }

    public Product addImage(ImageUrlVO newImageUrl) {
        Set<ImageUrlVO> updatedImages = new HashSet<>(this.gallery.images());
        updatedImages.add(newImageUrl);
        GalleryVO updatedGallery = new GalleryVO(updatedImages);
        // Corrected to use the updated gallery
        return new Product(this.id, this.businessId, this.category, this.description, updatedGallery, this.variants);
    }

    /*** Adds a new variant to the Product.
     * @param newVariant The variant to add (can be any type that implements the interface).
     * @return A new Product instance with the added variant.
     */
    public Product addVariant(Variant newVariant) { // Accepts the interface
        Objects.requireNonNull(newVariant, "newVariant must not be null");

        // Use a stream to check for existence based on the ID before attempting to add
        boolean idAlreadyExists = this.variants.stream()
                .anyMatch(v -> v.id().equals(newVariant.id()));

        if (idAlreadyExists) {
            // FIX: Access the internal value using the correct record accessor 'value()'
            throw new IllegalArgumentException("Variant with this ID already exists: " + newVariant.id().value());
        }

        Set<Variant> updatedVariants = new HashSet<>(this.variants);
        updatedVariants.add(newVariant);

        return new Product(this.id, this.businessId, this.category, this.description, this.gallery, Collections.unmodifiableSet(updatedVariants));
    }


    /*** Finds a variant by its ID.
     * @param variantId The ID to search for.
     * @return An Optional containing the variant, if found within this aggregate.
     */
    public Optional<Variant> findVariantById(VariantId variantId) { // Uses the generic VariantId
        return this.variants.stream().filter(v -> v.id().equals(variantId)).findFirst();
    }
}
