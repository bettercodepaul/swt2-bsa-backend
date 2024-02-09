package de.bogenliga.application.business.altsystem.wettkampfdaten.entity;


import java.util.LinkedList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject.AltsystemWettkampfdatenDO;
import de.bogenliga.application.business.altsystem.wettkampfdaten.mapper.AltsystemMatchMapper;
import de.bogenliga.application.business.altsystem.wettkampfdaten.mapper.AltsystemWettkampftagMapper;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *  Testet, dass die Klasse für Wettkampfdaten aus dem Altsystem die korrekten Methoden aufruft
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemWettkampfdatenTest {

    private static final long CURRENTUSERID = 1L;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    AltsystemWettkampftagMapper altsystemWettkampftagMapper;
    @Mock
    AltsystemMatchMapper altsystemMatchMapper;
    @Mock
    AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    MatchComponent matchComponent;

    @InjectMocks
    AltsystemWettkampfdaten altsystemWettkampfdaten;

    public static List<WettkampfDO> getMockWettkaempfe(){
        List<WettkampfDO> wettkaempfe = new LinkedList<>();
        wettkaempfe.add(new WettkampfDO());
        return wettkaempfe;
    }
    @Test
    public void testCreate_Sec0() {
        // Altsystem Data Object
        AltsystemWettkampfdatenDO altsystemWettkampfdatenDO = new AltsystemWettkampfdatenDO();
        altsystemWettkampfdatenDO.setId(2L);
        altsystemWettkampfdatenDO.setSatz1(45);
        altsystemWettkampfdatenDO.setSatz2(52);
        altsystemWettkampfdatenDO.setSatz3(56);
        altsystemWettkampfdatenDO.setSatz4(58);
        altsystemWettkampfdatenDO.setSec(0);
        // Ergebnis Objekt
        MatchDO[] result = new MatchDO[2];
        result[0] = new MatchDO();
        result[0].setId(1L);
        result[1] = new MatchDO();
        result[1].setId(2L);
        // wettkaempfe
        List<WettkampfDO> wettkaempfe = getMockWettkaempfe();

        // Mocks konfigurieren
        when(altsystemWettkampftagMapper.getOrCreateWettkampftage(altsystemWettkampfdatenDO, CURRENTUSERID)).thenReturn(wettkaempfe);
        when(altsystemMatchMapper.toDO(any(), any())).thenReturn(result);
        when(altsystemMatchMapper.addDefaultFields(result, wettkaempfe)).thenReturn(result);
        when(matchComponent.create(result[0], CURRENTUSERID)).thenReturn(result[0]);
        when(matchComponent.create(result[1], CURRENTUSERID)).thenReturn(result[1]);

        // Testaufruf
        altsystemWettkampfdaten.create(altsystemWettkampfdatenDO, CURRENTUSERID);

        // Test dass alle Methoden aufgerufen wurden
        verify(altsystemWettkampftagMapper).getOrCreateWettkampftage(altsystemWettkampfdatenDO, CURRENTUSERID);
        verify(altsystemMatchMapper).toDO(any(), any());
        verify(altsystemMatchMapper).addDefaultFields(result, wettkaempfe);
        verify(matchComponent).create(result[0], CURRENTUSERID);
        verify(matchComponent).create(result[1], CURRENTUSERID);
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Match_Saetze, result[0].getId(), 0L, String.valueOf(4));
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Wettkampfergebnis_Match, altsystemWettkampfdatenDO.getId(), result[0].getId(), String.valueOf(result[1].getId()));
    }

    @Test
    public void testUpdate_Sec0() {
        // Altsystem Data Object
        AltsystemWettkampfdatenDO altsystemWettkampfdatenDO = new AltsystemWettkampfdatenDO();
        altsystemWettkampfdatenDO.setId(2L);
        altsystemWettkampfdatenDO.setSatz1(45);
        altsystemWettkampfdatenDO.setSatz2(52);
        altsystemWettkampfdatenDO.setSatz3(56);
        altsystemWettkampfdatenDO.setSatz4(58);
        altsystemWettkampfdatenDO.setSatz5(51);
        altsystemWettkampfdatenDO.setSec(0);
        // Ergebnis Objekt
        MatchDO match1 = new MatchDO();
        match1.setId(1L);
        MatchDO match2 = new MatchDO();
        match2.setId(2L);
        MatchDO[] matches = {match1, match2};
        // wettkaempfe
        AltsystemUebersetzungDO uebersetzung = new AltsystemUebersetzungDO();
        uebersetzung.setBogenligaId(match1.getId());
        uebersetzung.setWert(String.valueOf(match2.getId()));

        // Mocks konfigurieren
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Wettkampfergebnis_Match, altsystemWettkampfdatenDO.getId())).thenReturn(uebersetzung);
        when(matchComponent.findById(uebersetzung.getBogenligaId())).thenReturn(match1);
        when(matchComponent.findById(Long.parseLong(uebersetzung.getWert()))).thenReturn(match2);
        when(altsystemMatchMapper.toDO(any(), any())).thenReturn(matches);
        when(matchComponent.update(match1, CURRENTUSERID)).thenReturn(match1);
        when(matchComponent.update(match2, CURRENTUSERID)).thenReturn(match2);

        // Testaufruf
        altsystemWettkampfdaten.update(altsystemWettkampfdatenDO, CURRENTUSERID);

        // Test dass alle Methoden aufgerufen wurden
        verify(altsystemMatchMapper).toDO(any(), any());
        verify(matchComponent).update(match1, CURRENTUSERID);
        verify(matchComponent).update(match2, CURRENTUSERID);
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Match_Saetze, match1.getId(), 0L, String.valueOf(5));
    }
}