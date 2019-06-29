package de.bogenliga.application.business.Bogenkontrollliste.impl.business;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.Schusszettel.impl.business.SchusszettelComponentImpl;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.business.MannschaftsmitgliedComponentImplTestAll;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.business.MatchComponentImplTest;
import de.bogenliga.application.business.match.impl.business.MatchComponentImplTestAll;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.business.VeranstaltungComponentImplTest;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.business.VereinComponentImplTest;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class BogenkontrolllisteComponentImplTest {

    private static final long VERANSTALTUNGSID = 1;
    private static final long WETTKAMPFID = 30;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;
    @Mock
    private VereinComponent vereinComponent;
    @Mock
    private WettkampfComponent wettkampfComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private MatchComponent matchComponent;
    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    @Mock
    private DsbMitgliedComponent dsbMitgliedComponent;

    @InjectMocks
    private BogenkontrolllisteComponentImpl underTest;

    @Test
    public void getBogenkontrolllistePDFasByteArray() {
        Hashtable<String, List<DsbMitgliedDO>> TeamMemberMapping = getTeamMemberMapping();

        DsbMannschaftDO dsbMannschaftDO = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
        VereinDO vereinDO = VereinComponentImplTest.getVereinDO();
        WettkampfDO wettkampfDO = WettkampfComponentImplTest.getWettkampfDO();
        VeranstaltungDO veranstaltungDO = VeranstaltungComponentImplTest.getVeranstaltungDO();
        List<MatchDO> matchDOList = new ArrayList<>();
        matchComponent.findAll();
        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = mannschaftsmitgliedComponent.findAllSchuetzeInTeam(dsbMannschaftDO.getId());
        DsbMitgliedDO dsbMitgliedDO = DsbMitgliedComponentImplTest.getDsbMitgliedDO();

        //configure Mocks
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(vereinComponent.findById(anyLong())).thenReturn(vereinDO);
        when(wettkampfComponent.findById(anyLong())).thenReturn(wettkampfDO);
        when(veranstaltungComponent.findById(anyLong())).thenReturn(veranstaltungDO);
        when(matchComponent.findByWettkampfId(anyLong())).thenReturn(matchDOList);
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeam(anyLong())).thenReturn(mannschaftsmitgliedDOList);
        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(dsbMitgliedDO);
    }

    private static Hashtable<String, List<DsbMitgliedDO>> getTeamMemberMapping(){
        Hashtable<String, List<DsbMitgliedDO>> TeamMemberMapping = new Hashtable<>();
        for(int i=1; i <= 8; i++){
            String TeamName = "Mannschaft" + i;
            List<DsbMitgliedDO> dsbMitgliedDOList = new ArrayList<>();
            for(int j = 0; j < 3; j++){
                dsbMitgliedDOList.add(DsbMitgliedComponentImplTest.getDsbMitgliedDO());
            }
            TeamMemberMapping.put(TeamName,dsbMitgliedDOList);
        }
        return TeamMemberMapping;
    }
}