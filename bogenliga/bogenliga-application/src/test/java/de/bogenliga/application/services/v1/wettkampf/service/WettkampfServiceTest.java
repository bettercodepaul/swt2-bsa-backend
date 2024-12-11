package de.bogenliga.application.services.v1.wettkampf.service;

import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;


/**
 * Test class for Wettkampf Service
 *
 * @Author Daniel Schott daniel.schott@student.reutlingen-university.de
 */

public class WettkampfServiceTest {
    private static final long user_Id = 13;

    private static final long wettkampf_Id = 322;
    private static final long wettkampf_Veranstaltung_Id = 0;
    private static final Date wettkampf_Datum = new Date(20190521L);
    private static final String wettkampf_Strasse = "Reutlingerstr. 6";
    private static final String wettkampf_Plz = "72764";
    private static final String wettkampf_Ortsname = "Reutlingen";
    private static final String wettkampf_Ortsinfo = "Im Keller";
    private static final String wettkampf_Beginn = "8:00";
    private static final long wettkampf_Tag = 8;
    private static final long wettkampf_Disziplin_Id = 0;
    private static final long wettkampf_Wettkampftyp_Id = 1;
    private static final long mannschafts_id = 1;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private static final long version = 1234;
    private static final long wettkampfAusrichter = 8;


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


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static WettkampfBE getWettkampfBE() {
        final WettkampfBE expectedBE = new WettkampfBE();
        expectedBE.setDatum(wettkampf_Datum);
        expectedBE.setId(wettkampf_Id);
        expectedBE.setVeranstaltungsId(wettkampf_Veranstaltung_Id);
        expectedBE.setWettkampfBeginn(wettkampf_Beginn);
        expectedBE.setWettkampfDisziplinId(wettkampf_Disziplin_Id);
        expectedBE.setWettkampfStrasse(wettkampf_Strasse);
        expectedBE.setWettkampfPlz(wettkampf_Plz);
        expectedBE.setWettkampfOrtsname(wettkampf_Ortsname);
        expectedBE.setWettkampfOrtsinfo(wettkampf_Ortsinfo);
        expectedBE.setWettkampfTypId(wettkampf_Wettkampftyp_Id);
        expectedBE.setWettkampfTag(wettkampf_Tag);
        expectedBE.setWettkampfAusrichter(wettkampfAusrichter);

        return expectedBE;
    }


    public static WettkampfDO getWettkampfDO() {
        return new WettkampfDO(
                wettkampf_Id,
                wettkampf_Veranstaltung_Id,
                wettkampf_Datum,
                wettkampf_Strasse,
                wettkampf_Plz,
                wettkampf_Ortsname,
                wettkampf_Ortsinfo,
                wettkampf_Beginn,
                wettkampf_Tag,
                wettkampf_Disziplin_Id,
                wettkampf_Wettkampftyp_Id,
                created_At_Utc,
                user_Id,
                version,
                wettkampfAusrichter
        );
    }


    private static WettkampfDTO getWettkampfDTO() {
        return new WettkampfDTO(
                wettkampf_Id,
                wettkampf_Veranstaltung_Id,
                wettkampf_Datum,
                wettkampf_Strasse,
                wettkampf_Plz,
                wettkampf_Ortsname,
                wettkampf_Ortsinfo,
                wettkampf_Beginn,
                wettkampf_Tag,
                wettkampf_Disziplin_Id,
                wettkampf_Wettkampftyp_Id,
                version,
                wettkampfAusrichter

        );

    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(user_Id));
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
    public void findById() {
        // prepare test data
        final WettkampfDO wettkampfDO = getWettkampfDO();

        // configure mocks
        when(wettkampfComponent.findById(anyLong())).thenReturn(wettkampfDO);

        // call test method
        final WettkampfDTO actual = underTest.findById(wettkampf_Id);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(wettkampfDO.getId());

        // verify invocations
        verify(wettkampfComponent).findById(wettkampf_Id);
    }


    @Test
    public void findAllWettkaempfeByMannschaftsId() {
        // prepare test data
        final WettkampfDO wettkampfDO = getWettkampfDO();
        final List<WettkampfDO> wettkampfDOList = Collections.singletonList(wettkampfDO);

        // configure mocks
        when(wettkampfComponent.findAllWettkaempfeByMannschaftsId(anyLong())).thenReturn(wettkampfDOList);

        // call test method
        final List<WettkampfDTO> actual = underTest.findAllWettkaempfeByMannschaftsId(mannschafts_id);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final WettkampfDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(wettkampfDO.getId());

        // verify invocations
        verify(wettkampfComponent).findAllWettkaempfeByMannschaftsId(mannschafts_id);
    }


    @Test
    public void findAllByVeranstaltungId(){
        //prepare test data
        final WettkampfDO wettkampfDO = getWettkampfDO();
        final List<WettkampfDO> wettkampfDOList = Collections.singletonList(wettkampfDO);

        //configure mocks
        when(wettkampfComponent.findAllByVeranstaltungId(anyLong())).thenReturn(wettkampfDOList);

        //call test method
        final List<WettkampfDTO> actual = underTest.findAllByVeranstaltungId(wettkampf_Veranstaltung_Id);

        //assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final WettkampfDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getwettkampfVeranstaltungsId()).isEqualTo(wettkampfDO.getWettkampfVeranstaltungsId());

        // verify invocations
        verify(wettkampfComponent).findAllByVeranstaltungId(wettkampf_Veranstaltung_Id);
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
        underTest.delete(wettkampf_Id, principal);

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

}
