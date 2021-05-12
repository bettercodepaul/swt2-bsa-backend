package de.bogenliga.application.services.v1.passe.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.services.v1.match.service.MatchService;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;

import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Jonas Winkler, HSRT MKI SS20 - SWT2
 */
public class PasseServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PasseComponent passeComponent;

    @Mock
    private MatchService matchService;

    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private PasseService underTest;

    protected static final Long MATCH_ID = 1L;
    protected static final Long MATCH_NR = 1L;
    protected static final Long MATCH_WETTKAMPF_ID = 1L;
    protected static final Long MATCH_MANNSCHAFT_ID = 1L;
    protected static final Long CURRENT_USER_ID = 1L;

    private static final Long PASSE_ID = 1L;
    private static final Long PASSE_LFDR_NR = 2L;
    private static final Integer PASSE_PFEIL_1 = 10;
    private static final Integer PASSE_PFEIL_2 = 8;
    private static final Long PASSE_DSB_MITGLIED_ID = 123L;


    private static final Long MM_ID_1 = 1L;
    private static final Long MM_ID_2 = 2L;
    private static final Long MM_ID_3 = 3L;
    private static final Long MM_mannschaftsId = 1L;
    private static final Long MM_dsbMitgliedId = 100L;
    private static final Integer MM_dsbMitgliedEingesetzt = 1;
    private static final String MM_dsbMitgliedVorname = "Foo";
    private static final String MM_dsbMitgliedNachname = "Bar";
    private static final Integer MM_rueckennummer_1 = 5;





    protected PasseDO getPasseDO() {
        return new PasseDO(
                PASSE_ID,
                MATCH_MANNSCHAFT_ID,
                MATCH_WETTKAMPF_ID,
                MATCH_NR,
                MATCH_ID,
                PASSE_LFDR_NR,
                PASSE_DSB_MITGLIED_ID,
                PASSE_PFEIL_1,
                PASSE_PFEIL_2,
                null, null, null, null
        );
    }


    protected MannschaftsmitgliedDO getMMDO(Long id, Long rueckennummer) {
        return new MannschaftsmitgliedDO(
                id,
                MM_mannschaftsId,
                MM_dsbMitgliedId,
                MM_dsbMitgliedEingesetzt,
                MM_dsbMitgliedVorname,
                MM_dsbMitgliedNachname,
                rueckennummer
        );
    }


    protected List<MannschaftsmitgliedDO> getMannschaftsMitglieder() {
        List<MannschaftsmitgliedDO> mmdos = new ArrayList<>();
        mmdos.add(getMMDO(MM_ID_1, 5L));
        mmdos.add(getMMDO(MM_ID_2, 6L));
        mmdos.add(getMMDO(MM_ID_3, 7L));
        return mmdos;
    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(CURRENT_USER_ID));
    }


    @Test
    public void findAll(){
        final PasseDO passeDo = getPasseDO();
        final List<PasseDO> passeDoList = Collections.singletonList(passeDo);
        //configure Mocks
        when(passeComponent.findAll()).thenReturn(passeDoList);
        // call test method
        final List<PasseDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final PasseDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(passeDo.getId());

        // verify invocations
        verify(passeComponent).findAll();

    }

    @Test
    public void testFindById() {
        final PasseDO passeDo = getPasseDO();
        //configure Mocks
        when(passeComponent.findById(anyLong())).thenReturn(passeDo);
        // call test method
        final PasseDTO actual = underTest.findById(1L);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(passeDo.getId());

        // verify invocations
        verify(passeComponent).findById(anyLong());

    }

    @Test
    public void testFindByMatchId() {
        final PasseDO passeDo = getPasseDO();
        final List<PasseDO> passeDoList = Collections.singletonList(passeDo);
        //configure Mocks
        when(passeComponent.findByMatchId(anyLong())).thenReturn(passeDoList);
        // call test method
        final List<PasseDTO> actual = underTest.findByMatchId(1L);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final PasseDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(passeDo.getId());

        // verify invocations
        verify(passeComponent).findByMatchId(anyLong());
    }

    @Test
    public void testFindByWettkampfId() {
        final PasseDO passeDo = getPasseDO();
        final List<PasseDO> passeDoList = Collections.singletonList(passeDo);
        //configure Mocks
        when(passeComponent.findByWettkampfId(anyLong())).thenReturn(passeDoList);
        // call test method
        final List<PasseDTO> actual = underTest.findByWettkampfId(1L);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final PasseDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(passeDo.getId());

        // verify invocations
        verify(passeComponent).findByWettkampfId(anyLong());
    }

    @Test
    public void testUpdate() {
        final PasseDO passeDo = getPasseDO();

        //configure Mocks
        when(passeComponent.update(any(PasseDO.class), anyLong())).thenReturn(passeDo);
        // call test method
        final PasseDTO actual = underTest.update(PasseDTOMapper.toDTO.apply(passeDo), principal);



        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(passeDo.getId());

        // verify invocations
        verify(passeComponent).update(any(PasseDO.class), anyLong());
    }

    @Test
    public void testFindAllByWettkampfIdAndDsbMitgliedId() {
        final PasseDO passeDo = getPasseDO();
        final List<PasseDO> passeDoList = Collections.singletonList(passeDo);
        //configure Mocks
        when(passeComponent.findByWettkampfIdAndMitgliedId(anyLong(), anyLong())).thenReturn(passeDoList);
        // call test method
        final List<PasseDTO> actual = underTest.findAllByWettkampfIdAndDsbMitgliedId(1L, 1L);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final PasseDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(passeDo.getId());

        // verify invocations
        verify(passeComponent).findByWettkampfIdAndMitgliedId(anyLong(), anyLong());

    }

    @Test
    public void testCreate() {
        final PasseDO passeDo = getPasseDO();
        final PasseDTO passeDto = PasseDTOMapper.toDTO.apply(passeDo);
        passeDto.setRueckennummer(MM_rueckennummer_1);


        final List<MannschaftsmitgliedDO> manschaftsmitgliederDOList = getMannschaftsMitglieder();

        //configure Mocks
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(anyLong())).thenReturn(manschaftsmitgliederDOList);
        when(passeComponent.create(any(PasseDO.class), anyLong())).thenReturn(passeDo);
        // call test method
        final PasseDTO actual = underTest.create(passeDto, principal);



        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(passeDo.getId());
        assertThat(actual.getDsbMitgliedId()).isEqualTo( PASSE_DSB_MITGLIED_ID);

        // verify invocations
        verify(passeComponent).create(any(PasseDO.class), anyLong());
    }
}
