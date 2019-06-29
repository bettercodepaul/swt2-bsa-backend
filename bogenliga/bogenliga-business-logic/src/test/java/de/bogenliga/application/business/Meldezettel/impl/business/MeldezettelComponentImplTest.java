package de.bogenliga.application.business.Meldezettel.impl.business;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mock.http.client.MockClientHttpRequest;
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.disziplin.impl.business.DisziplinComponentImplTest;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImpl;
import de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.business.MannschaftsmitgliedComponentImplTest;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.business.MatchComponentImplTest;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.business.VeranstaltungComponentImplTest;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.business.VereinComponentImplTest;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest;
import static org.mockito.Mockito.*;

/**
 * @author Nick Kerschagel
 */
public class MeldezettelComponentImplTest {

    private static final long MANNSCHAFTSID = 101;
    private static final long WETTKAMPFID = 30;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MatchComponent matchComponent;
    @Mock
    private WettkampfComponent wettkampfComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private DisziplinComponent disziplinComponent;
    @Mock
    private VereinComponent vereinComponent;
    @Mock
    private DsbMannschaftComponentImpl dsbMannschaftComponent;
    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    @Mock
    private DsbMitgliedComponent dsbMitgliedComponent;

    @InjectMocks
    private MeldezettelComponentImpl underTest;

    @Test
    public void getMeldezettelPDFasByteArray() {
        final List<MatchDO> matchDOList = getMatchesForWettkampf();

        DsbMitgliedDO dsbMitgliedDO = DsbMitgliedComponentImplTest.getDsbMitgliedDO();
        WettkampfDO wettkampfDO = WettkampfComponentImplTest.getWettkampfDO();
        VeranstaltungDO veranstaltungDO = VeranstaltungComponentImplTest.getVeranstaltungDO();
        DisziplinDO disziplinDO = DisziplinComponentImplTest.getDisziplinDO();
        MatchDO matchDO = MatchComponentImplTest.getMatchDO();
        DsbMannschaftDO dsbMannschaftDO = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
        VereinDO vereinDO = VereinComponentImplTest.getVereinDO();
        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            mannschaftsmitgliedDOList.add(MannschaftsmitgliedComponentImplTest.getMannschatfsmitgliedDO());
        }

        //configure Mocks
        when(matchComponent.findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong())).thenReturn(matchDO);
        when(matchComponent.findByWettkampfId(anyLong())).thenReturn(matchDOList);
        when(wettkampfComponent.findById(anyLong())).thenReturn(wettkampfDO);
        when(veranstaltungComponent.findById(anyLong())).thenReturn(veranstaltungDO);
        when(disziplinComponent.findById(any())).thenReturn(disziplinDO);
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(vereinComponent.findById(anyLong())).thenReturn(vereinDO);
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeam(anyLong())).thenReturn(mannschaftsmitgliedDOList);
        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(dsbMitgliedDO);


        //call test method
        final byte[] actual = underTest.getMeldezettelPDFasByteArray(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).isNotEmpty();

        //verify invocations
        verify(matchComponent).findByWettkampfId(anyLong());
    }

    private static List<MatchDO> getMatchesForWettkampf(){
        List<MatchDO> result = new ArrayList<>();
        //iterate through matches
        for (long match = 1; match <=7; match++){
            //iterate through encounter
            for(long encounter = 1; encounter <=4; encounter++){
                //iterate thorugh targets
                for(long i = 0; i <= 1; i++) {
                    MatchDO element = MatchComponentImplTest.getMatchDO();

                    element.setWettkampfId(WETTKAMPFID);
                    element.setNr(match);
                    element.setBegegnung(encounter);
                    element.setMannschaftId(MANNSCHAFTSID);

                    element.setScheibenNummer((encounter * 2) - 1 + i);

                    result.add(element);
                }
            }
        }

        return result;
    }
}