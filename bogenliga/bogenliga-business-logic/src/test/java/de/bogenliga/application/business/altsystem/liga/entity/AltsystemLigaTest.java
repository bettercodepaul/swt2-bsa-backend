package de.bogenliga.application.business.altsystem.liga.entity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import static org.mockito.Mockito.*;

/**
 * Testet, dass die Klasse für Ligen aus dem Altsystem die korrekten Methoden aufruft
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemLigaTest {
    // Constants for Mock Data
    private static final long CURRENTUSERID = 1L;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    AltsystemLigaMapper altsystemLigaMapper;
    @Mock
    AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    LigaComponent ligaComponent;

    @InjectMocks
    AltsystemLiga altsystemLiga;

    @Test
    public void testCreate() {
        // Altsystem Data Object
        AltsystemLigaDO altsystemLigaDO = new AltsystemLigaDO();
        altsystemLigaDO.setId(2L);
        // Ergebnis Objekt
        LigaDO result = new LigaDO();
        result.setId(1L);

        // Mocks konfigurieren
        when(altsystemLigaMapper.toDO(any(), any())).thenReturn(result);
        when(altsystemLigaMapper.addDefaultFields(result, CURRENTUSERID)).thenReturn(result);
        when(ligaComponent.create(result, CURRENTUSERID)).thenReturn(result);

        // Testaufruf
        altsystemLiga.create(altsystemLigaDO, CURRENTUSERID);

        // Test dass alle Methoden aufgerufen wurden
        verify(altsystemLigaMapper).toDO(new LigaDO(), altsystemLigaDO);
        verify(altsystemLigaMapper).addDefaultFields(result, CURRENTUSERID);
        verify(ligaComponent).create(result, CURRENTUSERID);
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Liga_Liga, altsystemLigaDO.getId(), result.getId(), "");
    }

    @Test
    public void testUpdate() {
        // Altsystem Data Object
        AltsystemLigaDO altsystemLigaDO = new AltsystemLigaDO();
        altsystemLigaDO.setId(2L);
        // Übersetzungsobjekt
        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setAltsystemId(2L);
        altsystemUebersetzungDO.setBogenligaId(4L);
        // Ergebnis Objekt
        LigaDO result = new LigaDO();
        result.setId(4L);

        // Mocks konfigurieren
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Liga_Liga, altsystemLigaDO.getId())).thenReturn(altsystemUebersetzungDO);
        when(ligaComponent.findById(altsystemUebersetzungDO.getBogenligaId())).thenReturn(result);
        when(altsystemLigaMapper.toDO(any(), any())).thenReturn(result);
        when(ligaComponent.update(result, CURRENTUSERID)).thenReturn(result);

        // Testaufruf
        altsystemLiga.update(altsystemLigaDO, CURRENTUSERID);

        // Test dass alle Methoden aufgerufen wurden
        verify(altsystemUebersetzung).findByAltsystemID(AltsystemUebersetzungKategorie.Liga_Liga, altsystemLigaDO.getId());
        verify(ligaComponent).findById(altsystemUebersetzungDO.getBogenligaId());
        verify(altsystemLigaMapper).toDO(result, altsystemLigaDO);
        verify(ligaComponent).update(result, CURRENTUSERID);
    }
}