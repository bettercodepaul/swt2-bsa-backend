package de.bogenliga.application.services.v1.wettkampf.service;

import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelAdminComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.VeranstaltungWettkampfDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
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
import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.NoPermissionException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import de.bogenliga.application.business.configuration.api.ConfigurationComponent;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
import de.bogenliga.application.services.v1.wettkampf.model.VeranstaltungWettkampfDTO;

/**
 * Test class for Wettkampf Service
 *
 * @Author Daniel Schott daniel.schott@student.reutlingen-university.de
 */

public class WettkampfServiceTest {
    private static final long USER_ID = 13;

    private static final long WETTKAMPF_ID = 322;
    private static final long WETTKAMPF_VERANSTALTUNG_ID = 0;
    private static final Date WETTKAMPF_DATUM = new Date(20190521L);
    private static final String WETTKAMPF_STRASSE = "Reutlingerstr. 6";
    private static final String WETTKAMPF_PLZ = "72764";
    private static final String WETTKAMPF_ORTSNAME = "Reutlingen";
    private static final String WETTKAMPF_ORTSINFO = "Im Keller";
    private static final String WETTKAMPF_BEGINN = "8:00";
    private static final long WETTKAMPF_TAG = 8;
    private static final long WETTKAMPF_DISZIPLIN_ID = 0;
    private static final long WETTKAMPF_WETTKAMPFTYP_ID = 1;
    private static final long MANNSCHAFTS_ID = 1;
    private static final OffsetDateTime CREATED_AT_UTC = OffsetDateTime.now();
    private static final long VERSION = 1234;
    private static final long WETTKAMPF_AUSRICHTER = 8;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private WettkampfComponent wettkampfComponent;

    @Mock
    private RequiresOnePermissionAspect requiresOnePermissionAspect;


    @Mock
    private Principal principal;

    @InjectMocks
    private WettkampfService underTest;

    @Captor
    private ArgumentCaptor<WettkampfDO> wettkampfDOArgumentCaptor;

    @Mock
    private TabletSchusszettelAdminComponent tabletSchusszettelComponent;

    @Mock
    private ConfigurationComponent configurationComponent;

    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static WettkampfBE getWettkampfBE() {
        final WettkampfBE expectedBE = new WettkampfBE();
        expectedBE.setDatum(WETTKAMPF_DATUM);
        expectedBE.setId(WETTKAMPF_ID);
        expectedBE.setVeranstaltungsId(WETTKAMPF_VERANSTALTUNG_ID);
        expectedBE.setWettkampfBeginn(WETTKAMPF_BEGINN);
        expectedBE.setWettkampfDisziplinId(WETTKAMPF_DISZIPLIN_ID);
        expectedBE.setWettkampfStrasse(WETTKAMPF_STRASSE);
        expectedBE.setWettkampfPlz(WETTKAMPF_PLZ);
        expectedBE.setWettkampfOrtsname(WETTKAMPF_ORTSNAME);
        expectedBE.setWettkampfOrtsinfo(WETTKAMPF_ORTSINFO);
        expectedBE.setWettkampfTypId(WETTKAMPF_WETTKAMPFTYP_ID);
        expectedBE.setWettkampfTag(WETTKAMPF_TAG);
        expectedBE.setWettkampfAusrichter(WETTKAMPF_AUSRICHTER);

        return expectedBE;
    }


    public static WettkampfDO getWettkampfDO() {
        return new WettkampfDO(
                WETTKAMPF_ID,
                WETTKAMPF_VERANSTALTUNG_ID,
                WETTKAMPF_DATUM,
                WETTKAMPF_STRASSE,
                WETTKAMPF_PLZ,
                WETTKAMPF_ORTSNAME,
                WETTKAMPF_ORTSINFO,
                WETTKAMPF_BEGINN,
                WETTKAMPF_TAG,
                WETTKAMPF_DISZIPLIN_ID,
                WETTKAMPF_WETTKAMPFTYP_ID,
                CREATED_AT_UTC,
                USER_ID,
                VERSION,
                WETTKAMPF_AUSRICHTER
        );
    }


    private static WettkampfDTO getWettkampfDTO() {
        return new WettkampfDTO(
                WETTKAMPF_ID,
                WETTKAMPF_VERANSTALTUNG_ID,
                WETTKAMPF_DATUM,
                WETTKAMPF_STRASSE,
                WETTKAMPF_PLZ,
                WETTKAMPF_ORTSNAME,
                WETTKAMPF_ORTSINFO,
                WETTKAMPF_BEGINN,
                WETTKAMPF_TAG,
                WETTKAMPF_DISZIPLIN_ID,
                WETTKAMPF_WETTKAMPFTYP_ID,
                VERSION,
                WETTKAMPF_AUSRICHTER

        );

    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER_ID));
    }


    @Test
    public void findAll() {

        // prepare test data
        final WettkampfDO wettkampfDO = getWettkampfDO();
        final List<WettkampfDO> wettkampfDOList = Collections.singletonList(wettkampfDO);

        // configure mocks
        when(wettkampfComponent.findAll()).thenReturn(wettkampfDOList);

        // call test method
        final List<WettkampfDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final WettkampfDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(wettkampfDO.getId());

        // verify invocations
        verify(wettkampfComponent).findAll();

    }
    @Test
    public void findFutureSix() {

        // prepare test data
        final WettkampfDO wettkampfDO = getWettkampfDO();
        final List<WettkampfDO> wettkampfDOList = Collections.singletonList(wettkampfDO);

        // configure mocks
        when(wettkampfComponent.findFutureSix()).thenReturn(wettkampfDOList);

        // call test method
        final List<WettkampfDTO> actual = underTest.findFutureSix();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final WettkampfDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(wettkampfDO.getId());

        // verify invocations
        verify(wettkampfComponent).findFutureSix();

    }


    @Test
    public void findById() {
        // prepare test data
        final WettkampfDO wettkampfDO = getWettkampfDO();

        // configure mocks
        when(wettkampfComponent.findById(anyLong())).thenReturn(wettkampfDO);

        // call test method
        final WettkampfDTO actual = underTest.findById(WETTKAMPF_ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(wettkampfDO.getId());

        // verify invocations
        verify(wettkampfComponent).findById(WETTKAMPF_ID);
    }


    @Test
    public void findAllWettkaempfeByMannschaftsId() {
        // prepare test data
        final WettkampfDO wettkampfDO = getWettkampfDO();
        final List<WettkampfDO> wettkampfDOList = Collections.singletonList(wettkampfDO);

        // configure mocks
        when(wettkampfComponent.findAllWettkaempfeByMannschaftsId(anyLong())).thenReturn(wettkampfDOList);

        // call test method
        final List<WettkampfDTO> actual = underTest.findAllWettkaempfeByMannschaftsId(MANNSCHAFTS_ID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final WettkampfDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(wettkampfDO.getId());

        // verify invocations
        verify(wettkampfComponent).findAllWettkaempfeByMannschaftsId(MANNSCHAFTS_ID);
    }


    @Test
    public void findAllByVeranstaltungId(){
        //prepare test data
        final WettkampfDO wettkampfDO = getWettkampfDO();
        final List<WettkampfDO> wettkampfDOList = Collections.singletonList(wettkampfDO);

        //configure mocks
        when(wettkampfComponent.findAllByVeranstaltungId(anyLong())).thenReturn(wettkampfDOList);

        //call test method
        final List<WettkampfDTO> actual = underTest.findAllByVeranstaltungId(WETTKAMPF_VERANSTALTUNG_ID);

        //assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final WettkampfDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getwettkampfVeranstaltungsId()).isEqualTo(wettkampfDO.getWettkampfVeranstaltungsId());

        // verify invocations
        verify(wettkampfComponent).findAllByVeranstaltungId(WETTKAMPF_VERANSTALTUNG_ID);
    }


    @Test
    public void create() {
        // prepare test data
        final WettkampfDTO input = getWettkampfDTO();

        final WettkampfDO expected = getWettkampfDO();

        // configure mocks
        when(wettkampfComponent.create(any(), anyLong())).thenReturn(expected);

        // call test method
        final WettkampfDTO actual = underTest.create(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());

        // verify invocations
        verify(wettkampfComponent).create(wettkampfDOArgumentCaptor.capture(), anyLong());

        final WettkampfDO createdDsbMitglied = wettkampfDOArgumentCaptor.getValue();

        assertThat(createdDsbMitglied).isNotNull();
        assertThat(createdDsbMitglied.getId()).isEqualTo(input.getId());
    }


    @Test
    public void update() {
        // prepare test data
        final WettkampfDTO input = getWettkampfDTO();

        final WettkampfDO expected = getWettkampfDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(wettkampfComponent.findById(anyLong())).thenReturn(expected);
        when(wettkampfComponent.update(any(), anyLong())).thenReturn(expected);

        try {
            // call test method
            final WettkampfDTO actual = underTest.update(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(input.getId());

            // verify invocations
            verify(wettkampfComponent).update(wettkampfDOArgumentCaptor.capture(), anyLong());

            final WettkampfDO updatedWettkampf = wettkampfDOArgumentCaptor.getValue();

            assertThat(updatedWettkampf).isNotNull();
            assertThat(updatedWettkampf.getId()).isEqualTo(input.getId());

        } catch (NoPermissionException e) {
        }
    }
    @Test
    public void updateNoPermission() {
        // prepare test data
        final WettkampfDTO input = getWettkampfDTO();

        final WettkampfDO expected = getWettkampfDO();

        // configure mocks
        when(wettkampfComponent.findById(anyLong())).thenReturn(expected);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionAusrichter(any(), anyLong())).thenReturn(false);

        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(()-> underTest.update(input, principal));

     }


    @Test
    public void delete() {
        // prepare test data
        final WettkampfDO expected = getWettkampfDO();

        // configure mocks

        // call test method
        underTest.delete(WETTKAMPF_ID, principal);

        // assert result

        // verify invocations
        verify(wettkampfComponent).delete(wettkampfDOArgumentCaptor.capture(), anyLong());

        final WettkampfDO deletedWettkampf = wettkampfDOArgumentCaptor.getValue();

        assertThat(deletedWettkampf).isNotNull();
        assertThat(deletedWettkampf.getId()).isEqualTo(expected.getId());

    }

    @Test
    public void getAllowedMitgliedForWettkampf() {
        // prepare test data
        List<Long> expected = new ArrayList<>();
        expected.add(77L);
        expected.add(120L);

        // configure mocks
        when(wettkampfComponent.getAllowedMitglieder(anyLong())).thenReturn(expected);

        // call test method
        List<Long> actual = underTest.getAllowedMitgliedForWettkampf(30L);

        // assert result
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getAllowedMitgliedForWettkampfByMannschaftIDs() {
        // prepare test data
        List<Long> expected = new ArrayList<>();
        expected.add(77L);

        // configure mocks
        when(wettkampfComponent.getAllowedMitglieder(anyLong(),anyLong(),anyLong())).thenReturn(expected);

        // call test method
        List<Long> actual = underTest.getAllowedMitgliedForWettkampf(30L,101L,102L);

        // assert result
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFindWettkaempfeWithVeranstaltungByLigaId() {
        List<VeranstaltungWettkampfDO> expected = new ArrayList<VeranstaltungWettkampfDO>();

        expected.add(new VeranstaltungWettkampfDO(
                1L,
                "2026-07-15",
                1L,
                "String wettkampfStrasse",
                "String wettkampfPlz",
                "String wettkampfOrtsname",
                "String wettkampfOrtsinfo",
                "String wettkampfBeginn",
                2000L,
                1999L,
                42L,
                69L,
                "String veranstaltungName",
                2026L,
                1002L,
                "ln"
        ));
        ConfigurationDO configDO = new ConfigurationDO(0L, "aktives-Sportjahr", "2026", "dddd",false);
        ConfigurationDO configDOerror = new ConfigurationDO(1L, "aktives-Sportjahr", "-2026", "dddd",false);
        when(configurationComponent.findByKey(anyString())).thenReturn(configDO);
        when(wettkampfComponent.findWettkaempfeWithVeranstaltungByLigaId(anyLong(),anyLong())).thenReturn(expected);
        List<VeranstaltungWettkampfDTO> actual = underTest.findWettkaempfeWithVeranstaltungByLigaId(10L);
        assertThat(actual.get(0).getWettkampfId()).isEqualTo(1L);
        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getWettkampfTag()).isEqualTo(1L);
        assertThat(actual.get(0).getWettkampfPlz()).isEqualTo("String wettkampfPlz");
        assertThat(actual.get(0).getVeranstaltungId()).isEqualTo(69L);
        assertThat(actual.get(0).getVeranstaltungSportjahr()).isEqualTo(2026L);
        assertThat(actual.get(0).getVeranstaltungLigaId()).isEqualTo(1002L);
        assertThat(actual.get(0).getWettkampfDisziplinId()).isEqualTo(2000L);
        assertThat(actual.get(0).getWettkampfDatum()).isEqualTo("2026-07-15");
        assertThat(actual.get(0).getWettkampfOrtsinfo()).isEqualTo("String wettkampfOrtsinfo");
        assertThat(actual.get(0).getWettkampfStrasse()).isEqualTo("String wettkampfStrasse");
        assertThat(actual.get(0).getWettkampfBeginn()).isEqualTo("String wettkampfBeginn");
        assertThat(actual.get(0).getWettkampfAusrichter()).isEqualTo(42L);
        assertThat(actual.get(0).getVeranstaltungLigaName()).isEqualTo("ln");
        // verify that calling with a negative liga id throws a BusinessException
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findWettkaempfeWithVeranstaltungByLigaId(-10L));
        // verify that calling with an invalid sportyear configuration throws a BusinessException
        when(configurationComponent.findByKey(anyString())).thenReturn(configDOerror);
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findWettkaempfeWithVeranstaltungByLigaId(10L));


}


}
