package de.bogenliga.application.business.schusszettel.api.types;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive tests for the SchuetzenMeldungDO class.
 * Tests all constructors, getters, setters, and utility methods for complete coverage.
 */
public class SchuetzenMeldungDOTest {

    @Test
    public void constructor_default_shouldCreateEmptyObject() {
        // Act
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        
        // Assert
        assertThat(meldung).isNotNull();
        assertThat(meldung.getGemeldeteSchuetzen()).isNull();
    }

    @Test
    public void getGemeldeteSchuetzen_setGemeldeteSchuetzen_shouldGetSetValue() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);
        
        // Act
        meldung.setGemeldeteSchuetzen(expectedIds);
        List<Long> actualIds = meldung.getGemeldeteSchuetzen();
        
        // Assert
        assertThat(actualIds).isEqualTo(expectedIds);
        assertThat(actualIds).hasSize(3);
        assertThat(actualIds).containsExactly(1L, 2L, 3L);
    }

    @Test
    public void getGemeldeteSchuetzen_setGemeldeteSchuetzenNull_shouldGetNull() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        
        // Act
        meldung.setGemeldeteSchuetzen(null);
        List<Long> actualIds = meldung.getGemeldeteSchuetzen();
        
        // Assert
        assertThat(actualIds).isNull();
    }

    @Test
    public void getGemeldeteSchuetzen_setEmptyList_shouldGetEmptyList() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        List<Long> emptyList = Collections.emptyList();
        
        // Act
        meldung.setGemeldeteSchuetzen(emptyList);
        List<Long> actualIds = meldung.getGemeldeteSchuetzen();
        
        // Assert
        assertThat(actualIds).isEqualTo(emptyList);
        assertThat(actualIds).isEmpty();
    }

    @Test
    public void getGemeldeteSchuetzen_setLargeList_shouldHandleCorrectly() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        List<Long> largeList = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
        
        // Act
        meldung.setGemeldeteSchuetzen(largeList);
        List<Long> actualIds = meldung.getGemeldeteSchuetzen();
        
        // Assert
        assertThat(actualIds).isEqualTo(largeList);
        assertThat(actualIds).hasSize(10);
    }

    @Test
    public void getGemeldeteSchuetzen_setSingleItem_shouldHandleCorrectly() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        List<Long> singleItemList = Arrays.asList(42L);
        
        // Act
        meldung.setGemeldeteSchuetzen(singleItemList);
        List<Long> actualIds = meldung.getGemeldeteSchuetzen();
        
        // Assert
        assertThat(actualIds).isEqualTo(singleItemList);
        assertThat(actualIds).hasSize(1);
        assertThat(actualIds.get(0)).isEqualTo(42L);
    }

    @Test
    public void setGemeldeteSchuetzen_multipleOperations_shouldRetainLastValue() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        List<Long> firstList = Arrays.asList(1L, 2L);
        List<Long> secondList = Arrays.asList(3L, 4L, 5L);
        
        // Act
        meldung.setGemeldeteSchuetzen(firstList);
        meldung.setGemeldeteSchuetzen(secondList);
        List<Long> actualIds = meldung.getGemeldeteSchuetzen();
        
        // Assert
        assertThat(actualIds).isEqualTo(secondList);
        assertThat(actualIds).hasSize(3);
        assertThat(actualIds).containsExactly(3L, 4L, 5L);
    }

    @Test
    public void toString_withNullSchuetzenIds_shouldHandleGracefully() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(null);
        
        // Act
        String result = meldung.toString();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).contains("SchuetzenMeldungDO");
        assertThat(result).contains("null");
    }

    @Test
    public void toString_withSchuetzenIds_shouldContainIds() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        meldung.setGemeldeteSchuetzen(ids);
        
        // Act
        String result = meldung.toString();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).contains("SchuetzenMeldungDO");
        assertThat(result).contains("1");
        assertThat(result).contains("2");
        assertThat(result).contains("3");
    }

    @Test
    public void toString_withEmptyList_shouldHandleCorrectly() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Collections.emptyList());
        
        // Act
        String result = meldung.toString();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).contains("SchuetzenMeldungDO");
        assertThat(result).contains("[]");
    }

    @Test
    public void equals_withSameObject_shouldReturnTrue() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        // Act
        boolean result = meldung.equals(meldung);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void equals_withNull_shouldReturnFalse() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        // Act
        boolean result = meldung.equals(null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withDifferentClass_shouldReturnFalse() {
        // Arrange
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        String differentObject = "not a meldung";
        
        // Act
        boolean result = meldung.equals(differentObject);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withSameSchuetzenIds_shouldReturnTrue() {
        // Arrange
        SchuetzenMeldungDO meldung1 = new SchuetzenMeldungDO();
        meldung1.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        SchuetzenMeldungDO meldung2 = new SchuetzenMeldungDO();
        meldung2.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        // Act
        boolean result = meldung1.equals(meldung2);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void equals_withDifferentSchuetzenIds_shouldReturnFalse() {
        // Arrange
        SchuetzenMeldungDO meldung1 = new SchuetzenMeldungDO();
        meldung1.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        SchuetzenMeldungDO meldung2 = new SchuetzenMeldungDO();
        meldung2.setGemeldeteSchuetzen(Arrays.asList(4L, 5L, 6L));
        
        // Act
        boolean result = meldung1.equals(meldung2);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withBothNull_shouldReturnTrue() {
        // Arrange
        SchuetzenMeldungDO meldung1 = new SchuetzenMeldungDO();
        meldung1.setGemeldeteSchuetzen(null);
        
        SchuetzenMeldungDO meldung2 = new SchuetzenMeldungDO();
        meldung2.setGemeldeteSchuetzen(null);
        
        // Act
        boolean result = meldung1.equals(meldung2);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void equals_withOneNull_shouldReturnFalse() {
        // Arrange
        SchuetzenMeldungDO meldung1 = new SchuetzenMeldungDO();
        meldung1.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        SchuetzenMeldungDO meldung2 = new SchuetzenMeldungDO();
        meldung2.setGemeldeteSchuetzen(null);
        
        // Act
        boolean result = meldung1.equals(meldung2);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void hashCode_withSameSchuetzenIds_shouldReturnSameHash() {
        // Arrange
        SchuetzenMeldungDO meldung1 = new SchuetzenMeldungDO();
        meldung1.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        SchuetzenMeldungDO meldung2 = new SchuetzenMeldungDO();
        meldung2.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        // Act
        int hash1 = meldung1.hashCode();
        int hash2 = meldung2.hashCode();
        
        // Assert
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    public void hashCode_withDifferentSchuetzenIds_shouldReturnDifferentHash() {
        // Arrange
        SchuetzenMeldungDO meldung1 = new SchuetzenMeldungDO();
        meldung1.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L));
        
        SchuetzenMeldungDO meldung2 = new SchuetzenMeldungDO();
        meldung2.setGemeldeteSchuetzen(Arrays.asList(4L, 5L, 6L));
        
        // Act
        int hash1 = meldung1.hashCode();
        int hash2 = meldung2.hashCode();
        
        // Assert
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    public void hashCode_withNullSchuetzenIds_shouldHandleGracefully() {
        // Arrange
        SchuetzenMeldungDO meldung1 = new SchuetzenMeldungDO();
        meldung1.setGemeldeteSchuetzen(null);
        
        SchuetzenMeldungDO meldung2 = new SchuetzenMeldungDO();
        meldung2.setGemeldeteSchuetzen(null);
        
        // Act
        int hash1 = meldung1.hashCode();
        int hash2 = meldung2.hashCode();
        
        // Assert
        assertThat(hash1).isEqualTo(hash2);
    }
}