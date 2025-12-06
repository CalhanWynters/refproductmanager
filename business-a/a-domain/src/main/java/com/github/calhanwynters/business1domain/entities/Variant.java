package com.github.calhanwynters.business1domain.entities;

import com.github.calhanwynters.business1domain.enums.VariantStatusEnums;
import com.github.calhanwynters.business1domain.valueobjects.*;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("UnnecessaryLocalVariable")
public record Variant(
        VariantId id,
        String sku,
        MonetaryAmount basePrice,
        MonetaryAmount currentPrice,
        List<FeatureInterface> features,
        CareInstructionVO careInstructions,
        WeightVO weight,
        VariantStatusEnums status) {

    /*** Compact Constructor for Validation and Normalization */
    public Variant {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(sku, "sku must not be null");
        Objects.requireNonNull(basePrice, "basePrice must not be null");
        Objects.requireNonNull(currentPrice, "currentPrice must not be null");
        Objects.requireNonNull(features, "features must not be null");
        Objects.requireNonNull(careInstructions, "careInstructions must not be null");
        Objects.requireNonNull(weight, "weight must not be null");
        Objects.requireNonNull(status, "status must not be null");
    }

    /**
     * Factory method to create a basic new DRAFT variant.
     * This hides the complexity of generating IDs and SKUs, and sets
     * the initial status and current price automatically.
     *
     * @param basePrice The initial base monetary amount.
     * @param weight The physical weight of the product.
     * @param careInstructions Specific care instructions for the product.
     * @param features Optional list of features.
     * @return A new Variant instance initialized as DRAFT.
     */
    public static Variant createDraft(
            MonetaryAmount basePrice,
            WeightVO weight,
            CareInstructionVO careInstructions,
            List<FeatureInterface> features) {

        // Logic for auto-generation is encapsulated in the factory method
        VariantId generatedId = VariantId.generate();
        String generatedSku = String.format("VARIANT-%s",
                UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        MonetaryAmount currentPrice = basePrice; // Current price starts same as base

        return new Variant(
                generatedId,
                generatedSku,
                basePrice,
                currentPrice,
                features,
                careInstructions,
                weight,
                VariantStatusEnums.DRAFT
        );
    }

    // --- Other existing methods (hasSameAttributes, calculateCurrentPrice, etc.) ---

    /*** Checks if this variant has the same physical attributes as another variant...*/
    public boolean hasSameAttributes(Variant other) {
        if (other == null) {
            return false;
        }
        return Objects.equals(careInstructions, other.careInstructions) &&
                Objects.equals(weight, other.weight) &&
                Objects.equals(features, other.features);
    }

    // ... (rest of the existing methods omitted for brevity) ...


    @Override
    public String toString() {
        return String.format("Variant[id=%s, sku=%s, basePrice=%s, currentPrice=%s, features=%s, status=%s]",
                id, sku, basePrice, currentPrice, features, status);
    }

    // --- Behavior Methods ---

    public Variant changeBasePrice(MonetaryAmount newBasePrice) {
        return new Variant(this.id, this.sku, newBasePrice, newBasePrice, this.features, this.careInstructions, this.weight, this.status);
    }

    public Variant changeCurrentPrice(MonetaryAmount newCurrentPrice) {
        return new Variant(this.id, this.sku, this.basePrice, newCurrentPrice, this.features, this.careInstructions, this.weight, this.status);
    }

    public Variant applyDiscount(PercentageVO discount) {
        MonetaryAmount discountedPrice = this.basePrice.multiply(BigDecimal.ONE.subtract(discount.value()));
        return this.changeCurrentPrice(discountedPrice);
    }

    public Variant removeDiscount() {
        return this.changeCurrentPrice(this.basePrice);
    }

    // --- Lifecycle/Status Behavior Methods ---
    public boolean isActive() {
        return this.status == VariantStatusEnums.ACTIVE;
    }

    public Variant activate() {
        if (this.status == VariantStatusEnums.DISCONTINUED) {
            throw new IllegalStateException("Cannot activate a discontinued variant.");
        }
        return new Variant(this.id, this.sku, this.basePrice, this.currentPrice, this.features, this.careInstructions, this.weight, VariantStatusEnums.ACTIVE);
    }

    public Variant deactivate() {
        return new Variant(this.id, this.sku, this.basePrice, this.currentPrice, this.features, this.careInstructions, this.weight, VariantStatusEnums.INACTIVE);
    }

    public Variant markAsDiscontinued() {
        return new Variant(this.id, this.sku, this.basePrice, this.currentPrice, this.features, this.careInstructions, this.weight, VariantStatusEnums.DISCONTINUED);
    }


}
