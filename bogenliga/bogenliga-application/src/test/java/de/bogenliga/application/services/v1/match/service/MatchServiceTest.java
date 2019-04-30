package de.bogenliga.application.services.v1.match.service;

import java.security.Principal;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.Passe.api.PasseComponent;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.services.v1.match.mapper.MatchDTOMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
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