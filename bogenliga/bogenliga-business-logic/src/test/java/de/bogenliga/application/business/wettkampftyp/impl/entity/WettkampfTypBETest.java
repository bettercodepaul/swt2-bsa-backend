package de.bogenliga.application.business.wettkampftyp.impl.entity;

import java.time.OffsetDateTime;
import org.junit.Test;
import static de.bogenliga.application.business.wettkampftyp.impl.business.WettkampfTypComponentImplTest.getWettkampfTypBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author Daniel Schott daniel.schott@student.reutlingen-university.de
 */
public class WettkampfTypBETest {

    private static final long user_Id=13;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private static final long version = 1234;

    private static final long wettkampftyp_Id = 1;
    private static final String wettkampftyp_Name = "Liga Satzsystem";



    @Test
    public void assertToString() {
        final WettkampfTypBE underTest = getWettkampfTypBE();
        underTest.setwettkampftypID(wettkampftyp_Id);
        underTest.setwettkampftypname(wettkampftyp_Name);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(wettkampftyp_Id))
                .contains(wettkampftyp_Name);
    }

}
