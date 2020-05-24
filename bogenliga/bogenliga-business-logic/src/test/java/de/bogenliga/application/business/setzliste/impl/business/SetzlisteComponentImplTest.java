package de.bogenliga.application.business.setzliste.impl.business;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.setzliste.impl.dao.SetzlisteDAO;
import de.bogenliga.application.business.setzliste.impl.entity.SetzlisteBE;
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
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.mockito.Mockito.*;

/**
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 */
public class SetzlisteComponentImplTest {

    private static final long MANNSCHAFTSID = 5;
    private static final long WETTKAMPFID = 30;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SetzlisteDAO SetzlisteDAO;
    @Mock
    private MatchComponent matchComponent;
    @Mock
    private WettkampfComponent wettkampfComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;
    @Mock
    private VereinComponent vereinComponent;


    @InjectMocks
    private SetzlisteComponentImpl underTest;

    @Captor
    private ArgumentCaptor<SetzlisteBE> SetzlisteBEArgumentCaptor;


    @Test
    public void getPDFasByteArray() {

        final List<SetzlisteBE> setzlisteBEList = getSetzlisteBEList();
        WettkampfDO wettkampfDO = WettkampfComponentImplTest.getWettkampfDO();
        VeranstaltungDO veranstaltungDO =  VeranstaltungComponentImplTest.getVeranstaltungDO();
        DsbMannschaftDO dsbMannschaftDO = DsbMannschaftComponentImplTest.getDsbMannschaftDO();
        VereinDO vereinDO = VereinComponentImplTest.getVereinDO();

        //configure Mocks
        when(SetzlisteDAO.getTableByWettkampfID(WETTKAMPFID)).thenReturn(setzlisteBEList);
        when(wettkampfComponent.findById(setzlisteBEList.get(0).getWettkampfid())).thenReturn(wettkampfDO);
        when(veranstaltungComponent.findById(wettkampfDO.getWettkampfVeranstaltungsId())).thenReturn(veranstaltungDO);
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(vereinComponent.findById(anyLong())).thenReturn(vereinDO);

        //call test method
        final byte[] actual = underTest.getPDFasByteArray(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).isNotEmpty();

        //verify invocations
        verify(SetzlisteDAO).getTableByWettkampfID(WETTKAMPFID);
    }

    @Test(expected = BusinessException.class)
    public void getPDFasByteArray_SetzlisteEmpty() {
        final List<SetzlisteBE> setzlisteBEList = new ArrayList<>();

        //configure Mocks
        when(SetzlisteDAO.getTableByWettkampfID(WETTKAMPFID)).thenReturn(setzlisteBEList);

        //call test method
        final byte[] actual = underTest.getPDFasByteArray(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).isEmpty();

        //verify invocations
        verify(SetzlisteDAO).getTableByWettkampfID(WETTKAMPFID);

    }


    @Test
    public void generateMatchesBySetzliste() {
        final List<SetzlisteBE> setzlisteBEList = getSetzlisteBEList();
        final List<MatchDO> matchDOList = new ArrayList<>();
        final MatchDO matchDO = MatchComponentImplTest.getMatchDO();
        matchDO.setWettkampfId(WETTKAMPFID);


        //configure Mocks
        when(SetzlisteDAO.getTableByWettkampfID(WETTKAMPFID)).thenReturn(setzlisteBEList);
        when(matchComponent.findByWettkampfId(WETTKAMPFID)).thenReturn(matchDOList);
        when(matchComponent.create(any(), anyLong())).thenReturn(matchDO);

        //call test method
        List<MatchDO> actual = underTest.generateMatchesBySetzliste(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).hasSize(56);
        for (MatchDO actualMatchDO: actual) {
            Assertions.assertThat(actualMatchDO.getWettkampfId()).isEqualTo(WETTKAMPFID);
        }

        //verify invocations
        verify(SetzlisteDAO).getTableByWettkampfID(WETTKAMPFID);

    }

    @Test(expected = BusinessException.class)
    public void generateMatchesBySetzliste_SetzlisteEmpty() {
        final List<SetzlisteBE> setzlisteBEList = new ArrayList<>();


        //configure Mocks
        when(SetzlisteDAO.getTableByWettkampfID(WETTKAMPFID)).thenReturn(setzlisteBEList);

        //call test method
        List<MatchDO> actual = underTest.generateMatchesBySetzliste(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).isEmpty();

        //verify invocations
        verify(SetzlisteDAO).getTableByWettkampfID(WETTKAMPFID);

    }

    @Test
    public void generateMatchesBySetzliste_MatchesExist() {
        final List<SetzlisteBE> setzlisteBEList = getSetzlisteBEList();
        final List<MatchDO> matchDOList = new ArrayList<>();
        for(int i=1;i<5;i++){
            MatchDO matchDO = MatchComponentImplTest.getMatchDO();
            matchDO.setWettkampfId(WETTKAMPFID);
            matchDOList.add(matchDO);
        }


        //configure Mocks
        when(SetzlisteDAO.getTableByWettkampfID(WETTKAMPFID)).thenReturn(setzlisteBEList);
        when(matchComponent.findByWettkampfId(WETTKAMPFID)).thenReturn(matchDOList);

        //call test method
        List<MatchDO> actual = underTest.generateMatchesBySetzliste(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).hasSize(4);
        for (MatchDO actualMatchDO: actual) {
            Assertions.assertThat(actualMatchDO.getWettkampfId()).isEqualTo(WETTKAMPFID);
        }

        //verify invocations
        verify(SetzlisteDAO).getTableByWettkampfID(WETTKAMPFID);

    }


    public static List<SetzlisteBE> getSetzlisteBEList(){
        List<SetzlisteBE> result = new ArrayList<>();
        for (int i = 1; i <= 8; i++){
            SetzlisteBE element = new SetzlisteBE();
            element.setLigatabelleTabellenplatz(i);
            element.setMannschaftid(MANNSCHAFTSID+i);
            element.setWettkampfid(WETTKAMPFID);
            result.add(element);
        }
        return result;
    }

}