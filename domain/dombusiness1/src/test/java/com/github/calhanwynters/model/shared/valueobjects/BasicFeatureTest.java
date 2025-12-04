package com.github.calhanwynters.model.shared.valueobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicFeatureTest {

    @Test
    public void testValidFeatureCreation() {
        String name = "VALID_FEATURE";
        String description = "A valid description.";
        String label = "Valid Label";

        // This should not throw an exception
        BasicFeature feature = new BasicFeature(name, description, label);

        // Verify that the getters work and match the input
        assertNotNull(feature);
        assertEquals(name, feature.name());
        assertEquals(description, feature.featureDescription());
        assertEquals(label, feature.label());
    }

    @Test
    public void testNullNameThrowsException() {
        String description = "A description";
        String label = "A label";

        // Assert that calling the constructor with a null name throws the correct exception
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new BasicFeature(null, description, label));

        // Verify the error message matches what you defined in the record
        assertTrue(thrown.getMessage().contains("name must not be null"));
    }

    @Test
    public void testNullDescriptionThrowsException() {
        String name = "A name";
        String label = "A label";

        // Assert that calling the constructor with a null description throws the correct exception
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new BasicFeature(name, null, label));

        // Verify the error message matches what you defined in the record
        assertTrue(thrown.getMessage().contains("feature description must not be null"));
    }

    @Test
    public void testNullLabelThrowsException() {
        String name = "A name";
        String description = "A description";

        // Assert that calling the constructor with a null label throws the correct exception
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new BasicFeature(name, description, null));

        // Verify the error message matches what you defined in the record
        assertTrue(thrown.getMessage().contains("label must not be null"));
    }
}
