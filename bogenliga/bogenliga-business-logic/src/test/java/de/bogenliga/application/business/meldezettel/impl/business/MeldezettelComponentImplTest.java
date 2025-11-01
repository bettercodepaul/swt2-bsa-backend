package de.bogenliga.application.business.meldezettel.impl.business;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.bogenliga.application.business.namemapping.api.NameMappingComponent;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAOext;
import de.bogenliga.application.business.vereine.impl.entity.VereinBEext;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.disziplin.impl.business.DisziplinComponentImplTest;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImpl;
import de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.business.MannschaftsmitgliedComponentImplTest;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.impl.business.MatchComponentImplTest;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.business.VeranstaltungComponentImplTest;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.business.VereinComponentImplTest;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest;
import static org.mockito.Mockito.*;

/**
 * @author Nick Kerschagel
 */
public class MeldezettelComponentImplTest {

    private static final long WETTKAMPFID = 30;
    private static int sizeTeam_8 = 8;
    private static int sizeTeam_6 = 6;
    private static int sizeTeam_4 = 4;

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
    @Mock
    private NameMappingComponent nameMappingComponent;
    @Mock
    private VereinDAOext vereinDAOext;

    @InjectMocks
    private MeldezettelComponentImpl underTest;

    @Test
    public void getMeldezettelPDFasByteArray_8_Teams() {

        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            mannschaftsmitgliedDOList.add(MannschaftsmitgliedComponentImplTest.getMannschatfsmitgliedDO());
        }
        List<DsbMannschaftDO> mannschaftDOList = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            DsbMannschaftDO ret = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
            ret.setNummer((long)(100+i));
            mannschaftDOList.add(ret);
        }
        VereinBEext vereinBEext = VereinComponentImplTest.getVereinBEext();

        //configure Mocks
        when(wettkampfComponent.findById(anyLong())).thenReturn(WettkampfComponentImplTest.getWettkampfDO());
        when(nameMappingComponent.getVeranstaltungsNameForVeranstaltungsId(anyLong())).thenReturn("VeranstaltungsName");
        when(nameMappingComponent.getDisziplinNameForDisziplinId(anyLong())).thenReturn("DisziplinName");

        when(dsbMannschaftComponent.findAllByVeranstaltungsId(anyLong())).thenReturn(mannschaftDOList);
        when(nameMappingComponent.getMannschaftsnameForVereinIDandMannschaftNr(anyLong(), anyLong())).thenAnswer(invocation -> {
            Long MannschaftNr = invocation.getArgument(1);
            return ("Vereiname " + MannschaftNr.toString()) ;
        });

        when(veranstaltungComponent.findById(anyLong())).thenReturn(VeranstaltungComponentImplTest.getVeranstaltungDO());
        when(disziplinComponent.findById(any())).thenReturn(DisziplinComponentImplTest.getDisziplinDO());
        when(matchComponent.findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong())).thenReturn(MatchComponentImplTest.getMatchDO());
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(anyLong())).thenReturn(mannschaftsmitgliedDOList);

        when(nameMappingComponent.getDsbMitgliedFullNameForDsbMitgliedId(anyLong())).thenReturn("DsbMitgliedName");

        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(DsbMitgliedComponentImplTest.getDsbMitgliedDO());


        //call test method
        final byte[] actual = underTest.getMeldezettelPDFasByteArray(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).isNotEmpty();

        //verify invocations
    }
    @Test
    public void getMeldezettelPDFasByteArray_6_Teams() {

        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            mannschaftsmitgliedDOList.add(MannschaftsmitgliedComponentImplTest.getMannschatfsmitgliedDO());
        }

        List<DsbMannschaftDO> mannschaftDOList = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            DsbMannschaftDO ret = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
            ret.setNummer((long)(100+i));
            mannschaftDOList.add(ret);
        }
        VereinBEext vereinBEext = VereinComponentImplTest.getVereinBEext();

        //configure Mocks
        when(wettkampfComponent.findById(anyLong())).thenReturn(WettkampfComponentImplTest.getWettkampfDO());
        when(nameMappingComponent.getVeranstaltungsNameForVeranstaltungsId(anyLong())).thenReturn("VeranstaltungsName");
        when(nameMappingComponent.getDisziplinNameForDisziplinId(anyLong())).thenReturn("DisziplinName");

        when(dsbMannschaftComponent.findAllByVeranstaltungsId(anyLong())).thenReturn(mannschaftDOList);
        when(nameMappingComponent.getMannschaftsnameForVereinIDandMannschaftNr(anyLong(), anyLong())).thenAnswer(invocation -> {
            Long MannschaftNr = invocation.getArgument(1);
            return ("Vereiname " + MannschaftNr.toString()) ;
        });

        when(veranstaltungComponent.findById(anyLong())).thenReturn(VeranstaltungComponentImplTest.getVeranstaltungDO());
        when(disziplinComponent.findById(any())).thenReturn(DisziplinComponentImplTest.getDisziplinDO());
        when(matchComponent.findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong())).thenReturn(MatchComponentImplTest.getMatchDO());
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(anyLong())).thenReturn(mannschaftsmitgliedDOList);

        when(nameMappingComponent.getDsbMitgliedFullNameForDsbMitgliedId(anyLong())).thenReturn("DsbMitgliedName");

        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(DsbMitgliedComponentImplTest.getDsbMitgliedDO());


        //call test method
        final byte[] actual = underTest.getMeldezettelPDFasByteArray(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).isNotEmpty();

        //verify invocations
    }
    @Test
    public void getMeldezettelPDFasByteArray_4_Teams() {

        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            mannschaftsmitgliedDOList.add(MannschaftsmitgliedComponentImplTest.getMannschatfsmitgliedDO());
        }

        List<DsbMannschaftDO> mannschaftDOList = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            DsbMannschaftDO ret = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
            ret.setNummer((long)(100+i));
            mannschaftDOList.add(ret);
        }
        VereinBEext vereinBEext = VereinComponentImplTest.getVereinBEext();

        //configure Mocks
        when(wettkampfComponent.findById(anyLong())).thenReturn(WettkampfComponentImplTest.getWettkampfDO());
        when(nameMappingComponent.getVeranstaltungsNameForVeranstaltungsId(anyLong())).thenReturn("VeranstaltungsName");
        when(nameMappingComponent.getDisziplinNameForDisziplinId(anyLong())).thenReturn("DisziplinName");

        when(dsbMannschaftComponent.findAllByVeranstaltungsId(anyLong())).thenReturn(mannschaftDOList);
        when(nameMappingComponent.getMannschaftsnameForVereinIDandMannschaftNr(anyLong(), anyLong())).thenAnswer(invocation -> {
            Long MannschaftNr = invocation.getArgument(1);
            return ("Vereiname " + MannschaftNr.toString()) ;
        });

        when(veranstaltungComponent.findById(anyLong())).thenReturn(VeranstaltungComponentImplTest.getVeranstaltungDO());
        when(disziplinComponent.findById(any())).thenReturn(DisziplinComponentImplTest.getDisziplinDO());
        when(matchComponent.findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong())).thenReturn(MatchComponentImplTest.getMatchDO());
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(anyLong())).thenReturn(mannschaftsmitgliedDOList);

        when(nameMappingComponent.getDsbMitgliedFullNameForDsbMitgliedId(anyLong())).thenReturn("DsbMitgliedName");

        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(DsbMitgliedComponentImplTest.getDsbMitgliedDO());

        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(DsbMitgliedComponentImplTest.getDsbMitgliedDO());


        //call test method
        final byte[] actual = underTest.getMeldezettelPDFasByteArray(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).isNotEmpty();

        //verify invocations
    }
}