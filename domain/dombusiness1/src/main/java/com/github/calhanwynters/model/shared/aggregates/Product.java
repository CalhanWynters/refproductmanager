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
        String category, // This field is part of the instance
        DescriptionVO description,
        GalleryVO gallery,
        Set<Variant> variants
) {
    public Product {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(category, "category must not be null"); // Added null check for category
        Objects.requireNonNull(description, "featureDescription must not be null");
        Objects.requireNonNull(gallery, "gallery must not be null");
        Objects.requireNonNull(variants, "variants must not be null");
        // Ensure the internal collection is deeply immutable
        variants = Set.copyOf(variants);
    }

    // --- Factory Methods ---

    /**
     * Updated Factory method: Now accepts initial variants as the generic interface
     * and requires the category to be passed in.
     */
    public static Product create(
            String category, // Category must be passed as an argument to the static method
            DescriptionVO description,
            GalleryVO gallery,
            Set<Variant> initialVariants
    ) {
        // Corrected: 'category' is now the local parameter variable
        return new Product(ProductId.generate(), category, description, gallery, initialVariants);
    }

    /**
     * Factory method for creating an item with no variants initially,
     * requires the category to be passed in.
     */
    public static Product create(
            String category, // Category required here too
            DescriptionVO description,
            GalleryVO gallery
    ) {
        return create(category, description, gallery, Collections.emptySet());
    }

    // --- Behavior Methods ---

    public Product changeDescription(DescriptionVO newDescription) {
        // Corrected to return a new instance with the updated description
        return new Product(this.id, this.category, newDescription, this.gallery, this.variants);
    }

    public Product addImage(ImageUrlVO newImageUrl) {
        Set<ImageUrlVO> updatedImages = new HashSet<>(this.gallery.images());
        updatedImages.add(newImageUrl);
        GalleryVO updatedGallery = new GalleryVO(updatedImages);
        // Corrected to use the updated gallery
        return new Product(this.id, this.category, this.description, updatedGallery, this.variants);
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

        return new Product(this.id, this.category, this.description, this.gallery, Collections.unmodifiableSet(updatedVariants));
    }


    /*** Finds a variant by its ID.
     * @param variantId The ID to search for.
     * @return An Optional containing the variant, if found within this aggregate.
     */
    public Optional<Variant> findVariantById(VariantId variantId) { // Uses the generic VariantId
        return this.variants.stream().filter(v -> v.id().equals(variantId)).findFirst();
    }
}
