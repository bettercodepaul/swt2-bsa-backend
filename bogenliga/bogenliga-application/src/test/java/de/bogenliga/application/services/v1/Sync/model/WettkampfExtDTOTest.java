package de.bogenliga.application.services.v1.Sync.model;

import java.sql.Date;
import org.junit.Test;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.services.v1.sync.model.WettkampfExtDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Jonas Sigloch
 */
public class WettkampfExtDTOTest {
    private static final Long ID = 2010L;
    private static final Long VERANSTALTUNGSID = 1002L;
    private static final Date DATUM = Date.valueOf("2022-06-06");
    private static final String STRASSE = "Teststrasse";
    private static final String PLZ = "72762";
    private static final String ORTSNAME = "Reutlingen";
    private static final String ORTSINFO = "Turnhalle";
    private static final String BEGINN = "15:30";
    private static final Long TAG = 4L;
    private static final Long DISZIPLINID = 0L;
    private static final Long WETTKAMPFTYPID = 1L;
    private static final Long VERSION = 1L;
    private static final Long AUSRICHTER = 0L;
    private static final String OFFLINETOKEN = "offline";

    private WettkampfExtDTO getExtDTO() {
        return new WettkampfExtDTO(ID, VERANSTALTUNGSID, DATUM, STRASSE, PLZ, ORTSNAME, ORTSINFO, BEGINN, TAG, DISZIPLINID,
                WETTKAMPFTYPID, VERSION, AUSRICHTER, OFFLINETOKEN);
    }

    @Test
    public void assertToString() {
        final WettkampfExtDTO underTest = getExtDTO();
        underTest.setId(ID);
        underTest.setWettkampfTypId(WETTKAMPFTYPID);
        underTest.setOfflineToken(OFFLINETOKEN);
        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(Long.toString(WETTKAMPFTYPID))
                .contains(OFFLINETOKEN);
    }
}