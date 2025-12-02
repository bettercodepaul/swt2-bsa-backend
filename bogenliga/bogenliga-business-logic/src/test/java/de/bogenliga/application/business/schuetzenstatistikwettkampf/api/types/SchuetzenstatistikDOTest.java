package de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.junit.Assert.*;

/**
 * @author Anna Baur
 */
public class SchuetzenstatistikDOTest {

    private static final String DSB_MITGLIED_NAME  = "Mitglied_Name";
    private static final int RUECKEN_NUMMER  = 5;
    private static final float WETTKAMPFTAG_1  = 8.5f;
    private static final float WETTKAMPFTAG_2  = 7.6f;
    private static final float WETTKAMPFTAG_3 =  9.1f;
    private static final float WETTKAMPFTAG_4 = 4.2f;
    private static final float WETTKAMPFTAGE_SCHNITT  = 8.6f;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private SchuetzenstatistikWettkampftageDO underTest;

    //create a SchuetzenstatistikWettkampfDO-Object for the test with values
    public static SchuetzenstatistikWettkampftageDO getSchuetzenstatistikWettkampfDO() {
        final SchuetzenstatistikWettkampftageDO expectedSchuetzenstatistikWettkampfDO = new SchuetzenstatistikWettkampftageDO();

        expectedSchuetzenstatistikWettkampfDO.setDsbMitgliedName(DSB_MITGLIED_NAME );
        expectedSchuetzenstatistikWettkampfDO.setRueckenNummer(RUECKEN_NUMMER);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag1(WETTKAMPFTAG_1);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag2(WETTKAMPFTAG_2);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag3(WETTKAMPFTAG_3);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag4(WETTKAMPFTAG_4);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftageSchnitt(WETTKAMPFTAGE_SCHNITT);

        return expectedSchuetzenstatistikWettkampfDO;
    }

    //to init the Object before Test
    @Before
    public void setUp()
    {
        underTest= getSchuetzenstatistikWettkampfDO();
    }

    //test if the Object is the same ->true
    @Test
    public void testTestEqualsSameObject() {

        assertEquals(underTest, underTest);
    }
    //test if the Values are the same ->true
    @Test
    public void testTestEqualsSameValues() {
        SchuetzenstatistikWettkampftageDO testObject = getSchuetzenstatistikWettkampfDO();
        assertEquals(underTest, testObject);
    }
    //test if the Object is null->false
    @Test
    public void testTestEqualsObjectIsNull() {

        assertNotEquals(null, underTest);
    }
    //test if Object has Different Values ->false
    @Test
    public void testTestEqualsDifferentValues() {
        SchuetzenstatistikWettkampftageDO testObject = new SchuetzenstatistikWettkampftageDO();
        assertNotEquals(underTest, testObject);
    }
}
