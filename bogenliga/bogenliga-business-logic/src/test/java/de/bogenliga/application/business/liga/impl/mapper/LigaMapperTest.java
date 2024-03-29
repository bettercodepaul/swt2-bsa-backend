package de.bogenliga.application.business.liga.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.disziplin.impl.entity.DisziplinBE;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.user.api.types.UserDO;
import static de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest.getLigaBE;
import static de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest.getLigaDO;
import static de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest.getUserDO;
import static de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest.getDisziplinDO;
import static de.bogenliga.application.business.regionen.impl.business.RegionenComponentImplTest.getRegionenDO;
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
        final DisziplinDO disziplinDO = getDisziplinDO();
        final RegionenDO regionDO = getRegionenDO();
        final UserDO userDO = getUserDO();


        final LigaDO actual = LigaMapper.toLigaDO(ligaBE, ligaUebergeordnetBE, regionDO, userDO,disziplinDO);

        assertThat(actual.getId()).isEqualTo(ligaBE.getLigaId());
        assertThat(actual.getName()).isEqualTo(ligaBE.getLigaName());

        LigaDO ligaDO = new LigaDO(ligaBE.getLigaId(), ligaBE.getLigaName(), regionDO.getId(), regionDO.getRegionName(), ligaUebergeordnetBE.getLigaId(),
                ligaUebergeordnetBE.getLigaName(), userDO.getId(), userDO.getEmail(), disziplinDO.getDisziplinId(),ligaBE.getLigaDetail(),
                ligaBE.getLigaFileBase64(),ligaBE.getLigaFileName(),ligaBE.getLigaFileType());

        assertThat(actual.hashCode()).isEqualTo(ligaDO.hashCode());
    }


    @Test
    public void toBE() throws Exception {
        final LigaDO ligaDO = getLigaDO();

        final LigaBE actual = LigaMapper.toLigaBE.apply(ligaDO);

        assertThat(actual.getLigaId()).isEqualTo(LIGAID);
        assertThat(actual.getLigaName()).isEqualTo(LIGANAME);
    }
}