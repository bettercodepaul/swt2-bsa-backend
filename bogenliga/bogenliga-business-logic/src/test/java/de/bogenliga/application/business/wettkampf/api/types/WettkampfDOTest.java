package de.bogenliga.application.business.wettkampf.api.types;

import java.sql.Date;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Jonas Sigloch
 */
public class WettkampfDOTest {
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

    private WettkampfDO getTokenDO() {
        return new WettkampfDO(ID, VERANSTALTUNGSID, DATUM, STRASSE, PLZ, ORTSNAME, ORTSINFO, BEGINN, TAG, DISZIPLINID,
                WETTKAMPFTYPID, VERSION, AUSRICHTER, OFFLINETOKEN);
    }

    private WettkampfDO getNoTokenDO() {
        return new WettkampfDO(ID, VERANSTALTUNGSID, DATUM, STRASSE, PLZ, ORTSNAME, ORTSINFO, BEGINN, TAG, DISZIPLINID,
                WETTKAMPFTYPID, VERSION, AUSRICHTER);
    }

    @Test
    public void noTokenDO() {
        final WettkampfDO underTest = getNoTokenDO();
        assertThat(underTest.getOfflineToken()).isNull();
        assertThat(underTest.getId()).isEqualTo(ID);
    }

    @Test
    public void tokenDO() {
        final WettkampfDO underTest = getTokenDO();
        assertThat(underTest.getOfflineToken()).isEqualTo(OFFLINETOKEN);
        assertThat(underTest.getId()).isEqualTo(ID);
    }

    @Test
    public void assertToString() {
        final WettkampfDO underTest = getTokenDO();
        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(Long.toString(WETTKAMPFTYPID))
                .contains(OFFLINETOKEN);
    }
}