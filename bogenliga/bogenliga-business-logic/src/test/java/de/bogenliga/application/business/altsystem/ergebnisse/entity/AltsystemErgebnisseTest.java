package de.bogenliga.application.business.altsystem.ergebnisse.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.ergebnisse.dataobject.AltsystemErgebnisseDO;
import de.bogenliga.application.business.altsystem.ergebnisse.mapper.AltsystemPasseMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import static org.mockito.Mockito.*;

/**
 * Testet, dass die Klasse für Ergebnisse aus dem Altsystem die korrekten Methoden aufruft
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemErgebnisseTest {
    private static final Long CURRENTUSERID = 1L;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private AltsystemPasseMapper altsystemPasseMapper;
    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    private PasseComponent passeComponent;

    @InjectMocks
    private AltsystemErgebnisse altsystemErgebnisse;


    @Test
    public void testCreate() {
        // Altsystem Data Object
        AltsystemErgebnisseDO altsystemErgebnisseDO = new AltsystemErgebnisseDO();
        altsystemErgebnisseDO.setId(14L);
        altsystemErgebnisseDO.setSchuetze_Id(1L);
        altsystemErgebnisseDO.setErgebniss(47L);
        altsystemErgebnisseDO.setMatch(4);

        List<PasseDO> passen = new LinkedList<>();
        PasseDO testPasse = new PasseDO();
        testPasse.setId(1L);
        passen.add(testPasse);

        when(altsystemPasseMapper.toDO(new ArrayList<>(), altsystemErgebnisseDO)).thenReturn(passen);
        when(passeComponent.create(testPasse, CURRENTUSERID)).thenReturn(testPasse);

        altsystemErgebnisse.create(altsystemErgebnisseDO, CURRENTUSERID);

        verify(altsystemPasseMapper).toDO(new ArrayList<>(), altsystemErgebnisseDO);
        verify(passeComponent).create(testPasse, CURRENTUSERID);
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Ergebnis_Passen, altsystemErgebnisseDO.getId(), 0L, "1;");
    }

    @Test
    public void testUpdate() {
        // Altsystem Data Object
        AltsystemErgebnisseDO altsystemErgebnisseDO = new AltsystemErgebnisseDO();
        altsystemErgebnisseDO.setId(14L);
        altsystemErgebnisseDO.setSchuetze_Id(1L);
        altsystemErgebnisseDO.setErgebniss(47L);
        altsystemErgebnisseDO.setMatch(4);

        // Uebersetzung DO
        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setWert("1;");

        List<PasseDO> passen = new LinkedList<>();
        PasseDO testPasse = new PasseDO();
        testPasse.setId(1L);
        passen.add(testPasse);

        // configure Mocks
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Ergebnis_Passen, altsystemErgebnisseDO.getId())).thenReturn(altsystemUebersetzungDO);
        when(passeComponent.findById(1L)).thenReturn(testPasse);
        when(altsystemPasseMapper.recalculatePassen(any(), any())).thenReturn(passen);
        when(passeComponent.update(testPasse, CURRENTUSERID)).thenReturn(testPasse);

        altsystemErgebnisse.update(altsystemErgebnisseDO, CURRENTUSERID);

        verify(altsystemUebersetzung).findByAltsystemID(AltsystemUebersetzungKategorie.Ergebnis_Passen, altsystemErgebnisseDO.getId());
        verify(passeComponent).findById(1L);
        verify(altsystemPasseMapper).recalculatePassen(any(), any());
        verify(passeComponent).update(testPasse, CURRENTUSERID);
    }
}