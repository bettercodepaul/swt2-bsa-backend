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
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class SetzlisteComponentImplTest {

    private static final int WETTKAMPFID = 1;
    private static final String vereinName = "TestVerein";
    private static final String veranstaltungName = "TestVeranstaltung";
    private static final Integer wettkampfTag = 1;
    private static final Date wettkampfDatum = new Date(987654321098L);
    private static final String wettkampfBeginn = "13.00";
    private static final String wettkampfOrt = "Reutlingen";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SetzlisteDAO SetzlisteDAO;


    @InjectMocks
    private SetzlisteComponentImpl underTest;

    @Captor
    private ArgumentCaptor<SetzlisteBE> SetzlisteBEArgumentCaptor;


    @Test
    public void getPDFasByteArray() {

        final List<SetzlisteBE> setzlisteBEList = getSetzlisteBEList();

        //configure Mocks
        when(SetzlisteDAO.getTable(WETTKAMPFID)).thenReturn(setzlisteBEList);

        //call test method
        final byte[] actual = underTest.getPDFasByteArray(WETTKAMPFID);

        //assert
        Assertions.assertThat(actual).isNotEmpty();

        //verify invocations
        verify(SetzlisteDAO).getTable(WETTKAMPFID);
    }

    private List<SetzlisteBE> getSetzlisteBEList(){
        List<SetzlisteBE> result = new ArrayList<>();
        for (int i = 1; i <= 8; i++){
            SetzlisteBE element = new SetzlisteBE();
            element.setLigatabelleTabellenplatz(i);
            element.setVereinName(vereinName+i);
            element.setMannschaftNummer(i);
            element.setVeranstaltungName(veranstaltungName);
            element.setWettkampfTag(wettkampfTag);
            element.setWettkampfDatum(wettkampfDatum);
            element.setWettkampfBeginn(wettkampfBeginn);
            element.setWettkampfOrt(wettkampfOrt);
            result.add(element);
        }
        return result;
    }

}