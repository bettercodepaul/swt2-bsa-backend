package de.bogenliga.application.services.v1.anzeigen;

import de.bogenliga.application.services.v1.wettkampf.model.AnzeigenDTO;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AnzeigenDTOTest {

    private static final Long ID = 123L;
    private static final String BILD_ID = "Screen_B";
    private static final String TABLE_TYP = "DEFAULT_TABLE";
    private static final Long VERANSTALTUNG_ID = 99L;
    private static final int AKTUELLES_MATCH = 5;

    @Test
    public void noArgsConstructor_shouldInitializeWithDefaultValues() {
        // When
        AnzeigenDTO result = new AnzeigenDTO();

        // Then - Überprüfung der im Konstruktor fest hinterlegten Default-Werte
        assertThat(result.getId()).isNull();
        assertThat(result.getPhysischeBildschirmId()).isNull();
        assertThat(result.getTableTyp()).isEqualTo("Tabelle");
        assertThat(result.getWettkampfId()).isNull();
        assertThat(result.getAktuellesMatch()).isEqualTo(1);
    }

    @Test
    public void allArgsConstructor_shouldMapFieldsCorrectly() {
        // When - Konstruktor mit allen Parametern aufrufen
        AnzeigenDTO result = new AnzeigenDTO(ID, BILD_ID, TABLE_TYP, VERANSTALTUNG_ID, AKTUELLES_MATCH);

        // Then
        assertThat(result.getId()).isEqualTo(ID);
        assertThat(result.getPhysischeBildschirmId()).isEqualTo(BILD_ID);
        assertThat(result.getTableTyp()).isEqualTo(TABLE_TYP);
        assertThat(result.getWettkampfId()).isEqualTo(VERANSTALTUNG_ID);
        assertThat(result.getAktuellesMatch()).isEqualTo(AKTUELLES_MATCH);
    }

    @Test
    public void settersAndGetters_shouldWorkCorrectly() {
        // Given
        AnzeigenDTO result = new AnzeigenDTO();

        // When - Setter explizit aufrufen
        result.setPhysischeBildschirmId(BILD_ID);
        result.setTableTyp(TABLE_TYP);
        result.setWettkampfId(VERANSTALTUNG_ID);
        result.setAktuellesMatch(AKTUELLES_MATCH);

        // Then - Getter validieren
        assertThat(result.getPhysischeBildschirmId()).isEqualTo(BILD_ID);
        assertThat(result.getTableTyp()).isEqualTo(TABLE_TYP);
        assertThat(result.getWettkampfId()).isEqualTo(VERANSTALTUNG_ID);
        assertThat(result.getAktuellesMatch()).isEqualTo(AKTUELLES_MATCH);
    }
}
