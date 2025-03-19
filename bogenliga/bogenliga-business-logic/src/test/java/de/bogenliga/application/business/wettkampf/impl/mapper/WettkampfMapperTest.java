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

    private static final long WETTKAMPF_ID = 322;
    private static final long WETTKAMPF_WETTKAMPFTYP_ID = 1;
    private static final String WETTKAMPF_OFFLINETOKEN = "offlineToken";

    @Test
    public void toWettkampfBE (){

        final WettkampfDO wettkampfDO = getWettkampfDO();

        final WettkampfBE actual = WettkampfMapper.toWettkampfBE.apply(wettkampfDO);

        assertThat(actual.getId()).isEqualTo(WETTKAMPF_ID);
        assertThat(actual.getWettkampfTypId()).isEqualTo(WETTKAMPF_WETTKAMPFTYP_ID);
        assertThat(actual.getOfflineToken()).isEqualTo(WETTKAMPF_OFFLINETOKEN);
    }

    @Test
    public void toWettkampfDO(){

        final WettkampfBE wettkampfBE = getWettkampfBE();
        final WettkampfDO actual = WettkampfMapper.toWettkampfDO.apply(wettkampfBE);

        assertThat(actual.getId()).isEqualTo(WETTKAMPF_ID);
        assertThat(actual.getWettkampfTypId()).isEqualTo(WETTKAMPF_WETTKAMPFTYP_ID);
        assertThat(actual.getOfflineToken()).isEqualTo(WETTKAMPF_OFFLINETOKEN);
    }
}
