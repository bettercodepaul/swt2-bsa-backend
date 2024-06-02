package de.bogenliga.application.business.altsystem.ergebnisse.mapper;


import java.util.LinkedList;
import java.util.List;

import de.bogenliga.application.business.altsystem.wettkampfdaten.mapper.AltsystemMatchMapper;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.ergebnisse.dataobject.AltsystemErgebnisseDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemPasseMapperTest{

    private static final Long MATCH_ID = 4L;
    private static final int MATCH_NR = 1;
    private static final Long WETTKAMPF_ID = 17L;
    private static final Long DSBMITGLIED_ID = 10L;
    private static final Long MANNSCHAFT_ID = 5L;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private MatchComponent matchComponent;
    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private WettkampfComponent wettkampfComponent;
    @Mock
    private AltsystemMatchMapper altsystemMatchMapper;
    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;
    @InjectMocks
    private AltsystemPasseMapper altsystemPasseMapper;

    public static List<MatchDO> getMockMatches(){
        List<MatchDO> matches = new LinkedList<>();
        MatchDO matchDO = new MatchDO();
        matchDO.setId(MATCH_ID);
        matchDO.setNr(4L);
        matchDO.setWettkampfId(WETTKAMPF_ID);
        matchDO.setMannschaftId(1L);
        matches.add(matchDO);
        return matches;
    }
    public List<WettkampfDO> getMockWettkaempfe(){
        List<WettkampfDO> wettkaempfe = new LinkedList<>();
        for (long i = 0; i < 4; i++){
            WettkampfDO wettkampfDO = new WettkampfDO();
            wettkampfDO.setId(i + 1);
            wettkampfDO.setWettkampfTag(i + 1);
            wettkampfDO.setWettkampfVeranstaltungsId(1L);
            wettkaempfe.add(wettkampfDO);
        }
        return wettkaempfe;
    }

    @Test
    public void testToDO(){
        // Prepare Test data
        AltsystemErgebnisseDO altsystemErgebnisDO = new AltsystemErgebnisseDO();
        altsystemErgebnisDO.setId(5L);
        altsystemErgebnisDO.setErgebniss(77L);
        altsystemErgebnisDO.setMatch(MATCH_NR);
        altsystemErgebnisDO.setSchuetze_Id(2L);

        DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO(1L);
        dsbMannschaftDO.setVeranstaltungId(123L);
        dsbMannschaftDO.setId(1L);

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungGroesse(8);
        veranstaltungDO.setVeranstaltungID(123L);

        List<WettkampfDO> wettkaempfe = getMockWettkaempfe();

        // Prepare mocks
        // Schütze Übersetzung
        AltsystemUebersetzungDO schuetzeUebersetzung = new AltsystemUebersetzungDO();
        schuetzeUebersetzung.setBogenligaId(DSBMITGLIED_ID);

        // Mannschaft Übersetzung
        AltsystemUebersetzungDO mannschaftUebersetzung = new AltsystemUebersetzungDO();
        mannschaftUebersetzung.setBogenligaId(MANNSCHAFT_ID);

        // Match / Satz Übersetzung
        int anzahlSaetze = 4;
        AltsystemUebersetzungDO satzUebersetzung = new AltsystemUebersetzungDO();
        satzUebersetzung.setWert(String.valueOf(anzahlSaetze));

        // configure mocks
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied), anyLong())).thenReturn(schuetzeUebersetzung);

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Schuetze_Mannschaft), anyLong())).thenReturn(mannschaftUebersetzung);

        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(veranstaltungComponent.findById(anyLong())).thenReturn(veranstaltungDO);
        when(wettkampfComponent.findAllByVeranstaltungId(anyLong())).thenReturn(wettkaempfe);
        when(altsystemMatchMapper.getCurrentWettkampfTag(anyLong(), any())).thenReturn(wettkaempfe.get(0));
        when(altsystemMatchMapper.getCurrentBsappMatch(anyLong())).thenReturn(MATCH_ID);

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Match_Saetze), anyLong())).thenReturn(satzUebersetzung);

        when(matchComponent.findByWettkampfId(anyLong())).thenReturn(getMockMatches());

        // call test function
        List<PasseDO> passeList = altsystemPasseMapper.toDO(new LinkedList<>(), altsystemErgebnisDO);

        // asserts
        assertThat(passeList.size()).isEqualTo(anzahlSaetze);

        for(int i = 0; i < anzahlSaetze; i++){
            PasseDO currentPasse = passeList.get(i);
            assertThat(currentPasse.getPasseLfdnr()).isEqualTo(i + 1);
            assertThat(currentPasse.getPasseMatchId()).isEqualTo(MATCH_ID);
            assertThat(currentPasse.getPasseDsbMitgliedId()).isEqualTo(DSBMITGLIED_ID);
            assertThat(currentPasse.getPasseMannschaftId()).isEqualTo(1L);
            assertThat(currentPasse.getPasseMatchId()).isEqualTo(MATCH_ID);
            assertThat(currentPasse.getPasseMatchNr()).isEqualTo(4L);
            assertThat(currentPasse.getPasseWettkampfId()).isEqualTo(WETTKAMPF_ID);
        }

        PasseDO erstePasse = passeList.get(0);
        PasseDO zweitePasse = passeList.get(1);
        PasseDO drittePasse = passeList.get(2);
        PasseDO viertePasse = passeList.get(3);
        // Check ob die Punkte richtig aufgeteilt wurden
        assertThat(erstePasse.getPfeil1()).isEqualTo(10);
        assertThat(erstePasse.getPfeil2()).isEqualTo(10);
        assertThat(zweitePasse.getPfeil1()).isEqualTo(10);
        assertThat(zweitePasse.getPfeil2()).isEqualTo(10);
        assertThat(drittePasse.getPfeil1()).isEqualTo(10);
        assertThat(drittePasse.getPfeil2()).isEqualTo(9);
        assertThat(viertePasse.getPfeil1()).isEqualTo(9);
        assertThat(viertePasse.getPfeil2()).isEqualTo(9);

    }

    @Test
    public void testMatchDoesNotExist(){
        // Prepare Test data
        AltsystemErgebnisseDO altsystemErgebnisDO = new AltsystemErgebnisseDO();
        altsystemErgebnisDO.setId(5L);
        altsystemErgebnisDO.setErgebniss(78L);
        altsystemErgebnisDO.setMatch(MATCH_NR);
        altsystemErgebnisDO.setSchuetze_Id(2L);
        DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO(1L);
        dsbMannschaftDO.setVeranstaltungId(123L);
        dsbMannschaftDO.setId(1L);

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungGroesse(8);
        veranstaltungDO.setVeranstaltungID(123L);

        List<WettkampfDO> wettkaempfe = getMockWettkaempfe();


        // Prepare mocks
        // Schütze Übersetzung
        AltsystemUebersetzungDO schuetzeUebersetzung = new AltsystemUebersetzungDO();
        schuetzeUebersetzung.setBogenligaId(DSBMITGLIED_ID);

        // Mannschaft Übersetzung
        AltsystemUebersetzungDO mannschaftUebersetzung = new AltsystemUebersetzungDO();
        mannschaftUebersetzung.setBogenligaId(MANNSCHAFT_ID);

        // Match / Satz Übersetzung
        int anzahlSaetze = 4;
        AltsystemUebersetzungDO satzUebersetzung = new AltsystemUebersetzungDO();
        satzUebersetzung.setWert(String.valueOf(anzahlSaetze));

        // configure mocks
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied), anyLong())).thenReturn(schuetzeUebersetzung);

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Schuetze_Mannschaft), anyLong())).thenReturn(mannschaftUebersetzung);
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(veranstaltungComponent.findById(anyLong())).thenReturn(veranstaltungDO);
        when(wettkampfComponent.findAllByVeranstaltungId(anyLong())).thenReturn(wettkaempfe);
        when(altsystemMatchMapper.getCurrentWettkampfTag(anyLong(), any())).thenReturn(wettkaempfe.get(0));
        when(altsystemMatchMapper.getCurrentBsappMatch(anyLong())).thenReturn(MATCH_ID);


        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Match_Saetze), anyLong())).thenReturn(satzUebersetzung);

        when(matchComponent.findByMannschaftId(anyLong())).thenReturn(new LinkedList<>());

        // assert that the function throws an exception
        assertThatThrownBy(() -> altsystemPasseMapper.toDO(new LinkedList<>(), altsystemErgebnisDO))
                .isInstanceOf(BusinessException.class);

    }

    @Test
    public void testRecalculatePassen(){
        // Prepare Test data
        AltsystemErgebnisseDO altsystemErgebnisDO = new AltsystemErgebnisseDO();
        altsystemErgebnisDO.setId(5L);
        altsystemErgebnisDO.setErgebniss(87L);
        altsystemErgebnisDO.setMatch(MATCH_NR);
        altsystemErgebnisDO.setSchuetze_Id(2L);

        // Liste von existierenden Passen erstellen
        List<PasseDO> passen = new LinkedList<>();
        for(int i = 0; i < 5; i++){
            PasseDO passe = new PasseDO();
            passe.setPfeil1(10);
            passe.setPfeil2(9);
            passen.add(passe);
        }

        // Testfunktion aufrufen
        passen = altsystemPasseMapper.recalculatePassen(passen, altsystemErgebnisDO);

        // Prüfen dass die Punkte richtig auf die Passen verteilt wurden
        PasseDO erstePasse = passen.get(0);
        assertThat(erstePasse.getPfeil1()).isEqualTo(9);
        assertThat(erstePasse.getPfeil2()).isEqualTo(9);
        PasseDO zweitePasse = passen.get(1);
        assertThat(zweitePasse.getPfeil1()).isEqualTo(9);
        assertThat(zweitePasse.getPfeil2()).isEqualTo(9);
        PasseDO drittePasse = passen.get(2);
        assertThat(drittePasse.getPfeil1()).isEqualTo(9);
        assertThat(drittePasse.getPfeil2()).isEqualTo(9);
        PasseDO viertePasse = passen.get(3);
        assertThat(viertePasse.getPfeil1()).isEqualTo(9);
        assertThat(viertePasse.getPfeil2()).isEqualTo(8);
        PasseDO fuenftePasse = passen.get(4);
        assertThat(fuenftePasse.getPfeil1()).isEqualTo(8);
        assertThat(fuenftePasse.getPfeil2()).isEqualTo(8);
    }
}