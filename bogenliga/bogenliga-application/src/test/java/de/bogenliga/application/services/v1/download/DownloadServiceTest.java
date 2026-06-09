package de.bogenliga.application.services.v1.download;

import de.bogenliga.application.services.v1.setzliste.service.SetzlisteService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import de.bogenliga.application.business.setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;

import java.security.Principal;

import static org.mockito.Mockito.*;

/**
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 */
public class DownloadServiceTest {

    private static final int WETTKAMPF_ID = 30;
    private static final long VERANSTALTUNGS_ID = 0;
    private static final long MANSCHAFTS_ID = 101;
    private static final int JAHR = 2018;
    private static final long WETTKAMPFTAG = 1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Mock
    private SetzlisteComponent setzlisteComponent;

    @Mock
    private SetzlisteService setzlisteService;

    @Mock
    private WettkampfComponent wettkampfComponent;

    @InjectMocks
    private DownloadService DownloadService;

    @Before
    public void initMocks() {

    }


    @Test
    public void downloadSetzlistePdf() {

        final byte[] test = new byte[0];

        //configure Mocks
        when(setzlisteComponent.getPDFasByteArray(WETTKAMPF_ID)).thenReturn(test);
        when(setzlisteService.generateSetzliste(anyLong(), any()).iterator()).thenReturn(null);
        final Principal principal = new Principal() {
            @Override
            public String getName() {
                return String.valueOf("1");
            }
        };
        //call test method
        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadSetzlistePdf(WETTKAMPF_ID, principal);

        //assert result
        Assertions.assertThat(actual).isNotNull();

        //verify invocations
        verify(setzlisteComponent).getPDFasByteArray(WETTKAMPF_ID);

    }
    
    @Test
    public void downloadeinzelstatistikPdf()
    {
        final byte[] test = new byte[0];

        //configure Mocks
        when(wettkampfComponent.getPDFasByteArray("Einzelstatistik",VERANSTALTUNGS_ID,MANSCHAFTS_ID,JAHR)).thenReturn(test);

        //call Method
        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadEinzelstatistikPdf(VERANSTALTUNGS_ID,MANSCHAFTS_ID,JAHR);

        //result is nut NULL
        Assertions.assertThat(actual).isNotNull();

        //verify invocations
        verify(wettkampfComponent).getPDFasByteArray("Einzelstatistik",VERANSTALTUNGS_ID,MANSCHAFTS_ID,JAHR);
    }

    @Test
    public void downloadgesamtstatistikPdf()
    {
        final byte[] test = new byte[0];

        //configure Mocks
        when(wettkampfComponent.getPDFasByteArray("Gesamtstatistik",VERANSTALTUNGS_ID,MANSCHAFTS_ID,JAHR)).thenReturn(test);

        //call Method
        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadGesamtstatistikPdf(VERANSTALTUNGS_ID,MANSCHAFTS_ID,JAHR);

        //result is nut NULL
        Assertions.assertThat(actual).isNotNull();

        //verify invocations
        verify(wettkampfComponent).getPDFasByteArray("Gesamtstatistik",VERANSTALTUNGS_ID,MANSCHAFTS_ID,JAHR);
    }

    @Test
    public void downloadUebersichtPdf()
    {
        final byte[] test = new byte[0];

        //configure Mocks
        when(wettkampfComponent.getUebersichtPDFasByteArray(VERANSTALTUNGS_ID, WETTKAMPFTAG)).thenReturn(test);

        //call Method
        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadUebersichtPdf(VERANSTALTUNGS_ID, WETTKAMPFTAG);

        //result is nut NULL
        Assertions.assertThat(actual).isNotNull();

        //verify invocations
        verify(wettkampfComponent).getUebersichtPDFasByteArray(VERANSTALTUNGS_ID, WETTKAMPFTAG);
    }

    @Test
    public void downloadErgebnislistePdf()
    {
        final byte[] test = new byte[0];

        final WettkampfDO wettkampf = new WettkampfDO();
        wettkampf.setWettkampfVeranstaltungsId(VERANSTALTUNGS_ID);
        wettkampf.setWettkampfTag(WETTKAMPFTAG);

        // configure Mocks
        when(wettkampfComponent.findById(WETTKAMPF_ID)).thenReturn(wettkampf);
        when(wettkampfComponent.getUebersichtPDFasByteArray(VERANSTALTUNGS_ID, WETTKAMPFTAG))
                .thenReturn(test);

        // call Method
        final ResponseEntity<InputStreamResource> actual =
                DownloadService.downloadErgebnislistePdf(WETTKAMPF_ID);

        // result is not NULL
        Assertions.assertThat(actual).isNotNull();

        // verify invocations
        verify(wettkampfComponent).findById(WETTKAMPF_ID);
        verify(wettkampfComponent).getUebersichtPDFasByteArray(VERANSTALTUNGS_ID, WETTKAMPFTAG);
    }


}
