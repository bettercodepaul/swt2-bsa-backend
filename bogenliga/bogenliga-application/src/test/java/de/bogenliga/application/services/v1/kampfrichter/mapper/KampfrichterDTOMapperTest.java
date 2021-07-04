package de.bogenliga.application.services.v1.kampfrichter.mapper;

import org.junit.Test;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterExtendedDTO;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * Test for KampfrichterDTOMapper
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class KampfrichterDTOMapperTest{

    private static final Long USERID = 1337L;
    private static final long WETTKAMPFID = 9999;
    private static final boolean LEITEND = true;

    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final String EMAIL = "Sorscha.Kratikoff@test.de";

    private KampfrichterExtendedDTO getKampfrichterDTO(){
        KampfrichterExtendedDTO kampfrichterExtendedDTO = new KampfrichterExtendedDTO(USERID,VORNAME,NACHNAME,EMAIL,WETTKAMPFID,LEITEND);
        return kampfrichterExtendedDTO;
    }
    private KampfrichterDO getKampfrichterDO(){
        KampfrichterDO kampfrichterDO = new KampfrichterDO(USERID,VORNAME,NACHNAME,EMAIL,WETTKAMPFID,LEITEND);
        return kampfrichterDO;
    }

    @Test
    public void toDOExtendedTest(){
        KampfrichterExtendedDTO kampfrichterDTO = getKampfrichterDTO();
        final KampfrichterDO actual = KampfrichterDTOMapper.toDOExtended.apply(kampfrichterDTO);
        assertThat(actual.getUserId()).isEqualTo(USERID);
        assertThat(actual.getKampfrichterEmail()).isEqualTo(EMAIL);
        assertThat(actual.getVorname()).isEqualTo(VORNAME);
        assertThat(actual.getNachname()).isEqualTo(NACHNAME);
        assertThat(actual.getWettkampfId()).isEqualTo(WETTKAMPFID);
        assertThat(actual.isLeitend()).isEqualTo(LEITEND);
    }
    @Test
    public void toDTOExtendedTest(){
        KampfrichterDO kampfrichterDO = getKampfrichterDO();
        final KampfrichterExtendedDTO actual = KampfrichterDTOMapper.toDTOExtended.apply(kampfrichterDO);
        assertThat(actual.getUserID()).isEqualTo(USERID);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
        assertThat(actual.getKampfrichterVorname()).isEqualTo(VORNAME);
        assertThat(actual.getKampfrichterNachname()).isEqualTo(NACHNAME);
        assertThat(actual.getWettkampfID()).isEqualTo(WETTKAMPFID);
        assertThat(actual.getLeitend()).isEqualTo(LEITEND);
    }


}