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
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.disziplin.impl.business.DisziplinComponentImpl;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.dao.LigaDAO;
import de.bogenliga.application.business.liga.impl.dao.LigaExtendedDAO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.liga.impl.entity.LigaExtendedBE;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.regionen.impl.business.RegionenComponentImpl;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.impl.business.UserComponentImpl;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static de.bogenliga.application.business.regionen.impl.business.RegionenComponentImplTest.getRegionenDO;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * I'm testing the LigaComponentImpl Class
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class LigaComponentImplTest {

    private static final Long USER = 0L;

    private static final Long LIGAID = 1337L;
    private static final String LIGANAME = "Test Liga";
    private static final Long LIGAREGIONID = 10L;
    private static final Long LIGAUEBERGEORDNETID = 1337L;
    private static final Long LIGAVERANTWORTLICH = 1L;

    private static final Long DISZIPLINID = 0L;

    private static final String LIGA_DETAIL = "Dies ist ein wichtiges Detail zur TestligaXY: Manchmal zielen wir mit Absicht daneben!";
    private static final String LIGA_FILE = "data:application/pdf;base64,JVBERi0xLjYNJeL....";
    private static final String LIGA_FILE_NAME = "TestFile.pdf";
    private static final String LIGA_FILE_TYPE = "application/pdf";



    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LigaDAO ligaDao;

    @Mock
    private RegionenComponentImpl regionenComponentImpl;

    @Mock
    private UserComponentImpl userComponentImpl;

    @Mock
    private DisziplinComponentImpl disziplinComponentImpl;
    @Mock
    private LigaExtendedDAO ligaExtendedDAO;



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
        expectedLigaBE.setLigaDisziplinId(DISZIPLINID);
        expectedLigaBE.setLigaDetail(LIGA_DETAIL);
        expectedLigaBE.setLigaFileBase64(LIGA_FILE);
        expectedLigaBE.setLigaFileName(LIGA_FILE_NAME);
        expectedLigaBE.setLigaFileType(LIGA_FILE_TYPE);

        return expectedLigaBE;
    }


    public static LigaDO getLigaDO() {
        final LigaDO expectedLigaDO = new LigaDO();
        expectedLigaDO.setId(LIGAID);
        expectedLigaDO.setName(LIGANAME);
        expectedLigaDO.setRegionId(LIGAREGIONID);
        expectedLigaDO.setLigaUebergeordnetId(LIGAUEBERGEORDNETID);
        expectedLigaDO.setLigaVerantwortlichId(LIGAVERANTWORTLICH);
        expectedLigaDO.setDisziplinId(DISZIPLINID);
        expectedLigaDO.setLigaDetail(LIGA_DETAIL);
        expectedLigaDO.setLigaDoFileBase64(LIGA_FILE);
        expectedLigaDO.setLigaDoFileName(LIGA_FILE_NAME);
        expectedLigaDO.setLigaDoFileType(LIGA_FILE_TYPE);

        return expectedLigaDO;
    }


    public static UserDO getUserDO(){
        final UserDO expectedUserDO = new UserDO();
        expectedUserDO.setEmail("test@mail.de");
        expectedUserDO.setId(1L);

        return expectedUserDO;
    }

    public static DisziplinDO getDisziplinDO() {
        final DisziplinDO expectedDisziplinDO = new DisziplinDO();
        expectedDisziplinDO.setDisziplinName("Recurve");
        expectedDisziplinDO.setDisziplinId(DISZIPLINID);

        return expectedDisziplinDO;
    }

    @Test
    public void findBySearch_whenEverythingIsSet() {
        // prepare test data
        final LigaExtendedBE expectedLigaExtendedBE = new LigaExtendedBE();
        expectedLigaExtendedBE.setLigaId(1337L);
        expectedLigaExtendedBE.setLigaName("Test Liga");
        expectedLigaExtendedBE.setLigaRegionId(42L);
        expectedLigaExtendedBE.setLigaUebergeordnetId(100L);
        expectedLigaExtendedBE.setLigaVerantwortlichId(200L);
        expectedLigaExtendedBE.setLigaDetail("Details");
        expectedLigaExtendedBE.setLigaFileBase64("base64EncodedString");
        expectedLigaExtendedBE.setLigaFileName("fileName.pdf");
        expectedLigaExtendedBE.setLigaFileType("application/pdf");
        expectedLigaExtendedBE.setDisziplinName("Recurve");
        expectedLigaExtendedBE.setRegionName("Bezirksliga");
        expectedLigaExtendedBE.setUebergeordneteLigaName("Uebergeordnete Liga");
        expectedLigaExtendedBE.setVerantwortlicherName("Verantwortlich Mail");

        final List<LigaExtendedBE> expectedBEList = Collections.singletonList(expectedLigaExtendedBE);

        // configure mocks
        when(ligaExtendedDAO.findBySearch(expectedLigaExtendedBE.getLigaName())).thenReturn(expectedBEList);

        // call test method
        final List<LigaDO> actual = underTest.findBySearch(expectedLigaExtendedBE.getLigaName());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        LigaDO actualLigaDO = actual.get(0);
        assertThat(actualLigaDO.getId()).isEqualTo(expectedLigaExtendedBE.getLigaId());
        assertThat(actualLigaDO.getName()).isEqualTo(expectedLigaExtendedBE.getLigaName());
        assertThat(actualLigaDO.getRegionId()).isEqualTo(expectedLigaExtendedBE.getLigaRegionId());
        assertThat(actualLigaDO.getLigaUebergeordnetId()).isEqualTo(expectedLigaExtendedBE.getLigaUebergeordnetId());
        assertThat(actualLigaDO.getLigaVerantwortlichId()).isEqualTo(expectedLigaExtendedBE.getLigaVerantwortlichId());
        assertThat(actualLigaDO.getLigaDetail()).isEqualTo(expectedLigaExtendedBE.getLigaDetail());
        assertThat(actualLigaDO.getLigaDoFileBase64()).isEqualTo(expectedLigaExtendedBE.getLigaFileBase64());
        assertThat(actualLigaDO.getLigaDoFileName()).isEqualTo(expectedLigaExtendedBE.getLigaFileName());
        assertThat(actualLigaDO.getLigaDoFileType()).isEqualTo(expectedLigaExtendedBE.getLigaFileType());
        assertThat(actualLigaDO.getLigaUebergeordnetName()).isEqualTo(expectedLigaExtendedBE.getUebergeordneteLigaName());
        assertThat(actualLigaDO.getLigaVerantwortlichMail()).isEqualTo(expectedLigaExtendedBE.getVerantwortlicherName());
        assertThat(actualLigaDO.getRegionName()).isEqualTo(expectedLigaExtendedBE.getRegionName());

        // verify invocations
        verify(ligaExtendedDAO).findBySearch(expectedLigaExtendedBE.getLigaName());
    }

    @Test
    public void findAll_whenEverythingIsSet() {
        // prepare test data
        final LigaExtendedBE expectedLigaExtendedBE = new LigaExtendedBE();
        expectedLigaExtendedBE.setLigaId(1337L);
        expectedLigaExtendedBE.setLigaName("Test Liga");
        expectedLigaExtendedBE.setLigaRegionId(42L);
        expectedLigaExtendedBE.setLigaUebergeordnetId(100L);
        expectedLigaExtendedBE.setLigaVerantwortlichId(200L);
        expectedLigaExtendedBE.setLigaDetail("Details");
        expectedLigaExtendedBE.setLigaFileBase64("base64EncodedString");
        expectedLigaExtendedBE.setLigaFileName("fileName.pdf");
        expectedLigaExtendedBE.setLigaFileType("application/pdf");
        expectedLigaExtendedBE.setDisziplinName("Recurve");
        expectedLigaExtendedBE.setRegionName("Bezirksliga");
        expectedLigaExtendedBE.setUebergeordneteLigaName("Uebergeordnete Liga");
        expectedLigaExtendedBE.setVerantwortlicherName("Verantwortlich Mail");

        final List<LigaExtendedBE> expectedBEList = Collections.singletonList(expectedLigaExtendedBE);

        // configure mocks
        when(ligaExtendedDAO.findEverything()).thenReturn(expectedBEList);

        // call test method
        final List<LigaDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);


        assertThat(actual.get(0).getId()).isEqualTo(expectedLigaExtendedBE.getLigaId());
        assertThat(actual.get(0).getName()).isEqualTo(expectedLigaExtendedBE.getLigaName());
        assertThat(actual.get(0).getRegionId()).isEqualTo(expectedLigaExtendedBE.getLigaRegionId());
        assertThat(actual.get(0).getRegionName()).isEqualTo(expectedLigaExtendedBE.getRegionName());
        assertThat(actual.get(0).getLigaUebergeordnetId()).isEqualTo(expectedLigaExtendedBE.getLigaUebergeordnetId());
        assertThat(actual.get(0).getLigaUebergeordnetName()).isEqualTo(expectedLigaExtendedBE.getUebergeordneteLigaName());
        assertThat(actual.get(0).getLigaVerantwortlichId()).isEqualTo(expectedLigaExtendedBE.getLigaVerantwortlichId());
        assertThat(actual.get(0).getLigaVerantwortlichMail()).isEqualTo(expectedLigaExtendedBE. getVerantwortlicherName());
        assertThat(actual.get(0).getDisziplinId()).isEqualTo(expectedLigaExtendedBE.getLigaDisziplinId());
        assertThat(actual.get(0).getLigaDetail()).isEqualTo(expectedLigaExtendedBE.getLigaDetail());
        assertThat(actual.get(0).getLigaDoFileBase64()).isEqualTo(expectedLigaExtendedBE.getLigaFileBase64());
        assertThat(actual.get(0).getLigaDoFileName()).isEqualTo(expectedLigaExtendedBE.getLigaFileName());
        assertThat(actual.get(0).getLigaDoFileType()).isEqualTo(expectedLigaExtendedBE.getLigaFileType());


        // verify invocations
        verify(ligaExtendedDAO).findEverything();
    }

    private static final long LIGA_ID = 1337L;

    @Test
    public void findByLowest(){
        // prepare test data
        final LigaBE expectedLigaBE = new LigaBE();
        expectedLigaBE.setLigaId(LIGA_ID);
        expectedLigaBE.setLigaName("Test Liga");
        expectedLigaBE.setLigaRegionId(42L);
        expectedLigaBE.setLigaUebergeordnetId(null);
        expectedLigaBE.setLigaVerantwortlichId(200L);
        expectedLigaBE.setLigaDetail("Details");
        expectedLigaBE.setLigaFileBase64("base64EncodedString");
        expectedLigaBE.setLigaFileName("fileName.pdf");
        expectedLigaBE.setLigaFileType("application/pdf");

        // configure mocks
        when(ligaDao.findByLowest(LIGA_ID)).thenReturn(expectedLigaBE);


        // call test method
        final LigaDO actual = underTest.findByLowest(LIGA_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(actual.getLigaUebergeordnetId()).isNull();
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
        assertThat(actual.getLigaDetail()).isEqualTo(expectedLigaBE.getLigaDetail());
        assertThat(actual.getLigaDoFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(actual.getLigaDoFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(actual.getLigaDoFileType()).isEqualTo(expectedLigaBE.getLigaFileType());


        // verify invocation
        verify(ligaDao).findByLowest(LIGA_ID);

    }

    @Test
    public void findByLowest_ReturnNull(){

        final LigaBE expectedLigaBE = new LigaBE();
        // call test method
        final LigaDO actual = underTest.findByLowest(LIGAID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(actual.getRegionName()).isEqualTo(null);
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(null);
        assertThat(actual.getDisziplinId()).isEqualTo(null);
        assertThat(actual.getLigaDetail()).isEqualTo(expectedLigaBE.getLigaDetail());
        assertThat(actual.getLigaDoFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(actual.getLigaDoFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(actual.getLigaDoFileType()).isEqualTo(expectedLigaBE.getLigaFileType());

        // verify invocations
        verify(ligaDao).findByLowest(LIGAID);


    }

    @Test
    public void findBySearch_whenAttributesAreNull() {

        // prepare test data
        final LigaExtendedBE expectedLigaBE = new LigaExtendedBE();
        expectedLigaBE.setLigaId(1L);
        expectedLigaBE.setLigaName("Test Liga");
        expectedLigaBE.setLigaUebergeordnetId(null);
        expectedLigaBE.setLigaRegionId(null);
        expectedLigaBE.setLigaVerantwortlichId(null);
        expectedLigaBE.setLigaDetail(null);
        expectedLigaBE.setLigaFileBase64("base64EncodedString");
        expectedLigaBE.setLigaFileName("fileName.pdf");
        expectedLigaBE.setLigaFileType("application/pdf");
        expectedLigaBE.setDisziplinName("Recurve");
        expectedLigaBE.setRegionName(null);
        expectedLigaBE.setUebergeordneteLigaName(null);
        expectedLigaBE.setVerantwortlicherName(null);

        final List<LigaExtendedBE> expectedBEList = Collections.singletonList(expectedLigaBE);

        // configure mocks
        when(ligaExtendedDAO.findBySearch(expectedLigaBE.getLigaName())).thenReturn(expectedBEList);

        // call test method
        final List<LigaDO> actual = underTest.findBySearch(expectedLigaBE.getLigaName());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        LigaDO actualLigaDO = actual.get(0);
        assertThat(actualLigaDO.getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actualLigaDO.getName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actualLigaDO.getRegionId()).isNull();
        assertThat(actualLigaDO.getRegionName()).isNull();
        assertThat(actualLigaDO.getLigaUebergeordnetId()).isNull();
        assertThat(actualLigaDO.getLigaUebergeordnetName()).isNull();
        assertThat(actualLigaDO.getLigaVerantwortlichId()).isNull();
        assertThat(actualLigaDO.getLigaVerantwortlichMail()).isNull();
        assertThat(actualLigaDO.getLigaDetail()).isNull();
        assertThat(actualLigaDO.getLigaDoFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(actualLigaDO.getLigaDoFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(actualLigaDO.getLigaDoFileType()).isEqualTo(expectedLigaBE.getLigaFileType());

        // verify invocations
        verify(ligaExtendedDAO).findBySearch(expectedLigaBE.getLigaName());
    }

    @Test
    public void findAll_whenAttributesAreNull() {
        // prepare test data
        final LigaExtendedBE expectedLigaBE = new LigaExtendedBE();
        expectedLigaBE.setLigaId(1L);
        expectedLigaBE.setLigaName("Test Liga");
        expectedLigaBE.setLigaUebergeordnetId(null);
        expectedLigaBE.setLigaRegionId(null);
        expectedLigaBE.setLigaVerantwortlichId(null);
        expectedLigaBE.setLigaDetail(null);
        expectedLigaBE.setLigaFileBase64("base64EncodedString");
        expectedLigaBE.setLigaFileName("fileName.pdf");
        expectedLigaBE.setLigaFileType("application/pdf");
        expectedLigaBE.setDisziplinName("Recurve");
        expectedLigaBE.setRegionName(null);
        expectedLigaBE.setUebergeordneteLigaName(null);
        expectedLigaBE.setVerantwortlicherName(null);

        final List<LigaExtendedBE> expectedBEList = Collections.singletonList(expectedLigaBE);


        // configure mocks
        when(ligaExtendedDAO.findEverything()).thenReturn(expectedBEList);


        // call test method
        final List<LigaDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        LigaDO actualLigaDO = actual.get(0);
        assertThat(actualLigaDO.getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actualLigaDO.getName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actualLigaDO.getRegionId()).isNull();
        assertThat(actualLigaDO.getRegionName()).isNull();
        assertThat(actualLigaDO.getLigaUebergeordnetId()).isNull();
        assertThat(actualLigaDO.getLigaUebergeordnetName()).isNull();
        assertThat(actualLigaDO.getLigaVerantwortlichId()).isNull();
        assertThat(actualLigaDO.getLigaVerantwortlichMail()).isNull();
        assertThat(actualLigaDO.getLigaDetail()).isNull();
        assertThat(actualLigaDO.getLigaDoFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(actualLigaDO.getLigaDoFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(actualLigaDO.getLigaDoFileType()).isEqualTo(expectedLigaBE.getLigaFileType());


        // verify invocations
        verify(ligaExtendedDAO).findEverything();
    }

    @Test
    public void findById__whenEverythingIsSet() {
        // prepare test data
        final LigaBE expectedLigaBE = new LigaBE();
        expectedLigaBE.setLigaId(1L);
        expectedLigaBE.setLigaName("Test Liga");
        expectedLigaBE.setLigaRegionId(2L);
        expectedLigaBE.setLigaUebergeordnetId(3L);
        expectedLigaBE.setLigaVerantwortlichId(4L);
        expectedLigaBE.setLigaDetail("Liga Details");
        expectedLigaBE.setLigaDisziplinId(5L);
        expectedLigaBE.setLigaFileBase64("base64EncodedString");
        expectedLigaBE.setLigaFileName("fileName.pdf");
        expectedLigaBE.setLigaFileType("application/pdf");

        final LigaExtendedBE expectedLigaExtendedBE = new LigaExtendedBE();
        expectedLigaExtendedBE.setRegionName("Test Region");
        expectedLigaExtendedBE.setUebergeordneteLigaName("Übergeordnete Liga");
        expectedLigaExtendedBE.setVerantwortlicherName("user@test.com");
        expectedLigaExtendedBE.setDisziplinName("Test Disziplin");


        // configure mocks
        when(ligaDao.findById(anyLong())).thenReturn(expectedLigaBE);
        when(ligaExtendedDAO.findAdditionalDataByLigaId(anyLong())).thenReturn(expectedLigaExtendedBE);

        // call test method
        final LigaDO actual = underTest.findById(expectedLigaBE.getLigaId());
        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actual.getRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(actual.getRegionName()).isEqualTo(expectedLigaExtendedBE.getRegionName());
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(expectedLigaBE.getLigaUebergeordnetId());
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(expectedLigaExtendedBE.getUebergeordneteLigaName());
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(expectedLigaExtendedBE.getVerantwortlicherName());
        assertThat(actual.getDisziplinId()).isEqualTo(expectedLigaBE.getLigaDisziplinId());
        assertThat(actual.getLigaDetail()).isEqualTo(expectedLigaBE.getLigaDetail());
        assertThat(actual.getLigaDoFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(actual.getLigaDoFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(actual.getLigaDoFileType()).isEqualTo(expectedLigaBE.getLigaFileType());


        // verify invocation
        verify(ligaDao).findById(expectedLigaBE.getLigaId());
        verify(ligaExtendedDAO).findAdditionalDataByLigaId(expectedLigaBE.getLigaId());
    }

    @Test
    public void findById_whenNoResult_shouldThrowException() {
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findById(anyLong()))
                .withMessageContaining("No result found for ID")
                .withNoCause();

        // assert result

        // verify invocations
        verify(ligaDao).findById(anyLong());
    }

    @Test
    public void findById_whenAttributesAreNull() {
        // prepare test data
        final LigaBE expectedLigaBE = getLigaBE();
        final DisziplinDO expectedDisziplinDO = getDisziplinDO();

        expectedLigaBE.setLigaDisziplinId(0L);
        expectedLigaBE.setLigaUebergeordnetId(null);
        expectedLigaBE.setLigaRegionId(null);
        expectedLigaBE.setLigaVerantwortlichId(null);
        expectedLigaBE.setLigaDetail(null);

        final LigaExtendedBE expectedLigaExtendedBE = new LigaExtendedBE();
        expectedLigaExtendedBE.setRegionName(null);
        expectedLigaExtendedBE.setUebergeordneteLigaName(null);
        expectedLigaExtendedBE.setVerantwortlicherName(null);
        expectedLigaExtendedBE.setDisziplinName("Test Disziplin");

        // configure mocks
        when(ligaDao.findById(anyLong())).thenReturn(expectedLigaBE);
        when(disziplinComponentImpl.findById(anyLong())).thenReturn(expectedDisziplinDO);
        when(ligaExtendedDAO.findAdditionalDataByLigaId(anyLong())).thenReturn(expectedLigaExtendedBE);

        // call test method
        final LigaDO actual = underTest.findById(LIGAID);


        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actual.getRegionId()).isNull();
        assertThat(actual.getRegionName()).isEqualTo(null);
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(expectedLigaBE.getLigaUebergeordnetId());
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(null);
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(null);
        assertThat(actual.getLigaDetail()).isEqualTo(null);
        assertThat(actual.getLigaDoFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(actual.getLigaDoFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(actual.getLigaDoFileType()).isEqualTo(expectedLigaBE.getLigaFileType());

        // verify invocation
        verify(ligaDao).findById(LIGAID);
    }

    @Test
    public void checkExist__whenLigaExists() {
        // prepare test data
        final LigaBE expectedLigaBE = getLigaBE();
        final RegionenDO expectedRegionBE = getRegionenDO();
        final UserDO expectedUserDO = getUserDO();
        final DisziplinDO expectedDisziplinDO = getDisziplinDO();

        final LigaExtendedBE expectedLigaExtendedBE = new LigaExtendedBE();
        expectedLigaExtendedBE.setRegionName("Test Region");
        expectedLigaExtendedBE.setUebergeordneteLigaName("Übergeordnete Liga");
        expectedLigaExtendedBE.setVerantwortlicherName("test@mail.de");
        expectedLigaExtendedBE.setDisziplinName(null);

        // configure mocks
        when(ligaDao.findById(anyLong())).thenReturn(expectedLigaBE);
        when(regionenComponentImpl.findById(anyLong())).thenReturn(expectedRegionBE);
        when(userComponentImpl.findById(anyLong())).thenReturn(expectedUserDO);
        when(disziplinComponentImpl.findById(anyLong())).thenReturn(expectedDisziplinDO);
        when(ligaExtendedDAO.findAdditionalDataByLigaId(anyLong())).thenReturn(expectedLigaExtendedBE);

        // call test method
        final LigaDO actual = underTest.checkExist(LIGAID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(actual.getRegionName()).isEqualTo(expectedLigaExtendedBE.getRegionName());
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(expectedLigaExtendedBE.getUebergeordneteLigaName());
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(expectedUserDO.getEmail());
        assertThat(actual.getDisziplinId()).isEqualTo(expectedDisziplinDO.getDisziplinId());
        assertThat(actual.getLigaDetail()).isEqualTo(expectedLigaBE.getLigaDetail());
        assertThat(actual.getLigaDoFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(actual.getLigaDoFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(actual.getLigaDoFileType()).isEqualTo(expectedLigaBE.getLigaFileType());


        // verify invocation
        verify(ligaDao).findById(LIGAID);

    }
    
    @Test
    public void checkExist_whenLigaDoesNotExist() {
        // prepare test data
        final long id = 1;
        when(ligaDao.findById(anyLong())).thenReturn(null);

        // call test method
        final LigaDO actual = underTest.checkExist(id);

        // assert result
        assertThat(actual.getId()).isNull();
        assertThat(actual.getLigaDetail()).isNull();
        assertThat(actual.getName()).isNull();

        verify(ligaDao).findById(id);
    }

    @Test
    public void checkExistsLigaName__whenLigaNameExists() {
        // prepare test data
        final LigaBE expectedLigaBE = getLigaBE();
        final LigaBE expectedLigaUebergeordnetBE = expectedLigaBE;
        final RegionenDO expectedRegionBE = getRegionenDO();
        final UserDO expectedUserDO = getUserDO();
        final DisziplinDO expectedDisziplinDO = getDisziplinDO();
        final LigaExtendedBE expectedLigaExtendedBE = new LigaExtendedBE();
        expectedLigaExtendedBE.setRegionName("Test");
        expectedLigaExtendedBE.setUebergeordneteLigaName("Test Liga");
        expectedLigaExtendedBE.setVerantwortlicherName("test@mail.de");
        expectedLigaExtendedBE.setDisziplinName(null);

        // configure mocks
        when(ligaDao.findByLigaName(expectedLigaBE.getLigaName())).thenReturn(expectedLigaBE);
        when(ligaDao.findById(anyLong())).thenReturn(expectedLigaBE);
        when(regionenComponentImpl.findById(anyLong())).thenReturn(expectedRegionBE);
        when(userComponentImpl.findById(anyLong())).thenReturn(expectedUserDO);
        when(disziplinComponentImpl.findById(anyLong())).thenReturn(expectedDisziplinDO);
        when(ligaExtendedDAO.findAdditionalDataByLigaId(anyLong())).thenReturn(expectedLigaExtendedBE);

        // call test method
        final LigaDO actual = underTest.checkExistsLigaName(expectedLigaBE.getLigaName());

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId()).isEqualTo(expectedLigaBE.getLigaId());
        assertThat(actual.getName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actual.getRegionId()).isEqualTo(expectedLigaBE.getLigaRegionId());
        assertThat(actual.getRegionName()).isEqualTo(expectedRegionBE.getRegionName());
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(expectedLigaUebergeordnetBE.getLigaId());
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(expectedLigaUebergeordnetBE.getLigaName());
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(expectedLigaBE.getLigaVerantwortlichId());
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(expectedUserDO.getEmail());
        assertThat(actual.getLigaDetail()).isEqualTo(expectedLigaBE.getLigaDetail());
        assertThat(actual.getLigaDoFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(actual.getLigaDoFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(actual.getLigaDoFileType()).isEqualTo(expectedLigaBE.getLigaFileType());

        // verify invocations
        verify(ligaDao).findByLigaName(expectedLigaBE.getLigaName());


    }

    @Test
    public void checkExistsLigaName_whenLigaNameDoesNotExist() {
        // prepare test data
        final String liganame = "jUnitTestLiga";
        when(ligaDao.findByLigaName(liganame)).thenReturn(null);

        // call test method
        final LigaDO actual = underTest.checkExistsLigaName(liganame);

        // assert result
        assertThat(actual.getId()).isNull();
        assertThat(actual.getLigaDetail()).isNull();
        assertThat(actual.getName()).isNull();

        verify(ligaDao).findByLigaName(liganame);
    }

    @Test
    public void create__whenEverythingIsSet() {
        // prepare test data
        final LigaDO input = getLigaDO();
        final LigaBE expectedLigaBE = getLigaBE();
        final RegionenDO expectedRegionBE = getRegionenDO();
        final UserDO expectedUserDO = getUserDO();
        final DisziplinDO expectedDisziplinDO = getDisziplinDO();

        final LigaExtendedBE expectedLigaExtendedBE = new LigaExtendedBE();
        expectedLigaExtendedBE.setRegionName("Test");
        expectedLigaExtendedBE.setUebergeordneteLigaName("Test Liga");
        expectedLigaExtendedBE.setVerantwortlicherName("test@mail.de");
        expectedLigaExtendedBE.setDisziplinName("Test");

        // connfigure mocks
        when(ligaDao.create(any(LigaBE.class), anyLong())).thenReturn(expectedLigaBE);
        when(ligaDao.findById(anyLong())).thenReturn(expectedLigaBE);
        when(regionenComponentImpl.findById(anyLong())).thenReturn(expectedRegionBE);
        when(userComponentImpl.findById(anyLong())).thenReturn(expectedUserDO);
        when(disziplinComponentImpl.findById(anyLong())).thenReturn(expectedDisziplinDO);
        when(ligaExtendedDAO.findAdditionalDataByLigaId(anyLong())).thenReturn(expectedLigaExtendedBE);

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
        assertThat(actual.getRegionName()).isEqualTo(expectedLigaExtendedBE.getDisziplinName());
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(expectedLigaBE.getLigaName());
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(expectedUserDO.getEmail());
        assertThat(actual.getDisziplinId()).isEqualTo(expectedDisziplinDO.getDisziplinId());
        assertThat(actual.getLigaDetail()).isEqualTo(expectedLigaBE.getLigaDetail());
        assertThat(actual.getLigaDoFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(actual.getLigaDoFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(actual.getLigaDoFileType()).isEqualTo(expectedLigaBE.getLigaFileType());
    }


    @Test
    public void create_whenArgumentsAreNull() {
        // prepare test data
        final LigaDO input = getLigaDO();
        final LigaBE expectedLigaBE = getLigaBE();
        final RegionenDO expectedRegionBE = getRegionenDO();
        final DisziplinDO expectedDisziplinDO = getDisziplinDO();

        expectedLigaBE.setLigaUebergeordnetId(null);
        expectedLigaBE.setLigaVerantwortlichId(null);
        input.setLigaUebergeordnetId(null);
        input.setLigaVerantwortlichId(null);

        // connfigure mocks
        when(ligaDao.create(any(LigaBE.class), anyLong())).thenReturn(expectedLigaBE);
        when(regionenComponentImpl.findById(anyLong())).thenReturn(expectedRegionBE);
        when(disziplinComponentImpl.findById(anyLong())).thenReturn(expectedDisziplinDO);

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
        assertThat(persistedLigaBE.getLigaDisziplinId()).isEqualTo(expectedLigaBE.getLigaDisziplinId());
        assertThat(persistedLigaBE.getLigaDetail()).isEqualTo(expectedLigaBE.getLigaDetail());
        assertThat(persistedLigaBE.getLigaFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(persistedLigaBE.getLigaFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(persistedLigaBE.getLigaFileType()).isEqualTo(expectedLigaBE.getLigaFileType());
        assertThat(persistedLigaBE.getLigaUebergeordnetId()).isEqualTo(null);
        assertThat(persistedLigaBE.getLigaVerantwortlichId()).isEqualTo(null);
    }


    @Test
    public void update_whenEverythingIsSet() {
        // prepare test data
        final LigaDO input = getLigaDO();
        final LigaBE expectedLigaBE = getLigaBE();
        final RegionenDO expectedRegionBE = getRegionenDO();
        final UserDO expectedUserDO = getUserDO();
        final DisziplinDO expectedDisziplinDO = getDisziplinDO();

        final LigaExtendedBE expectedLigaExtendedBE = new LigaExtendedBE();
        expectedLigaExtendedBE.setRegionName("Test Region");
        expectedLigaExtendedBE.setUebergeordneteLigaName("Übergeordnete Liga");
        expectedLigaExtendedBE.setVerantwortlicherName("user@test.com");
        expectedLigaExtendedBE.setDisziplinName(null);

        // configure mocks
        when(ligaDao.update(any(LigaBE.class), anyLong())).thenReturn(expectedLigaBE);
        when(ligaDao.findById(anyLong())).thenReturn(expectedLigaBE);
        when(regionenComponentImpl.findById(anyLong())).thenReturn(expectedRegionBE);
        when(userComponentImpl.findById(anyLong())).thenReturn(expectedUserDO);
        when(disziplinComponentImpl.findById(anyLong())).thenReturn(expectedDisziplinDO);

        // call test method
        final LigaDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getName()).isEqualTo(input.getName());

        // verify invocation
        verify(ligaDao).update(ligaBEArgumentCaptor.capture(), anyLong());

        final LigaBE persistedBE = ligaBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getLigaId()).isEqualTo(input.getId());
        assertThat(persistedBE.getLigaName()).isEqualTo(input.getName());
        assertThat(persistedBE.getLigaRegionId()).isEqualTo(input.getRegionId());
        assertThat(persistedBE.getLigaDetail()).isEqualTo(input.getLigaDetail());
        assertThat(persistedBE.getLigaFileBase64()).isEqualTo(input.getLigaDoFileBase64());
        assertThat(persistedBE.getLigaFileName()).isEqualTo(input.getLigaDoFileName());
        assertThat(persistedBE.getLigaFileType()).isEqualTo(input.getLigaDoFileType());

        // test mapping of do
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(expectedLigaExtendedBE.getLigaName());
        assertThat(actual.getDisziplinId()).isEqualTo(expectedDisziplinDO.getDisziplinId());
        assertThat(actual.getLigaDetail()).isEqualTo(expectedLigaBE.getLigaDetail());
        assertThat(actual.getLigaDoFileBase64()).isEqualTo(expectedLigaBE.getLigaFileBase64());
        assertThat(actual.getLigaDoFileName()).isEqualTo(expectedLigaBE.getLigaFileName());
        assertThat(actual.getLigaDoFileType()).isEqualTo(expectedLigaBE.getLigaFileType());
    }


    @Test
    public void update_whenArgumentsAreNull() {
        // prepare test data
        final LigaDO input = getLigaDO();
        final LigaBE expectedLigaBE = getLigaBE();
        final RegionenDO expectedRegionBE = getRegionenDO();
        final DisziplinDO expectedDisziplinDO = getDisziplinDO();

        expectedLigaBE.setLigaUebergeordnetId(null);
        expectedLigaBE.setLigaVerantwortlichId(null);
        expectedLigaBE.setLigaDetail(null);

        final LigaExtendedBE expectedLigaExtendedBE = new LigaExtendedBE();
        expectedLigaExtendedBE.setRegionName("Test Region");
        expectedLigaExtendedBE.setUebergeordneteLigaName("Übergeordnete Liga");
        expectedLigaExtendedBE.setVerantwortlicherName("user@test.com");
        expectedLigaExtendedBE.setDisziplinName(null);

        // configure mocks
        when(ligaDao.update(any(LigaBE.class), anyLong())).thenReturn(expectedLigaBE);
        when(regionenComponentImpl.findById(anyLong())).thenReturn(expectedRegionBE);
        when(disziplinComponentImpl.findById(anyLong())).thenReturn(expectedDisziplinDO);

        // call test method
        final LigaDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getName()).isEqualTo(input.getName());

        // verify invocation
        verify(ligaDao).update(ligaBEArgumentCaptor.capture(), anyLong());

        final LigaBE persistedBE = ligaBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getLigaId()).isEqualTo(input.getId());
        assertThat(persistedBE.getLigaName()).isEqualTo(input.getName());
        assertThat(persistedBE.getLigaRegionId()).isEqualTo(input.getRegionId());
        assertThat(persistedBE.getLigaDetail()).isEqualTo(input.getLigaDetail());
        assertThat(persistedBE.getLigaFileBase64()).isEqualTo(input.getLigaDoFileBase64());
        assertThat(persistedBE.getLigaFileName()).isEqualTo(input.getLigaDoFileName());
        assertThat(persistedBE.getLigaFileType()).isEqualTo(input.getLigaDoFileType());

        // test mapping of do
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(null);
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(null);
        assertThat(actual.getLigaDetail()).isEqualTo(null);


    }


    @Test
    public void delete() {
        // prepare test data
        final LigaDO input = getLigaDO();

        // call test method
        underTest.delete(input, USER);

        // verify invocations
        verify(ligaDao).delete(ligaBEArgumentCaptor.capture(), anyLong());
        final LigaBE persistedLigaBE = ligaBEArgumentCaptor.getValue();

        assertThat(persistedLigaBE).isNotNull();

        assertThat(persistedLigaBE.getLigaId()).isEqualTo(input.getId());
    }
}