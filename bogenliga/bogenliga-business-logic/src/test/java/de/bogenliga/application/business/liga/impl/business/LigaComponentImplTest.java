package de.bogenliga.application.business.liga.impl.business;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.dao.LigaDAO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.regionen.impl.dao.RegionenDAO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import static de.bogenliga.application.business.regionen.impl.business.RegionenComponentImplTest.getRegionenBE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * I'm testing the LigaComponentImpl Class
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class LigaComponentImplTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long LIGAID = 1337L;
    private static final String LIGANAME = "Test Liga";
    private static final Long LIGAREGIONID = 10L;
    private static final Long LIGAUEBERGEORDNETID = 1337L;
    private static final Long LIGAVERANTWORTLICH = 1L;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LigaDAO ligaDao;

    @Mock
    private RegionenDAO regionenDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private LigaComponentImpl underTest;

    @Captor
    private ArgumentCaptor<LigaBE> ligaBEArgumentCaptor;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */

    public static LigaBE getLigaBE() {
        final LigaBE expectedLigaBE = new LigaBE();
        expectedLigaBE.setLigaId(LIGAID);
        expectedLigaBE.setLigaName(LIGANAME);
        expectedLigaBE.setLigaRegionId(LIGAREGIONID);
        expectedLigaBE.setLigaUebergeordnetId(LIGAUEBERGEORDNETID);
        expectedLigaBE.setLigaVerantwortlichId(LIGAVERANTWORTLICH);

        return expectedLigaBE;
    }


    public static LigaDO getLigaDO() {
        final LigaDO expectedLigaDO = new LigaDO();
        expectedLigaDO.setId(LIGAID);
        expectedLigaDO.setName(LIGANAME);
        expectedLigaDO.setRegionId(LIGAREGIONID);
        expectedLigaDO.setLigaUebergeordnetId(LIGAUEBERGEORDNETID);
        expectedLigaDO.setLigaVerantwortlichId(LIGAVERANTWORTLICH);

        return expectedLigaDO;
    }


    public static UserBE getUserBE() {
        final UserBE expectedUserBE = new UserBE();
        expectedUserBE.setUserEmail("test@mail.de");
        expectedUserBE.setUserId(1L);

        return expectedUserBE;
    }


    @Test
    public void findAll_whenEverythingIsSet() {
        // prepare test data
        final LigaBE expectedLigaBE = getLigaBE();
        final LigaBE expectedLigaUebergeordnetBE = expectedLigaBE;
        final RegionenBE expectedRegionBE = getRegionenBE();
        final UserBE expectedUserBE = getUserBE();
        final List<LigaBE> expectedBEList = Collections.singletonList(expectedLigaBE);

        // configure mocks
        when(ligaDao.findAll()).thenReturn(expectedBEList);
        when(ligaDao.findById(anyLong())).thenReturn(expectedLigaBE);
        when(regionenDAO.findById(anyLong())).thenReturn(expectedRegionBE);
        when(userDAO.findById(anyLong())).thenReturn(expectedUserBE);


        // call test method
        final List<LigaDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.get(0).getName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actual.get(0).getRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(actual.get(0).getRegionName()).isEqualTo(expectedRegionBE.getRegionName());
        assertThat(actual.get(0).getLigaUebergeordnetId()).isEqualTo(expectedLigaUebergeordnetBE.getLigaId());
        assertThat(actual.get(0).getLigaUebergeordnetName()).isEqualTo(expectedLigaUebergeordnetBE.getLigaName());
        assertThat(actual.get(0).getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
        assertThat(actual.get(0).getLigaVerantwortlichMail()).isEqualTo(expectedUserBE.getUserEmail());

        // verify invocations
        verify(ligaDao).findAll();
        verify(ligaDao).findById(expectedLigaBE.getLigaUebergeordnetId());
        verify(regionenDAO).findById(expectedLigaBE.getLigaRegionId());
        verify(userDAO).findById(expectedLigaBE.getLigaVerantwortlichId());
    }


    @Test
    public void findAll_whenAttributesNull() {
        // prepare test data
        final LigaBE expectedLigaBE = getLigaBE();
        final List<LigaBE> expectedBEList = Collections.singletonList(expectedLigaBE);

        expectedLigaBE.setLigaUebergeordnetId(null);
        expectedLigaBE.setLigaRegionId(null);
        expectedLigaBE.setLigaVerantwortlichId(null);

        // configure mocks
        when(ligaDao.findAll()).thenReturn(expectedBEList);


        // call test method
        final List<LigaDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.get(0).getName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actual.get(0).getRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(actual.get(0).getRegionName()).isEqualTo(null);
        assertThat(actual.get(0).getLigaUebergeordnetId()).isEqualTo(expectedLigaBE.getLigaUebergeordnetId());
        assertThat(actual.get(0).getLigaUebergeordnetName()).isEqualTo(null);
        assertThat(actual.get(0).getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
        assertThat(actual.get(0).getLigaVerantwortlichMail()).isEqualTo(null);

        // verify invocations
        verify(ligaDao).findAll();
    }


    @Test
    public void findById() {
        // prepare test data
        final LigaBE expectedLigaBE = getLigaBE();
        final RegionenBE expectedRegionBE = getRegionenBE();
        final UserBE expectedUserBE = getUserBE();

        // configure mocks
        when(ligaDao.findById(anyLong())).thenReturn(expectedLigaBE);
        when(regionenDAO.findById(anyLong())).thenReturn(expectedRegionBE);
        when(userDAO.findById(anyLong())).thenReturn(expectedUserBE);

        // call test method
        final LigaDO actual = underTest.findById(LIGAID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(actual.getRegionName()).isEqualTo(expectedRegionBE.getRegionName());
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(expectedUserBE.getUserEmail());

        // verify invocation
        verify(regionenDAO).findById(expectedLigaBE.getLigaRegionId());
        verify(userDAO).findById(expectedLigaBE.getLigaVerantwortlichId());
    }


    @Test
    public void findById_whenAttributesNull() {
        // prepare test data
        final LigaBE expectedLigaBE = getLigaBE();

        expectedLigaBE.setLigaUebergeordnetId(null);
        expectedLigaBE.setLigaRegionId(null);
        expectedLigaBE.setLigaVerantwortlichId(null);


        // configure mocks
        when(ligaDao.findById(anyLong())).thenReturn(expectedLigaBE);


        // call test method
        final LigaDO actual = underTest.findById(LIGAID);


        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actual.getRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(actual.getRegionName()).isEqualTo(null);
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(expectedLigaBE.getLigaUebergeordnetId());
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(null);
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(null);

        // verify invocation
        verify(ligaDao).findById(LIGAID);
    }


    @Test
    public void create() {
        // prepare test data
        final LigaDO input = getLigaDO();
        final LigaBE expectedLigaBE = getLigaBE();
        final RegionenBE expectedRegionBE = getRegionenBE();
        final UserBE expectedUserBE = getUserBE();

        // connfigure mocks
        when(ligaDao.create(any(LigaBE.class), anyLong())).thenReturn(expectedLigaBE);
        when(ligaDao.findById(anyLong())).thenReturn(expectedLigaBE);
        when(regionenDAO.findById(anyLong())).thenReturn(expectedRegionBE);
        when(userDAO.findById(anyLong())).thenReturn(expectedUserBE);


        // call test method
        final LigaDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(ligaDao).create(ligaBEArgumentCaptor.capture(), anyLong());
        final LigaBE persistedLigaBE = ligaBEArgumentCaptor.getValue();

        assertThat(persistedLigaBE).isNotNull();

        assertThat(persistedLigaBE.getLigaId()).isEqualTo(input.getId());
        assertThat(persistedLigaBE.getLigaRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(persistedLigaBE.getLigaUebergeordnetId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(persistedLigaBE.getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());

        // test mapping of do
        assertThat(actual.getRegionName()).isEqualTo(expectedRegionBE.getRegionName());
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(expectedUserBE.getUserEmail());
    }

    @Test
    public void create_whenArgumentsNull() {
        // prepare test data
        final LigaDO input = getLigaDO();
        final LigaBE expectedLigaBE = getLigaBE();

        expectedLigaBE.setLigaUebergeordnetId(null);
        expectedLigaBE.setLigaRegionId(null);
        expectedLigaBE.setLigaVerantwortlichId(null);

        // connfigure mocks
        when(ligaDao.create(any(LigaBE.class), anyLong())).thenReturn(expectedLigaBE);

        // call test method
        final LigaDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(ligaDao).create(ligaBEArgumentCaptor.capture(), anyLong());
        final LigaBE persistedLigaBE = ligaBEArgumentCaptor.getValue();

        assertThat(persistedLigaBE).isNotNull();

        assertThat(persistedLigaBE.getLigaId()).isEqualTo(input.getId());
        assertThat(persistedLigaBE.getLigaRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(persistedLigaBE.getLigaUebergeordnetId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(persistedLigaBE.getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
    }


}