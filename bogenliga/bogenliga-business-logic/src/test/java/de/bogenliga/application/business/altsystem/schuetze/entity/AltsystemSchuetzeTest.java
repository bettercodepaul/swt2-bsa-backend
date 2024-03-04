package de.bogenliga.application.business.altsystem.schuetze.entity;

import java.sql.SQLException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.altsystem.schuetze.mapper.AltsystemSchuetzeMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDAO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemSchuetzeTest {
    // Constants for Mock Data
    private static final Long CURRENTUSERID = 1L;

    public AltsystemSchuetzeMapper altsystemSchuetzeMapper;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    DsbMitgliedComponent dsbMitgliedComponent;
    @Mock
    AltsystemUebersetzungDAO altsystemUebersetzungDAO;

    @InjectMocks
    AltsystemSchuetze altsystemSchuetze;


    @Before
    public void setUp() {
        altsystemSchuetzeMapper = Mockito.mock(AltsystemSchuetzeMapper.class);
        dsbMitgliedComponent = Mockito.mock(DsbMitgliedComponent.class);
        altsystemUebersetzung = Mockito.mock(AltsystemUebersetzung.class);
        altsystemSchuetze = new AltsystemSchuetze(altsystemSchuetzeMapper, dsbMitgliedComponent, altsystemUebersetzung);
        altsystemUebersetzungDAO = Mockito.mock(AltsystemUebersetzungDAO.class);
    }

    @Test
    public void testCreate() throws SQLException{
        // prepare test data
        AltsystemSchuetzeDO altsystemSchuetzeDO = new AltsystemSchuetzeDO();
        altsystemSchuetzeDO.setId(2L);
        altsystemSchuetzeDO.setName("Bammert, Marco");

        // Ergebnis Objekt
        DsbMitgliedDO result = new DsbMitgliedDO();
        result.setId(1L);
        result.setVorname("Marco");
        result.setNachname("Bammert");
        result.setVereinsId(1L);

        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setAltsystemId(1L);
        altsystemUebersetzungDO.setBogenligaId(1L);
        altsystemUebersetzungDO.setWert("MarcoBammert1");

        when(altsystemUebersetzung.findByAltsystemID(any(), any())).thenReturn(altsystemUebersetzungDO);

        // Mocks konfigurieren
        when(altsystemSchuetzeMapper.toDO(any(), any())).thenReturn(result);
        when(altsystemSchuetzeMapper.addDefaultFields(result, CURRENTUSERID)).thenReturn(result);
        when(dsbMitgliedComponent.create(result, CURRENTUSERID)).thenReturn(result);

        // Testaufruf
        altsystemSchuetze.create(altsystemSchuetzeDO, CURRENTUSERID);

        // Teste dass alle Methoden aufgerufen wurden
        verify(altsystemSchuetzeMapper).toDO(new DsbMitgliedDO(), altsystemSchuetzeDO);
        verify(altsystemSchuetzeMapper).addDefaultFields(result, CURRENTUSERID);
        verify(dsbMitgliedComponent).create(result, CURRENTUSERID);
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Schuetze_Mannschaft, altsystemSchuetzeDO.getId(), result.getId(), "");
    }


    @Test
    public void testUpdate() throws SQLException {
        // prepare test data
        AltsystemSchuetzeDO altsystemSchuetzeDO = new AltsystemSchuetzeDO();
        altsystemSchuetzeDO.setId(1L);
        altsystemSchuetzeDO.setName("Bammert, Marco");


        String[] namen = new String[2];

        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setAltsystemId(1L);
        altsystemUebersetzungDO.setBogenligaId(1L);
        altsystemUebersetzungDO.setWert("MarcoBammert1");


        // Ergebnis Objekt
        DsbMitgliedDO result = new DsbMitgliedDO();
        result.setId(2L);
        result.setVorname("Marco");
        result.setNachname("Hallert");
        result.setVereinsId(2L);

        when(altsystemUebersetzung.findByAltsystemID(any(), any())).thenReturn(altsystemUebersetzungDO);

        // Mocks konfigurieren
        when(altsystemSchuetzeMapper.toDO(any(), any())).thenReturn(result);
        when(altsystemSchuetzeMapper.parseName(altsystemSchuetzeDO)).thenReturn(namen);
        when(dsbMitgliedComponent.create(result, CURRENTUSERID)).thenReturn(result);
        when(dsbMitgliedComponent.findById(altsystemSchuetzeDO.getId())).thenReturn(result);
        when(dsbMitgliedComponent.update(result, CURRENTUSERID)).thenReturn(result);


        // Testaufruf
        altsystemSchuetze.update(altsystemSchuetzeDO, CURRENTUSERID);


        // Teste dass alle Methoden aufgerufen wurden
        verify(altsystemUebersetzung).findByAltsystemID(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied,
                CURRENTUSERID);
        verify(dsbMitgliedComponent).findById(CURRENTUSERID);
        verify(altsystemSchuetzeMapper).parseName(altsystemSchuetzeDO);
        verify(dsbMitgliedComponent).update(result, CURRENTUSERID);

    }
}