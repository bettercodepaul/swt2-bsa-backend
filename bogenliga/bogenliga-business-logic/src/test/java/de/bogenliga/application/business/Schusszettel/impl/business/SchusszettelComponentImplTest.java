package de.bogenliga.application.business.Schusszettel.impl.business;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.Setzliste.impl.business.SetzlisteComponentImpl;
import de.bogenliga.application.business.Setzliste.impl.dao.SetzlisteDAO;
import de.bogenliga.application.business.Setzliste.impl.entity.SetzlisteBE;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 *
 * @author Michael Hesse
 */
public class SchusszettelComponentImplTest {

    private static final long MANNSCHAFTSID = 101;
    private static final long WETTKAMPFID = 30;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MatchComponent matchComponent;
    @Mock
    private WettkampfComponent wettkampfComponent;
    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;
    @Mock
    private VereinComponent vereinComponent;


    @InjectMocks
    private SchusszettelComponentImpl underTest;

    @Test
    public void getAllSchusszettelPDFasByteArray() {
        final List<MatchDO> matchDOList = getMatchesForWettkampf();

        WettkampfDO wettkampfDO = WettkampfComponentImplTest.getWettkampfDO();
        DsbMannschaftDO dsbMannschaftDO = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
        VereinDO vereinDO = VereinComponentImplTest.getVereinDO();

        //configure Mocks
        when(matchComponent.findByWettkampfId(anyLong())).thenReturn(matchDOList);
        when(wettkampfComponent.findById(anyLong())).thenReturn(wettkampfDO);
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(vereinComponent.findById(anyLong())).thenReturn(vereinDO);


        //call test method
        final byte[] actual = underTest.getAllSchusszettelPDFasByteArray(WETTKAMPFID);

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