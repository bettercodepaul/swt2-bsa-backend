package de.bogenliga.application.business.match.impl;

import java.util.List;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.match.impl.entity.MatchBE;

/**
 * TODO [AL] class documentation
 *
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public abstract class BaseMatchTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    protected static final Long MATCH_ID = 1L;
    protected static final Long MATCH_NR = 1L;
    protected static final Long MATCH_BEGEGNUNG = 1L;
    protected static final Long MATCH_WETTKAMPF_ID = 1L;
    protected static final Long MATCH_MANNSCHAFT_ID = 1L;
    protected static final Long MATCH_SCHEIBENNUMMER = 3L;
    protected static final Long MATCH_MATCHPUNKTE = 6L;
    protected static final Long MATCH_SATZPUNKTE = 3L;
    protected static final Long CURRENT_USER_ID = 1L;


    protected MatchBE getMatchBE() {
        MatchBE matchBE = new MatchBE();
        matchBE.setId(MATCH_ID);
        matchBE.setNr(MATCH_NR);
        matchBE.setBegegnung(MATCH_BEGEGNUNG);
        matchBE.setMannschaftId(MATCH_MANNSCHAFT_ID);
        matchBE.setWettkampfId(MATCH_WETTKAMPF_ID);
        matchBE.setMatchpunkte(MATCH_MATCHPUNKTE);
        matchBE.setScheibenNummer(MATCH_SCHEIBENNUMMER);
        matchBE.setSatzpunkte(MATCH_SATZPUNKTE);
        return matchBE;
    }
}
