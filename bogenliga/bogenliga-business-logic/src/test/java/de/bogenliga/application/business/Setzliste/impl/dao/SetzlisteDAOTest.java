package de.bogenliga.application.business.Setzliste.impl.dao;

import java.sql.Date;
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
    private static final String VEREIN_NAME = "Testverein";
    private static final Integer MANNSCHAFT_NUMMER = 1;
    private static final String VERANSTALTUNG_NAME = "Testveranstaltung";
    private static final Integer WETTKAMPF_TAG = 1;
    private static final Date WETTKAMPF_DATUM = new Date(9876543210987L);
    private static final String WETTKAMPF_BEGINN = "Jetzt";
    private static final String WETTKAMPF_ORT = "Testort";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private SetzlisteDAO underTest;


    @Test
    public void getTable() {
        //Prepare testdata
        final SetzlisteBE setzlisteBE = new SetzlisteBE();
        setzlisteBE.setLigatabelleTabellenplatz(LIGATABELLE_TABELLENPLATZ);
        setzlisteBE.setMannschaftNummer(MANNSCHAFT_NUMMER);
        setzlisteBE.setVeranstaltungName(VERANSTALTUNG_NAME);
        setzlisteBE.setVereinName(VEREIN_NAME);
        setzlisteBE.setWettkampfBeginn(WETTKAMPF_BEGINN);
        setzlisteBE.setWettkampfDatum(WETTKAMPF_DATUM);
        setzlisteBE.setWettkampfOrt(WETTKAMPF_ORT);
        setzlisteBE.setWettkampfTag(WETTKAMPF_TAG);

        //configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(setzlisteBE));

        //Call test method
        final List<SetzlisteBE> actual = underTest.getTable(0);
        for (SetzlisteBE entity : actual) {
            System.out.println(entity.toString());
        }
        //Assert result
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);

        //Assert that every field of every entity is not null

            assertThat(actual).isNotNull();
            assertThat(actual.get(0).getLigatabelleTabellenplatz()).isEqualTo(setzlisteBE.getLigatabelleTabellenplatz());
            assertThat(actual.get(0).getMannschaftNummer()).isEqualTo(setzlisteBE.getMannschaftNummer());
            assertThat(actual.get(0).getVeranstaltungName()).isEqualTo(setzlisteBE.getVeranstaltungName());
            assertThat(actual.get(0).getVereinName()).isEqualTo(setzlisteBE.getVereinName());
            assertThat(actual.get(0).getWettkampfBeginn()).isEqualTo(setzlisteBE.getWettkampfBeginn());
            assertThat(actual.get(0).getWettkampfDatum()).isEqualTo(setzlisteBE.getWettkampfDatum());
            assertThat(actual.get(0).getWettkampfOrt()).isEqualTo(setzlisteBE.getWettkampfOrt());
            assertThat(actual.get(0).getWettkampfTag()).isEqualTo(setzlisteBE.getWettkampfTag());


        //Verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
}