package de.bogenliga.application.business.ligamatch.impl.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.api.types.LigamatchDO;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Christopher Luzzi Base for Ligamatch Tests & Tests for LigamatchDAO
 */

public class BaseLigamatchTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BasicDAO basicDAO;

    @InjectMocks
    private LigamatchDAO underTest;

    private LigamatchBE expectedBE;


    protected static final Long WETTKAMPF_ID = 1L;
    protected static final Long MATCH_ID = 1L;
    protected static final Long MATCH_ID_GEGNER = 3L;
    protected static final Long MATCH_NR = 1L;
    protected static final Long SCHEIBENNUMMER = 3L;
    protected static final Long SCHEIBENNUMMER_GEGNER = 4L;
    protected static final Long MANNSCHAFT_ID = 1L;
    protected static final Long BEGEGNUNG = 1L;
    protected static final Long NAECHSTE_MATCH_ID = 1L;
    protected static final Long NAECHSTE_NAECHSTE_MATCH_ID = 2L;
    protected static final Long STRAFPUNKT_SATZ_1 = 0L;
    protected static final Long STRAFPUNKT_SATZ_2 = 10L;
    protected static final Long STRAFPUNKT_SATZ_3 = 0L;
    protected static final Long STRAFPUNKT_SATZ_4 = 10L;
    protected static final Long STRAFPUNKT_SATZ_5 = 0L;
    protected static final String WETTKAMP_TYP_ID = "0";
    protected static final Long WETTKAMPF_TAG = 1L;
    protected static final String MANNSCHAFT_NAME = "TSV_GRAFENBERG";
    protected static final String MANNSCHAFT_NAME_GEGNER = "TSV_GRAFENBERG_GEGNER";
    protected static final Integer RUECKENNUMMER = 2;
    protected static final Long SATZPUNKTE = 1L;
    protected static final Long MATCHPUNKTE = 2L;

    private HashMap<String, Object> valuesToMethodMap = new HashMap<>();

    public static LigamatchBE getLigamatchBE(){
        LigamatchBE ligamatchBE = new LigamatchBE();
        ligamatchBE.setWettkampfId(WETTKAMPF_ID);
        ligamatchBE.setMatchId(MATCH_ID);
        ligamatchBE.setMatchIdGegner(MATCH_ID_GEGNER);
        ligamatchBE.setMatchNr(MATCH_NR);
        ligamatchBE.setMatchScheibennummer(SCHEIBENNUMMER);
        ligamatchBE.setScheibennummerGegner(SCHEIBENNUMMER_GEGNER);
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
        ligamatchBE.setMannschaftNameGegner(MANNSCHAFT_NAME_GEGNER);
        ligamatchBE.setSatzpunkte(SATZPUNKTE);
        ligamatchBE.setMatchpunkte(MATCHPUNKTE);
        return ligamatchBE;
    }

    public static void assertValid(LigamatchBE ligamatchBE, LigamatchBE actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.getMatchNr()).isEqualTo(ligamatchBE.getMatchNr()).isEqualTo(MATCH_NR);
        assertThat(actual.getMatchId()).isEqualTo(ligamatchBE.getMatchId()).isEqualTo(MATCH_ID);
        assertThat(actual.getMatchIdGegner()).isEqualTo(ligamatchBE.getMatchIdGegner()).isEqualTo(MATCH_ID_GEGNER);
        assertThat(actual.getMatchScheibennummer()).isEqualTo(ligamatchBE.getMatchScheibennummer()).isEqualTo(SCHEIBENNUMMER);
        assertThat(actual.getScheibennummerGegner()).isEqualTo(ligamatchBE.getScheibennummerGegner()).isEqualTo(SCHEIBENNUMMER_GEGNER);
        assertThat(actual.getMannschaftId()).isEqualTo(ligamatchBE.getMannschaftId()).isEqualTo(MANNSCHAFT_ID);
        assertThat(actual.getBegegnung()).isEqualTo(ligamatchBE.getBegegnung()).isEqualTo(BEGEGNUNG);
        assertThat(actual.getNaechsteMatchId()).isEqualTo(ligamatchBE.getNaechsteMatchId()).isEqualTo(NAECHSTE_MATCH_ID);
        assertThat(actual.getNaechsteNaechsteMatchId()).isEqualTo(ligamatchBE.getNaechsteNaechsteMatchId()).isEqualTo(NAECHSTE_NAECHSTE_MATCH_ID);

        assertThat(actual.getStrafpunkteSatz1()).isEqualTo(ligamatchBE.getStrafpunkteSatz1()).isEqualTo(STRAFPUNKT_SATZ_1);
        assertThat(actual.getStrafpunkteSatz2()).isEqualTo(ligamatchBE.getStrafpunkteSatz2()).isEqualTo(STRAFPUNKT_SATZ_2);
        assertThat(actual.getStrafpunkteSatz3()).isEqualTo(ligamatchBE.getStrafpunkteSatz3()).isEqualTo(STRAFPUNKT_SATZ_3);
        assertThat(actual.getStrafpunkteSatz4()).isEqualTo(ligamatchBE.getStrafpunkteSatz4()).isEqualTo(STRAFPUNKT_SATZ_4);
        assertThat(actual.getStrafpunkteSatz5()).isEqualTo(ligamatchBE.getStrafpunkteSatz5()).isEqualTo(STRAFPUNKT_SATZ_5);
        assertThat(actual.getWettkampfId()).isEqualTo(ligamatchBE.getWettkampfId()).isEqualTo(WETTKAMPF_ID);
        assertThat(actual.getWettkampfTag()).isEqualTo(ligamatchBE.getWettkampfTag()).isEqualTo(WETTKAMPF_TAG);
        assertThat(actual.getMannschaftName()).isEqualTo(ligamatchBE.getMannschaftName()).isEqualTo(MANNSCHAFT_NAME);
        assertThat(actual.getMannschaftNameGegner()).isEqualTo(ligamatchBE.getMannschaftNameGegner()).isEqualTo(MANNSCHAFT_NAME_GEGNER);
        assertThat(actual.getSatzpunkte()).isEqualTo(ligamatchBE.getSatzpunkte()).isEqualTo(SATZPUNKTE);
        assertThat(actual.getMatchpunkte()).isEqualTo(ligamatchBE.getMatchpunkte()).isEqualTo(MATCHPUNKTE);
    }

    public static void validateObjectList (List<LigamatchBE> actual) {
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();
    }

    public static void validateDOObjectList (List<LigamatchDO> actual) {
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();
    }

    public BaseLigamatchTest(){
        valuesToMethodMap.put("getWettkampfId", WETTKAMPF_ID);
        valuesToMethodMap.put("getMatchId", MATCH_ID);
        valuesToMethodMap.put("getMatchIdGegner", MATCH_ID_GEGNER);
        valuesToMethodMap.put("getMatchNr", MATCH_NR);
        valuesToMethodMap.put("getMatchScheibennummer", SCHEIBENNUMMER);
        valuesToMethodMap.put("getScheibennummerGegner", SCHEIBENNUMMER_GEGNER);
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
        valuesToMethodMap.put("getMannschaftNameGegner", MANNSCHAFT_NAME_GEGNER);
        valuesToMethodMap.put("getRueckennummer", RUECKENNUMMER);
        valuesToMethodMap.put("getSatzpunkte", SATZPUNKTE);
        valuesToMethodMap.put("getMatchpunkte", MATCHPUNKTE);
    }

    public HashMap<String, Object> getValuesToMethodMap(){return valuesToMethodMap; }


    /**
     * Tests for LigamatchDAO
     */

    //Implements generic way to test business entities methods
    private BasicTest<LigamatchBE, LigamatchBE> basicDAOTest;

    @Before
    public void testSetup() {
        expectedBE = getLigamatchBE();
        basicDAOTest = new BasicTest<>(expectedBE, getValuesToMethodMap());
        //configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        when(basicDAO.selectEntityList(any(),any(),any())).thenReturn(Collections.singletonList(expectedBE));
    }

    @Test
    public void testAllFindMethods() throws InvocationTargetException, IllegalAccessException{
        basicDAOTest.testAllFindMethods(underTest);
    }

    @Test
    public void findById(){
        try{
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findById(MATCH_ID));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findLigamatchesByWettkampfId(){
        try{
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findLigamatchesByWettkampfId(WETTKAMPF_ID));
        } catch (InvocationTargetException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
