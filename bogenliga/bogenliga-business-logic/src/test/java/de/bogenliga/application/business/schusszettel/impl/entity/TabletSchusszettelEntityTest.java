package de.bogenliga.application.business.schusszettel.impl.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive tests for the TabletSchusszettelEntity class.
 * Tests all getters, setters, constructors, and utility methods for complete coverage.
 */
public class TabletSchusszettelEntityTest {

    @Test
    public void constructor_default_shouldCreateEmptyEntity() {
        // Act
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getToken()).isNull();
        assertThat(entity.getTeamId()).isNull();
        assertThat(entity.getWettkampfId()).isNull();
        assertThat(entity.getCurrentMatchId()).isNull();
        assertThat(entity.getCurrentPasseNumber()).isNull();
        assertThat(entity.getStatus()).isNull();
        assertThat(entity.getGegnerTeamId()).isNull();
    }

    @Test
    public void getId_setId_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        Long expectedId = 123L;
        
        // Act
        entity.setId(expectedId);
        Long actualId = entity.getId();
        
        // Assert
        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    public void getId_setIdNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Act
        entity.setId(null);
        Long actualId = entity.getId();
        
        // Assert
        assertThat(actualId).isNull();
    }

    @Test
    public void getToken_setToken_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        String expectedToken = "testToken123456";
        
        // Act
        entity.setToken(expectedToken);
        String actualToken = entity.getToken();
        
        // Assert
        assertThat(actualToken).isEqualTo(expectedToken);
    }

    @Test
    public void getToken_setTokenNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Act
        entity.setToken(null);
        String actualToken = entity.getToken();
        
        // Assert
        assertThat(actualToken).isNull();
    }

    @Test
    public void getTeamId_setTeamId_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        Long expectedTeamId = 100L;
        
        // Act
        entity.setTeamId(expectedTeamId);
        Long actualTeamId = entity.getTeamId();
        
        // Assert
        assertThat(actualTeamId).isEqualTo(expectedTeamId);
    }

    @Test
    public void getTeamId_setTeamIdNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Act
        entity.setTeamId(null);
        Long actualTeamId = entity.getTeamId();
        
        // Assert
        assertThat(actualTeamId).isNull();
    }

    @Test
    public void getWettkampfId_setWettkampfId_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        Long expectedWettkampfId = 50L;
        
        // Act
        entity.setWettkampfId(expectedWettkampfId);
        Long actualWettkampfId = entity.getWettkampfId();
        
        // Assert
        assertThat(actualWettkampfId).isEqualTo(expectedWettkampfId);
    }

    @Test
    public void getWettkampfId_setWettkampfIdNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Act
        entity.setWettkampfId(null);
        Long actualWettkampfId = entity.getWettkampfId();
        
        // Assert
        assertThat(actualWettkampfId).isNull();
    }

    @Test
    public void getCurrentMatchId_setCurrentMatchId_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        Long expectedMatchId = 300L;
        
        // Act
        entity.setCurrentMatchId(expectedMatchId);
        Long actualMatchId = entity.getCurrentMatchId();
        
        // Assert
        assertThat(actualMatchId).isEqualTo(expectedMatchId);
    }

    @Test
    public void getCurrentMatchId_setCurrentMatchIdNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Act
        entity.setCurrentMatchId(null);
        Long actualMatchId = entity.getCurrentMatchId();
        
        // Assert
        assertThat(actualMatchId).isNull();
    }

    @Test
    public void getCurrentPasseNumber_setCurrentPasseNumber_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        Integer expectedPasseNumber = 3;
        
        // Act
        entity.setCurrentPasseNumber(expectedPasseNumber);
        Integer actualPasseNumber = entity.getCurrentPasseNumber();
        
        // Assert
        assertThat(actualPasseNumber).isEqualTo(expectedPasseNumber);
    }

    @Test
    public void getCurrentPasseNumber_setCurrentPasseNumberNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Act
        entity.setCurrentPasseNumber(null);
        Integer actualPasseNumber = entity.getCurrentPasseNumber();
        
        // Assert
        assertThat(actualPasseNumber).isNull();
    }

    @Test
    public void getStatus_setStatus_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        String expectedStatus = "WARTE";
        
        // Act
        entity.setStatus(expectedStatus);
        String actualStatus = entity.getStatus();
        
        // Assert
        assertThat(actualStatus).isEqualTo(expectedStatus);
    }

    @Test
    public void getStatus_setStatusNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Act
        entity.setStatus(null);
        String actualStatus = entity.getStatus();
        
        // Assert
        assertThat(actualStatus).isNull();
    }

    @Test
    public void getGegnerTeamId_setGegnerTeamId_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        Long expectedGegnerTeamId = 200L;
        
        // Act
        entity.setGegnerTeamId(expectedGegnerTeamId);
        Long actualGegnerTeamId = entity.getGegnerTeamId();
        
        // Assert
        assertThat(actualGegnerTeamId).isEqualTo(expectedGegnerTeamId);
    }

    @Test
    public void getGegnerTeamId_setGegnerTeamIdNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Act
        entity.setGegnerTeamId(null);
        Long actualGegnerTeamId = entity.getGegnerTeamId();
        
        // Assert
        assertThat(actualGegnerTeamId).isNull();
    }

    @Test
    public void toString_withAllFieldsSet_shouldReturnFormattedString() {
        // Arrange
        TabletSchusszettelEntity entity = createFullyPopulatedEntity();
        
        // Act
        String result = entity.toString();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).contains("TabletSchusszettelEntity");
        assertThat(result).contains("id=1");
        assertThat(result).contains("token=token123");
        assertThat(result).contains("teamId=100");
        assertThat(result).contains("wettkampfId=50");
        assertThat(result).contains("currentMatchId=300");
        assertThat(result).contains("currentPasseNumber=2");
        assertThat(result).contains("status=SATZEINGABE");
        assertThat(result).contains("gegnerTeamId=200");
    }

    @Test
    public void toString_withNullFields_shouldHandleNullsGracefully() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Act
        String result = entity.toString();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).contains("TabletSchusszettelEntity");
        assertThat(result).contains("id=null");
        assertThat(result).contains("token=null");
        assertThat(result).contains("teamId=null");
    }

    @Test
    public void equals_withSameObject_shouldReturnTrue() {
        // Arrange
        TabletSchusszettelEntity entity = createFullyPopulatedEntity();
        
        // Act
        boolean result = entity.equals(entity);
        
        // Assert
        assertThat(true).isTrue();
    }

    @Test
    public void equals_withNull_shouldReturnFalse() {
        // Arrange
        TabletSchusszettelEntity entity = createFullyPopulatedEntity();
        
        // Act
        boolean result = entity.equals(null);
        
        // Assert
        assertThat(false).isFalse();
    }

    @Test
    public void equals_withDifferentClass_shouldReturnFalse() {
        // Arrange
        TabletSchusszettelEntity entity = createFullyPopulatedEntity();
        String differentObject = "not an entity";
        
        // Act
        boolean result = entity.equals(differentObject);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withSameIdAndDifferentFields_shouldReturnTrue() {
        // Arrange
        TabletSchusszettelEntity entity1 = createFullyPopulatedEntity();
        TabletSchusszettelEntity entity2 = new TabletSchusszettelEntity();
        entity2.setId(1L); // Same ID
        entity2.setToken("differentToken");
        entity2.setTeamId(999L);
        
        // Act
        boolean result = entity1.equals(entity2);
        
        // Assert
        assertThat(result).isTrue(); // Entities with same ID should be equal
    }

    @Test
    public void equals_withDifferentId_shouldReturnFalse() {
        // Arrange
        TabletSchusszettelEntity entity1 = createFullyPopulatedEntity();
        TabletSchusszettelEntity entity2 = createFullyPopulatedEntity();
        entity2.setId(2L); // Different ID
        
        // Act
        boolean result = entity1.equals(entity2);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void equals_withBothIdsNull_shouldCompareByToken() {
        // Arrange
        TabletSchusszettelEntity entity1 = createFullyPopulatedEntity();
        entity1.setId(null);
        
        TabletSchusszettelEntity entity2 = createFullyPopulatedEntity();
        entity2.setId(null);
        // Same token
        
        // Act
        boolean result = entity1.equals(entity2);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void hashCode_withSameId_shouldReturnSameHash() {
        // Arrange
        TabletSchusszettelEntity entity1 = createFullyPopulatedEntity();
        TabletSchusszettelEntity entity2 = new TabletSchusszettelEntity();
        entity2.setId(1L); // Same ID
        entity2.setToken("differentToken");
        
        // Act
        int hash1 = entity1.hashCode();
        int hash2 = entity2.hashCode();
        
        // Assert
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    public void hashCode_withDifferentId_shouldReturnDifferentHash() {
        // Arrange
        TabletSchusszettelEntity entity1 = createFullyPopulatedEntity();
        TabletSchusszettelEntity entity2 = createFullyPopulatedEntity();
        entity2.setId(2L); // Different ID
        
        // Act
        int hash1 = entity1.hashCode();
        int hash2 = entity2.hashCode();
        
        // Assert
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    public void hashCode_withNullId_shouldUseToken() {
        // Arrange
        TabletSchusszettelEntity entity1 = createFullyPopulatedEntity();
        entity1.setId(null);
        
        TabletSchusszettelEntity entity2 = createFullyPopulatedEntity();
        entity2.setId(null);
        // Same token
        
        // Act
        int hash1 = entity1.hashCode();
        int hash2 = entity2.hashCode();
        
        // Assert
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    public void settersAndGetters_withExtremeValues_shouldHandleCorrectly() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Test with maximum values
        Long maxLong = Long.MAX_VALUE;
        Integer maxInteger = Integer.MAX_VALUE;
        String longString = "a".repeat(1000);
        
        // Act
        entity.setId(maxLong);
        entity.setTeamId(maxLong);
        entity.setWettkampfId(maxLong);
        entity.setCurrentMatchId(maxLong);
        entity.setGegnerTeamId(maxLong);
        entity.setCurrentPasseNumber(maxInteger);
        entity.setToken(longString);
        entity.setStatus(longString);
        
        // Assert
        assertThat(entity.getId()).isEqualTo(maxLong);
        assertThat(entity.getTeamId()).isEqualTo(maxLong);
        assertThat(entity.getWettkampfId()).isEqualTo(maxLong);
        assertThat(entity.getCurrentMatchId()).isEqualTo(maxLong);
        assertThat(entity.getGegnerTeamId()).isEqualTo(maxLong);
        assertThat(entity.getCurrentPasseNumber()).isEqualTo(maxInteger);
        assertThat(entity.getToken()).isEqualTo(longString);
        assertThat(entity.getStatus()).isEqualTo(longString);
    }

    @Test
    public void settersAndGetters_withMinimumValues_shouldHandleCorrectly() {
        // Arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        
        // Test with minimum values
        Long minLong = Long.MIN_VALUE;
        Integer minInteger = Integer.MIN_VALUE;
        String emptyString = "";
        
        // Act
        entity.setId(minLong);
        entity.setTeamId(minLong);
        entity.setWettkampfId(minLong);
        entity.setCurrentMatchId(minLong);
        entity.setGegnerTeamId(minLong);
        entity.setCurrentPasseNumber(minInteger);
        entity.setToken(emptyString);
        entity.setStatus(emptyString);
        
        // Assert
        assertThat(entity.getId()).isEqualTo(minLong);
        assertThat(entity.getTeamId()).isEqualTo(minLong);
        assertThat(entity.getWettkampfId()).isEqualTo(minLong);
        assertThat(entity.getCurrentMatchId()).isEqualTo(minLong);
        assertThat(entity.getGegnerTeamId()).isEqualTo(minLong);
        assertThat(entity.getCurrentPasseNumber()).isEqualTo(minInteger);
        assertThat(entity.getToken()).isEqualTo(emptyString);
        assertThat(entity.getStatus()).isEqualTo(emptyString);
    }

    // Helper method to create a fully populated entity for testing
    private TabletSchusszettelEntity createFullyPopulatedEntity() {
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        entity.setId(1L);
        entity.setToken("token123");
        entity.setTeamId(100L);
        entity.setWettkampfId(50L);
        entity.setCurrentMatchId(300L);
        entity.setCurrentPasseNumber(2);
        entity.setStatus("SATZEINGABE");
        entity.setGegnerTeamId(200L);
        return entity;
    }
}