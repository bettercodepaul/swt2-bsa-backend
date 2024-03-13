package de.bogenliga.application.business.altsystem.verein.entity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.verein.mapper.AltsystemVereinMapper;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemVereinTest {
    public static final long CURRENTUSERID = 1L;


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public AltsystemVereinMapper altsystemVereinMapper;

    @Mock
    AltsystemUebersetzung altsystemUebersetzung;

    @Mock
    VereinComponent vereinComponent;

    @InjectMocks
    AltsystemVerein altsystemVerein;

    @Test
    public void testCreateMannschaftNichtVorhanden() {


        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setId(2L);

        VereinDO resultNull = new VereinDO();
        resultNull.setId(null);
        VereinDO result = new VereinDO();
        result.setId(1L);
        result.setName("TestVerein");
        result.setDsbIdentifier("WT4424");

        when(altsystemVereinMapper.getVereinDO(anyString(), anyString())).thenReturn(null);
        when(altsystemVereinMapper.toDO(any(), any())).thenReturn(result);
        when(altsystemVereinMapper.addDefaultFields(result)).thenReturn(result);
        when(vereinComponent.create(any(), anyLong())).thenReturn(result);
        doNothing().when(altsystemUebersetzung).updateOrInsertUebersetzung(any(), anyLong(), anyLong(), anyString());

        altsystemVerein.create(altsystemMannschaftDO, CURRENTUSERID);

        verify(altsystemVereinMapper).getVereinDO("TestVerein", "WT4424");
        verify(altsystemVereinMapper).toDO(new VereinDO(), altsystemMannschaftDO);
        verify(altsystemVereinMapper).addDefaultFields(result);
        verify(vereinComponent).create(result, CURRENTUSERID);
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemMannschaftDO.getId(), result.getId(), "");

    }

    @Test
    public void testCreateManschaftVorhanden() {
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setId(2L);

        VereinDO result = new VereinDO();
        result.setId(1L);
        result.setName("TestVerein");
        result.setDsbIdentifier("WT4424");

        when(altsystemVereinMapper.toDO(any(), any())).thenReturn(result);
        when(altsystemVereinMapper.getVereinDO(anyString(), anyString())).thenReturn(result);
        when(altsystemVereinMapper.addDefaultFields(result)).thenReturn(result);
        when(vereinComponent.create(any(), anyLong())).thenReturn(result);

        altsystemVerein.create(altsystemMannschaftDO, CURRENTUSERID);

        verify(altsystemVereinMapper).getVereinDO("TestVerein", "WT4424");
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemMannschaftDO.getId(), result.getId(), "");

    }

    @Test
    public void testUpdate(){
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO();
        altsystemMannschaftDO.setId(2L);

        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setAltsystemId(3L);
        altsystemUebersetzungDO.setBogenligaId(1L);

        VereinDO result = new VereinDO();
        result.setId(1L);

        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemMannschaftDO.getId())).thenReturn(altsystemUebersetzungDO);
        when(vereinComponent.findById(altsystemUebersetzungDO.getBogenligaId())).thenReturn(result);
        when(altsystemVereinMapper.toDO(result, altsystemMannschaftDO)).thenReturn(result);
        when(vereinComponent.update(result, CURRENTUSERID)).thenReturn(result);

        altsystemVerein.update(altsystemMannschaftDO, CURRENTUSERID);

        verify(altsystemUebersetzung).findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemMannschaftDO.getId());
        verify(vereinComponent).findById(altsystemUebersetzungDO.getBogenligaId());
        verify(altsystemVereinMapper).toDO(result, altsystemMannschaftDO);
        verify(vereinComponent).update(result,CURRENTUSERID);

    }

}
