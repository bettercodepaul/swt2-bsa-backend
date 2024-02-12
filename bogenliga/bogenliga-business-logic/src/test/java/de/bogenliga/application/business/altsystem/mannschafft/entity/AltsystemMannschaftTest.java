package de.bogenliga.application.business.altsystem.mannschafft.entity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import de.bogenliga.application.business.altsystem.verein.entity.AltsystemVerein;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
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
    AltsystemMannschaftMapper altsystemMannschaftMapper;
    @Mock
    AltsystemVeranstaltungMapper altsystemVeranstaltungMapper;
    @Mock
    AltsystemVerein altsystemVerein;
    @Mock
    AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    VeranstaltungDO veranstaltungDO;
    @Mock
    DsbMannschaftComponent dsbMannschaftComponent;
    @Mock
    VeranstaltungComponent veranstaltungComponent;
    @InjectMocks
    AltsystemMannschaft altsystemMannschaft;
    @Mock
    AltsystemLigaMapper altsystemLigaMapper;
    @Mock
    AltsystemUebersetzungDO altsystemUebersetzungDO;


    @Before
    public void setUp() {
        altsystemUebersetzung = Mockito.mock(AltsystemUebersetzung.class);

    }

//    @Test
//    public void testCreate(){
//        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
//        altsystemMannschaftDO.setId(1L);
//        altsystemMannschaftDO.setName("BS Nürtingen 3");
//
//        AltsystemLigaMapper altsystemLigaMapper = mock(AltsystemLigaMapper.class);
//
//
//
//        AltsystemMannschaftMapper altsystemMannschaftMapper = new AltsystemMannschaftMapper(altsystemUebersetzung,altsystemLigaMapper,dsbMannschaftComponent );
//        veranstaltungDO = new VeranstaltungDO();
//        veranstaltungDO.setVeranstaltungID(1L);
//
//        DsbMannschaftDO result = new DsbMannschaftDO();
//        result.setId(1L);
//        result.setNummer(3);
//        result.setVeranstaltungId(1L);
//        result.setVereinId(1L);
//
//        when(altsystemUebersetzung.findByAltsystemID(any(), any())).thenReturn(altsystemUebersetzungDO);
//
//        when(veranstaltungComponent.findByLigaIDAndSportjahr(any(), any())).thenReturn(veranstaltungDO);
//        when(altsystemVeranstaltungMapper.getOrCreateVeranstaltung(any(), any())).thenReturn(any());
//        when(altsystemMannschaftMapper.toDO(any(), any())).thenReturn(result);
//        when(altsystemMannschaftMapper.addDefaultFields(result, CURRENTUSERID, altsystemMannschaftDO, any())).thenReturn(result);
//        when(dsbMannschaftComponent.create(any(), any())).thenReturn(result);
//
//
//        altsystemMannschaft.create(altsystemMannschaftDO, CURRENTUSERID);
//
//        // dsbMannschaftDO.setVereinId(vereinUebersetzung.getBogenligaId());
//        verify(dsbMannschaftComponent).create(result, CURRENTUSERID);
//    }

    @Test
    public void testAddDefaultFields() {
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setId(1L);
        altsystemMannschaftDO.setName("BS Nürtingen 3");

        AltsystemLigaMapper altsystemLigaMapper = mock(AltsystemLigaMapper.class);

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();


        AltsystemMannschaftMapper altsystemMannschaftMapper = new AltsystemMannschaftMapper(altsystemUebersetzung,
                altsystemLigaMapper, dsbMannschaftComponent);
        veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungID(1L);

        DsbMannschaftDO result = new DsbMannschaftDO();
        result.setId(1L);
        result.setNummer(3);
        result.setVeranstaltungId(1L);
        result.setVereinId(1L);

        when(altsystemUebersetzung.findByAltsystemID(any(), any())).thenReturn(altsystemUebersetzungDO);

        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein,
                CURRENTUSERID)).thenReturn(altsystemUebersetzungDO);
        when(altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID)).thenReturn(veranstaltungDO);
        when(dsbMannschaftComponent.create(result, CURRENTUSERID)).thenReturn(result);

        altsystemMannschaftMapper.addDefaultFields(result, CURRENTUSERID, altsystemMannschaftDO, veranstaltungDO);

        // dsbMannschaftDO.setVereinId(vereinUebersetzung.getBogenligaId());
        verify(altsystemUebersetzung).findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein,
                CURRENTUSERID);
    }


}