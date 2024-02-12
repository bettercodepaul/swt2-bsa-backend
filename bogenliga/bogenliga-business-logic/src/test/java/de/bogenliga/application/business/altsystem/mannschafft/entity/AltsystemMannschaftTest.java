package de.bogenliga.application.business.altsystem.mannschafft.entity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.mannschaft.entity.AltsystemMannschaft;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemMannschaftMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemMannschaftTest {

    private static final long CURRENTUSERID = 1L;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    AltsystemMannschaftMapper altsystemMannschaftMapper;
    @Mock
    AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    DsbMannschaftComponent dsbMannschaftComponent;
    @InjectMocks
    AltsystemMannschaft altsystemMannschaft;

    @Test
    public void testCreate(){

        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setId(2L);

        DsbMannschaftDO result = new DsbMannschaftDO();
        result.setId(1L);

        when(altsystemMannschaftMapper.toDO(any(), any())).thenReturn(result);
        //when(altsystemMannschaftMapper.addDefaultFields(result)

    }


}
