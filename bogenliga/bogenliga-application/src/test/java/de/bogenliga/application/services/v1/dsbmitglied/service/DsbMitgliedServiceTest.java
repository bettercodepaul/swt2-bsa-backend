package de.bogenliga.application.services.v1.dsbmitglied.service;

import java.security.Principal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import javax.naming.NoPermissionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.services.v1.dsbmitglied.model.DsbMitgliedDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;

/**
 * @author Andre Lehnert, BettercallPaul gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class DsbMitgliedServiceTest {

    private static final long USER = 0;

    private static final long ID = 1337;
    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final Date GEBURTSDATUM = Date.valueOf("1991-09-01");
    private static final String NATIONALITAET = "DE";
    private static final String MITGLIEDSNUMMER = "223344uu";
    private static final long VEREINSID = 2;

    private static final String VEREINNAME = "TEST VEREIN";
    private static final long USERID = 4242;
    private static final long USERIDUPDATE = 2121;

    private static final boolean KAMPFRICHTER = true;
    private static final Date BEITRITTSDATUM = Date.valueOf("2001-01-01");

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private DsbMitgliedComponent dsbMitgliedComponent;

    @Mock
    private Principal principal;

    @Mock
    private RequiresOnePermissionAspect requiresOnePermissionAspect;

    @InjectMocks
    private DsbMitgliedService underTest;

    @Captor
    private ArgumentCaptor<DsbMitgliedDO> dsbMitgliedVOArgumentCaptor;


    private static DsbMitgliedDO getDsbMitgliedDO() {
        return new DsbMitgliedDO(
                ID,
                VORNAME,
                NACHNAME,
                GEBURTSDATUM,
                NATIONALITAET,
                MITGLIEDSNUMMER,
                VEREINSID,
                VEREINNAME,
                USERID,
                KAMPFRICHTER,
                BEITRITTSDATUM
        );
    }


    private static DsbMitgliedDTO getDsbMitgliedDTO() {
        final DsbMitgliedDTO dsbMitgliedDTO = new DsbMitgliedDTO();
        dsbMitgliedDTO.setId(ID);
        dsbMitgliedDTO.setVorname(VORNAME);
        dsbMitgliedDTO.setNachname(NACHNAME);
        dsbMitgliedDTO.setGeburtsdatum(GEBURTSDATUM.toString());
        dsbMitgliedDTO.setNationalitaet(NATIONALITAET);
        dsbMitgliedDTO.setMitgliedsnummer(MITGLIEDSNUMMER);
        dsbMitgliedDTO.setVereinsId(VEREINSID);
        dsbMitgliedDTO.setVereinsName(VEREINNAME);
        dsbMitgliedDTO.setUserId(USERID);
        dsbMitgliedDTO.setKampfrichter(KAMPFRICHTER);
        dsbMitgliedDTO.setBeitrittsdatum(BEITRITTSDATUM.toString());
        return dsbMitgliedDTO;
    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }


    @Test
    public void findAll() {
        // prepare test data
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();
        final List<DsbMitgliedDO> dsbMitgliedDOList = Collections.singletonList(dsbMitgliedDO);

        // configure mocks
        when(dsbMitgliedComponent.findAll()).thenReturn(dsbMitgliedDOList);

        // call test method
        final List<DsbMitgliedDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        final DsbMitgliedDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(dsbMitgliedDO.getId());
        assertThat(actualDTO.getVorname()).isEqualTo(dsbMitgliedDO.getVorname());

        // verify invocations
        verify(dsbMitgliedComponent).findAll();
    }


    @Test
    public void findAllByTeamId() {
        // prepare test data
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();
        final List<DsbMitgliedDO> dsbMitgliedDOList = Collections.singletonList(dsbMitgliedDO);

        // configure mocks
        when(dsbMitgliedComponent.findAllByTeamId(anyLong())).thenReturn(dsbMitgliedDOList);

        // call test method
        final List<DsbMitgliedDTO> actual = underTest.findAllByTeamId(ID);

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        final DsbMitgliedDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(dsbMitgliedDO.getId());
        assertThat(actualDTO.getVorname()).isEqualTo(dsbMitgliedDO.getVorname());

        // verify invocations
        verify(dsbMitgliedComponent).findAllByTeamId(ID);
    }

    @Test
    public void findAllByTeamIdPreconditionTest(){
        try {
            assertThat(underTest.findAllByTeamId(-1L)).isNotNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void findAllNotInTeamIdPreconditionTest(){
        try {
            assertThat(underTest.findAllNotInTeamId(-1L, -1L)).isNotNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void insertUserIdPreconditionTest(){
        try {
            assertThat(underTest.insertUserId(-1L, -1L, principal )).isNotNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void findByIdPreconditionTest(){
        try {
            assertThat(underTest.findById(-1L)).isNotNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void findAllNotInTeamId() {

        long teamId = 103, vereinsId = 11;

        // prepare test data
        final DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO(
                ID,
                VORNAME,
                NACHNAME,
                GEBURTSDATUM,
                NATIONALITAET,
                MITGLIEDSNUMMER,
                vereinsId,
                VEREINNAME,
                USERID,
                KAMPFRICHTER,
                BEITRITTSDATUM
        );

        final List<DsbMitgliedDO> dsbMitgliedDOList = Collections.singletonList(dsbMitgliedDO);

        // configure mocks
        when(dsbMitgliedComponent.findAllNotInTeam(anyLong(), anyLong())).thenReturn(dsbMitgliedDOList);

        // call test method
        final List<DsbMitgliedDTO> actual = underTest.findAllNotInTeamId(vereinsId, teamId);

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        final DsbMitgliedDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(dsbMitgliedDO.getId());
        assertThat(actualDTO.getVereinsId()).isEqualTo(vereinsId);

        // verify invocations
        verify(dsbMitgliedComponent).findAllNotInTeam(teamId, vereinsId);
    }


    @Test
    public void findById() {
        // prepare test data
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();

        // configure mocks
        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(dsbMitgliedDO);

        // call test method
        final DsbMitgliedDTO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(dsbMitgliedDO.getId());
        assertThat(actual.getVorname()).isEqualTo(dsbMitgliedDO.getVorname());

        // verify invocations
        verify(dsbMitgliedComponent).findById(ID);
    }


    @Test
    public void findBySearch() {
        //prepare test data
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();
        final List<DsbMitgliedDO> dsbMitgliedDOList = Collections.singletonList(dsbMitgliedDO);
        // configure mocks
        when(dsbMitgliedComponent.findBySearch(dsbMitgliedDO.getVorname())).thenReturn(dsbMitgliedDOList);

        // call test method
        final List<DsbMitgliedDTO> actual = underTest.findBySearch(dsbMitgliedDO.getVorname());

        // assert result
        assertThat(actual).isNotNull();

        final DsbMitgliedDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(dsbMitgliedDO.getId());
        assertThat(actualDTO.getVorname()).isEqualTo(dsbMitgliedDO.getVorname());

        // verify invocations
        verify(dsbMitgliedComponent).findBySearch(dsbMitgliedDO.getVorname());
    }


    @Test
    public void insertUserId() {
        // prepare test data
        final DsbMitgliedDTO input = getDsbMitgliedDTO();
        final DsbMitgliedDO expected = getDsbMitgliedDO();
        final DsbMitgliedDO expectedupdate = getDsbMitgliedDO();
        expectedupdate.setUserId(USERIDUPDATE);

        // configure mocks
        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(expected);
        when(dsbMitgliedComponent.update(any(), anyLong())).thenReturn(expected);

        // call test method
        final DsbMitgliedDTO actual = underTest.insertUserId(ID, USERIDUPDATE, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getUserId()).isEqualTo(USERIDUPDATE);

        // verify invocations
        verify(dsbMitgliedComponent).findById(anyLong());
        verify(dsbMitgliedComponent).update(any(), anyLong());
    }


    @Test
    public void create() {
        // prepare test data
        final DsbMitgliedDTO input = getDsbMitgliedDTO();
        final DsbMitgliedDO expected = getDsbMitgliedDO();

        // configure mocks
        when(dsbMitgliedComponent.create(any(), anyLong())).thenReturn(expected);

        // call test method
        final DsbMitgliedDTO actual = underTest.create(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getVorname()).isEqualTo(input.getVorname());

        // verify invocations
        verify(dsbMitgliedComponent).create(dsbMitgliedVOArgumentCaptor.capture(), anyLong());

        final DsbMitgliedDO createdDsbMitglied = dsbMitgliedVOArgumentCaptor.getValue();

        assertThat(createdDsbMitglied).isNotNull();
        assertThat(createdDsbMitglied.getId()).isEqualTo(input.getId());
        assertThat(createdDsbMitglied.getVorname()).isEqualTo(input.getVorname());
    }


    @Test
    public void update() {
        // prepare test data
        final DsbMitgliedDTO input = getDsbMitgliedDTO();
        final DsbMitgliedDO expected = getDsbMitgliedDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(dsbMitgliedComponent.update(any(), anyLong())).thenReturn(expected);

        // call test method
        try {
            final DsbMitgliedDTO actual = underTest.update(input, principal);//,false);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(input.getId());
            assertThat(actual.getVorname()).isEqualTo(input.getVorname());

            // verify invocations
            verify(dsbMitgliedComponent).update(dsbMitgliedVOArgumentCaptor.capture(), anyLong());

            final DsbMitgliedDO updatedDsbMitglied = dsbMitgliedVOArgumentCaptor.getValue();

            assertThat(updatedDsbMitglied).isNotNull();
            assertThat(updatedDsbMitglied.getId()).isEqualTo(input.getId());
            assertThat(updatedDsbMitglied.getVorname()).isEqualTo(input.getVorname());

        } catch (NoPermissionException e) {
        }
    }


    @Test
    public void updateNoPermission() {
        // prepare test data
        final DsbMitgliedDTO input = getDsbMitgliedDTO();
        final DsbMitgliedDO expected = getDsbMitgliedDO();

        // configure mocks
        when(dsbMitgliedComponent.update(any(), anyLong())).thenReturn(expected);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(any(), anyLong())).thenReturn(false);

        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(() -> underTest.update(input, principal));
    }


    @Test
    public void delete() {
        // prepare test data
        final DsbMitgliedDO expected = getDsbMitgliedDO();

        // call test method
        underTest.delete(ID, principal);

        // verify invocations
        verify(dsbMitgliedComponent).delete(dsbMitgliedVOArgumentCaptor.capture(), anyLong());

        final DsbMitgliedDO deletedDsbMitglied = dsbMitgliedVOArgumentCaptor.getValue();

        assertThat(deletedDsbMitglied).isNotNull();
        assertThat(deletedDsbMitglied.getId()).isEqualTo(expected.getId());
        assertThat(deletedDsbMitglied.getVorname()).isNullOrEmpty();
    }
}