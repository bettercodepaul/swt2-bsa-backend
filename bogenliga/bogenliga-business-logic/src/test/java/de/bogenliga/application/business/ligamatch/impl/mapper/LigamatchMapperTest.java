package de.bogenliga.application.business.ligamatch.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.ligamatch.impl.BaseLigamatchTest;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.mapper.MatchMapper;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class LigamatchMapperTest extends BaseLigamatchTest {


    private void validateDO(LigamatchBE ligamatchBE, MatchDO matchDOMapped){
        assertThat(ligamatchBE).isNotNull();
        assertThat(ligamatchBE.getWettkampfId()).isEqualTo(matchDOMapped.getWettkampfId()).isEqualTo(WETTKAMPF_ID);
        assertThat(ligamatchBE.getMatchId()).isEqualTo(matchDOMapped.getId()).isEqualTo(MATCH_ID);
        assertThat(ligamatchBE.getMatchNr()).isEqualTo(matchDOMapped.getNr()).isEqualTo(MATCH_NR);
        assertThat(ligamatchBE.getScheibennummer()).isEqualTo(matchDOMapped.getScheibenNummer()).isEqualTo(SCHEIBENNUMMER);
        assertThat(ligamatchBE.getMannschaftId()).isEqualTo(matchDOMapped.getMannschaftId()).isEqualTo(
                MANNSCHAFT_ID);
        assertThat(matchDOMapped.getMatchpunkte()).isEqualTo(null);
        assertThat(matchDOMapped.getSatzpunkte()).isEqualTo(null);
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

//    private void validateBES(LigamatchBE ligamatchBE, LigamatchBE ligamatchBEMapped) {
//        assertThat(ligamatchBE).isNotNull();
//        assertThat(ligamatchBE.getWettkampfId()).isEqualTo(ligamatchBEMapped.getWettkampfId()).isEqualTo(WETTKAMPF_ID);
//        assertThat(ligamatchBE.getMatchId()).isEqualTo(ligamatchBEMapped.getMatchId()).isEqualTo(MATCH_ID);
//        assertThat(ligamatchBE.getMatchNr()).isEqualTo(ligamatchBEMapped.getMatchNr()).isEqualTo(MATCH_NR);
//        assertThat(ligamatchBE.getScheibennummer()).isEqualTo(ligamatchBEMapped.getScheibennummer()).isEqualTo(SCHEIBENNUMMER);
//        assertThat(ligamatchBE.getMannschaftId()).isEqualTo(ligamatchBEMapped.getMannschaftId()).isEqualTo(
//                MANNSCHAFT_ID);
//        assertThat(ligamatchBE.getBegegnung()).isEqualTo(ligamatchBEMapped.getBegegnung()).isEqualTo(BEGEGNUNG);
//        assertThat(ligamatchBE.getNaechsteMatchId()).isEqualTo(ligamatchBEMapped.getNaechsteMatchId()).isEqualTo(NAECHSTE_MATCH_ID);
//        assertThat(ligamatchBE.getNaechsteNaechsteMatchId()).isEqualTo(ligamatchBEMapped.getNaechsteNaechsteMatchId()).isEqualTo(NAECHSTE_NAECHSTE_MATCH_ID);
//        assertThat(ligamatchBE.getStrafpunkteSatz1()).isEqualTo(ligamatchBEMapped.getStrafpunkteSatz1()).isEqualTo(
//                STRAFPUNKT_SATZ_1);
//        assertThat(ligamatchBE.getStrafpunkteSatz2()).isEqualTo(ligamatchBEMapped.getStrafpunkteSatz2()).isEqualTo(
//                STRAFPUNKT_SATZ_2);
//        assertThat(ligamatchBE.getStrafpunkteSatz3()).isEqualTo(ligamatchBEMapped.getStrafpunkteSatz3()).isEqualTo(
//                STRAFPUNKT_SATZ_3);
//        assertThat(ligamatchBE.getStrafpunkteSatz4()).isEqualTo(ligamatchBEMapped.getStrafpunkteSatz4()).isEqualTo(
//                STRAFPUNKT_SATZ_4);
//        assertThat(ligamatchBE.getStrafpunkteSatz5()).isEqualTo(ligamatchBEMapped.getStrafpunkteSatz5()).isEqualTo(
//                STRAFPUNKT_SATZ_5);
//        assertThat(ligamatchBE.getWettkampftypId()).isEqualTo(ligamatchBEMapped.getWettkampftypId()).isEqualTo(WETTKAMP_TYP_ID);
//        assertThat(ligamatchBE.getWettkampfTag()).isEqualTo(ligamatchBEMapped.getWettkampfTag()).isEqualTo(WETTKAMPF_TAG);
//        assertThat(ligamatchBE.getMannschaftName()).isEqualTo(ligamatchBEMapped.getMannschaftName()).isEqualTo(MANNSCHAFT_NAME);
//        assertThat(ligamatchBE.getRueckennummer()).isEqualTo(ligamatchBEMapped.getRueckennummer()).isEqualTo(RUECKENNUMMER);
//
//    }
}
