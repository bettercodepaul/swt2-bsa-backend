package de.bogenliga.application.business.wettkampf.impl.entity;

import org.junit.Test;

import java.time.OffsetDateTime;

import static de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest.getWettkampfBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author Daniel Schott daniel.schott@student.reutlingen-university.de
 */
public class WettkampfBETest {

    private static final long user_Id=13;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private static final long version = 1234;

    private static final long wettkampf_Id = 322;
    private static final long wettkampf_Veranstaltung_Id = 0;
    private static final String wettkampf_Datum = "2019-05-21";
    private static final String wettkampf_Strasse ="Reutlingerstr. 7";
    private static final String wettkampf_Plz ="72810";
    private static final String wettkampf_Ortsname =" Gomaringen";
    private static final String wettkampf_Ortsinfo ="Sporthalle";
    private static final String wettkampf_Beginn ="8:00";
    private static final long wettkampf_Tag = 8;
    private static final long wettkampf_Disziplin_Id = 0;
    private static final long wettkampf_Wettkampftyp_Id = 1;



    @Test
    public void assertToString() {
        final WettkampfBE underTest = getWettkampfBE();
        underTest.setId(wettkampf_Id);
        underTest.setWettkampfTypId(wettkampf_Wettkampftyp_Id);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(wettkampf_Id))
                .contains(Long.toString(wettkampf_Wettkampftyp_Id));
    }

}
