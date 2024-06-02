package de.bogenliga.application.business.altsystem.schuetze.entity;

import java.sql.SQLException;
import java.time.OffsetDateTime;

import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
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

import static org.assertj.core.api.Java6Assertions.assertThat;
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
    MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    @Mock
    AltsystemUebersetzungDAO altsystemUebersetzungDAO;

    @InjectMocks
    AltsystemSchuetze altsystemSchuetze;


    @Before
    public void setUp() {
        altsystemSchuetzeMapper = Mockito.mock(AltsystemSchuetzeMapper.class);
        dsbMitgliedComponent = Mockito.mock(DsbMitgliedComponent.class);
        mannschaftsmitgliedComponent = Mockito.mock(MannschaftsmitgliedComponent.class);
        altsystemUebersetzung = Mockito.mock(AltsystemUebersetzung.class);
        altsystemSchuetze = new AltsystemSchuetze(altsystemSchuetzeMapper, dsbMitgliedComponent, mannschaftsmitgliedComponent, altsystemUebersetzung);
        altsystemUebersetzungDAO = Mockito.mock(AltsystemUebersetzungDAO.class);
    }

    //Test create mit vorhandenem Eintrag in Übersetzungstabelle
    @Test
    public void testCreateExist() throws SQLException{
        // prepare test data
        AltsystemSchuetzeDO altsystemSchuetzeDO = new AltsystemSchuetzeDO();
        altsystemSchuetzeDO.setId(2L);
        altsystemSchuetzeDO.setMannschaft_id(22);
        altsystemSchuetzeDO.setName("Bammert, Marco");

        // Ergebnis Objekt
        DsbMitgliedDO result = new DsbMitgliedDO();
        result.setId(1L);
        result.setVorname("Marco");
        result.setNachname("Bammert");
        result.setVereinsId(1L);
        // Ergebnis Mannschaft

        AltsystemUebersetzungDO schuetzeUebersetzungDO = new AltsystemUebersetzungDO();
        schuetzeUebersetzungDO.setAltsystemId(1L);
        schuetzeUebersetzungDO.setBogenligaId(1L);
        schuetzeUebersetzungDO.setWert("MarcoBammert1");

        AltsystemUebersetzungDO mannschaftUebersetzungDO = new AltsystemUebersetzungDO();
        mannschaftUebersetzungDO.setAltsystemId(22L);
        mannschaftUebersetzungDO.setBogenligaId(2L);

        when(altsystemSchuetzeMapper.getIdentifier(any())).thenReturn("MarcoBammert1");
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied,2L)).thenReturn(schuetzeUebersetzungDO);
        when(altsystemUebersetzung.findByWert(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied,"MarcoBammert1")).thenReturn(schuetzeUebersetzungDO);
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, 22L)).thenReturn(mannschaftUebersetzungDO);
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, 22L)).thenReturn(mannschaftUebersetzungDO);

        altsystemSchuetze.create(altsystemSchuetzeDO, CURRENTUSERID);

        verify(altsystemUebersetzung).updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied, altsystemSchuetzeDO.getId(), result.getId(), "MarcoBammert1");
    }

    //Test create ohne Eintrag in Übersetzungstabelle
    @Test
    public void testCreate() throws SQLException{
        // prepare test data
        AltsystemSchuetzeDO altsystemSchuetzeDO = new AltsystemSchuetzeDO();
        altsystemSchuetzeDO.setId(2L);
        altsystemSchuetzeDO.setMannschaft_id(22);
        altsystemSchuetzeDO.setName("Bammert, Marco");

        // Ergebnis Objekt
        DsbMitgliedDO result = new DsbMitgliedDO();
        result.setId(1L);
        result.setVorname("Marco");
        result.setNachname("Bammert");
        result.setVereinsId(1L);
        // Ergebnis Mannschaft
        MannschaftsmitgliedDO mannschaftresult = new MannschaftsmitgliedDO(
                1l, 2L, 3L,1, "Marco", "Bammert", 7L);

        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setAltsystemId(1L);
        altsystemUebersetzungDO.setBogenligaId(1L);
        altsystemUebersetzungDO.setWert("MarcoBammert1");

        AltsystemUebersetzungDO altsystemMannschaftUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setAltsystemId(22L);
        altsystemUebersetzungDO.setBogenligaId(2L);

        when(altsystemSchuetzeMapper.getIdentifier(any())).thenReturn("MarcoBammert1");
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied,2L)).thenReturn(null);
        when(altsystemUebersetzung.findByWert(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied,"MarcoBammert1")).thenReturn(null);
        when(altsystemSchuetzeMapper.toDO(any(), any())).thenReturn(result);
        when(altsystemSchuetzeMapper.addDefaultFields(result, CURRENTUSERID)).thenReturn(result);
        when(dsbMitgliedComponent.create(result, CURRENTUSERID)).thenReturn(result);
        when(altsystemSchuetzeMapper.buildMannschaftsMitglied(anyLong(), anyLong(), any())).thenReturn(mannschaftresult);
        when(mannschaftsmitgliedComponent.create(any(), anyLong())).thenReturn(mannschaftresult);

         when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, 22L)).thenReturn(altsystemMannschaftUebersetzungDO);

        // Mocks konfigurieren
        // Testaufruf
        altsystemSchuetze.create(altsystemSchuetzeDO, CURRENTUSERID);

        // Teste dass alle Methoden aufgerufen wurden
        verify(altsystemSchuetzeMapper).toDO(new DsbMitgliedDO(), altsystemSchuetzeDO);
        verify(altsystemSchuetzeMapper).addDefaultFields(result, CURRENTUSERID);
        verify(dsbMitgliedComponent).create(result, CURRENTUSERID);
        verify(mannschaftsmitgliedComponent).create(mannschaftresult, CURRENTUSERID);
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied, altsystemSchuetzeDO.getId(), result.getId(), "MarcoBammert1");
    }


    @Test
    public void testUpdate() throws SQLException {
        // prepare test data
        AltsystemSchuetzeDO altsystemSchuetzeDO = new AltsystemSchuetzeDO();
        altsystemSchuetzeDO.setId(1L);
        altsystemSchuetzeDO.setMannschaft_id(22);
        altsystemSchuetzeDO.setName("Bammert, Marco");


        String[] namen = new String[2];

        AltsystemUebersetzungDO schuetzeUebersetzungDO = new AltsystemUebersetzungDO();
        schuetzeUebersetzungDO.setAltsystemId(1L);
        schuetzeUebersetzungDO.setBogenligaId(1L);
        schuetzeUebersetzungDO.setWert("MarcoBammert1");

        AltsystemUebersetzungDO mannschaftUebersetzungDO = new AltsystemUebersetzungDO();
        mannschaftUebersetzungDO.setAltsystemId(22L);
        mannschaftUebersetzungDO.setBogenligaId(2L);


        // Ergebnis Objekt
        DsbMitgliedDO result = new DsbMitgliedDO();
        result.setId(2L);
        result.setVorname("Marco");
        result.setNachname("Hallert");
        result.setVereinsId(2L);

        MannschaftsmitgliedDO mannschaftresult = new MannschaftsmitgliedDO(
                1l, 2L, 3L,1, "Marco", "Bammert", 7L);

        when(altsystemUebersetzung.findByAltsystemID(any(), any())).thenReturn(schuetzeUebersetzungDO);

        // Mocks konfigurieren
        when(altsystemSchuetzeMapper.toDO(any(), any())).thenReturn(result);
        when(altsystemSchuetzeMapper.parseName(altsystemSchuetzeDO)).thenReturn(namen);
        when(dsbMitgliedComponent.create(result, CURRENTUSERID)).thenReturn(result);
        when(dsbMitgliedComponent.findById(altsystemSchuetzeDO.getId())).thenReturn(result);
        when(dsbMitgliedComponent.update(result, CURRENTUSERID)).thenReturn(result);
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, 22L)).thenReturn(mannschaftUebersetzungDO);
        when(mannschaftsmitgliedComponent.findByMemberAndTeamId(anyLong(), anyLong())).thenReturn(mannschaftresult);
        when(mannschaftsmitgliedComponent.update(any(), anyLong())).thenReturn(mannschaftresult);


        // Testaufruf
        altsystemSchuetze.update(altsystemSchuetzeDO, CURRENTUSERID);


        // Teste dass alle Methoden aufgerufen wurden
        verify(altsystemUebersetzung).findByAltsystemID(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied,
                CURRENTUSERID);
        verify(dsbMitgliedComponent).findById(CURRENTUSERID);
        verify(altsystemSchuetzeMapper).parseName(altsystemSchuetzeDO);
        verify(dsbMitgliedComponent).update(result, CURRENTUSERID);
        verify(mannschaftsmitgliedComponent).update(mannschaftresult, CURRENTUSERID);

    }
}
