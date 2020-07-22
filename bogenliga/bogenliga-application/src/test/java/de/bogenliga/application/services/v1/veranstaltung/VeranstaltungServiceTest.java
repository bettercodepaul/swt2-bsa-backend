package de.bogenliga.application.services.v1.veranstaltung;

import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.services.v1.veranstaltung.model.VeranstaltungDTO;
import de.bogenliga.application.services.v1.veranstaltung.service.VeranstaltungService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


import java.security.Principal;

import java.util.Collections;
import java.util.List;
import java.sql.Date;

import javax.naming.NoPermissionException;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VeranstaltungServiceTest {

    private static final long USER = 0;
    private static final long ID = 0;
    private static final long VERSION = 0;

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


   
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private VeranstaltungComponent VeranstaltungComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private VeranstaltungService underTest;

    @Captor
    private ArgumentCaptor<VeranstaltungDO> VeranstaltungDOArgumentCaptor;

    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static VeranstaltungBE VeranstaltungBE() {
        final VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltung_id(VERANSTALTUNG_ID);
        expectedBE.setVeranstaltung_liga_id(LIGAID);
        expectedBE.setVeranstaltung_ligaleiter_id(LIGALEITERID);
        expectedBE.setVeranstaltung_meldedeadline(MELDEDEADLINE);
        expectedBE.setVeranstaltung_name(VERANSTALTUNG_NAME);
        expectedBE.setVeranstaltung_sportjahr(SPORTJAHR);
        expectedBE.setVeranstaltung_wettkampftyp_id(WETTKAMPFTYP_ID);

        return expectedBE;
    }

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
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

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
        /*
        assertThat(actual.getWettkampfTypId()).isEqualTo(VeranstaltungDO.getVeranstaltungWettkampftypID());
        assertThat(actual.getSportjahr()).isEqualTo(VeranstaltungDO.getVeranstaltungSportJahr());
        assertThat(actual.getMeldeDeadline()).isEqualTo(VeranstaltungDO.getVeranstaltungMeldeDeadline());
        assertThat(actual.getLigaleiterID()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaleiterID());
        assertThat(actual.getLigaID()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaID());
        assertThat(actual.getLigaleiterEmail()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaleiterEmail());
        assertThat(actual.getWettkampftypName()).isEqualTo(VeranstaltungDO.getVeranstaltungWettkampftypName());;
        assertThat(actual.getLigaName()).isEqualTo(VeranstaltungDO.getVeranstaltungLigaName());
        */



        // verify invocations
        verify(VeranstaltungComponent).findById(ID);
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
        /*
        assertThat(actual.getWettkampfTypId()).isEqualTo(input.getWettkampfTypId());
        assertThat(actual.getSportjahr()).isEqualTo(input.getSportjahr());
        assertThat(actual.getMeldeDeadline()).isEqualTo(input.getMeldeDeadline());
        assertThat(actual.getLigaleiterID()).isEqualTo(input.getLigaleiterID());
        assertThat(actual.getLigaID()).isEqualTo(input.getLigaID());

        assertThat(actual.getLigaleiterEmail()).isEqualTo(input.getLigaleiterEmail());
        assertThat(actual.getWettkampftypName()).isEqualTo(input.getWettkampftypName());;
        assertThat(actual.getLigaName()).isEqualTo(input.getLigaName());
         */

        // verify invocations
        verify(VeranstaltungComponent).create(VeranstaltungDOArgumentCaptor.capture(), anyLong());

        final VeranstaltungDO createdVeranstaltung = VeranstaltungDOArgumentCaptor.getValue();

        assertThat(createdVeranstaltung).isNotNull();
        assertThat(createdVeranstaltung.getVeranstaltungID()).isEqualTo(input.getId());
        assertThat(createdVeranstaltung.getVeranstaltungName()).isEqualTo(input.getName());
        /*
        assertThat(createdVeranstaltung.getVeranstaltungWettkampftypID()).isEqualTo(input.getWettkampftypName());
        assertThat(createdVeranstaltung.getVeranstaltungSportJahr()).isEqualTo(input.getSportjahr());
        assertThat(createdVeranstaltung.getVeranstaltungMeldeDeadline()).isEqualTo(input.getMeldeDeadline());
        assertThat(createdVeranstaltung.getVeranstaltungLigaleiterID()).isEqualTo(input.getLigaleiterID());
        assertThat(createdVeranstaltung.getVeranstaltungLigaID()).isEqualTo(input.getLigaID());

        assertThat(createdVeranstaltung.getVeranstaltungLigaleiterEmail()).isEqualTo(input.getLigaleiterEmail());
        assertThat(createdVeranstaltung.getVeranstaltungWettkampftypName()).isEqualTo(input.getWettkampftypName());
        assertThat(createdVeranstaltung.getVeranstaltungLigaName()).isEqualTo(input.getLigaName());

         */

    }

    @Test
    public void update() {
        // prepare test data
        final VeranstaltungDTO input = getVeranstaltungDTO();

        final VeranstaltungDO expected = getVeranstaltungDO();

        // configure mocks
        when(VeranstaltungComponent.update(any(), anyLong())).thenReturn(expected);

        try {
            // call test method
            final VeranstaltungDTO actual = underTest.update(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(input.getId());
            assertThat(actual.getName()).isEqualTo(input.getName());
            /*
            assertThat(actual.getWettkampfTypId()).isEqualTo(input.getWettkampfTypId());
            assertThat(actual.getSportjahr()).isEqualTo(input.getSportjahr());
            assertThat(actual.getMeldeDeadline()).isEqualTo(input.getMeldeDeadline());
            assertThat(actual.getLigaleiterID()).isEqualTo(input.getLigaleiterID());
            assertThat(actual.getLigaID()).isEqualTo(input.getLigaID());
            assertThat(actual.getLigaleiterEmail()).isEqualTo(input.getLigaleiterEmail());
            assertThat(actual.getWettkampftypName()).isEqualTo(input.getWettkampftypName());;
            assertThat(actual.getLigaName()).isEqualTo(input.getLigaName());

             */


            // verify invocations
            verify(VeranstaltungComponent).update(VeranstaltungDOArgumentCaptor.capture(), anyLong());

            final VeranstaltungDO updatedVeranstaltung = VeranstaltungDOArgumentCaptor.getValue();

            assertThat(updatedVeranstaltung).isNotNull();
            assertThat(updatedVeranstaltung.getVeranstaltungID()).isEqualTo(input.getId());
            assertThat(updatedVeranstaltung.getVeranstaltungName()).isEqualTo(input.getName());
            /*
            assertThat(updatedVeranstaltung.getVeranstaltungWettkampftypID()).isEqualTo(input.getWettkampftypName());
            assertThat(updatedVeranstaltung.getVeranstaltungSportJahr()).isEqualTo(input.getSportjahr());
            assertThat(updatedVeranstaltung.getVeranstaltungMeldeDeadline()).isEqualTo(input.getMeldeDeadline());
            assertThat(updatedVeranstaltung.getVeranstaltungLigaleiterID()).isEqualTo(input.getLigaleiterID());
            assertThat(updatedVeranstaltung.getVeranstaltungLigaID()).isEqualTo(input.getLigaID());
            assertThat(updatedVeranstaltung.getVeranstaltungLigaleiterEmail()).isEqualTo(input.getLigaleiterEmail());
            assertThat(updatedVeranstaltung.getVeranstaltungWettkampftypName()).isEqualTo(input.getWettkampftypName());
            assertThat(updatedVeranstaltung.getVeranstaltungLigaName()).isEqualTo(input.getLigaName());

             */
        }catch (NoPermissionException e) {
        }

    }

    @Test
    public void delete() {
        // prepare test data
        final VeranstaltungDO expected = getVeranstaltungDO();

        // configure mocks

        // call test method
        underTest.delete(ID, principal);

        // assert result

        // verify invocations
        verify(VeranstaltungComponent).delete(VeranstaltungDOArgumentCaptor.capture(), anyLong());

        final VeranstaltungDO deletedVeranstaltung = VeranstaltungDOArgumentCaptor.getValue();

        assertThat(deletedVeranstaltung).isNotNull();
        assertThat(deletedVeranstaltung.getVeranstaltungID()).isEqualTo(expected.getVeranstaltungID());
        assertThat(deletedVeranstaltung.getVeranstaltungName()).isNullOrEmpty();
    }
}