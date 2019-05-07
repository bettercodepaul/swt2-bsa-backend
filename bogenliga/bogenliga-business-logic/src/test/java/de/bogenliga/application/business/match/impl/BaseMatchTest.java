package de.bogenliga.application.business.match.impl;

import java.util.HashMap;
import java.util.List;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;

/**
 * TODO [AL] class documentation
 *
 * @author Dominik Halle & Kay Scheerer, HSRT MKI SS19 - SWT2
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
    private HashMap<String, Object> valuesToMethodMap = new HashMap<>();


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


    protected MatchDO getMatchDO() {
        return new MatchDO(MATCH_ID, MATCH_NR, MATCH_WETTKAMPF_ID, MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                MATCH_SCHEIBENNUMMER, MATCH_MATCHPUNKTE, MATCH_SATZPUNKTE);
    }


    public BaseMatchTest() {
        valuesToMethodMap.put("getId", MATCH_ID);
        valuesToMethodMap.put("getNr", MATCH_NR);
        valuesToMethodMap.put("getBegegnung", MATCH_BEGEGNUNG);
        valuesToMethodMap.put("getMannschaftId", MATCH_MANNSCHAFT_ID);
        valuesToMethodMap.put("getWettkampfId", MATCH_WETTKAMPF_ID);
        valuesToMethodMap.put("getMatchpunkte", MATCH_MATCHPUNKTE);
        valuesToMethodMap.put("getScheibenNummer", MATCH_SCHEIBENNUMMER);
        valuesToMethodMap.put("getSatzpunkte", MATCH_SATZPUNKTE);
    }


    public HashMap<String, Object> getValuesToMethodMap() {
        return valuesToMethodMap;
    }
}
