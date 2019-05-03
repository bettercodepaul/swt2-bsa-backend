package de.bogenliga.application.services.v1.match.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.Passe.api.PasseComponent;
import de.bogenliga.application.business.Passe.api.types.PasseDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.services.v1.match.mapper.MatchDTOMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PasseComponent passeComponent;

    @Mock
    private Principal principal;

    @Mock
    private MatchComponent matchComponent;

    @InjectMocks
    private MatchService underTest;


    protected static final Long MATCH_ID = 1L;
    protected static final Long MATCH_NR = 1L;
    protected static final Long MATCH_BEGEGNUNG = 1L;
    protected static final Long MATCH_WETTKAMPF_ID = 1L;
    protected static final Long MATCH_MANNSCHAFT_ID = 1L;
    protected static final Long MATCH_SCHEIBENNUMMER = 3L;
    protected static final Long MATCH_MATCHPUNKTE = 6L;
    protected static final Long MATCH_SATZPUNKTE = 3L;
    protected static final Long CURRENT_USER_ID = 1L;


    private static final Long PASSE_ID_1 = 1L;
    private static final Long PASSE_ID_2 = 2L;
    private static final Long PASSE_LFDR_NR = 2L;
    private static final Long PASSE_DSB_MITGLIED_ID = 1L;
    private static final Integer PASSE_PFEIL_1 = 10;
    private static final Integer PASSE_PFEIL_2 = 5;
    private static final Long VERSION = 2L;


    protected MatchDO getMatchDO() {
        return new MatchDO(
                MATCH_ID,
                MATCH_NR,
                MATCH_BEGEGNUNG,
                MATCH_MANNSCHAFT_ID,
                MATCH_WETTKAMPF_ID,
                MATCH_MATCHPUNKTE,
                MATCH_SCHEIBENNUMMER,
                MATCH_SATZPUNKTE
        );
    }


    protected PasseDO getPasseDO(Long id) {
        return new PasseDO(
                id,
                MATCH_MANNSCHAFT_ID,
                MATCH_WETTKAMPF_ID,
                MATCH_NR,
                PASSE_LFDR_NR,
                PASSE_DSB_MITGLIED_ID,
                PASSE_PFEIL_1,
                PASSE_PFEIL_2,
                0,0,0,0
        );
    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(CURRENT_USER_ID));
    }


    @Test
    public void findById() {
        MatchDO matchDO = getMatchDO();
        when(matchComponent.findById(anyLong())).thenReturn(matchDO);
        final MatchDTO actual = underTest.findById(MATCH_ID);
        assertThat(actual).isNotNull();
        MatchService.checkPreconditions(actual);
    }


    @Test
    public void findMatchesByIds() {
        MatchDO matchDO1 = getMatchDO();
        when(matchComponent.findById(anyLong())).thenReturn(matchDO1);
        final List<MatchDTO> actual = underTest.findMatchesByIds(MATCH_ID, MATCH_ID);
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        MatchService.checkPreconditions(actual.get(0));
    }


    @Test
    public void saveMatches() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);
        final List<MatchDTO> actual = underTest.saveMatches(matchDTO, matchDTO, principal);
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        MatchService.checkPreconditions(actual.get(0));
    }


    @Test
    public void saveMatches_WithPasseUpdate() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);
        List<PasseDO> passeDOS = new ArrayList<>();

        PasseDO passe1 = getPasseDO(PASSE_ID_1);
        PasseDO passe2 = getPasseDO(PASSE_ID_2);
        // change lfdnr of passe2 to make them distinguishable
        passe2.setPasseLfdnr(PASSE_LFDR_NR + 1);

        passeDOS.add(passe1);
        passeDOS.add(passe2);
        List<PasseDTO> passeDTOS = passeDOS.stream().map(PasseDTOMapper.toDTO).collect(Collectors.toList());

        matchDTO.setPassen(passeDTOS);

        when(passeComponent.findByPk(anyLong(), anyLong(), anyLong(), eq(PASSE_LFDR_NR), anyLong())).thenReturn(passe1);
        when(passeComponent.findByPk(anyLong(), anyLong(), anyLong(), eq(PASSE_LFDR_NR + 1), anyLong())).thenReturn(passe2);
        final List<MatchDTO> actual = underTest.saveMatches(matchDTO, matchDTO, principal);
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        MatchService.checkPreconditions(actual.get(0));
        MatchService.checkPreconditions(actual.get(1));

        // make sure update was called twice per passed DTO
        verify(passeComponent, times(4)).update(any(PasseDO.class), eq(CURRENT_USER_ID));
    }


    @Test
    public void saveMatches_WithPasseCreate() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);
        List<PasseDO> passeDOS = new ArrayList<>();

        PasseDO passe1 = getPasseDO(PASSE_ID_1);
        PasseDO passe2 = getPasseDO(PASSE_ID_2);
        // change lfdnr of passe2 to make them distinguishable
        passe2.setPasseLfdnr(PASSE_LFDR_NR + 1);

        passeDOS.add(passe1);
        passeDOS.add(passe2);
        List<PasseDTO> passeDTOS = passeDOS.stream().map(PasseDTOMapper.toDTO).collect(Collectors.toList());

        matchDTO.setPassen(passeDTOS);

        // passe don't exist yet in DB, return null...
        when(passeComponent.findByPk(anyLong(), anyLong(), anyLong(), eq(PASSE_LFDR_NR), anyLong())).thenReturn(null);
        when(passeComponent.findByPk(anyLong(), anyLong(), anyLong(), eq(PASSE_LFDR_NR + 1), anyLong())).thenReturn(null);
        final List<MatchDTO> actual = underTest.saveMatches(matchDTO, matchDTO, principal);
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        MatchService.checkPreconditions(actual.get(0));
        MatchService.checkPreconditions(actual.get(1));

        // make sure create was called twice per passed DTO
        verify(passeComponent, times(4)).create(any(PasseDO.class), eq(CURRENT_USER_ID));
    }


    @Test
    public void create() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);
        when(matchComponent.create(any(MatchDO.class), anyLong())).thenReturn(matchDO1);
        final MatchDTO actual = underTest.create(matchDTO, principal);
        assertThat(actual).isNotNull();
        MatchService.checkPreconditions(actual);
    }


    @Test
    public void update() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);
        when(matchComponent.update(any(MatchDO.class), anyLong())).thenReturn(matchDO1);
        final MatchDTO actual = underTest.update(matchDTO, principal);
        assertThat(actual).isNotNull();
        MatchService.checkPreconditions(actual);
    }
}