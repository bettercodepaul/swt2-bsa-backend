package de.bogenliga.application.services.v1.download;

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
import de.bogenliga.application.business.Setzliste.api.SetzlisteComponent;
import static org.mockito.Mockito.*;

/**
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 */
public class DownloadServiceTest {

    private static final int WETTKAMPF_ID = 30;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Mock
    private SetzlisteComponent setzlisteComponent;

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

        //call test method
        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadSetzlistePdf(WETTKAMPF_ID);

        //assert result
        Assertions.assertThat(actual).isNotNull();

        //verify invocations
        verify(setzlisteComponent).getPDFasByteArray(WETTKAMPF_ID);

    }
}