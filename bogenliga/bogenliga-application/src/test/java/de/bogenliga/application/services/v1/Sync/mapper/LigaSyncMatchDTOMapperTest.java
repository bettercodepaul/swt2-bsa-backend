package de.bogenliga.application.services.v1.Sync.mapper;

import org.junit.Test;
import de.bogenliga.application.business.match.api.types.LigamatchDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
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

    protected MatchDO getMatchDOWithNull() {
        return new MatchDO(
                MATCH_ID,
                MATCH_NR,
                MATCH_WETTKAMPF_ID,
                MATCH_MANNSCHAFT_ID,
                MATCH_BEGEGNUNG,
                null,
                MATCH_SCHEIBENNUMMER,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private static LigaSyncMatchDTO getLigaSyncMatchDTO() {
        return new LigaSyncMatchDTO (
                MATCH_ID,
                version,
                MATCH_WETTKAMPF_ID,
                MATCH_NR.intValue(),
                MATCH_SCHEIBENNUMMER.intValue(),
                MATCH_MATCHPUNKTE,
                MATCH_SATZPUNKTE,
                MATCH_MANNSCHAFT_ID,
                MATCH_MANNSCHAFT_NAME,
                MATCH_NAME_GEGNER,
                MATCH_SCHEIBENNUMMER_GEGNER.intValue(),
                MATCH_ID_GEGNER,
                MATCH_NAECHSTE_MATCH_ID,
                MATCH_NAECHSTE_NAECHSTE_MATCH_ID,
                MATCH_STRAFPUNKTE_SATZ_1.intValue(),
                MATCH_STRAFPUNKTE_SATZ_2.intValue(),
                MATCH_STRAFPUNKTE_SATZ_3.intValue(),
                MATCH_STRAFPUNKTE_SATZ_4.intValue(),
                MATCH_STRAFPUNKTE_SATZ_5.intValue()
        );
    }

    private static LigaSyncMatchDTO getLigaSyncMatchDTOWithNull() {
        return new LigaSyncMatchDTO (
                MATCH_ID,
                version,
                MATCH_WETTKAMPF_ID,
                MATCH_NR.intValue(),
                MATCH_SCHEIBENNUMMER.intValue(),
                MATCH_MATCHPUNKTE,
                MATCH_SATZPUNKTE,
                MATCH_MANNSCHAFT_ID,
                MATCH_MANNSCHAFT_NAME,
                MATCH_NAME_GEGNER,
                MATCH_SCHEIBENNUMMER_GEGNER.intValue(),
                MATCH_ID_GEGNER,
                MATCH_NAECHSTE_MATCH_ID,
                MATCH_NAECHSTE_NAECHSTE_MATCH_ID,
                MATCH_STRAFPUNKTE_SATZ_1.intValue(),
                MATCH_STRAFPUNKTE_SATZ_2.intValue(),
                MATCH_STRAFPUNKTE_SATZ_3.intValue(),
                MATCH_STRAFPUNKTE_SATZ_4.intValue(),
                MATCH_STRAFPUNKTE_SATZ_5.intValue()
        );
    }

    protected LigamatchDO getLigamatchDO() {
        return new LigamatchDO(
                MATCH_ID,
                MATCH_WETTKAMPF_ID,
                MATCH_NR,
                MATCH_SCHEIBENNUMMER,
                MATCH_MATCHPUNKTE,
                MATCH_SATZPUNKTE,
                MATCH_MANNSCHAFT_ID,
                MATCH_MANNSCHAFT_NAME,
                MATCH_NAME_GEGNER,
                MATCH_SCHEIBENNUMMER_GEGNER,
                MATCH_ID_GEGNER,
                MATCH_NAECHSTE_MATCH_ID,
                MATCH_NAECHSTE_NAECHSTE_MATCH_ID,
                MATCH_STRAFPUNKTE_SATZ_1,
                MATCH_STRAFPUNKTE_SATZ_2,
                MATCH_STRAFPUNKTE_SATZ_3,
                MATCH_STRAFPUNKTE_SATZ_4,
                MATCH_STRAFPUNKTE_SATZ_5
        );
    }

    protected LigamatchDO getLigamatchDOWithNull() {
        return new LigamatchDO(
                MATCH_ID,
                MATCH_WETTKAMPF_ID,
                MATCH_NR,
                MATCH_SCHEIBENNUMMER,
                null,
                null,
                MATCH_MANNSCHAFT_ID,
                MATCH_MANNSCHAFT_NAME,
                MATCH_NAME_GEGNER,
                null,
                MATCH_ID_GEGNER,
                MATCH_NAECHSTE_MATCH_ID,
                MATCH_NAECHSTE_NAECHSTE_MATCH_ID,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    public void fromMatchDOtoDTO() {
        MatchDO matchDO = getMatchDO();
        final LigaSyncMatchDTO actual = LigaSyncMatchDTOMapper.fromMatchDOtoDTO.apply(matchDO);
        assertThat(actual.getId()).isEqualTo(MATCH_ID);
        assertThat(actual.getWettkampfId()).isEqualTo(MATCH_WETTKAMPF_ID);
    }

    @Test
    public void fromMatchDOWithNulltoDTO() {
        MatchDO matchDO = getMatchDOWithNull();
        final LigaSyncMatchDTO actual = LigaSyncMatchDTOMapper.fromMatchDOtoDTO.apply(matchDO);
        assertThat(actual.getId()).isEqualTo(MATCH_ID);
        assertThat(actual.getWettkampfId()).isEqualTo(MATCH_WETTKAMPF_ID);
    }

    @Test
    public void toDTO() {
        LigamatchDO ligamatchDO = getLigamatchDO();
        final LigaSyncMatchDTO actual = LigaSyncMatchDTOMapper.toDTO.apply(ligamatchDO);
        assertThat(actual.getId()).isEqualTo(MATCH_ID);
        assertThat(actual.getWettkampfId()).isEqualTo(MATCH_WETTKAMPF_ID);
    }

    @Test
    public void toDTOWithNull() {
        LigamatchDO ligamatchDO = getLigamatchDOWithNull();
        final LigaSyncMatchDTO actual = LigaSyncMatchDTOMapper.toDTO.apply(ligamatchDO);
        assertThat(actual.getId()).isEqualTo(MATCH_ID);
        assertThat(actual.getWettkampfId()).isEqualTo(MATCH_WETTKAMPF_ID);
    }

    @Test
    public void toMatchDTO() {
        LigaSyncMatchDTO ligaSyncMatchDTO = getLigaSyncMatchDTO();
        final MatchDTO actual = LigaSyncMatchDTOMapper.toMatchDTO.apply(ligaSyncMatchDTO);
        assertThat(actual.getId()).isEqualTo(MATCH_ID);
        assertThat(actual.getWettkampfId()).isEqualTo(MATCH_WETTKAMPF_ID);
    }

    @Test
    public void toMatchDTOWithNull() {
        LigaSyncMatchDTO ligaSyncMatchDTO = getLigaSyncMatchDTOWithNull();
        final MatchDTO actual = LigaSyncMatchDTOMapper.toMatchDTO.apply(ligaSyncMatchDTO);
        assertThat(actual.getId()).isEqualTo(MATCH_ID);
        assertThat(actual.getWettkampfId()).isEqualTo(MATCH_WETTKAMPF_ID);
    }
}