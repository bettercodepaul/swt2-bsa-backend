package de.bogenliga.application.business.dsbmannschaft.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBEext;
import static de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest.getDsbMannschaftBE;
import static de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest.getDsbMannschaftDO;
import static org.assertj.core.api.Assertions.assertThat;

public class DsbMannschaftMapperTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final long ID = 2222L;
    private static final long VEREINID=101010;
    private static final long NUMMER=111;
    private static final long BENUTZERID=12;
    private static final long VERANSTALTUNGID=1;


    @Test
    public void toVO() throws Exception {
        final DsbMannschaftBE dsbMannschaftBE =  getDsbMannschaftBE();

        final DsbMannschaftDO actual = DsbMannschaftMapper.toDsbMannschaftDO.apply(dsbMannschaftBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getVereinId()).isEqualTo(VEREINID);
    }

    @Test
    public void toBE() throws Exception {
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();

        final DsbMannschaftBE actual = DsbMannschaftMapper.toDsbMannschaftBE.apply(dsbMannschaftDO);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getVereinId()).isEqualTo(VEREINID);
    }
    @Test
    public void toDsbMannschaftVerUWettDO() {
        // Arrange
        DsbMannschaftBEext dsbMannschaftBEext = new DsbMannschaftBEext();
        dsbMannschaftBEext.setId(ID);
        dsbMannschaftBEext.setVereinId(VEREINID);
        dsbMannschaftBEext.setNummer(NUMMER);
        dsbMannschaftBEext.setBenutzerId(BENUTZERID);
        dsbMannschaftBEext.setVeranstaltungId(VERANSTALTUNGID);
        dsbMannschaftBEext.setVeranstaltungName("Olympia");

        dsbMannschaftBEext.setVeranstaltungName("Olympia");
        dsbMannschaftBEext.setWettkampfTag("2024-08-01");
        dsbMannschaftBEext.setWettkampfOrtsname("Berlin");
        dsbMannschaftBEext.setVereinName("Sportverein Berlin");
        // Act
        DsbMannschaftDO dsbMannschaftDO = DsbMannschaftMapper.toDsbMannschaftVerUWettDO.apply(dsbMannschaftBEext);

        // Assert
        assertThat(dsbMannschaftDO.getId()).isEqualTo(ID);
        assertThat(dsbMannschaftDO.getVereinId()).isEqualTo(VEREINID);
        assertThat(dsbMannschaftDO.getNummer()).isEqualTo(NUMMER);
        assertThat(dsbMannschaftDO.getBenutzerId()).isEqualTo(BENUTZERID);
        assertThat(dsbMannschaftDO.getName()).isEqualTo("Sportverein Berlin"+NUMMER);
        assertThat(dsbMannschaftDO.getVeranstaltung_name()).isEqualTo("Olympia");
        assertThat(dsbMannschaftDO.getWettkampfTag()).isEqualTo("2024-08-01");
        assertThat(dsbMannschaftDO.getWettkampf_ortsname()).isEqualTo("Berlin");
        assertThat(dsbMannschaftDO.getVerein_name()).isEqualTo("Sportverein Berlin");
        assertThat(dsbMannschaftDO.getNummer()).isEqualTo(NUMMER);
    }
}
