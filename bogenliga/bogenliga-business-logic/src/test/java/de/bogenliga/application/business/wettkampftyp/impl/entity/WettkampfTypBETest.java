package de.bogenliga.application.business.wettkampftyp.impl.entity;

import java.time.OffsetDateTime;
import org.junit.Test;
import static de.bogenliga.application.business.wettkampftyp.impl.business.WettkampfTypComponentImplTest.getWettkampfTypBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author Daniel Schott daniel.schott@student.reutlingen-university.de
 */
public class WettkampfTypBETest {

    private static final long WETTKAMPFTYP_ID = 1;
    private static final String WETTKAMPFTYP_NAME = "Liga Satzsystem";

    @Test
    public void assertToString() {
        final WettkampfTypBE underTest = getWettkampfTypBE();
        underTest.setwettkampftypID(WETTKAMPFTYP_ID);
        underTest.setwettkampftypname(WETTKAMPFTYP_NAME);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(WETTKAMPFTYP_ID))
                .contains(WETTKAMPFTYP_NAME);
    }

}
