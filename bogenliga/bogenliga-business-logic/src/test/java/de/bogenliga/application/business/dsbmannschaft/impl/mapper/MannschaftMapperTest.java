package de.bogenliga.application.business.dsbmannschaft.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.MannschaftBE;
import static de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest.getDsbMannschaftBE;
import static de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest.getDsbMannschaftDO;
import static org.assertj.core.api.Assertions.assertThat;

public class MannschaftMapperTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final long ID = 2222L;
    private static final long VEREINID=101010;
    private static final long NUMMER=111;
    private static final long BENUTZERID=12;
    private static final long VERANSTALTUNGID=1;


    @Test
    public void toVO() throws Exception {
        final MannschaftBE mannschaftBE =  getDsbMannschaftBE();

        final DsbMannschaftDO actual = MannschaftMapper.toDsbMannschaftDO.apply(mannschaftBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getVereinId()).isEqualTo(VEREINID);
    }

    @Test
    public void toBE() throws Exception {
        final DsbMannschaftDO dsbMannschaftDO = getDsbMannschaftDO();

        final MannschaftBE actual = MannschaftMapper.toDsbMannschaftBE.apply(dsbMannschaftDO);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getVereinId()).isEqualTo(VEREINID);
    }

}
