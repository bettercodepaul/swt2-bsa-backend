package de.bogenliga.application.business.liga.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import static de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest.getLigaBE;
import static de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest.getLigaDO;
import static de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest.getUserBE;
import static de.bogenliga.application.business.regionen.impl.business.RegionenComponentImplTest.getRegionenBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * I'm testing the LigaMapper Class
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class LigaMapperTest {

    private static final Long LIGAID = 1337L;
    private static final String LIGANAME = "Test Liga";
    private static final Long LIGAREGIONID = 10L;
    private static final Long LIGAUEBERGEORDNETID = 1337L;
    private static final Long LIGAVERANTWORTLICH = 1L;


    @Test
    public void toDO() throws Exception {
        final LigaBE ligaBE = getLigaBE();
        final LigaBE ligaUebergeordnetBE = ligaBE;
        final RegionenBE regionBE = getRegionenBE();
        final UserBE userBE = getUserBE();

        final LigaDO actual = LigaMapper.toLigaDO(ligaBE, ligaUebergeordnetBE, regionBE, userBE);

        assertThat(actual.getId()).isEqualTo(LIGAID);
        assertThat(actual.getName()).isEqualTo(LIGANAME);

        LigaDO ligaDO = new LigaDO(LIGAID, LIGANAME, LIGAREGIONID, regionBE.getRegionName(), LIGAUEBERGEORDNETID,
                ligaUebergeordnetBE.getLigaName(), LIGAVERANTWORTLICH, userBE.getUserEmail());
        assertThat(actual.equals(ligaDO));
    }


    @Test
    public void toBE() throws Exception {
        final LigaDO ligaDO = getLigaDO();

        final LigaBE actual = LigaMapper.toLigaBE.apply(ligaDO);

        assertThat(actual.getLigaId()).isEqualTo(LIGAID);
        assertThat(actual.getLigaName()).isEqualTo(LIGANAME);
    }
}