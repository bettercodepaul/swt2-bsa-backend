package de.bogenliga.application.business.ligapasse.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.ligamatch.impl.BaseLigamatchTest;
import de.bogenliga.application.business.ligapasse.impl.BaseLigapasseTest;
import de.bogenliga.application.business.ligapasse.impl.dao.LigapasseDAO;
import de.bogenliga.application.business.ligapasse.impl.entity.LigapasseBE;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class LigapasseMapperTest extends BaseLigapasseTest {

    private void validateDO(LigapasseBE ligapasseBE, PasseDO passeDOMapped){
        assertThat(ligapasseBE).isNotNull();
        assertThat(ligapasseBE.getWettkampfId()).isEqualTo(passeDOMapped.getPasseWettkampfId()).isEqualTo(LIGAPASSE_WETTKAMPF_ID);
        assertThat(ligapasseBE.getMatchId()).isEqualTo(passeDOMapped.getPasseMatchId()).isEqualTo(LIGAPASSE_MATCH_ID);
        assertThat(ligapasseBE.getPasseId()).isEqualTo(passeDOMapped.getId()).isEqualTo(LIGAPASSE_PASSE_ID);
        assertThat(ligapasseBE.getPasseLfdnr()).isEqualTo(passeDOMapped.getPasseLfdnr()).isEqualTo(LIGAPASSE_LFDR_NR);
        assertThat(ligapasseBE.getPasseMannschaftId()).isEqualTo(passeDOMapped.getPasseMannschaftId()).isEqualTo(LIGAPASSE_MANNSCHAFT_ID);
        assertThat(ligapasseBE.getDsbMitgliedId()).isEqualTo(passeDOMapped.getPasseDsbMitgliedId()).isEqualTo(LIGAPASSE_DSB_MITGLIED_ID);
        assertThat(ligapasseBE.getPasseRingzahlPfeil1()).isEqualTo(passeDOMapped.getPfeil1()).isEqualTo(LIGAPASSE_PFEIL_1);
        assertThat(ligapasseBE.getPasseRingzahlPfeil2()).isEqualTo(passeDOMapped.getPfeil2()).isEqualTo(LIGAPASSE_PFEIL_2);
        assertThat(passeDOMapped.getPfeil3()).isEqualTo(null);
        assertThat(passeDOMapped.getPfeil4()).isEqualTo(null);
        assertThat(passeDOMapped.getPfeil5()).isEqualTo(null);

    }

    @Test
    public void testMapping(){
        LigapasseBE ligapasseBE = getLigapasseBE();
        PasseDO passeDO = LigapasseToPasseMapper.ligapasseToPasseDO.apply(ligapasseBE);
        this.validateDO(ligapasseBE, passeDO);
    }


}
