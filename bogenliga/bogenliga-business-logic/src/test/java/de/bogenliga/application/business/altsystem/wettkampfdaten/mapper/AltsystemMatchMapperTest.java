package de.bogenliga.application.business.altsystem.wettkampfdaten.mapper;

import java.util.LinkedList;
import java.util.List;

import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
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
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemMatchMapperTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private MatchComponent matchComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;
    @InjectMocks
    private AltsystemMatchMapper altsystemMatchMapper;

    public List<MatchDO> getMockMatches(){
        List<MatchDO> matches = new LinkedList<>();
        matches.add(new MatchDO());
        matches.add(new MatchDO());
        matches.add(new MatchDO());
        matches.add(new MatchDO());
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
    public void testToDO() {
        // prepare test data
        // altsystem DO
        AltsystemWettkampfdatenDO altsystemWettkampfdatenDO = new AltsystemWettkampfdatenDO();
        altsystemWettkampfdatenDO.setId(1L);
        altsystemWettkampfdatenDO.setLigaID(3);
        altsystemWettkampfdatenDO.setMannschaft(4);
        altsystemWettkampfdatenDO.setGegner(5);
        altsystemWettkampfdatenDO.setMatch(10);
        altsystemWettkampfdatenDO.setSatzPlus(6);
        altsystemWettkampfdatenDO.setSatzMinus(2);
        altsystemWettkampfdatenDO.setMatchPlus(2);
        altsystemWettkampfdatenDO.setMatchMinus(0);
        altsystemWettkampfdatenDO.setSatz1(10);
        altsystemWettkampfdatenDO.setSatz2(10);
        altsystemWettkampfdatenDO.setSatz3(10);
        altsystemWettkampfdatenDO.setSatz4(10);
        altsystemWettkampfdatenDO.setSatz5(10);
        altsystemWettkampfdatenDO.setSaisonID(2);
        altsystemWettkampfdatenDO.setSec(1);
        // mock Uebersetzung
        AltsystemUebersetzungDO mannschaftUebersetzung = new AltsystemUebersetzungDO();
        mannschaftUebersetzung.setKategorie("Mannschaft_Mannschaft");
        mannschaftUebersetzung.setAltsystemId(2L);
        mannschaftUebersetzung.setBogenligaId(10L);
        AltsystemUebersetzungDO gegnerUebersetzung = new AltsystemUebersetzungDO();
        gegnerUebersetzung.setBogenligaId(12L);

        // expectedResult
        MatchDO expected = new MatchDO();
        expected = new MatchDO();
        expected.setMannschaftId(mannschaftUebersetzung.getBogenligaId());
        expected.setSatzpunkte((long) altsystemWettkampfdatenDO.getSatzPlus());
        expected.setMatchpunkte((long) altsystemWettkampfdatenDO.getMatchPlus());
        expected.setNr((long) altsystemWettkampfdatenDO.getMatch());



        // configure mocks
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, altsystemWettkampfdatenDO.getMannschaft())).thenReturn(mannschaftUebersetzung);

        // call test method
        MatchDO actual = new MatchDO();
        actual = altsystemMatchMapper.toDO(actual, altsystemWettkampfdatenDO);

        // assert result
        assertThat(actual.getMannschaftId()).isEqualTo(expected.getMannschaftId());
        assertThat(actual.getSatzpunkte()).isEqualTo(expected.getSatzpunkte());
        assertThat(actual.getMatchpunkte()).isEqualTo(expected.getMatchpunkte());
        assertThat(actual.getNr()).isEqualTo(expected.getNr());

    }

    @Test
    public void testAddDefaultFields() {
        // existing matches for Wettkampf
        List<MatchDO> matches = getMockMatches();
        List<WettkampfDO> wettkaempfe = getMockWettkaempfe();
        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungGroesse(8);
        // configure mocks
        when(veranstaltungComponent.findById(anyLong())).thenReturn(veranstaltungDO);

        // configure mocks
        when(matchComponent.findByWettkampfId(anyLong())).thenReturn(matches);


        // call test method
        MatchDO actual = new MatchDO();
        actual.setNr(14L);
        actual = altsystemMatchMapper.addDefaultFields(actual, wettkaempfe);

        // assert result
        assertThat(actual.getWettkampfId()).isEqualTo(2L);
        assertThat(actual.getBegegnung()).isEqualTo(3L);
        assertThat(actual.getStrafPunkteSatz1()).isEqualTo(0L);
        assertThat(actual.getStrafPunkteSatz2()).isEqualTo(0L);
        assertThat(actual.getStrafPunkteSatz3()).isEqualTo(0L);
        assertThat(actual.getStrafPunkteSatz4()).isEqualTo(0L);
        assertThat(actual.getStrafPunkteSatz5()).isEqualTo(0L);

        assertThat(actual.getMatchScheibennummer()).isEqualTo(((matches.size() % 8)+1) );

    }

    @Test
    public void getFirstWettkampfTag(){
        MatchDO matchDO = new MatchDO();
        matchDO.setNr(7L);
        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungGroesse(8);
        // configure mocks
        when(veranstaltungComponent.findById(anyLong())).thenReturn(veranstaltungDO);
        WettkampfDO wettkampfDO = altsystemMatchMapper.getCurrentWettkampfTag(matchDO.getNr(), getMockWettkaempfe());

        assertThat(wettkampfDO.getWettkampfTag()).isEqualTo(1L);
    }

    @Test
    public void getSecondWettkampfTag(){
        MatchDO matchDO = new MatchDO();
        matchDO.setNr(14L);
        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungGroesse(8);
        // configure mocks
        when(veranstaltungComponent.findById(anyLong())).thenReturn(veranstaltungDO);
        WettkampfDO wettkampfDO = altsystemMatchMapper.getCurrentWettkampfTag(matchDO.getNr(), getMockWettkaempfe());

        assertThat(wettkampfDO.getWettkampfTag()).isEqualTo(2L);
    }

    @Test
    public void getThirdWettkampfTag(){
        MatchDO matchDO = new MatchDO();
        matchDO.setNr(21L);
        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungGroesse(8);
        // configure mocks
        when(veranstaltungComponent.findById(anyLong())).thenReturn(veranstaltungDO);

        WettkampfDO wettkampfDO = altsystemMatchMapper.getCurrentWettkampfTag(matchDO.getNr(), getMockWettkaempfe());

        assertThat(wettkampfDO.getWettkampfTag()).isEqualTo(3L);
    }

    @Test
    public void getLastWettkampfTag(){
        MatchDO matchDO = new MatchDO();
        matchDO.setNr(28L);
        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungGroesse(8);
        // configure mocks
        when(veranstaltungComponent.findById(anyLong())).thenReturn(veranstaltungDO);
        WettkampfDO wettkampfDO = altsystemMatchMapper.getCurrentWettkampfTag(matchDO.getNr(),  getMockWettkaempfe());

        assertThat(wettkampfDO.getWettkampfTag()).isEqualTo(4L);
    }
}