package de.bogenliga.application.business.regionen.impl.mapper;

import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import org.junit.Test;

import static de.bogenliga.application.business.regionen.impl.business.RegionenComponentImplTest.getRegionenBE;
import static de.bogenliga.application.business.regionen.impl.business.RegionenComponentImplTest.getRegionenDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class RegionenMapperTest {

    private static final long ID=10;
    private static final String REGION_NAME="Test";

    @Test
    public void toVO() throws Exception {
        final RegionenBE regionenBE = getRegionenBE();

        final RegionenDO actual = RegionenMapper.toRegionDO.apply(regionenBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getRegionName()).isEqualTo(REGION_NAME);
    }


    @Test
    public void toBE() throws Exception {
        final RegionenDO regionenDO = getRegionenDO();

        final RegionenBE actual = RegionenMapper.toRegionBE.apply(regionenDO);

        assertThat(actual.getRegionId()).isEqualTo(ID);
        assertThat(actual.getRegionName()).isEqualTo(REGION_NAME);
    }

}