package de.bogenliga.application.business.wettkampf.impl.mapper;

import java.time.OffsetDateTime;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import org.junit.Test;

import static de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest.getWettkampfBE;
import static de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest.getWettkampfDO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author Daniel Schott daniel.schott@student.reutlingen-university.de
 */
public class WettkampfMapperTest {


    private static final long user_Id=13;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private final long version = 1234;

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
    public void toWettkampfBE (){

        final WettkampfDO wettkampfDO = getWettkampfDO();

        final WettkampfBE actual = WettkampfMapper.toWettkampfBE.apply(wettkampfDO);

        assertThat(actual.getId()).isEqualTo(wettkampf_Id);
        assertThat(actual.getWettkampfTypId()).isEqualTo(wettkampf_Wettkampftyp_Id);
    }

    @Test
    public void toWettkampfDO(){

        final WettkampfBE wettkampfBE = getWettkampfBE();
        final WettkampfDO actual = WettkampfMapper.toWettkampfDO.apply(wettkampfBE);

        assertThat(actual.getId()).isEqualTo(wettkampf_Id);
        assertThat(actual.getWettkampfTypId()).isEqualTo(wettkampf_Wettkampftyp_Id);
    }
}
