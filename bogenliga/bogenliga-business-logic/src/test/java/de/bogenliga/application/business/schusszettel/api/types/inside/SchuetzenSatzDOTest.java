package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive tests for the SchuetzenSatzDO class.
 * Tests all constructors, getters, setters, and utility methods for complete coverage.
 */
public class SchuetzenSatzDOTest {

    @Test
    public void constructor_default_shouldCreateEmptyObject() {
        // Act
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        
        // Assert
        assertThat(schuetzenSatz).isNotNull();
        assertThat(schuetzenSatz.getSchuetzenId()).isNull();
        assertThat(schuetzenSatz.getSchuss1()).isNull();
        assertThat(schuetzenSatz.getSchuss2()).isNull();
        assertThat(schuetzenSatz.getSchuss3()).isNull();
    }

    @Test
    public void getSchuetzenId_setSchuetzenId_shouldGetSetValue() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        Long expectedId = 123L;
        
        // Act
        schuetzenSatz.setSchuetzenId(expectedId);
        Long actualId = schuetzenSatz.getSchuetzenId();
        
        // Assert
        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    public void getSchuetzenId_setSchuetzenIdNull_shouldGetNull() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        
        // Act
        schuetzenSatz.setSchuetzenId(null);
        Long actualId = schuetzenSatz.getSchuetzenId();
        
        // Assert
        assertThat(actualId).isNull();
    }

    @Test
    public void getSchuss1_setSchuss1_shouldGetSetValue() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        Integer expectedPfeil1 = 10;
        
        // Act
        schuetzenSatz.setSchuss1(expectedPfeil1);
        Integer actualPfeil1 = schuetzenSatz.getSchuss1();
        
        // Assert
        assertThat(actualPfeil1).isEqualTo(expectedPfeil1);
    }

    @Test
    public void getSchuss1_setSchuss1Null_shouldGetNull() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        
        // Act
        schuetzenSatz.setSchuss1(null);
        Integer actualPfeil1 = schuetzenSatz.getSchuss1();
        
        // Assert
        assertThat(actualPfeil1).isNull();
    }

    @Test
    public void getSchuss2_setSchuss2_shouldGetSetValue() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        Integer expectedPfeil2 = 9;
        
        // Act
        schuetzenSatz.setSchuss2(expectedPfeil2);
        Integer actualPfeil2 = schuetzenSatz.getSchuss2();
        
        // Assert
        assertThat(actualPfeil2).isEqualTo(expectedPfeil2);
    }

    @Test
    public void getSchuss2_setSchuss2Null_shouldGetNull() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        
        // Act
        schuetzenSatz.setSchuss2(null);
        Integer actualPfeil2 = schuetzenSatz.getSchuss2();
        
        // Assert
        assertThat(actualPfeil2).isNull();
    }

    @Test
    public void getSchuss3_setSchuss3_shouldGetSetValue() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        Integer expectedPfeil3 = 8;
        
        // Act
        schuetzenSatz.setSchuss3(expectedPfeil3);
        Integer actualPfeil3 = schuetzenSatz.getSchuss3();
        
        // Assert
        assertThat(actualPfeil3).isEqualTo(expectedPfeil3);
    }

    @Test
    public void getSchuss3_setSchuss3Null_shouldGetNull() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        
        // Act
        schuetzenSatz.setSchuss3(null);
        Integer actualPfeil3 = schuetzenSatz.getSchuss3();
        
        // Assert
        assertThat(actualPfeil3).isNull();
    }

    @Test
    public void settersAndGetters_withValidArcheryScores_shouldHandleCorrectly() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        
        // Act - Set typical archery scores
        schuetzenSatz.setSchuetzenId(42L);
        schuetzenSatz.setSchuss1(10); // Perfect score
        schuetzenSatz.setSchuss2(9);  // Good score
        schuetzenSatz.setSchuss3(8);  // Good score
        
        // Assert
        assertThat(schuetzenSatz.getSchuetzenId()).isEqualTo(42L);
        assertThat(schuetzenSatz.getSchuss1()).isEqualTo(10);
        assertThat(schuetzenSatz.getSchuss2()).isEqualTo(9);
        assertThat(schuetzenSatz.getSchuss3()).isEqualTo(8);
    }

    @Test
    public void settersAndGetters_withMinimumValidScores_shouldHandleCorrectly() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        
        // Act - Set minimum valid archery scores
        schuetzenSatz.setSchuetzenId(1L);
        schuetzenSatz.setSchuss1(0); // Miss
        schuetzenSatz.setSchuss2(0); // Miss
        schuetzenSatz.setSchuss3(0); // Miss
        
        // Assert
        assertThat(schuetzenSatz.getSchuetzenId()).isEqualTo(1L);
        assertThat(schuetzenSatz.getSchuss1()).isEqualTo(0);
        assertThat(schuetzenSatz.getSchuss2()).isEqualTo(0);
        assertThat(schuetzenSatz.getSchuss3()).isEqualTo(0);
    }

    @Test
    public void settersAndGetters_withMaximumValidScores_shouldHandleCorrectly() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        
        // Act - Set maximum valid archery scores
        schuetzenSatz.setSchuetzenId(Long.MAX_VALUE);
        schuetzenSatz.setSchuss1(10); // Maximum archery score
        schuetzenSatz.setSchuss2(10); // Maximum archery score
        schuetzenSatz.setSchuss3(10); // Maximum archery score
        
        // Assert
        assertThat(schuetzenSatz.getSchuetzenId()).isEqualTo(Long.MAX_VALUE);
        assertThat(schuetzenSatz.getSchuss1()).isEqualTo(10);
        assertThat(schuetzenSatz.getSchuss2()).isEqualTo(10);
        assertThat(schuetzenSatz.getSchuss3()).isEqualTo(10);
    }

    @Test
    public void settersAndGetters_withInvalidScores_shouldAcceptValues() {
        // Arrange - Note: DAO level validation should handle invalid archery scores
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        
        // Act - Set potentially invalid scores (validation happens elsewhere)
        schuetzenSatz.setSchuetzenId(-1L);
        schuetzenSatz.setSchuss1(-1);  // Invalid negative score
        schuetzenSatz.setSchuss2(11);  // Invalid score > 10
        schuetzenSatz.setSchuss3(999); // Invalid high score
        
        // Assert - The DO should accept these values (validation is business logic responsibility)
        assertThat(schuetzenSatz.getSchuetzenId()).isEqualTo(-1L);
        assertThat(schuetzenSatz.getSchuss1()).isEqualTo(-1);
        assertThat(schuetzenSatz.getSchuss2()).isEqualTo(11);
        assertThat(schuetzenSatz.getSchuss3()).isEqualTo(999);
    }

    @Test
    public void toString_withAllFieldsSet_shouldReturnFormattedString() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = createFullyPopulatedSchuetzenSatz();
        
        // Act
        String result = schuetzenSatz.toString();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).contains("SchuetzenSatzDO");
        assertThat(result).contains("schuetzenId=1");
        assertThat(result).contains("schuss1=10");
        assertThat(result).contains("schuss2=9");
        assertThat(result).contains("schuss3=8");
    }

    @Test
    public void toString_withNullFields_shouldHandleNullsGracefully() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        
        // Act
        String result = schuetzenSatz.toString();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).contains("SchuetzenSatzDO");
        assertThat(result).contains("schuetzenId=null");
        assertThat(result).contains("schuss1=null");
        assertThat(result).contains("schuss2=null");
        assertThat(result).contains("schuss3=null");
    }

    @Test
    public void equals_withSameObject_shouldReturnTrue() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = createFullyPopulatedSchuetzenSatz();
        
        // Act
        boolean result = schuetzenSatz.equals(schuetzenSatz);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void equals_withNull_shouldReturnFalse() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = createFullyPopulatedSchuetzenSatz();
        
        // Act
        boolean result = schuetzenSatz.equals(null);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withDifferentClass_shouldReturnFalse() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = createFullyPopulatedSchuetzenSatz();
        String differentObject = "not a schuetzen satz";
        
        // Act
        boolean result = schuetzenSatz.equals(differentObject);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withSameFields_shouldReturnTrue() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz1 = createFullyPopulatedSchuetzenSatz();
        SchuetzenSatzDO schuetzenSatz2 = createFullyPopulatedSchuetzenSatz();
        
        // Act
        boolean result = schuetzenSatz1.equals(schuetzenSatz2);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void equals_withDifferentSchuetzeId_shouldReturnFalse() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz1 = createFullyPopulatedSchuetzenSatz();
        SchuetzenSatzDO schuetzenSatz2 = createFullyPopulatedSchuetzenSatz();
        schuetzenSatz2.setSchuetzenId(2L);
        
        // Act
        boolean result = schuetzenSatz1.equals(schuetzenSatz2);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withDifferentPfeil1_shouldReturnFalse() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz1 = createFullyPopulatedSchuetzenSatz();
        SchuetzenSatzDO schuetzenSatz2 = createFullyPopulatedSchuetzenSatz();
        schuetzenSatz2.setSchuss1(5);
        
        // Act
        boolean result = schuetzenSatz1.equals(schuetzenSatz2);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withDifferentPfeil2_shouldReturnFalse() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz1 = createFullyPopulatedSchuetzenSatz();
        SchuetzenSatzDO schuetzenSatz2 = createFullyPopulatedSchuetzenSatz();
        schuetzenSatz2.setSchuss2(5);
        
        // Act
        boolean result = schuetzenSatz1.equals(schuetzenSatz2);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withDifferentPfeil3_shouldReturnFalse() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz1 = createFullyPopulatedSchuetzenSatz();
        SchuetzenSatzDO schuetzenSatz2 = createFullyPopulatedSchuetzenSatz();
        schuetzenSatz2.setSchuss3(5);
        
        // Act
        boolean result = schuetzenSatz1.equals(schuetzenSatz2);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void hashCode_withSameFields_shouldReturnSameHash() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz1 = createFullyPopulatedSchuetzenSatz();
        SchuetzenSatzDO schuetzenSatz2 = createFullyPopulatedSchuetzenSatz();
        
        // Act
        int hash1 = schuetzenSatz1.hashCode();
        int hash2 = schuetzenSatz2.hashCode();
        
        // Assert
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    public void hashCode_withDifferentFields_shouldReturnDifferentHash() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz1 = createFullyPopulatedSchuetzenSatz();
        SchuetzenSatzDO schuetzenSatz2 = createFullyPopulatedSchuetzenSatz();
        schuetzenSatz2.setSchuetzenId(2L);
        
        // Act
        int hash1 = schuetzenSatz1.hashCode();
        int hash2 = schuetzenSatz2.hashCode();
        
        // Assert
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    public void hashCode_withNullFields_shouldHandleGracefully() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz1 = new SchuetzenSatzDO();
        SchuetzenSatzDO schuetzenSatz2 = new SchuetzenSatzDO();
        
        // Act
        int hash1 = schuetzenSatz1.hashCode();
        int hash2 = schuetzenSatz2.hashCode();
        
        // Assert
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    public void perfectScore_calculation_shouldBe30() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        schuetzenSatz.setSchuss1(10);
        schuetzenSatz.setSchuss2(10);
        schuetzenSatz.setSchuss3(10);
        
        // Act
        int totalScore = (schuetzenSatz.getSchuss1() != null ? schuetzenSatz.getSchuss1() : 0) +
                        (schuetzenSatz.getSchuss2() != null ? schuetzenSatz.getSchuss2() : 0) +
                        (schuetzenSatz.getSchuss3() != null ? schuetzenSatz.getSchuss3() : 0);
        
        // Assert
        assertThat(totalScore).isEqualTo(30);
    }

    @Test
    public void zeroScore_calculation_shouldBe0() {
        // Arrange
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        schuetzenSatz.setSchuss1(0);
        schuetzenSatz.setSchuss2(0);
        schuetzenSatz.setSchuss3(0);
        
        // Act
        int totalScore = (schuetzenSatz.getSchuss1() != null ? schuetzenSatz.getSchuss1() : 0) +
                        (schuetzenSatz.getSchuss2() != null ? schuetzenSatz.getSchuss2() : 0) +
                        (schuetzenSatz.getSchuss3() != null ? schuetzenSatz.getSchuss3() : 0);
        
        // Assert
        assertThat(totalScore).isEqualTo(0);
    }

    // Helper method for creating test data
    private SchuetzenSatzDO createFullyPopulatedSchuetzenSatz() {
        SchuetzenSatzDO schuetzenSatz = new SchuetzenSatzDO();
        schuetzenSatz.setSchuetzenId(1L);
        schuetzenSatz.setSchuss1(10);
        schuetzenSatz.setSchuss2(9);
        schuetzenSatz.setSchuss3(8);
        return schuetzenSatz;
    }
}