package de.bogenliga.application.business.Setzliste.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.Setzliste.impl.entity.SetzlisteBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class SetzlisteDAOTest {
    private static final Integer LIGATABELLE_TABELLENPLATZ = 1;
    private static final long MANNSCHAFTSID = 5;
    private static final long WETTKAMPFID = 30;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private SetzlisteDAO underTest;


    @Test
    public void getTableByWettkampfID() {
        //Prepare testdata
        final SetzlisteBE setzlisteBE = new SetzlisteBE();
        setzlisteBE.setLigatabelleTabellenplatz(LIGATABELLE_TABELLENPLATZ);
        setzlisteBE.setWettkampfid(WETTKAMPFID);
        setzlisteBE.setMannschaftid(MANNSCHAFTSID);

        //configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(setzlisteBE));

        //Call test method
        final List<SetzlisteBE> actual = underTest.getTableByWettkampfID(0);
        for (SetzlisteBE entity : actual) {
            System.out.println(entity.toString());
        }
        //Assert result
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);

        //Assert that every field of every entity is not null

            assertThat(actual).isNotNull();
            assertThat(actual.get(0).getLigatabelleTabellenplatz()).isEqualTo(setzlisteBE.getLigatabelleTabellenplatz());
            assertThat(actual.get(0).getMannschaftid()).isEqualTo(setzlisteBE.getMannschaftid());
            assertThat(actual.get(0).getWettkampfid()).isEqualTo(setzlisteBE.getWettkampfid());


        //Verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
}