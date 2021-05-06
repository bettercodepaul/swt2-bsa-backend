package de.bogenliga.application.services.v1.vereine.service;

import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.services.v1.vereine.model.VereineDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
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
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import javax.naming.NoPermissionException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class VereineServiceTest {

    private static final long USER = 0;
    private static final long ID = 0;
    private static final long VERSION = 0;

    private static final String VEREIN_NAME = "";
    private static final long VEREIN_ID = 0;
    private static final String VEREIN_DSB_IDENTIFIER = "";
    private static final long REGION_ID = 0;
    private static final String REGION_NAME = "";
    private static final String VEREIN_WEBSITE = "";
    private static final String VEREIN_DESCRIPTION = "";
    private static final String VEREIN_ICON = "";
    private static final OffsetDateTime VEREIN_OFFSETDATETIME = null;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private VereinComponent vereinComponent;

    @Mock
    private RequiresOnePermissionAspect requiresOnePermissionAspect;

    @Mock
    private Principal principal;

    @InjectMocks
    private VereineService underTest;

    @Captor
    private ArgumentCaptor<VereinDO> vereinDOArgumentCaptor;


    public static VereinDO getVereinDO() {
        return new VereinDO(VEREIN_ID,
                VEREIN_NAME,
                VEREIN_DSB_IDENTIFIER,
                REGION_ID,
                REGION_NAME,
                VEREIN_WEBSITE,
                VEREIN_DESCRIPTION,
                VEREIN_ICON,
                VEREIN_OFFSETDATETIME,
                USER,
                VEREIN_OFFSETDATETIME,
                USER,
                VERSION);
    }

    public static VereineDTO getVereineDTO() {
        final VereineDTO vereineDTO = new VereineDTO();
        vereineDTO.setId(VEREIN_ID);
        vereineDTO.setCreatedAtUtc(VEREIN_OFFSETDATETIME);
        vereineDTO.setIdentifier(VEREIN_DSB_IDENTIFIER);
        vereineDTO.setName(VEREIN_NAME);
        vereineDTO.setRegionId(REGION_ID);
        vereineDTO.setVersion(VERSION);
        vereineDTO.setCreatedByUserId(USER);
        vereineDTO.setRegionName(REGION_NAME);
        return vereineDTO;
    }



    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }

    @Test
    public void findAll() {
        // prepare test data
        final VereinDO vereinDO = getVereinDO();
        final List<VereinDO> VereinDOList = Collections.singletonList(vereinDO);

        // configure mocks
        when(vereinComponent.findAll()).thenReturn(VereinDOList);

        // call test method
        final List<VereineDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        final VereineDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(vereinDO.getId());
        assertThat(actualDTO.getName()).isEqualTo(vereinDO.getName());

        // verify invocations
        verify(vereinComponent).findAll();
    }


    @Test
    public void findById() {
        // prepare test data
        final VereinDO vereinDO = getVereinDO();

        // configure mocks
        when(vereinComponent.findById(anyLong())).thenReturn(vereinDO);

        // call test method
        final VereineDTO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(vereinDO.getId());
        assertThat(actual.getName()).isEqualTo(vereinDO.getName());

        // verify invocations
        verify(vereinComponent).findById(ID);
    }


    @Test
    public void create() {
        // prepare test data
        final VereineDTO input = getVereineDTO();
        final VereinDO expected = getVereinDO();

        // configure mocks
        when(vereinComponent.create(any(), anyLong())).thenReturn(expected);

        // call test method
        final VereineDTO actual = underTest.create(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getName()).isEqualTo(input.getName());

        // verify invocations
        verify(vereinComponent).create(vereinDOArgumentCaptor.capture(), anyLong());

        final VereinDO createdDsbMitglied = vereinDOArgumentCaptor.getValue();

        assertThat(createdDsbMitglied).isNotNull();
        assertThat(createdDsbMitglied.getId()).isEqualTo(input.getId());
        assertThat(createdDsbMitglied.getName()).isEqualTo(input.getName());
    }


    @Test
    public void update() {
        // prepare test data
        final VereineDTO input = getVereineDTO();
        final VereinDO expected = getVereinDO();

        // configure mocks
        when(vereinComponent.update(any(), anyLong())).thenReturn(expected);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);

        try {
            // call test method
            final VereineDTO actual = underTest.update(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(input.getId());
            assertThat(actual.getName()).isEqualTo(input.getName());

            // verify invocations
            verify(vereinComponent).update(vereinDOArgumentCaptor.capture(), anyLong());

            final VereinDO updatedDsbMitglied = vereinDOArgumentCaptor.getValue();

            assertThat(updatedDsbMitglied).isNotNull();
            assertThat(updatedDsbMitglied.getId()).isEqualTo(input.getId());
            assertThat(updatedDsbMitglied.getName()).isEqualTo(input.getName());

        }catch (NoPermissionException e) { }
    }


    @Test
    public void updateNoPermission() {
        // prepare test data
        final VereineDTO input = getVereineDTO();
        final VereinDO expected = getVereinDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(any(), anyLong())).thenReturn(false);
        when(vereinComponent.update(any(), anyLong())).thenReturn(expected);

        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(()-> underTest.update(input, principal));
    }


    @Test
    public void delete() {
        // prepare test data
        final VereinDO expected = getVereinDO();

        // call test method
        underTest.delete(ID, principal);

        // verify invocations
        verify(vereinComponent).delete(vereinDOArgumentCaptor.capture(), anyLong());

        final VereinDO deletedDsbMitglied = vereinDOArgumentCaptor.getValue();

        assertThat(deletedDsbMitglied).isNotNull();
        assertThat(deletedDsbMitglied.getId()).isEqualTo(expected.getId());
        assertThat(deletedDsbMitglied.getName()).isNullOrEmpty();
    }
}