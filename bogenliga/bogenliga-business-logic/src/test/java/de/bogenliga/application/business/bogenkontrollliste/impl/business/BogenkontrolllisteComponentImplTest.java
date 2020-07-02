package de.bogenliga.application.business.bogenkontrollliste.impl.business;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.business.MannschaftsmitgliedComponentImplTest;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.impl.business.MatchComponentImplTest;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.impl.business.VeranstaltungComponentImplTest;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.business.VereinComponentImplTest;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 *
 * @author Nick Kerschagel
 * @author Michael Hesse
 * @author Sebastian Eckl
 */
public class BogenkontrolllisteComponentImplTest {

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
    @Mock
    private LigaComponent ligaComponent;

    @InjectMocks
    private BogenkontrolllisteComponentImpl underTest;

    @Test
    public void getBogenkontrolllistePDFasByteArray() {
        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            mannschaftsmitgliedDOList.add(MannschaftsmitgliedComponentImplTest.getMannschatfsmitgliedDO());
        }

        //configure Mocks
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(anyLong())).thenReturn(mannschaftsmitgliedDOList);
        when(vereinComponent.findById(anyLong())).thenAnswer((Answer<VereinDO>) invocation -> {
            VereinDO ret = VereinComponentImplTest.getVereinDO();
            ret.setName("Verein " + UUID.randomUUID());
            return ret;
        });
        when(matchComponent.findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong())).thenReturn(MatchComponentImplTest.getMatchDO());
        when(wettkampfComponent.findById(anyLong())).thenReturn(WettkampfComponentImplTest.getWettkampfDO());
        when(veranstaltungComponent.findById(anyLong())).thenReturn(VeranstaltungComponentImplTest.getVeranstaltungDO());
        when(dsbMannschaftComponent.findById(anyLong())).thenAnswer((Answer<DsbMannschaftDO>) invocation -> {
            DsbMannschaftDO ret = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
            ret.setNummer((long)(Math.random() * 2 + 1));
            return ret;
        });
        List<LigaDO> ligen=new ArrayList<LigaDO>();
        ligen.add(LigaComponentImplTest.getLigaDO());
        when(ligaComponent.findAll()).thenReturn(ligen);

        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(DsbMitgliedComponentImplTest.getDsbMitgliedDO());


        //call test method
        final byte[] actual = underTest.getBogenkontrolllistePDFasByteArray(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).isNotEmpty();

        //verify invocations
        verify(matchComponent, atLeastOnce()).findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong());
    }
}