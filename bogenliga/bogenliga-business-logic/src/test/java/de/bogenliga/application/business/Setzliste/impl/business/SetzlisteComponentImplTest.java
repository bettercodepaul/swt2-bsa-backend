package de.bogenliga.application.business.Setzliste.impl.business;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.Setzliste.impl.dao.SetzlisteDAO;
import de.bogenliga.application.business.Setzliste.impl.entity.SetzlisteBE;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest;
import de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.business.MatchComponentImplTest;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.business.VeranstaltungComponentImpl;
import de.bogenliga.application.business.veranstaltung.impl.business.VeranstaltungComponentImplTest;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.business.VereinComponentImplTest;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest;
import static de.bogenliga.application.business.wettkampf.impl.business.WettkampfComponentImplTest.getWettkampfDO;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
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
        when(veranstaltungComponent.findById(wettkampfDO.getVeranstaltungsId())).thenReturn(veranstaltungDO);
        when(dsbMannschaftComponent.findById(anyLong())).thenReturn(dsbMannschaftDO);
        when(vereinComponent.findById(anyLong())).thenReturn(vereinDO);

        //call test method
        final byte[] actual = underTest.getPDFasByteArray(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).isNotEmpty();

        //verify invocations
        verify(SetzlisteDAO).getTableByWettkampfID(WETTKAMPFID);
    }


    @Test
    public void generateMatchesBySetzliste() {
        final List<SetzlisteBE> setzlisteBEList = getSetzlisteBEList();
        final List<MatchDO> matchDOList = new ArrayList<>();


        //configure Mocks
        when(SetzlisteDAO.getTableByWettkampfID(WETTKAMPFID)).thenReturn(setzlisteBEList);
        when(matchComponent.findByWettkampfId(WETTKAMPFID)).thenReturn(matchDOList);

        //call test method
        underTest.generateMatchesBySetzliste(WETTKAMPFID);

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