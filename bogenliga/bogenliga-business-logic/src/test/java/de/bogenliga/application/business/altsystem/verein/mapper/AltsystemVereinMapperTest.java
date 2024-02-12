package de.bogenliga.application.business.altsystem.verein.mapper;

import java.util.Arrays;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
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
    AltsystemUebersetzung altsystemUebersetzung;
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

        AltsystemMannschaftDO mannschaftIdentifier = new AltsystemMannschaftDO();
        mannschaftIdentifier.setMannr("34WT414424");

        String parsedIdentifier = altsystemVereinMapper.parseIdentifier(mannschaftIdentifier);

        String expectetIdentifier = "WT4424";

        assertEquals(expectetIdentifier, parsedIdentifier);

    }

    @Test
    public void testParseName() {

        AltsystemMannschaftDO mannschaftsName = new AltsystemMannschaftDO();
        mannschaftsName.setName("BS Nürtingen");

        String parsedName = altsystemVereinMapper.parseName(mannschaftsName);

        String expectetName = "BS Nürtingen";

        assertEquals(expectetName, parsedName);

    }


    @Test
    public void testParseNameZahl() {

        AltsystemMannschaftDO mannschaftsName = new AltsystemMannschaftDO();
        mannschaftsName.setName("BS Nürtingen 3");

        String parsedName = altsystemVereinMapper.parseName(mannschaftsName);

        String expectetName = "BS Nürtingen";

        assertEquals(expectetName, parsedName);

    }

    @Test
    public void testParseNameBindestrich() {

        AltsystemMannschaftDO mannschaftsName = new AltsystemMannschaftDO();
        mannschaftsName.setName("BS-Nürtingen 3");

        String parsedName = altsystemVereinMapper.parseName(mannschaftsName);

        String expectetName = "BS Nürtingen";

        assertEquals(expectetName, parsedName);

    }




}
