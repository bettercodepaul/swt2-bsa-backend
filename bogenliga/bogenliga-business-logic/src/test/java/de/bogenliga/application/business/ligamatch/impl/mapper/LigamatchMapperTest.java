package de.bogenliga.application.business.ligamatch.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.ligamatch.impl.dao.BaseLigamatchTest;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.api.types.LigamatchDO;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author Adrian Kempf, Tests the Mapping from LigamatchBE to LigamatchDO
 */

public class LigamatchMapperTest extends BaseLigamatchTest {

    private void validateDO(LigamatchBE ligamatchBE, LigamatchDO ligamatchDOMapped){
        assertThat(ligamatchBE).isNotNull();
        assertThat(ligamatchBE.getMatchId()).isEqualTo(ligamatchDOMapped.getId()).isEqualTo(MATCH_ID);
        assertThat(ligamatchBE.getMatchIdGegner()).isEqualTo(ligamatchDOMapped.getMatchIdGegner()).isEqualTo(MATCH_ID_GEGNER);
        assertThat(ligamatchBE.getWettkampfId()).isEqualTo(ligamatchDOMapped.getWettkampfId()).isEqualTo(WETTKAMPF_ID);
        assertThat(ligamatchBE.getMatchNr()).isEqualTo(ligamatchDOMapped.getMatchNr()).isEqualTo(MATCH_NR);
        assertThat(ligamatchBE.getMatchScheibennummer()).isEqualTo(ligamatchDOMapped.getMatchScheibennummer()).isEqualTo(SCHEIBENNUMMER);
        assertThat(ligamatchBE.getScheibennummerGegner()).isEqualTo(ligamatchDOMapped.getScheibennummerGegner()).isEqualTo(SCHEIBENNUMMER_GEGNER);
        assertThat(ligamatchBE.getMatchpunkte()).isEqualTo(ligamatchDOMapped.getMatchpunkte()).isEqualTo(MATCHPUNKTE);
        assertThat(ligamatchBE.getSatzpunkte()).isEqualTo(ligamatchDOMapped.getSatzpunkte()).isEqualTo(SATZPUNKTE);
        assertThat(ligamatchBE.getMannschaftId()).isEqualTo(ligamatchDOMapped.getMannschaftId()).isEqualTo(
                MANNSCHAFT_ID);
        assertThat(ligamatchBE.getMannschaftName()).isEqualTo(ligamatchDOMapped.getMannschaftName()).isEqualTo(
                MANNSCHAFT_NAME);
        assertThat(ligamatchBE.getMannschaftNameGegner()).isEqualTo(ligamatchDOMapped.getNameGegner()).isEqualTo(
                MANNSCHAFT_NAME_GEGNER);
        assertThat(ligamatchBE.getNaechsteMatchId()).isEqualTo(ligamatchDOMapped.getNaechsteMatchId()).isEqualTo(
                NAECHSTE_MATCH_ID);
        assertThat(ligamatchBE.getNaechsteNaechsteMatchId()).isEqualTo(ligamatchDOMapped.getNaechsteNaechsteMatchNrMatchId()).isEqualTo(
                NAECHSTE_NAECHSTE_MATCH_ID);
        assertThat(ligamatchBE.getStrafpunkteSatz1()).isEqualTo(ligamatchDOMapped.getStrafpunkteSatz1()).isEqualTo(
                STRAFPUNKT_SATZ_1);
        assertThat(ligamatchBE.getStrafpunkteSatz2()).isEqualTo(ligamatchDOMapped.getStrafpunkteSatz2()).isEqualTo(
                STRAFPUNKT_SATZ_2);
        assertThat(ligamatchBE.getStrafpunkteSatz3()).isEqualTo(ligamatchDOMapped.getStrafpunkteSatz3()).isEqualTo(
                STRAFPUNKT_SATZ_3);
        assertThat(ligamatchBE.getStrafpunkteSatz4()).isEqualTo(ligamatchDOMapped.getStrafpunkteSatz4()).isEqualTo(
                STRAFPUNKT_SATZ_4);
        assertThat(ligamatchBE.getStrafpunkteSatz5()).isEqualTo(ligamatchDOMapped.getStrafpunkteSatz5()).isEqualTo(
                STRAFPUNKT_SATZ_5);
    }

    @Test
    public void testMapping(){
        LigamatchBE ligamatchBE = getLigamatchBE();
        LigamatchDO ligamatchDO = LigamatchMapper.toLigamatchDO.apply(ligamatchBE);
        this.validateDO(ligamatchBE, ligamatchDO);
    }

}
