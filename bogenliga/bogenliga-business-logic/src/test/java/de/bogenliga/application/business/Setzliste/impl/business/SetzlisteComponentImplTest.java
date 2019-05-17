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

    private static final long MANNSCHAFTSID = 5;
    private static final long WETTKAMPFID = 30;

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
            element.setMannschaftid(MANNSCHAFTSID+i);
            element.setWettkampfid(WETTKAMPFID);
            result.add(element);
        }
        return result;
    }

}