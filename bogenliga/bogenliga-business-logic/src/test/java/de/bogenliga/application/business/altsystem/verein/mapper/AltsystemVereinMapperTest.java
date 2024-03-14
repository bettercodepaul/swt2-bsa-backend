package de.bogenliga.application.business.altsystem.verein.mapper;

import java.util.Arrays;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.verein.entity.AltsystemVerein;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemVereinMapperTest {


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    AltsystemLigaMapper altsystemLigaMapper;
    @Mock
    VereinComponent vereinComponent;
    @InjectMocks
    public AltsystemVereinMapper altsystemVereinMapper;




    @Test
    public void testToDO() {
        // Erstellen der benötigten Objekte und Mocks
        AltsystemMannschaftDO altsystemDataObject = new AltsystemMannschaftDO();
        altsystemDataObject.setId(1L);
        altsystemDataObject.setName("BS Nürtingen");
        altsystemDataObject.setMannr("12WT564444");

        VereinDO vereinDO = new VereinDO();

        // Mocken der Abhängigkeiten
        altsystemVereinMapper.parseVereinName(altsystemDataObject);
        altsystemVereinMapper.parseIdentifier(altsystemDataObject);


        // Aufruf der zu testenden Methode
        altsystemVereinMapper.toDO(vereinDO, altsystemDataObject);

        // Überprüfen, ob die Daten korrekt gesetzt wurden
        assertEquals("BS Nürtingen", vereinDO.getName());
        assertEquals("WT4444", vereinDO.getDsbIdentifier());
    }

    @Test
    public void testGetVereinDO() {
        // Erstelle Beispiel-Daten
        String vereinName = "Testvereio";
        String vereinIdentifier = "WT4424";
        List<VereinDO> mockVereinDOS = Arrays.asList(
                new VereinDO(123L, "TestVerein", "WT4424", null, null, null, null, null, null, null, null, null, null),
                new VereinDO(125L, " ", "XY4467", null, null, null, null, null, null, null, null, null, null)
        );

        // Setze das Mock-Verhalten für findBySearch
        when(vereinComponent.findBySearch(anyString())).thenReturn(mockVereinDOS);

        // Führt die Methode aus
        VereinDO result = altsystemVereinMapper.getVereinDO(vereinName, vereinIdentifier);

        // Überprüfe das Ergebnis
        assertThat(result.getId()).isEqualTo(123L);

    }



    @Test
    public void testParseIdentifier() {

        //Initialisierung
        AltsystemMannschaftDO mannschaftIdentifier = new AltsystemMannschaftDO();
        mannschaftIdentifier.setMannr("34WT414424");
        //Testaufruf
        String parsedIdentifier = altsystemVereinMapper.parseIdentifier(mannschaftIdentifier);
        //result Identifier
        String expectetIdentifier = "WT4424";
        //Vergleich des results
        assertEquals(expectetIdentifier, parsedIdentifier);

    }

    @Test
    public void testParseVereinName() {
        //Initialisierung Testfall 0
        AltsystemMannschaftDO mannschaftsName = new AltsystemMannschaftDO();
        mannschaftsName.setName("BS Nürtingen");
        //Testaufruf
        String parsedName = altsystemVereinMapper.parseVereinName(mannschaftsName);
        //result Name
        String expectetName = "BS Nürtingen";
        //Vergleich des results
        assertEquals(expectetName, parsedName);


        //Initialisierung Testfall 1
        AltsystemMannschaftDO mannschaftsName1 = new AltsystemMannschaftDO();
        mannschaftsName1.setName("BS Nürtingen 3");
        //Testaufruf
        String parsedName1 = altsystemVereinMapper.parseVereinName(mannschaftsName1);
        //result Name
        String expectetName1 = "BS Nürtingen";
        //Vergleich des results
        assertEquals(expectetName1, parsedName1);


        //Initialisierung Testfall 2
        AltsystemMannschaftDO mannschaftsName2 = new AltsystemMannschaftDO();
        mannschaftsName2.setName("BS-Nürtingen 3");
        //Testaufruf
        String parsedName2 = altsystemVereinMapper.parseVereinName(mannschaftsName2);
        //result Name
        String expectetName2 = "BS Nürtingen";
        //Vergleich des results
        assertEquals(expectetName2, parsedName2);

        //Initialisierung Testfall 3
        AltsystemMannschaftDO mannschaftsName3 = new AltsystemMannschaftDO();
        mannschaftsName3.setName("BSV Hausen i. K");
        //Testaufruf
        String parsedName3 = altsystemVereinMapper.parseVereinName(mannschaftsName3);
        //result Name
        String expectetName3 = "BSV Hausen i.K.";
        //Vergleich des results
        assertEquals(expectetName3, parsedName3);


        //Initialisierung Testfall 4
        AltsystemMannschaftDO mannschaftsName4 = new AltsystemMannschaftDO();
        mannschaftsName4.setName("SVng Endersbach Strümpf");
        //Testaufruf
        String parsedName4 = altsystemVereinMapper.parseVereinName(mannschaftsName4);
        //result Name
        String expectetName4 = "SVng Endersbach Strümpfelbach";
        //Vergleich des results
        assertEquals(expectetName4, parsedName4);


        //Initialisierung Testfall 5
        AltsystemMannschaftDO mannschaftsName5 = new AltsystemMannschaftDO();
        mannschaftsName5.setName("BWT Kichentellinsfurt");
        //Testaufruf
        String parsedName5 = altsystemVereinMapper.parseVereinName(mannschaftsName5);
        //result Name
        String expectetName5 = "BWT Kirchentellinsfurt";
        //Vergleich des results
        assertEquals(expectetName5, parsedName5);


        //Initialisierung Testfall 6
        AltsystemMannschaftDO mannschaftsName6 = new AltsystemMannschaftDO();
        mannschaftsName6.setName("SV Weilheim Teck");
        //Testaufruf
        String parsedName6 = altsystemVereinMapper.parseVereinName(mannschaftsName6);
        //result Name
        String expectetName6 = "SV Tell Weilheim";
        //Vergleich des results
        assertEquals(expectetName6, parsedName6);


        //Initialisierung Testfall 7
        AltsystemMannschaftDO mannschaftsName7 = new AltsystemMannschaftDO();
        mannschaftsName7.setName("BSV Hausen i. K");
        //Testaufruf
        String parsedName7 = altsystemVereinMapper.parseVereinName(mannschaftsName7);
        //result Name
        String expectetName7 = "BSV Hausen i.K.";
        //Vergleich des results
        assertEquals(expectetName7, parsedName7);

        //Initialisierung Testfall 8
        AltsystemMannschaftDO mannschaftsName8 = new AltsystemMannschaftDO();
        mannschaftsName8.setName("BoAbt. 1. SV Fasanenhof 2");
        //Testaufruf
        String parsedName8 = altsystemVereinMapper.parseVereinName(mannschaftsName8);
        //result Name
        String expectetName8 = "BoAbt 1. SV Fasanenhof";
        //Vergleich des results
        assertEquals(expectetName8, parsedName8);

        //Initialisierung Testfall 9
        AltsystemMannschaftDO mannschaftsName9 = new AltsystemMannschaftDO();
        mannschaftsName9.setName("BSG Odenheim e.V.");
        //Testaufruf
        String parsedName9 = altsystemVereinMapper.parseVereinName(mannschaftsName9);
        //result Name
        String expectetName9 = "BSG Odenheim e.V.";
        //Vergleich des results
        assertEquals(expectetName9, parsedName9);

    }



    @Test
    public void testAddDefaultFields(){
        //Initialisierung
        VereinDO vereinDO = new VereinDO();
        vereinDO.setId(2L);
        //result
        RegionenDO regionenDO = new RegionenDO(1L);
        regionenDO.setId(1L);
        //Mock konfig
        when(altsystemLigaMapper.getDsbDO()).thenReturn(regionenDO);
        //Testaufruf
        altsystemVereinMapper.addDefaultFields(vereinDO);
        //Testet, ob alle Methoden aufgerufen wurden.
        verify(altsystemLigaMapper).getDsbDO();
    }

}
