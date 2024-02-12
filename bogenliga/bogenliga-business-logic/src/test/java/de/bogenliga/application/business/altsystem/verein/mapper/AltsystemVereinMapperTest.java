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
    public void testGetVereinDO() {
        // Erstelle Beispiel-Daten
        String vereinIdentifier = "WT4424";
        List<VereinDO> mockVereinDOS = Arrays.asList(
                new VereinDO(123L, " ", "WT4424", null, null, null, null, null, null, null, null, null, null),
                new VereinDO(125L, " ", "XY4467", null, null, null, null, null, null, null, null, null, null)
        );

        // Setze das Mock-Verhalten für findBySearch
        when(vereinComponent.findAll()).thenReturn(mockVereinDOS);

        // Führt die Methode aus
        VereinDO result = altsystemVereinMapper.getVereinDO(vereinIdentifier);

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
    public void testParseName() {
        //Initialisierung
        AltsystemMannschaftDO mannschaftsName = new AltsystemMannschaftDO();
        mannschaftsName.setName("BS Nürtingen");
        //Testaufruf
        String parsedName = altsystemVereinMapper.parseName(mannschaftsName);
        //result Name
        String expectetName = "BS Nürtingen";
        //Vergleich des results
        assertEquals(expectetName, parsedName);

    }


    @Test
    public void testParseNameMitZahl() {
        //Initialisierung
        AltsystemMannschaftDO mannschaftsName = new AltsystemMannschaftDO();
        mannschaftsName.setName("BS Nürtingen 3");
        //Testaufruf
        String parsedName = altsystemVereinMapper.parseName(mannschaftsName);
        //result Name
        String expectetName = "BS Nürtingen";
        //Vergleich des results
        assertEquals(expectetName, parsedName);

    }

    @Test
    public void testParseNameMitBindestrich() {
        //Initialisierung
        AltsystemMannschaftDO mannschaftsName = new AltsystemMannschaftDO();
        mannschaftsName.setName("BS-Nürtingen 3");
        //Testaufruf
        String parsedName = altsystemVereinMapper.parseName(mannschaftsName);
        //result Name
        String expectetName = "BS Nürtingen";
        //Vergleich des results
        assertEquals(expectetName, parsedName);

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
