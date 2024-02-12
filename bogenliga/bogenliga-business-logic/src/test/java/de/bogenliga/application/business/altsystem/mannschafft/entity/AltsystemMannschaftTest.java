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
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemVeranstaltungMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.verein.entity.AltsystemVerein;
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
    @InjectMocks
    AltsystemMannschaft altsystemMannschaft;

    @Test
    public void testCreate(){
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setId(2L);
        altsystemMannschaftDO.setName("BS Nürtingen 3");

        veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungID(1L);

        DsbMannschaftDO result = new DsbMannschaftDO();
        result.setId(1L);
        result.setNummer(3);
        result.setVeranstaltungId(1L);
        result.setVereinId(1L);

        when(altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID)).thenReturn(veranstaltungDO);
        when(altsystemMannschaftMapper.toDO(any(), any())).thenReturn(result);
        when(altsystemMannschaftMapper.addDefaultFields(result, CURRENTUSERID, altsystemMannschaftDO, veranstaltungDO)).thenReturn(result);
        when(dsbMannschaftComponent.create(result, CURRENTUSERID)).thenReturn(result);
        altsystemVerein.create(altsystemMannschaftDO, CURRENTUSERID);

        altsystemMannschaft.create(altsystemMannschaftDO, CURRENTUSERID);

        verify(altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID));
        verify(altsystemMannschaftMapper.toDO(altsystemMannschaftDO, result));
        verify(altsystemMannschaftMapper.addDefaultFields(result, CURRENTUSERID, altsystemMannschaftDO, veranstaltungDO));
        verify(dsbMannschaftComponent).create(result, CURRENTUSERID);

    }



}