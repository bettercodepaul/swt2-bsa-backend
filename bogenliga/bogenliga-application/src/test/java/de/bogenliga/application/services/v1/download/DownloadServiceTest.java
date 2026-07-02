package de.bogenliga.application.services.v1.download;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.lizenz.api.LizenzComponent;
import de.bogenliga.application.business.setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.services.v1.setzliste.service.SetzlisteService;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
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

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
    
    private static final long DSB_MITGLIED_ID = 555;
    private static final long TEAM_ID = 666;
    private static final long VEREIN_ID = 777;
    private static final long VERANSTALTUNG_ID = 888;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Mock
    private SetzlisteComponent setzlisteComponent;

    @Mock
    private SetzlisteService setzlisteService;

    @Mock
    private WettkampfComponent wettkampfComponent;
    
    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;
    
    @Mock
    private LizenzComponent lizenzComponent;
    
    @Mock
    private RequiresOnePermissionAspect requiresOnePermissionAspect;

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

    @Test
    public void downloadLizenz_successWithSportleiter() {
        // prepare test data
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(TEAM_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        // configure mocks
        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        when(lizenzComponent.getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID)).thenReturn(testPdf);

        // call test method
        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID);

        // assert result
        Assertions.assertThat(actual).isNotNull();

        // verify invocations
        verify(dsbMannschaftComponent).findById(TEAM_ID);
        verify(requiresOnePermissionAspect).hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID);
        verify(lizenzComponent).getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID);
    }

    @Test
    public void downloadLizenz_successWithLigaleiter() {
        // prepare test data
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(TEAM_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        // configure mocks
        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(true);
        when(lizenzComponent.getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID)).thenReturn(testPdf);

        // call test method
        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID);

        // assert result
        Assertions.assertThat(actual).isNotNull();

        // verify invocations
        verify(dsbMannschaftComponent).findById(TEAM_ID);
        verify(requiresOnePermissionAspect).hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID);
        verify(lizenzComponent).getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID);
    }

    @Test
    public void downloadLizenz_teamNotFound() {
        // configure mocks
        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(null);

        // call test method and assert exception
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID))
                .withMessageContaining("Team not found");

        // verify invocations
        verify(dsbMannschaftComponent).findById(TEAM_ID);
        verify(lizenzComponent, never()).getLizenzPDFasByteArray(anyLong(), anyLong());
    }

    @Test
    public void downloadLizenz_noPermission() {
        // prepare test data
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(TEAM_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        // configure mocks
        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);

        // call test method and assert exception
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID))
                .withMessageContaining("User does not have permission to download license for team");

        // verify invocations
        verify(dsbMannschaftComponent).findById(TEAM_ID);
        verify(lizenzComponent, never()).getLizenzPDFasByteArray(anyLong(), anyLong());
    }

    @Test
    public void downloadLizenzenPdf_successWithSportleiter() {
        // prepare test data
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(MANSCHAFTS_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        // configure mocks
        when(dsbMannschaftComponent.findById(MANSCHAFTS_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        when(lizenzComponent.getMannschaftsLizenzenPDFasByteArray(MANSCHAFTS_ID)).thenReturn(testPdf);

        // call test method
        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenzenPdf(MANSCHAFTS_ID);

        // assert result
        Assertions.assertThat(actual).isNotNull();

        // verify invocations
        verify(dsbMannschaftComponent).findById(MANSCHAFTS_ID);
        verify(requiresOnePermissionAspect).hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID);
        verify(lizenzComponent).getMannschaftsLizenzenPDFasByteArray(MANSCHAFTS_ID);
    }

    @Test
    public void downloadLizenzenPdf_successWithLigaleiter() {
        // prepare test data
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(MANSCHAFTS_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        // configure mocks
        when(dsbMannschaftComponent.findById(MANSCHAFTS_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(true);
        when(lizenzComponent.getMannschaftsLizenzenPDFasByteArray(MANSCHAFTS_ID)).thenReturn(testPdf);

        // call test method
        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenzenPdf(MANSCHAFTS_ID);

        // assert result
        Assertions.assertThat(actual).isNotNull();

        // verify invocations
        verify(dsbMannschaftComponent).findById(MANSCHAFTS_ID);
        verify(requiresOnePermissionAspect).hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID);
        verify(lizenzComponent).getMannschaftsLizenzenPDFasByteArray(MANSCHAFTS_ID);
    }

    @Test
    public void downloadLizenzenPdf_teamNotFound() {
        // configure mocks
        when(dsbMannschaftComponent.findById(MANSCHAFTS_ID)).thenReturn(null);

        // call test method and assert exception
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadLizenzenPdf(MANSCHAFTS_ID))
                .withMessageContaining("Team not found");

        // verify invocations
        verify(dsbMannschaftComponent).findById(MANSCHAFTS_ID);
        verify(lizenzComponent, never()).getMannschaftsLizenzenPDFasByteArray(anyLong());
    }

    @Test
    public void downloadLizenzenPdf_noPermission() {
        // prepare test data
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(MANSCHAFTS_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        // configure mocks
        when(dsbMannschaftComponent.findById(MANSCHAFTS_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);

        // call test method and assert exception
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadLizenzenPdf(MANSCHAFTS_ID))
                .withMessageContaining("User does not have permission to download licenses for team");

        // verify invocations
        verify(dsbMannschaftComponent).findById(MANSCHAFTS_ID);
        verify(lizenzComponent, never()).getMannschaftsLizenzenPDFasByteArray(anyLong());
    }

}
