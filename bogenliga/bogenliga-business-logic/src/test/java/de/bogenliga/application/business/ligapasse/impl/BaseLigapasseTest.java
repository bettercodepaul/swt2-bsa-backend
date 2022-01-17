package de.bogenliga.application.business.ligapasse.impl;

import java.time.OffsetDateTime;
import java.util.HashMap;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.ligapasse.impl.entity.LigapasseBE;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class BaseLigapasseTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    protected static final Long LIGAPASSE_WETTKAMPF_ID = 1L;
    protected static final Long LIGAPASSE_MATCH_ID = 2L;
    protected static final Long LIGAPASSE_PASSE_ID = 1L;
    protected static final Long LIGAPASSE_LFDR_NR = 2L;
    protected static final Long LIGAPASSE_MANNSCHAFT_ID = 1L;
    protected static final Long LIGAPASSE_DSB_MITGLIED_ID = 1L;
    protected static final String LIGAPASSE_DSB_MITGLIED_NAME = "Hans";
    protected static final Integer LIGAPASSE_MANNSCHAFTSMITLIED_RUECKENNUMMER = 1;
    protected static final Integer LIGAPASSE_PFEIL_1 = 9;
    protected static final Integer LIGAPASSE_PFEIL_2 = 5;
    protected static final Integer LIGAPASSE_PFEIL_3 = 9;
    protected static final Integer LIGAPASSE_PFEIL_4 = 7;
    protected static final Integer LIGAPASSE_PFEIL_5 = 0;
    protected static final Long LIGAPASSE_MATCH_NR = 4L;

    private HashMap<String, Object> valuesToMethodMap = new HashMap<>();

    public static LigapasseBE getLigapasseBE(){
        LigapasseBE ligapasseBE = new LigapasseBE();
        ligapasseBE.setWettkampfId(LIGAPASSE_WETTKAMPF_ID);
        ligapasseBE.setMatchId(LIGAPASSE_MATCH_ID);
        ligapasseBE.setPasseId(LIGAPASSE_PASSE_ID);
        ligapasseBE.setPasseLfdnr(LIGAPASSE_LFDR_NR);
        ligapasseBE.setPasseMannschaftId(LIGAPASSE_MANNSCHAFT_ID);
        ligapasseBE.setDsbMitgliedId(LIGAPASSE_DSB_MITGLIED_ID);
        ligapasseBE.setDsbMitgliedName(LIGAPASSE_DSB_MITGLIED_NAME);
        ligapasseBE.setMannschaftsmitgliedRueckennummer(LIGAPASSE_MANNSCHAFTSMITLIED_RUECKENNUMMER);
        ligapasseBE.setPasseRingzahlPfeil1(LIGAPASSE_PFEIL_1);
        ligapasseBE.setPasseRingzahlPfeil2(LIGAPASSE_PFEIL_2);
        ligapasseBE.setPasseMatchNr(LIGAPASSE_MATCH_NR);
        return ligapasseBE;
    }


    public BaseLigapasseTest(){
        valuesToMethodMap.put("getWettkampfId", LIGAPASSE_WETTKAMPF_ID);
        valuesToMethodMap.put("getMatchId", LIGAPASSE_MATCH_ID);
        valuesToMethodMap.put("getPasseId", LIGAPASSE_PASSE_ID);
        valuesToMethodMap.put("getPasseLfdnr",LIGAPASSE_LFDR_NR);
        valuesToMethodMap.put("getPasseMannschaftId", LIGAPASSE_MANNSCHAFT_ID);
        valuesToMethodMap.put("getDsbMitgliedId", LIGAPASSE_DSB_MITGLIED_ID);
        valuesToMethodMap.put("getDsbMitgliedName", LIGAPASSE_DSB_MITGLIED_NAME);
        valuesToMethodMap.put("getMannschaftsmitgliedRueckennummer", LIGAPASSE_MANNSCHAFTSMITLIED_RUECKENNUMMER);
        valuesToMethodMap.put("getPasseRingzahlPfeil1", LIGAPASSE_PFEIL_1);
        valuesToMethodMap.put("getPasseRingzahlPfeil2", LIGAPASSE_PFEIL_2);
        valuesToMethodMap.put("getPasseMatchNr", LIGAPASSE_MATCH_NR);
    }

    public HashMap<String, Object> getValuesToMethodMap(){return valuesToMethodMap; }


}
