package de.bogenliga.application.services.v1.veranstaltung;

import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.services.v1.veranstaltung.model.VeranstaltungDTO;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.services.v1.sportjahr.SportjahrDTO;
import de.bogenliga.application.services.v1.veranstaltung.service.VeranstaltungService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;

import java.security.Principal;

import java.util.*;
import java.sql.Date;

import javax.naming.NoPermissionException;
import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



public class VeranstaltungServiceTest {

    private static final long USER = 0;
    private static final long ID = 0;

    private static final long VERANSTALTUNG_ID = 0;
    private static final long WETTKAMPFTYP_ID = 0;
    private static final String VERANSTALTUNG_NAME = "";
    private static final long SPORTJAHR = 0;
    private static final Date MELDEDEADLINE = new Date(20180211L);
    private static final long LIGALEITERID = 0;
    private static final long LIGAID = 0;
    private static final String LIGALEITER_EMAIL = "";
    private static final String WETTKAMPTYP_NAME = "";
    private static final String LIGANAME = "";
    private static final long SPORTJAHR_ID = 0;
    private static final long SPORTJAHR_JAHR = 0;


   
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private VeranstaltungComponent VeranstaltungComponent;

    @Mock
    private RequiresOnePermissionAspect requiresOnePermissionAspect;

    @Mock
    private Principal principal;

    @InjectMocks
    private VeranstaltungService underTest;

    @Captor
    private ArgumentCaptor<VeranstaltungDO> VeranstaltungDOArgumentCaptor;



    public static VeranstaltungDO getVeranstaltungDO() {
        return new VeranstaltungDO(
            VERANSTALTUNG_ID ,
            WETTKAMPFTYP_ID ,
            VERANSTALTUNG_NAME ,
            SPORTJAHR ,
            MELDEDEADLINE ,
            LIGALEITERID ,
            LIGAID ,
            LIGALEITER_EMAIL ,
            WETTKAMPTYP_NAME ,
            LIGANAME
        );
    }

    public static VeranstaltungDTO getVeranstaltungDTO() {
        final VeranstaltungDTO veranstaltungDTO = new VeranstaltungDTO();
        veranstaltungDTO.setId(VERANSTALTUNG_ID);
        veranstaltungDTO.setWettkampfTypId(WETTKAMPFTYP_ID  );
        veranstaltungDTO.setName(VERANSTALTUNG_NAME);
        veranstaltungDTO.setSportjahr(SPORTJAHR);
        veranstaltungDTO.setMeldeDeadline(MELDEDEADLINE);
        veranstaltungDTO.setLigaleiterId(LIGALEITERID);
        veranstaltungDTO.setLigaId(LIGAID);
        veranstaltungDTO.setLigaleiterEmail(LIGALEITER_EMAIL);
        veranstaltungDTO.setWettkampftypName(WETTKAMPTYP_NAME);
        veranstaltungDTO.setLigaName(LIGANAME);
        return veranstaltungDTO;
    }

    public static SportjahrDO getSportjahrDO() {
        return new SportjahrDO(SPORTJAHR_ID, SPORTJAHR_JAHR);
    }



    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }


    @Test
    public void findAll() {
        // prepare test data
        final VeranstaltungDO VeranstaltungDO = getVeranstaltungDO();
        final List<VeranstaltungDO> VeranstaltungDOList = Collections.singletonList(VeranstaltungDO);

        // configure mocks
        when(VeranstaltungComponent.findAll()).thenReturn(VeranstaltungDOList);

        // call test method
        final List<VeranstaltungDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        final VeranstaltungDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(VeranstaltungDO.getVeranstaltungID());
        assertThat(actualDTO.getName()).isEqualTo(VeranstaltungDO.getVeranstaltungName());
        /*
        assertThat(actualDTO.getWettkampfTypId()).isEqualTo(VeranstaltungDO.getVeranstaltungWettkampftypID());
        assertThat(actualDTO.getSportjahr()).isEqualTo(VeranstaltungDO.getVeranstaltungSportJahr());
        assertThat(actualDTO.getMeldeDeadline()).isEqualTo(VeranstaltungDO.getVeranstaltungMeldeDeadline());
        assertThat(actualDTO.getLigaleiterID()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaleiterID());
        assertThat(actualDTO.getLigaID()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaID());
        assertThat(actualDTO.getLigaleiterEmail()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaleiterEmail());
        assertThat(actualDTO.getWettkampftypName()).isEqualTo(VeranstaltungDO.getVeranstaltungWettkampftypName());;
        assertThat(actualDTO.getLigaName()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaName());
        */

        // verify invocations
        verify(VeranstaltungComponent).findAll();
    }

    @Test
    public void findById() {
        // prepare test data
        final VeranstaltungDO VeranstaltungDO = getVeranstaltungDO();

        // configure mocks
        when(VeranstaltungComponent.findById(anyLong())).thenReturn(VeranstaltungDO);

        // call test method
        final VeranstaltungDTO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(VeranstaltungDO.getVeranstaltungID());
        assertThat(actual.getName()).isEqualTo(VeranstaltungDO.getVeranstaltungName());

        // verify invocations
        verify(VeranstaltungComponent).findById(ID);
    }


    @Test
    public void findByLigaId() {
        // prepare test data
        final VeranstaltungDO VeranstaltungDO = getVeranstaltungDO();
        final List<VeranstaltungDO> VeranstaltungDOList = Collections.singletonList(VeranstaltungDO);

        // configure mocks
        when(VeranstaltungComponent.findByLigaID(anyLong())).thenReturn(VeranstaltungDOList);

        // call test method
        final List<VeranstaltungDTO> actual = underTest.findByLigaId(ID);

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        // verify invocations
        verify(VeranstaltungComponent).findByLigaID(ID);
    }


    @Test
    public void findAllSportjahrDestinct() {
        // prepare test data
        final SportjahrDO SportjahrDO = getSportjahrDO();
        final List<SportjahrDO> SportjahrDOList = Collections.singletonList(SportjahrDO);

        // configure mocks
        when(VeranstaltungComponent.findAllSportjahreDestinct()).thenReturn(SportjahrDOList);

        // call test method
        final List<SportjahrDTO> actual = underTest.findAllSportjahrDestinct();

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        // verify invocations
        verify(VeranstaltungComponent).findAllSportjahreDestinct();
    }


    @Test
    public void findBySportjahr() {
        // prepare test data
        final VeranstaltungDO VeranstaltungDO = getVeranstaltungDO();
        final List<VeranstaltungDO> VeranstaltungDOList = Collections.singletonList(VeranstaltungDO);

        // configure mocks
        when(VeranstaltungComponent.findBySportjahr(anyLong())).thenReturn(VeranstaltungDOList);

        // call test method
        final List<VeranstaltungDTO> actual = underTest.findBySportjahr(SPORTJAHR);

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        // verify invocations
        verify(VeranstaltungComponent).findBySportjahr(SPORTJAHR);
    }


    @Test
    public void create() {
        // prepare test data
        final VeranstaltungDTO input = getVeranstaltungDTO();
        final VeranstaltungDO expected = getVeranstaltungDO();

        // configure mocks
        when(VeranstaltungComponent.create(any(), anyLong())).thenReturn(expected);

        // call test method
        final VeranstaltungDTO actual = underTest.create(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getName()).isEqualTo(input.getName());

        // verify invocations
        verify(VeranstaltungComponent).create(VeranstaltungDOArgumentCaptor.capture(), anyLong());

        final VeranstaltungDO createdVeranstaltung = VeranstaltungDOArgumentCaptor.getValue();

        assertThat(createdVeranstaltung).isNotNull();
        assertThat(createdVeranstaltung.getVeranstaltungID()).isEqualTo(input.getId());
        assertThat(createdVeranstaltung.getVeranstaltungName()).isEqualTo(input.getName());
    }


    @Test
    public void update() {
        // prepare test data
        final VeranstaltungDTO input = getVeranstaltungDTO();
        final VeranstaltungDO expected = getVeranstaltungDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(VeranstaltungComponent.update(any(), anyLong())).thenReturn(expected);

        try {
            // call test method
            final VeranstaltungDTO actual = underTest.update(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(input.getId());
            assertThat(actual.getName()).isEqualTo(input.getName());

            assertThat(actual.getWettkampfTypId()).isEqualTo(input.getWettkampfTypId());
            assertThat(actual.getSportjahr()).isEqualTo(input.getSportjahr());
            assertThat(actual.getMeldeDeadline()).isEqualTo(input.getMeldeDeadline());
            assertThat(actual.getLigaleiterId()).isEqualTo(input.getLigaleiterId());
            assertThat(actual.getLigaId()).isEqualTo(input.getLigaId());
            assertThat(actual.getLigaleiterEmail()).isEqualTo(input.getLigaleiterEmail());
            assertThat(actual.getWettkampftypName()).isEqualTo(input.getWettkampftypName());
            assertThat(actual.getLigaName()).isEqualTo(input.getLigaName());

            // verify invocations
            verify(VeranstaltungComponent).update(VeranstaltungDOArgumentCaptor.capture(), anyLong());

            final VeranstaltungDO updatedVeranstaltung = VeranstaltungDOArgumentCaptor.getValue();

            assertThat(updatedVeranstaltung).isNotNull();
            assertThat(updatedVeranstaltung.getVeranstaltungID()).isEqualTo(input.getId());
            assertThat(updatedVeranstaltung.getVeranstaltungName()).isEqualTo(input.getName());
        }catch (NoPermissionException e) { }
    }


    @Test
    public void updateNoPermission() {
        // prepare test data
        final VeranstaltungDTO input = getVeranstaltungDTO();
        final VeranstaltungDO expected = getVeranstaltungDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(any(), anyLong())).thenReturn(false);
        when(VeranstaltungComponent.update(any(), anyLong())).thenReturn(expected);

        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(() -> underTest.update(input, principal));
    }


    @Test
    public void delete() {
        // prepare test data
        final VeranstaltungDO expected = getVeranstaltungDO();

        // call test method
        underTest.delete(ID, principal);

        // verify invocations
        verify(VeranstaltungComponent).delete(VeranstaltungDOArgumentCaptor.capture(), anyLong());

        final VeranstaltungDO deletedVeranstaltung = VeranstaltungDOArgumentCaptor.getValue();

        assertThat(deletedVeranstaltung).isNotNull();
        assertThat(deletedVeranstaltung.getVeranstaltungID()).isEqualTo(expected.getVeranstaltungID());
        assertThat(deletedVeranstaltung.getVeranstaltungName()).isNullOrEmpty();
    }

    @Test
    public void findBySportjahrDestinct(){
        //prepare data
        final VeranstaltungDO VeranstaltungDO = getVeranstaltungDO();
        final List<VeranstaltungDO> VeranstaltungDOList = Collections.singletonList(VeranstaltungDO);

        //configure mocks
        when(VeranstaltungComponent.findBySportjahrDestinct(anyLong())).thenReturn(VeranstaltungDOList);

        //call test method
        final List<VeranstaltungDTO> actual = underTest.findBySportjahrDestinct(anyLong());

        //asserting returns
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final VeranstaltungDTO testObject = actual.get(0);

        //asserting testObject in VeranstaltungDOList
        assertThat(testObject).isNotNull();
        assertThat(testObject.getId()).isEqualTo(VeranstaltungDO.getVeranstaltungID());
        assertThat(testObject.getWettkampfTypId()).isEqualTo(VeranstaltungDO.getVeranstaltungWettkampftypID());
        assertThat(testObject.getName()).isEqualTo(VeranstaltungDO.getVeranstaltungName());
        assertThat(testObject.getSportjahr()).isEqualTo(VeranstaltungDO.getVeranstaltungSportJahr());
        assertThat(testObject.getMeldeDeadline()).isEqualTo(VeranstaltungDO.getVeranstaltungMeldeDeadline());
        assertThat(testObject.getLigaleiterId()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaleiterID());
        assertThat(testObject.getLigaId()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaID());
        assertThat(testObject.getLigaleiterEmail()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaleiterEmail());
        assertThat(testObject.getWettkampftypName()).isEqualTo(VeranstaltungDO.getVeranstaltungWettkampftypName());
        assertThat(testObject.getLigaName()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaName());

        //verifying invocations
        verify(VeranstaltungComponent).findBySportjahrDestinct(anyLong());

    }

    @Test
    public void findLastVeranstaltungById(){
        // prepare test data
        VeranstaltungDO expectedVeranstaltung = getVeranstaltungDO();

        // configure mocks
        when(VeranstaltungComponent.findLastVeranstaltungById(anyLong())).thenReturn(expectedVeranstaltung);

        // call test method
        final VeranstaltungDTO actual = underTest.findLastVeranstaltungById(VERANSTALTUNG_ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expectedVeranstaltung.getVeranstaltungID());
        assertThat(actual.getLigaId()).isEqualTo(expectedVeranstaltung.getVeranstaltungLigaID());
        assertThat(actual.getSportjahr()).isEqualTo(expectedVeranstaltung.getVeranstaltungSportJahr());

        // verify invocations
        verify(VeranstaltungComponent).findLastVeranstaltungById(anyLong());

    }
}