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

    private static final String dsbMitgliedName = "Mitglied_Name";
    private static final int rueckenNummer = 5;
    private static final float wettkampftag1 = (float) 8.5;
    private static final float wettkampftag2 = (float) 7.6;
    private static final float wettkampftag3 = (float) 9.1;
    private static final float wettkampftag4 = (float) 4.2;
    private static final float wettkampftageSchnitt = (float) 8.6;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private SchuetzenstatistikWettkampftageDO underTest;

    //create a SchuetzenstatistikWettkampfDO-Object for the test with values
    public static SchuetzenstatistikWettkampftageDO getSchuetzenstatistikWettkampfDO() {
        final SchuetzenstatistikWettkampftageDO expectedSchuetzenstatistikWettkampfDO = new SchuetzenstatistikWettkampftageDO();

        expectedSchuetzenstatistikWettkampfDO.setDsbMitgliedName(dsbMitgliedName);
        expectedSchuetzenstatistikWettkampfDO.setRueckenNummer(rueckenNummer);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag1(wettkampftag1);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag2(wettkampftag2);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag3(wettkampftag3);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag4(wettkampftag4);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftageSchnitt(wettkampftageSchnitt);

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
