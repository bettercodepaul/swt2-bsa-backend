package de.bogenliga.application.business.altsystem.wettkampfdaten.mapper;

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
        altsystemWettkampfdatenDO.setMannschaftId(4);
        altsystemWettkampfdatenDO.setGegnerId(5);
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
        MatchDO[] expected = new MatchDO[2];
        expected[0] = new MatchDO();
        expected[0].setMannschaftId(mannschaftUebersetzung.getBogenligaId());
        expected[0].setSatzpunkte((long) altsystemWettkampfdatenDO.getSatzPlus());
        expected[0].setMatchpunkte((long) altsystemWettkampfdatenDO.getMatchPlus());
        expected[0].setNr((long) altsystemWettkampfdatenDO.getMatch());

        expected[1] = new MatchDO();
        expected[1].setMannschaftId(gegnerUebersetzung.getBogenligaId());
        expected[1].setSatzpunkte((long) altsystemWettkampfdatenDO.getSatzMinus());
        expected[1].setMatchpunkte((long) altsystemWettkampfdatenDO.getMatchMinus());
        expected[1].setNr((long) altsystemWettkampfdatenDO.getMatch());


        // configure mocks
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, altsystemWettkampfdatenDO.getMannschaftId())).thenReturn(mannschaftUebersetzung);
        when(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, altsystemWettkampfdatenDO.getGegnerId())).thenReturn(gegnerUebersetzung);

        // call test method
        MatchDO[] actual = new MatchDO[2];
        actual[0] = new MatchDO();
        actual[1] = new MatchDO();
        actual = altsystemMatchMapper.toDO(actual, altsystemWettkampfdatenDO);

        // assert result
        assertThat(actual[0].getMannschaftId()).isEqualTo(expected[0].getMannschaftId());
        assertThat(actual[0].getSatzpunkte()).isEqualTo(expected[0].getSatzpunkte());
        assertThat(actual[0].getMatchpunkte()).isEqualTo(expected[0].getMatchpunkte());
        assertThat(actual[0].getNr()).isEqualTo(expected[0].getNr());

        assertThat(actual[1].getMannschaftId()).isEqualTo(expected[1].getMannschaftId());
        assertThat(actual[1].getSatzpunkte()).isEqualTo(expected[1].getSatzpunkte());
        assertThat(actual[1].getMatchpunkte()).isEqualTo(expected[1].getMatchpunkte());
        assertThat(actual[1].getNr()).isEqualTo(expected[1].getNr());
    }

    @Test
    public void testAddDefaultFields() {
        // existing matches for Wettkampf
        List<MatchDO> matches = getMockMatches();
        List<WettkampfDO> wettkaempfe = getMockWettkaempfe();

        // configure mocks
        when(matchComponent.findByWettkampfId(anyLong())).thenReturn(matches);


        // call test method
        MatchDO[] actual = new MatchDO[2];
        actual[0] = new MatchDO();
        actual[0].setNr(14L);
        actual[1] = new MatchDO();
        actual = altsystemMatchMapper.addDefaultFields(actual, wettkaempfe);

        // assert result
        for(MatchDO match : actual){
            assertThat(match.getWettkampfId()).isEqualTo(2L);
            assertThat(match.getBegegnung()).isEqualTo(3L);
            assertThat(match.getStrafPunkteSatz1()).isEqualTo(0L);
            assertThat(match.getStrafPunkteSatz2()).isEqualTo(0L);
            assertThat(match.getStrafPunkteSatz3()).isEqualTo(0L);
            assertThat(match.getStrafPunkteSatz4()).isEqualTo(0L);
            assertThat(match.getStrafPunkteSatz5()).isEqualTo(0L);
        }

        assertThat(actual[0].getMatchScheibennummer()).isEqualTo((matches.size() % 8) + 1);
        assertThat(actual[1].getMatchScheibennummer()).isEqualTo((matches.size() % 8) + 2);

    }

    @Test
    public void getFirstWettkampfTag(){
        MatchDO matchDO = new MatchDO();
        matchDO.setNr(7L);
        WettkampfDO wettkampfDO = altsystemMatchMapper.getCurrentWettkampfTag(matchDO, getMockWettkaempfe());

        assertThat(wettkampfDO.getWettkampfTag()).isEqualTo(1L);
    }

    @Test
    public void getSecondWettkampfTag(){
        MatchDO matchDO = new MatchDO();
        matchDO.setNr(14L);
        WettkampfDO wettkampfDO = altsystemMatchMapper.getCurrentWettkampfTag(matchDO, getMockWettkaempfe());

        assertThat(wettkampfDO.getWettkampfTag()).isEqualTo(2L);
    }

    @Test
    public void getThirdWettkampfTag(){
        MatchDO matchDO = new MatchDO();
        matchDO.setNr(21L);
        WettkampfDO wettkampfDO = altsystemMatchMapper.getCurrentWettkampfTag(matchDO, getMockWettkaempfe());

        assertThat(wettkampfDO.getWettkampfTag()).isEqualTo(3L);
    }

    @Test
    public void getLastWettkampfTag(){
        MatchDO matchDO = new MatchDO();
        matchDO.setNr(28L);
        WettkampfDO wettkampfDO = altsystemMatchMapper.getCurrentWettkampfTag(matchDO, getMockWettkaempfe());

        assertThat(wettkampfDO.getWettkampfTag()).isEqualTo(4L);
    }
}