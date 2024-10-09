package de.bogenliga.application.business.altsystem.mannschafft.entity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.mannschaft.entity.AltsystemMannschaft;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemMannschaftMapper;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemVeranstaltungMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
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
    AltsystemVeranstaltungMapper altsystemVeranstaltungMapper;
    @Mock
    AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    VeranstaltungDO veranstaltungDO;
    @Mock
    DsbMannschaftComponent dsbMannschaftComponent;
    @InjectMocks
    AltsystemMannschaft altsystemMannschaft;
    @Mock
    AltsystemUebersetzungDO altsystemUebersetzungDO;



    @Test
    public void testAddDefaultFields() {
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setId(1L);
        altsystemMannschaftDO.setName("BS Nürtingen 3");

        AltsystemLigaMapper altsystemLigaMapper = mock(AltsystemLigaMapper.class);


        AltsystemMannschaftMapper altsystemMannschaftMapper = new AltsystemMannschaftMapper(altsystemUebersetzung,
                altsystemLigaMapper, dsbMannschaftComponent);
        veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungID(1L);

        DsbMannschaftDO result = new DsbMannschaftDO();
        result.setId(1L);
        result.setNummer(3L);
        result.setVeranstaltungId(1L);
        result.setVereinId(1L);

        when(altsystemUebersetzung.findByAltsystemID(any(), any())).thenReturn(altsystemUebersetzungDO);

        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein,
                CURRENTUSERID)).thenReturn(altsystemUebersetzungDO);
        when(altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID)).thenReturn(veranstaltungDO);
        when(dsbMannschaftComponent.create(result, CURRENTUSERID)).thenReturn(result);

        altsystemMannschaftMapper.addDefaultFields(result, CURRENTUSERID, altsystemMannschaftDO, veranstaltungDO);

        verify(altsystemUebersetzung).findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein,
                CURRENTUSERID);
    }


}