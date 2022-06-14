package de.bogenliga.application.business.ligamatch.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.ligamatch.impl.dao.BaseLigamatchTest;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.api.types.MatchDO;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author Christopher Luzzi, Tests the Mapping from LigamatchBE to MatchDO
 */

public class LigamatchToMatchMapperTest extends BaseLigamatchTest {

    private void validateDO(LigamatchBE ligamatchBE, MatchDO matchDOMapped){
        assertThat(ligamatchBE).isNotNull();
        assertThat(ligamatchBE.getWettkampfId()).isEqualTo(matchDOMapped.getWettkampfId()).isEqualTo(WETTKAMPF_ID);
        assertThat(ligamatchBE.getMatchId()).isEqualTo(matchDOMapped.getId()).isEqualTo(MATCH_ID);
        assertThat(ligamatchBE.getMatchNr()).isEqualTo(matchDOMapped.getNr()).isEqualTo(MATCH_NR);
        assertThat(ligamatchBE.getScheibennummer()).isEqualTo(matchDOMapped.getScheibenNummer()).isEqualTo(SCHEIBENNUMMER);
        assertThat(ligamatchBE.getMannschaftId()).isEqualTo(matchDOMapped.getMannschaftId()).isEqualTo(
                MANNSCHAFT_ID);
        assertThat(ligamatchBE.getSatzpunkte()).isEqualTo(matchDOMapped.getSatzpunkte()).isEqualTo(SATZPUNKTE);
        assertThat(ligamatchBE.getMatchpunkte()).isEqualTo(matchDOMapped.getMatchpunkte()).isEqualTo(MATCHPUNKTE);
        assertThat(ligamatchBE.getBegegnung()).isEqualTo(matchDOMapped.getBegegnung()).isEqualTo(BEGEGNUNG);
        assertThat(ligamatchBE.getStrafpunkteSatz1()).isEqualTo(matchDOMapped.getStrafPunkteSatz1()).isEqualTo(
                STRAFPUNKT_SATZ_1);
        assertThat(ligamatchBE.getStrafpunkteSatz2()).isEqualTo(matchDOMapped.getStrafPunkteSatz2()).isEqualTo(
                STRAFPUNKT_SATZ_2);
        assertThat(ligamatchBE.getStrafpunkteSatz3()).isEqualTo(matchDOMapped.getStrafPunkteSatz3()).isEqualTo(
                STRAFPUNKT_SATZ_3);
        assertThat(ligamatchBE.getStrafpunkteSatz4()).isEqualTo(matchDOMapped.getStrafPunkteSatz4()).isEqualTo(
                STRAFPUNKT_SATZ_4);
        assertThat(ligamatchBE.getStrafpunkteSatz5()).isEqualTo(matchDOMapped.getStrafPunkteSatz5()).isEqualTo(
                STRAFPUNKT_SATZ_5);

    }

    @Test
    public void testMapping(){
        LigamatchBE ligamatchBE = getLigamatchBE();
        MatchDO matchDO = LigamatchToMatchMapper.LigamatchToMatchDO.apply(ligamatchBE);
        this.validateDO(ligamatchBE, matchDO);
    }

}
