package de.bogenliga.application.business.altsystem.mannschafft.mapper;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.junit.Assert.*;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemMannschaftMapper;
/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemMannschaftMapperTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;

    @InjectMocks
    private AltsystemMannschaftMapper altsystemMannschaftMapper;

    @Test
    public void testToDOWithoutNum() {
        //creat test data
        AltsystemMannschaftDO mannschaftsName = new AltsystemMannschaftDO();
        mannschaftsName.setName("BS Nürtingen");
        DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO();
        //Test method
        long parsedName = altsystemMannschaftMapper.toDO(mannschaftsName, dsbMannschaftDO).getNummer();
        //Expectet Result
        long expectetName = 0;
        //Assert result
        assertEquals(expectetName, parsedName);

    }

    @Test
    public void testToDOWithNum() {
        //creat test data
        AltsystemMannschaftDO mannschaftsName = new AltsystemMannschaftDO();
        mannschaftsName.setName("BS Nürtingen 3");
        DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO();
        //Test method
        long parsedName = altsystemMannschaftMapper.toDO(mannschaftsName, dsbMannschaftDO).getNummer();
        //Expectet Result
        long expectetName = 3;
        //Assert result
        assertEquals(expectetName, parsedName);

    }
}
