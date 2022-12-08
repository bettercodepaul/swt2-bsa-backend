package de.bogenliga.application.business.bogenkontrollliste.impl.business;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockingDetails;
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
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.passe.impl.business.PasseComponentImplTest;
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
 * @author Nick Kerschagel
 * @author Michael Hesse
 * @author Sebastian Eckl
 * @author Marcel Prostka
 */
public class BogenkontrolllisteComponentImplTest {

    // Erstelle Mock Klassen
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
    @Mock
    private PasseComponent passeComponent;


    // Injetzire Mocks in Testklasse
    @InjectMocks
    private BogenkontrolllisteComponentImpl underTest;

    @Test
    public void getBogenkontrolllistePDFasByteArray() {

        // Dummy Listen mit korrespondierenden Werten
        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            mannschaftsmitgliedDOList.add(MannschaftsmitgliedComponentImplTest.getMannschatfsmitgliedDO());
            // Setze variable werte für die Einsätze in der liga
            mannschaftsmitgliedDOList.get(i).setDsbMitgliedEingesetzt(i);
        }
        // Liga liste erstellen
        List<LigaDO> ligen=new ArrayList<LigaDO>();
        for(int i = 0; i < 3; i++) {
            // ligaDOs hinzufügen
            ligen.add(LigaComponentImplTest.getLigaDO());
            // werte setzen für unterschiedlliche Testfälle
            ligen.get(i).setLigaUebergeordnetId((long)i);
            ligen.get(i).setId((long)i+1);
        }
        // Wettkampfliste erstellen
        List<WettkampfDO> wettkampfDOList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            wettkampfDOList.add(WettkampfComponentImplTest.getWettkampfDO());
        }
        // PasseDO Liste erstellen
        List<PasseDO> passeDOList =new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            passeDOList.add(PasseComponentImplTest.getPasseDO());
        }
        // Veranstaltungliste erstellen
        List<VeranstaltungDO> veranstaltungDOList = new ArrayList<>();
        for (int i = 0; i < 4 ; i++){
            veranstaltungDOList.add(VeranstaltungComponentImplTest.getVeranstaltungDO());
            // Liga id setzen um mehrerre testfälle abzudecken
            veranstaltungDOList.get(i).setVeranstaltungLigaID((long)i);
        }

        //configure Mocks
        when(wettkampfComponent.findById(anyLong())).thenReturn(wettkampfDOList.get(0));
        when(veranstaltungComponent.findById(wettkampfDOList.get(1).getWettkampfVeranstaltungsId())).thenReturn(veranstaltungDOList.get(1));
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeam(anyLong())).thenReturn(mannschaftsmitgliedDOList);
        when(ligaComponent.findAll()).thenReturn(ligen);
        when(mannschaftsmitgliedComponent.findByMemberId(anyLong())).thenReturn(mannschaftsmitgliedDOList);
        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(DsbMitgliedComponentImplTest.getDsbMitgliedDO());
        when(wettkampfComponent.findAllWettkaempfeByMannschaftsId(anyLong())).thenReturn(wettkampfDOList);
        when(passeComponent.findByWettkampfIdAndMitgliedId(anyLong(),anyLong())).thenReturn(passeDOList);
        when(matchComponent.findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong())).thenReturn(MatchComponentImplTest.getMatchDO());

        // configure invocation mocks
        when(vereinComponent.findById(anyLong())).thenAnswer((Answer<VereinDO>) invocation -> {
            VereinDO ret = VereinComponentImplTest.getVereinDO();
            ret.setName("Verein " + UUID.randomUUID());
            return ret;
        });
        when(dsbMannschaftComponent.findById(anyLong())).thenAnswer((Answer<DsbMannschaftDO>) invocation -> {
            DsbMannschaftDO ret = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
            ret.setNummer((long)(Math.random() * 2 + 1));
            return ret;
        });

        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(DsbMitgliedComponentImplTest.getDsbMitgliedDO());

        // Testmethodenaufruf
        final byte[] actual = underTest.getBogenkontrolllistePDFasByteArray(WETTKAMPFID);

        // Prüfung: bytearray ist nicht leer
        Assertions.assertThat(actual).isNotEmpty();

        // verify invocations
        verify(matchComponent, atLeastOnce()).findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong());

    }


}