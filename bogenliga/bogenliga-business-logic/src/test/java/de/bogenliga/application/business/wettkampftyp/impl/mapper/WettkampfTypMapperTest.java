package de.bogenliga.application.business.wettkampftyp.impl.mapper;

import java.time.OffsetDateTime;
import org.junit.Test;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampfTypBE;
import static de.bogenliga.application.business.wettkampftyp.impl.business.WettkampfTypComponentImplTest.getWettkampfTypBE;
import static de.bogenliga.application.business.wettkampftyp.impl.business.WettkampfTypComponentImplTest.getWettkampfTypDO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author Daniel Schott daniel.schott@student.reutlingen-university.de
 */
public class WettkampfTypMapperTest {

    private static final long WETTKAMPFTYP_ID = 1;
    private static final String WETTKAMPFTYP_NAME = "Liga Satzsystem";

    @Test
    public void toWettkampfTypBE (){

        final WettkampfTypDO wettkampftypDO = getWettkampfTypDO();

        final WettkampfTypBE actual = WettkampfTypMapper.toWettkampfTypBE.apply(wettkampftypDO);

        assertThat(actual.getwettkampftypID()).isEqualTo(WETTKAMPFTYP_ID);
        assertThat(actual.getwettkampftypname()).isEqualTo(WETTKAMPFTYP_NAME);
    }

    @Test
    public void toWettkampfTypDO(){

        final WettkampfTypBE wettkampftypBE = getWettkampfTypBE();
        final WettkampfTypDO actual = WettkampfTypMapper.toWettkampfTypDO.apply(wettkampftypBE);

        assertThat(actual.getId()).isEqualTo(WETTKAMPFTYP_ID);
        assertThat(actual.getName()).isEqualTo(WETTKAMPFTYP_NAME);
    }
}
