package de.bogenliga.application.business.schusszettel.api.types;

import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzenSatzDO;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive tests for the SatzEingabeDO class.
 * Tests all constructors, getters, setters, and utility methods for complete coverage.
 */
public class SatzEingabeDOTest {

    @Test
    public void constructor_default_shouldCreateEmptyObject() {
        // Act
        SatzEingabeDO eingabe = new SatzEingabeDO();
        
        // Assert
        assertThat(eingabe).isNotNull();
        assertThat(eingabe.getSatzeingabe()).isNull();
    }

    @Test
    public void getSatzeingabe_setSatzeingabe_shouldGetSetValue() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        List<SchuetzenSatzDO> expectedSatzeingabe = createTestSchuetzenSatzList();
        
        // Act
        eingabe.setSatzeingabe(expectedSatzeingabe);
        List<SchuetzenSatzDO> actualSatzeingabe = eingabe.getSatzeingabe();
        
        // Assert
        assertThat(actualSatzeingabe).isEqualTo(expectedSatzeingabe);
        assertThat(actualSatzeingabe).hasSize(3);
    }

    @Test
    public void getSatzeingabe_setSatzeingabeNull_shouldGetNull() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        
        // Act
        eingabe.setSatzeingabe(null);
        List<SchuetzenSatzDO> actualSatzeingabe = eingabe.getSatzeingabe();
        
        // Assert
        assertThat(actualSatzeingabe).isNull();
    }

    @Test
    public void getSatzeingabe_setEmptyList_shouldGetEmptyList() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        List<SchuetzenSatzDO> emptyList = Collections.emptyList();
        
        // Act
        eingabe.setSatzeingabe(emptyList);
        List<SchuetzenSatzDO> actualSatzeingabe = eingabe.getSatzeingabe();
        
        // Assert
        assertThat(actualSatzeingabe).isEqualTo(emptyList);
        assertThat(actualSatzeingabe).isEmpty();
    }

    @Test
    public void getSatzeingabe_setSingleSchuetze_shouldHandleCorrectly() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        SchuetzenSatzDO schuetze = createTestSchuetzenSatz(1L, 10, 9, 8);
        List<SchuetzenSatzDO> singleSchuetzeList = Arrays.asList(schuetze);
        
        // Act
        eingabe.setSatzeingabe(singleSchuetzeList);
        List<SchuetzenSatzDO> actualSatzeingabe = eingabe.getSatzeingabe();
        
        // Assert
        assertThat(actualSatzeingabe).isEqualTo(singleSchuetzeList);
        assertThat(actualSatzeingabe).hasSize(1);
        assertThat(actualSatzeingabe.get(0).getSchuetzenId()).isEqualTo(1L);
    }

    @Test
    public void setSatzeingabe_multipleOperations_shouldRetainLastValue() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        List<SchuetzenSatzDO> firstList = createTestSchuetzenSatzList();
        List<SchuetzenSatzDO> secondList = Arrays.asList(createTestSchuetzenSatz(99L, 5, 6, 7));
        
        // Act
        eingabe.setSatzeingabe(firstList);
        eingabe.setSatzeingabe(secondList);
        List<SchuetzenSatzDO> actualSatzeingabe = eingabe.getSatzeingabe();
        
        // Assert
        assertThat(actualSatzeingabe).isEqualTo(secondList);
        assertThat(actualSatzeingabe).hasSize(1);
        assertThat(actualSatzeingabe.get(0).getSchuetzenId()).isEqualTo(99L);
    }

    @Test
    public void toString_withNullSatzeingabe_shouldHandleGracefully() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(null);
        
        // Act
        String result = eingabe.toString();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).contains("SatzEingabeDO");
        assertThat(result).contains("null");
    }

    @Test
    public void toString_withSatzeingabe_shouldContainSchuetzenData() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        List<SchuetzenSatzDO> satzeingabe = createTestSchuetzenSatzList();
        eingabe.setSatzeingabe(satzeingabe);
        
        // Act
        String result = eingabe.toString();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).contains("SatzEingabeDO");
        // Should contain references to the SchuetzenSatzDO objects
    }

    @Test
    public void toString_withEmptyList_shouldHandleCorrectly() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(Collections.emptyList());
        
        // Act
        String result = eingabe.toString();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).contains("SatzEingabeDO");
        assertThat(result).contains("[]");
    }

    @Test
    public void equals_withSameObject_shouldReturnTrue() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(createTestSchuetzenSatzList());
        
        // Act
        boolean result = eingabe.equals(eingabe);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void equals_withNull_shouldReturnFalse() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(createTestSchuetzenSatzList());
        
        // Act
        boolean result = eingabe.equals(null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withDifferentClass_shouldReturnFalse() {
        // Arrange
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(createTestSchuetzenSatzList());
        String differentObject = "not a satz eingabe";
        
        // Act
        boolean result = eingabe.equals(differentObject);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withSameSatzeingabe_shouldReturnTrue() {
        // Arrange
        List<SchuetzenSatzDO> satzeingabe = createTestSchuetzenSatzList();
        
        SatzEingabeDO eingabe1 = new SatzEingabeDO();
        eingabe1.setSatzeingabe(satzeingabe);
        
        SatzEingabeDO eingabe2 = new SatzEingabeDO();
        eingabe2.setSatzeingabe(satzeingabe); // Same reference
        
        // Act
        boolean result = eingabe1.equals(eingabe2);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void equals_withDifferentSatzeingabe_shouldReturnFalse() {
        // Arrange
        SatzEingabeDO eingabe1 = new SatzEingabeDO();
        eingabe1.setSatzeingabe(createTestSchuetzenSatzList());
        
        SatzEingabeDO eingabe2 = new SatzEingabeDO();
        eingabe2.setSatzeingabe(Arrays.asList(createTestSchuetzenSatz(99L, 1, 2, 3)));
        
        // Act
        boolean result = eingabe1.equals(eingabe2);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withBothNull_shouldReturnTrue() {
        // Arrange
        SatzEingabeDO eingabe1 = new SatzEingabeDO();
        eingabe1.setSatzeingabe(null);
        
        SatzEingabeDO eingabe2 = new SatzEingabeDO();
        eingabe2.setSatzeingabe(null);
        
        // Act
        boolean result = eingabe1.equals(eingabe2);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void equals_withOneNull_shouldReturnFalse() {
        // Arrange
        SatzEingabeDO eingabe1 = new SatzEingabeDO();
        eingabe1.setSatzeingabe(createTestSchuetzenSatzList());
        
        SatzEingabeDO eingabe2 = new SatzEingabeDO();
        eingabe2.setSatzeingabe(null);
        
        // Act
        boolean result = eingabe1.equals(eingabe2);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void hashCode_withSameSatzeingabe_shouldReturnSameHash() {
        // Arrange
        List<SchuetzenSatzDO> satzeingabe = createTestSchuetzenSatzList();
        
        SatzEingabeDO eingabe1 = new SatzEingabeDO();
        eingabe1.setSatzeingabe(satzeingabe);
        
        SatzEingabeDO eingabe2 = new SatzEingabeDO();
        eingabe2.setSatzeingabe(satzeingabe);
        
        // Act
        int hash1 = eingabe1.hashCode();
        int hash2 = eingabe2.hashCode();
        
        // Assert
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    public void hashCode_withDifferentSatzeingabe_shouldReturnDifferentHash() {
        // Arrange
        SatzEingabeDO eingabe1 = new SatzEingabeDO();
        eingabe1.setSatzeingabe(createTestSchuetzenSatzList());
        
        SatzEingabeDO eingabe2 = new SatzEingabeDO();
        eingabe2.setSatzeingabe(Arrays.asList(createTestSchuetzenSatz(99L, 1, 2, 3)));
        
        // Act
        int hash1 = eingabe1.hashCode();
        int hash2 = eingabe2.hashCode();
        
        // Assert
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    public void hashCode_withNullSatzeingabe_shouldHandleGracefully() {
        // Arrange
        SatzEingabeDO eingabe1 = new SatzEingabeDO();
        eingabe1.setSatzeingabe(null);
        
        SatzEingabeDO eingabe2 = new SatzEingabeDO();
        eingabe2.setSatzeingabe(null);
        
        // Act
        int hash1 = eingabe1.hashCode();
        int hash2 = eingabe2.hashCode();
        
        // Assert
        assertThat(hash1).isEqualTo(hash2);
    }

    // Helper methods for creating test data

    private List<SchuetzenSatzDO> createTestSchuetzenSatzList() {
        return Arrays.asList(
            createTestSchuetzenSatz(1L, 10, 9, 8),
            createTestSchuetzenSatz(2L, 8, 9, 10),
            createTestSchuetzenSatz(3L, 9, 8, 7)
        );
    }

    private SchuetzenSatzDO createTestSchuetzenSatz(Long schuetzeId, int pfeil1, int pfeil2, int pfeil3) {
        SchuetzenSatzDO schuetze = new SchuetzenSatzDO();
        schuetze.setSchuetzenId(schuetzeId);
        schuetze.setSchuss1(pfeil1);
        schuetze.setSchuss2(pfeil2);
        schuetze.setSchuss3(pfeil3);
        return schuetze;
    }
}