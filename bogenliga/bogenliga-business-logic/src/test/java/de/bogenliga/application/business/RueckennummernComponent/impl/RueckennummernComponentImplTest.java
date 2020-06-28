package de.bogenliga.application.business.RueckennummernComponent.impl;

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
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.business.LigaComponentImplTest;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.business.MannschaftsmitgliedComponentImplTest;
import de.bogenliga.application.business.match.impl.business.MatchComponentImplTest;
import de.bogenliga.application.business.rueckennummern.impl.business.RueckennummernComponentImpl;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.impl.business.VeranstaltungComponentImplTest;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.business.VereinComponentImplTest;
import de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 *
 * @author David Winkler
 */
public class RueckennummernComponentImplTest {

    private static final long MANNSCHAFTSID = 30;
    private static final long DSBMITGLIEDID = 30;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;
    @Mock
    private VereinComponent vereinComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    @Mock
    private DsbMitgliedComponent dsbMitgliedComponent;

    @InjectMocks
    private RueckennummernComponentImpl underTest;

    @Test
    public void getRueckennummerPDFasByteArray() {

        //configure Mocks
        when(vereinComponent.findById(anyLong())).thenAnswer((Answer<VereinDO>) invocation -> {
            VereinDO ret = VereinComponentImplTest.getVereinDO();
            ret.setName("Verein " + UUID.randomUUID());
            return ret;
        });
        when(veranstaltungComponent.findById(anyLong())).thenReturn(VeranstaltungComponentImplTest.getVeranstaltungDO());
        when(dsbMannschaftComponent.findById(anyLong())).thenAnswer((Answer<DsbMannschaftDO>) invocation -> {
            DsbMannschaftDO ret = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
            ret.setNummer((long)(Math.random() * 2 + 1));
            return ret;
        });
        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(DsbMitgliedComponentImplTest.getDsbMitgliedDO());
        when(mannschaftsmitgliedComponent.findByMemberAndTeamId(anyLong(),anyLong())).thenReturn(MannschaftsmitgliedComponentImplTest.getMannschatfsmitgliedDO());

        //call test method
        final byte[] actual = underTest.getRueckennummerPDFasByteArray(MANNSCHAFTSID,DSBMITGLIEDID);

        //assert
        Assertions.assertThat(actual).isNotEmpty();

        //verify invocations
        verify(dsbMannschaftComponent, atLeastOnce()).findById(anyLong());
        verify(veranstaltungComponent, atLeastOnce()).findById(anyLong());
        verify(dsbMitgliedComponent, atLeastOnce()).findById(anyLong());
        verify(vereinComponent, atLeastOnce()).findById(anyLong());
    }

    @Test
    public void getMannschaftsRueckennummernPDFasByteArray(){

        //configure Mocks
        when(vereinComponent.findById(anyLong())).thenAnswer((Answer<VereinDO>) invocation -> {
            VereinDO ret = VereinComponentImplTest.getVereinDO();
            ret.setName("Verein " + UUID.randomUUID());
            return ret;
        });
        when(veranstaltungComponent.findById(anyLong())).thenReturn(VeranstaltungComponentImplTest.getVeranstaltungDO());
        when(dsbMannschaftComponent.findById(anyLong())).thenAnswer((Answer<DsbMannschaftDO>) invocation -> {
            DsbMannschaftDO ret = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
            ret.setNummer((long)(Math.random() * 2 + 1));
            return ret;
        });
        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(DsbMitgliedComponentImplTest.getDsbMitgliedDO());

        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            mannschaftsmitgliedDOList.add(MannschaftsmitgliedComponentImplTest.getMannschatfsmitgliedDO());
        }
        when(mannschaftsmitgliedComponent.findByTeamId(anyLong())).thenReturn(mannschaftsmitgliedDOList);


        //call test method
        final byte[] actual = underTest.getMannschaftsRueckennummernPDFasByteArray(MANNSCHAFTSID);

        //assert
        Assertions.assertThat(actual).isNotEmpty();

        //verify invocations
        verify(dsbMannschaftComponent, atLeastOnce()).findById(anyLong());
        verify(mannschaftsmitgliedComponent, atLeastOnce()).findByTeamId(anyLong());
        verify(veranstaltungComponent, atLeastOnce()).findById(anyLong());
        verify(dsbMitgliedComponent, atLeastOnce()).findById(anyLong());
        verify(vereinComponent, atLeastOnce()).findById(anyLong());
    }
}