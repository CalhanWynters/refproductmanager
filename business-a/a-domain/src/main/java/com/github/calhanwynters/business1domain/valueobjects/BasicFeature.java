package com.github.calhanwynters.business1domain.valueobjects;

import java.util.Objects;

public record BasicFeature(String name, String featureDescription, String label) implements FeatureInterface {
    public BasicFeature(String name, String featureDescription, String label) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.featureDescription = Objects.requireNonNull(featureDescription, "feature description must not be null");
        this.label = Objects.requireNonNull(label, "label must not be null");
    }
}
