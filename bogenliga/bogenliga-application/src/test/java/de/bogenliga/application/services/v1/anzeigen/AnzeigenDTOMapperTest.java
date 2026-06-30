package de.bogenliga.application.services.v1.anzeigen;

import de.bogenliga.application.services.v1.wettkampf.mapper.AnzeigenDTOMapper;
import org.junit.Test;
import de.bogenliga.application.business.wettkampf.api.types.AnzeigenDO;
import de.bogenliga.application.services.v1.wettkampf.model.AnzeigenDTO;

import static org.assertj.core.api.Assertions.assertThat;

public class AnzeigenDTOMapperTest {

    private static final Long ID = 1L;
    private static final String BILD_ID = "Screen_01";
    private static final String TABLE_TYP = "LED_WALL";
    private static final Long VERANSTALTUNG_ID = 42L;
    private static final int AKTUELLES_MATCH = 3;

    @Test
    public void toDTO_shouldMapAllFieldsCorrectly() {
        AnzeigenDO anzeigenDO = new AnzeigenDO(ID, BILD_ID, TABLE_TYP, VERANSTALTUNG_ID, AKTUELLES_MATCH);

        AnzeigenDTO result = AnzeigenDTOMapper.toDTO.apply(anzeigenDO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ID);
        assertThat(result.getPhysischeBildschirmId()).isEqualTo(BILD_ID);
        assertThat(result.getTableTyp()).isEqualTo(TABLE_TYP);
        assertThat(result.getWettkampfId()).isEqualTo(VERANSTALTUNG_ID);
        assertThat(result.getAktuellesMatch()).isEqualTo(AKTUELLES_MATCH);
    }

    @Test
    public void toDO_shouldMapAllFieldsCorrectly() {
        AnzeigenDTO anzeigenDTO = new AnzeigenDTO(ID, BILD_ID, TABLE_TYP, VERANSTALTUNG_ID, AKTUELLES_MATCH);

        AnzeigenDO result = AnzeigenDTOMapper.toDO.apply(anzeigenDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ID);
        assertThat(result.getPhysischeBildschirmId()).isEqualTo(BILD_ID);
        assertThat(result.getTableTyp()).isEqualTo(TABLE_TYP);
        assertThat(result.getWettkampfId()).isEqualTo(VERANSTALTUNG_ID);
        assertThat(result.getAktuellesMatch()).isEqualTo(AKTUELLES_MATCH);
    }
}
