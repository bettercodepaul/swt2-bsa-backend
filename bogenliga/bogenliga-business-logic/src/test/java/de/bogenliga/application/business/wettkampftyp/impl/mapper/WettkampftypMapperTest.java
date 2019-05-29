package de.bogenliga.application.business.wettkampftyp.impl.mapper;

import java.time.OffsetDateTime;
import org.junit.Test;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampftypDO;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampftypBE;
import de.bogenliga.application.business.wettkampftyp.impl.mapper.WettkampftypMapper;
import static de.bogenliga.application.business.wettkampftyp.impl.business.WettkampftypComponentImplTest.getWettkampftypBE;
import static de.bogenliga.application.business.wettkampftyp.impl.business.WettkampftypComponentImplTest.getWettkampftypDO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author Daniel Schott daniel.schott@student.reutlingen-university.de
 */
public class WettkampftypMapperTest {


    private static final long user_Id=13;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private final long version = 1234;

    private static final long wettkampftyp_Id = 1;
    private static final String wettkampftyp_Name = "Liga Satzsystem";




    @Test
    public void toWettkampftypBE (){

        final WettkampftypDO wettkampftypDO = getWettkampftypDO();

        final WettkampftypBE actual = WettkampftypMapper.toWettkampftypBE.apply(wettkampftypDO);

        assertThat(actual.getwettkampftypID()).isEqualTo(wettkampftyp_Id);
        assertThat(actual.getwettkampftypname()).isEqualTo(wettkampftyp_Name);
    }

    @Test
    public void toWettkampftypDO(){

        final WettkampftypBE wettkampftypBE = getWettkampftypBE();
        final WettkampftypDO actual = WettkampftypMapper.toWettkampftypDO.apply(wettkampftypBE);

        assertThat(actual.getId()).isEqualTo(wettkampftyp_Id);
        assertThat(actual.getName()).isEqualTo(wettkampftyp_Name);
    }
}
