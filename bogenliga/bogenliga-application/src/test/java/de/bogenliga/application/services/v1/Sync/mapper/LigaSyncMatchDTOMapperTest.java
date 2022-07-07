package de.bogenliga.application.services.v1.Sync.mapper;

import org.junit.Test;
import de.bogenliga.application.business.match.api.types.LigamatchDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncMatchDTOMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adrian Kempf
 */
public class LigaSyncMatchDTOMapperTest {
    protected static final Long MATCH_ID = 1L;
    private static final long version = 1234;
    private static final Long MATCH_WETTKAMPF_ID = 2L;
    protected static final Long MATCH_NR = 1L;
    protected static final Long MATCH_BEGEGNUNG = 1L;
    protected static final Long MATCH_SCHEIBENNUMMER = 3L;
    protected static final Long MATCH_MATCHPUNKTE = 6L;
    protected static final Long MATCH_SATZPUNKTE = 3L;
    protected static final Long MATCH_MANNSCHAFT_ID = 1L;
    protected static final String MATCH_MANNSCHAFT_NAME = "TSV_Grafenberg";
    protected static final String MATCH_NAME_GEGNER = "TSV Grafenberg Gegner";
    protected static final Long MATCH_ID_GEGNER = 2L;
    protected static final Long MATCH_SCHEIBENNUMMER_GEGNER = 4L;
    protected static final Long MATCH_NAECHSTE_MATCH_ID = 1L;
    protected static final Long MATCH_NAECHSTE_NAECHSTE_MATCH_ID = 1L;
    protected static final Long MATCH_STRAFPUNKTE_SATZ_1 = 1L;
    protected static final Long MATCH_STRAFPUNKTE_SATZ_2 = 2L;
    protected static final Long MATCH_STRAFPUNKTE_SATZ_3 = 3L;
    protected static final Long MATCH_STRAFPUNKTE_SATZ_4 = 4L;
    protected static final Long MATCH_STRAFPUNKTE_SATZ_5 = 5L;

    protected MatchDO getMatchDO() {
        return new MatchDO(
                MATCH_ID,
                MATCH_NR,
                MATCH_WETTKAMPF_ID,
                MATCH_MANNSCHAFT_ID,
                MATCH_BEGEGNUNG,
                MATCH_MATCHPUNKTE,
                MATCH_SCHEIBENNUMMER,
                MATCH_SATZPUNKTE,
                MATCH_STRAFPUNKTE_SATZ_1,
                MATCH_STRAFPUNKTE_SATZ_2,
                MATCH_STRAFPUNKTE_SATZ_3,
                MATCH_STRAFPUNKTE_SATZ_4,
                MATCH_STRAFPUNKTE_SATZ_5
        );
    }

    @Test
    public void fromMatchDOtoDTO() {
        MatchDO matchDO = getMatchDO();
        final LigaSyncMatchDTO actual = LigaSyncMatchDTOMapper.fromMatchDOtoDTO.apply(matchDO);
        assertThat(actual.getId()).isEqualTo(MATCH_ID);
        assertThat(actual.getWettkampfId()).isEqualTo(MATCH_WETTKAMPF_ID);
    }
}