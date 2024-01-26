package de.bogenliga.application.business.altsystem.wettkampfdaten.mapper;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject.AltsystemWettkampfdatenDO;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
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
        // mock Uebersetzung
        AltsystemUebersetzungDO mannschaftUebersetzung = new AltsystemUebersetzungDO();
        mannschaftUebersetzung.setBogenliga_id(10L);
        AltsystemUebersetzungDO gegnerUebersetzung = new AltsystemUebersetzungDO();
        gegnerUebersetzung.setBogenliga_id(12L);

        // expectedResult
        MatchDO[] expected = new MatchDO[2];
        expected[0] = new MatchDO();
        expected[0].setMannschaftId(mannschaftUebersetzung.getBogenliga_id());
        expected[0].setSatzpunkte((long) altsystemWettkampfdatenDO.getSatzPlus());
        expected[0].setMatchpunkte((long) altsystemWettkampfdatenDO.getMatchPlus());
        expected[0].setNr((long) altsystemWettkampfdatenDO.getMatch());

        expected[1] = new MatchDO();
        expected[1].setMannschaftId(gegnerUebersetzung.getBogenliga_id());
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

}