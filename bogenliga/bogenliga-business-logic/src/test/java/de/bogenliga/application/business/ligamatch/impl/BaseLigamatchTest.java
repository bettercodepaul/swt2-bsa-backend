package de.bogenliga.application.business.ligamatch.impl;

import java.util.HashMap;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.api.types.MatchDO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class BaseLigamatchTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    protected static final Long WETTKAMPF_ID = 1L;
    protected static final Long MATCH_ID = 1L;
    protected static final Long MATCH_NR = 1L;
    protected static final Long SCHEIBENNUMMER = 3L;
    protected static final Long MANNSCHAFT_ID = 1L;
    protected static final Long BEGEGNUNG = 1L;
    protected static final Long NAECHSTE_MATCH_ID = 1L;
    protected static final Long NAECHSTE_NAECHSTE_MATCH_ID = 1L;
    protected static final Long STRAFPUNKT_SATZ_1 = 0L;
    protected static final Long STRAFPUNKT_SATZ_2 = 10L;
    protected static final Long STRAFPUNKT_SATZ_3 = 0L;
    protected static final Long STRAFPUNKT_SATZ_4 = 10L;
    protected static final Long STRAFPUNKT_SATZ_5 = 0L;
    protected static final String WETTKAMP_TYP_ID = "0";
    protected static final Long WETTKAMPF_TAG = 1L;
    protected static final String MANNSCHAFT_NAME = "TSV_GRAFENBERG";
    protected static final Integer RUECKENNUMMER = 2;

    protected static final Long CURRENT_USER_ID = 1L;
    private HashMap<String, Object> valuesToMethodMap = new HashMap<>();

    public static LigamatchBE getLigamatchBE(){
        LigamatchBE ligamatchBE = new LigamatchBE();
        ligamatchBE.setWettkampfId(WETTKAMPF_ID);
        ligamatchBE.setMatchId(MATCH_ID);
        ligamatchBE.setMatchNr(MATCH_NR);
        ligamatchBE.setScheibennummer(SCHEIBENNUMMER);
        ligamatchBE.setMannschaftId(MANNSCHAFT_ID);
        ligamatchBE.setBegegnung(BEGEGNUNG);
        ligamatchBE.setNaechsteMatchId(NAECHSTE_MATCH_ID);
        ligamatchBE.setNaechsteNaechsteMatchId(NAECHSTE_NAECHSTE_MATCH_ID);
        ligamatchBE.setStrafpunkteSatz1(STRAFPUNKT_SATZ_1);
        ligamatchBE.setStrafpunkteSatz2(STRAFPUNKT_SATZ_2);
        ligamatchBE.setStrafpunkteSatz3(STRAFPUNKT_SATZ_3);
        ligamatchBE.setStrafpunkteSatz4(STRAFPUNKT_SATZ_4);
        ligamatchBE.setStrafpunkteSatz5(STRAFPUNKT_SATZ_5);
        ligamatchBE.setWettkampftypId(WETTKAMP_TYP_ID);
        ligamatchBE.setWettkampfTag(WETTKAMPF_TAG);
        ligamatchBE.setMannschaftName(MANNSCHAFT_NAME);
        ligamatchBE.setRueckennummer(RUECKENNUMMER);
        return ligamatchBE;
    }

    public static MatchDO getMatchDO() {
        return new MatchDO(MATCH_ID, MATCH_NR, WETTKAMPF_ID, MANNSCHAFT_ID, BEGEGNUNG,
                SCHEIBENNUMMER, null, null, STRAFPUNKT_SATZ_1,
                STRAFPUNKT_SATZ_2, STRAFPUNKT_SATZ_3, STRAFPUNKT_SATZ_4, STRAFPUNKT_SATZ_5);
    }

    public BaseLigamatchTest(){
        valuesToMethodMap.put("getWettkampfId", WETTKAMPF_ID);
        valuesToMethodMap.put("getMatchId", MATCH_ID);
        valuesToMethodMap.put("getMatchNr", MATCH_NR);
        valuesToMethodMap.put("getScheibennummer", SCHEIBENNUMMER);
        valuesToMethodMap.put("getMannschaftId", MANNSCHAFT_ID);
        valuesToMethodMap.put("getBegegnung", BEGEGNUNG);
        valuesToMethodMap.put("getNaechsteMatchId", NAECHSTE_MATCH_ID);
        valuesToMethodMap.put("getNaechsteNaechsteMatchId",NAECHSTE_NAECHSTE_MATCH_ID);
        valuesToMethodMap.put("getStrafpunkteSatz1", STRAFPUNKT_SATZ_1);
        valuesToMethodMap.put("getStrafpunkteSatz2", STRAFPUNKT_SATZ_2);
        valuesToMethodMap.put("getStrafpunkteSatz3", STRAFPUNKT_SATZ_3);
        valuesToMethodMap.put("getStrafpunkteSatz4", STRAFPUNKT_SATZ_4);
        valuesToMethodMap.put("getStrafpunkteSatz5", STRAFPUNKT_SATZ_5);
        valuesToMethodMap.put("getWettkampftypId", WETTKAMP_TYP_ID);
        valuesToMethodMap.put("getWettkampfTag", WETTKAMPF_TAG);
        valuesToMethodMap.put("getMannschaftName", MANNSCHAFT_NAME);
        valuesToMethodMap.put("getRueckennummer", RUECKENNUMMER);
    }

    public HashMap<String, Object> getValuesToMethodMap(){return valuesToMethodMap; }
}
