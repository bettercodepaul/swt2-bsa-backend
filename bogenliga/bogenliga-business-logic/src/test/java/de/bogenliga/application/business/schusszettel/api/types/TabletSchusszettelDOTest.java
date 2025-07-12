package de.bogenliga.application.business.schusszettel.api.types;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive tests for the TabletSchusszettelDO class.
 * Tests all constructors, getters, setters, and utility methods for complete coverage.
 */
public class TabletSchusszettelDOTest {

    @Test
    public void constructor_default_shouldCreateEmptyObject() {
        // Act
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        
        // Assert
        assertThat(tabletDO).isNotNull();
        assertThat(tabletDO.getStatus()).isNull();
        assertThat(tabletDO.getEigenesTeam()).isNull();
        assertThat(tabletDO.getGegnerischesTeam()).isNull();
        assertThat(tabletDO.getSchuetzenMatchPunkte()).isNull();
        assertThat(tabletDO.getSchuetzeStammDaten()).isNull();
        assertThat(tabletDO.getSatzErgebnisse()).isNull();
        assertThat(tabletDO.getMatchErgebnis()).isNull();
        assertThat(tabletDO.getVerfuegbareSchuetzen()).isNull();
        assertThat(tabletDO.getWettkampfInfo()).isNull();
        assertThat(tabletDO.getCurrentPasseNumber()).isNull();
        assertThat(tabletDO.getEigenesTeamMatchId()).isNull();
        assertThat(tabletDO.getGegnerischesTeamMatchId()).isNull();
    }

    @Test
    public void getStatus_setStatus_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        TabletSchusszettelDO.TabletSchusszettelStatus expectedStatus = TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE;
        
        // Act
        tabletDO.setStatus(expectedStatus);
        TabletSchusszettelDO.TabletSchusszettelStatus actualStatus = tabletDO.getStatus();
        
        // Assert
        assertThat(actualStatus).isEqualTo(expectedStatus);
    }

    @Test
    public void getStatus_setStatusNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        
        // Act
        tabletDO.setStatus(null);
        TabletSchusszettelDO.TabletSchusszettelStatus actualStatus = tabletDO.getStatus();
        
        // Assert
        assertThat(actualStatus).isNull();
    }

    @Test
    public void getStatus_setAllValidStatuses_shouldGetSetValues() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        TabletSchusszettelDO.TabletSchusszettelStatus[] validStatuses = {
            TabletSchusszettelDO.TabletSchusszettelStatus.SCHUETZENMELDUNG,
            TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE,
            TabletSchusszettelDO.TabletSchusszettelStatus.WARTE,
            TabletSchusszettelDO.TabletSchusszettelStatus.MATCH_ENDE,
            TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED,
            TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE
        };
        
        // Act & Assert
        for (TabletSchusszettelDO.TabletSchusszettelStatus status : validStatuses) {
            tabletDO.setStatus(status);
            assertThat(tabletDO.getStatus()).isEqualTo(status);
        }
    }

    @Test
    public void getCurrentPasseNumber_setCurrentPasseNumber_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        Integer expectedPasseNumber = 3;
        
        // Act
        tabletDO.setCurrentPasseNumber(expectedPasseNumber);
        Integer actualPasseNumber = tabletDO.getCurrentPasseNumber();
        
        // Assert
        assertThat(actualPasseNumber).isEqualTo(expectedPasseNumber);
    }

    @Test
    public void getCurrentPasseNumber_setCurrentPasseNumberNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        
        // Act
        tabletDO.setCurrentPasseNumber(null);
        Integer actualPasseNumber = tabletDO.getCurrentPasseNumber();
        
        // Assert
        assertThat(actualPasseNumber).isNull();
    }

    @Test
    public void getEigenesTeamMatchId_setEigenesTeamMatchId_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        Long expectedMatchId = 100L;
        
        // Act
        tabletDO.setEigenesTeamMatchId(expectedMatchId);
        Long actualMatchId = tabletDO.getEigenesTeamMatchId();
        
        // Assert
        assertThat(actualMatchId).isEqualTo(expectedMatchId);
    }

    @Test
    public void getEigenesTeamMatchId_setEigenesTeamMatchIdNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        
        // Act
        tabletDO.setEigenesTeamMatchId(null);
        Long actualMatchId = tabletDO.getEigenesTeamMatchId();
        
        // Assert
        assertThat(actualMatchId).isNull();
    }

    @Test
    public void getGegnerischesTeamMatchId_setGegnerischesTeamMatchId_shouldGetSetValue() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        Long expectedMatchId = 200L;
        
        // Act
        tabletDO.setGegnerischesTeamMatchId(expectedMatchId);
        Long actualMatchId = tabletDO.getGegnerischesTeamMatchId();
        
        // Assert
        assertThat(actualMatchId).isEqualTo(expectedMatchId);
    }

    @Test
    public void getGegnerischesTeamMatchId_setGegnerischesTeamMatchIdNull_shouldGetNull() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        
        // Act
        tabletDO.setGegnerischesTeamMatchId(null);
        Long actualMatchId = tabletDO.getGegnerischesTeamMatchId();
        
        // Assert
        assertThat(actualMatchId).isNull();
    }

    @Test
    public void settersAndGetters_withNullValues_shouldHandleCorrectly() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        
        // Act
        tabletDO.setStatus(null);
        tabletDO.setEigenesTeam(null);
        tabletDO.setGegnerischesTeam(null);
        tabletDO.setSchuetzenMatchPunkte(null);
        tabletDO.setSchuetzeStammDaten(null);
        tabletDO.setSatzErgebnisse(null);
        tabletDO.setMatchErgebnis(null);
        tabletDO.setVerfuegbareSchuetzen(null);
        tabletDO.setWettkampfInfo(null);
        tabletDO.setCurrentPasseNumber(null);
        tabletDO.setEigenesTeamMatchId(null);
        tabletDO.setGegnerischesTeamMatchId(null);
        
        // Assert
        assertThat(tabletDO.getStatus()).isNull();
        assertThat(tabletDO.getEigenesTeam()).isNull();
        assertThat(tabletDO.getGegnerischesTeam()).isNull();
        assertThat(tabletDO.getSchuetzenMatchPunkte()).isNull();
        assertThat(tabletDO.getSchuetzeStammDaten()).isNull();
        assertThat(tabletDO.getSatzErgebnisse()).isNull();
        assertThat(tabletDO.getMatchErgebnis()).isNull();
        assertThat(tabletDO.getVerfuegbareSchuetzen()).isNull();
        assertThat(tabletDO.getWettkampfInfo()).isNull();
        assertThat(tabletDO.getCurrentPasseNumber()).isNull();
        assertThat(tabletDO.getEigenesTeamMatchId()).isNull();
        assertThat(tabletDO.getGegnerischesTeamMatchId()).isNull();
    }

    @Test
    public void settersAndGetters_withValidValues_shouldHandleCorrectly() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        
        // Act
        tabletDO.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE);
        tabletDO.setCurrentPasseNumber(5);
        tabletDO.setEigenesTeamMatchId(1000L);
        tabletDO.setGegnerischesTeamMatchId(2000L);
        
        // Assert
        assertThat(tabletDO.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE);
        assertThat(tabletDO.getCurrentPasseNumber()).isEqualTo(5);
        assertThat(tabletDO.getEigenesTeamMatchId()).isEqualTo(1000L);
        assertThat(tabletDO.getGegnerischesTeamMatchId()).isEqualTo(2000L);
    }

    @Test
    public void settersAndGetters_multipleOperations_shouldRetainLastValue() {
        // Arrange
        TabletSchusszettelDO tabletDO = new TabletSchusszettelDO();
        
        // Act
        tabletDO.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.SCHUETZENMELDUNG);
        tabletDO.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);
        tabletDO.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE);
        
        tabletDO.setCurrentPasseNumber(1);
        tabletDO.setCurrentPasseNumber(3);
        tabletDO.setCurrentPasseNumber(5);
        
        tabletDO.setEigenesTeamMatchId(100L);
        tabletDO.setEigenesTeamMatchId(500L);
        
        // Assert
        assertThat(tabletDO.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE);
        assertThat(tabletDO.getCurrentPasseNumber()).isEqualTo(5);
        assertThat(tabletDO.getEigenesTeamMatchId()).isEqualTo(500L);
    }

    @Test
    public void tabletSchusszettelStatus_enum_shouldHaveAllExpectedValues() {
        // Act & Assert
        TabletSchusszettelDO.TabletSchusszettelStatus[] values = TabletSchusszettelDO.TabletSchusszettelStatus.values();
        
        assertThat(values).hasSize(6);
        assertThat(values).contains(
            TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE,
            TabletSchusszettelDO.TabletSchusszettelStatus.SCHUETZENMELDUNG,
            TabletSchusszettelDO.TabletSchusszettelStatus.WARTE,
            TabletSchusszettelDO.TabletSchusszettelStatus.MATCH_ENDE,
            TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED,
            TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE
        );
    }

    @Test
    public void tabletSchusszettelStatus_valueOf_shouldReturnCorrectEnum() {
        // Act & Assert
        assertThat(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf("SATZEINGABE"))
            .isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);
        assertThat(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf("SCHUETZENMELDUNG"))
            .isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.SCHUETZENMELDUNG);
        assertThat(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf("WARTE"))
            .isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE);
        assertThat(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf("MATCH_ENDE"))
            .isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.MATCH_ENDE);
        assertThat(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf("NOT_ALLOWED"))
            .isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED);
        assertThat(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf("WETTKAMPF_ENDE"))
            .isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE);
    }
}