package de.bogenliga.application.services.v1.download;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.lizenz.api.LizenzComponent;
import de.bogenliga.application.business.rueckennummern.api.RueckennummernComponent;
import de.bogenliga.application.business.setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
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
import java.sql.Date;

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

    /** One day in milliseconds */
    private static final long ONE_DAY_MS = 86_400_000L;

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
    private RueckennummernComponent rueckennummernComponent;

    @Mock
    private VeranstaltungComponent veranstaltungComponent;

    @Mock
    private RequiresOnePermissionAspect requiresOnePermissionAspect;

    @InjectMocks
    private DownloadService DownloadService;

    @Before
    public void initMocks() {

    }

    // ---- helpers ----

    private DsbMannschaftDO mannschaft() {
        return new DsbMannschaftDO(TEAM_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);
    }

    private VeranstaltungDO veranstaltungWithDeadline(Date deadline) {
        VeranstaltungDO v = new VeranstaltungDO();
        v.setVeranstaltungMeldeDeadline(deadline);
        return v;
    }

    /** Deadline was yesterday → download allowed */
    private Date pastDeadline() {
        return new Date(System.currentTimeMillis() - ONE_DAY_MS);
    }

    /** Deadline is tomorrow → download blocked */
    private Date futureDeadline() {
        return new Date(System.currentTimeMillis() + ONE_DAY_MS);
    }

    // ---- existing tests ----

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

    // ---- downloadLizenz ----

    @Test
    public void downloadLizenz_successWithSportleiter() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        // deadline already passed → download allowed
        when(veranstaltungComponent.findById(VERANSTALTUNG_ID))
                .thenReturn(veranstaltungWithDeadline(pastDeadline()));
        when(lizenzComponent.getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(lizenzComponent).getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID);
    }

    @Test
    public void downloadLizenz_sportleiterBlockedBeforeDeadline() {
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        when(veranstaltungComponent.findById(VERANSTALTUNG_ID))
                .thenReturn(veranstaltungWithDeadline(futureDeadline()));

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID))
                .matches(e -> e.getErrorCode() == ErrorCode.PDF_DOWNLOAD_BEFORE_MELDEDEADLINE);

        verify(lizenzComponent, never()).getLizenzPDFasByteArray(anyLong(), anyLong());
    }

    @Test
    public void downloadLizenz_ligaleiterNotBlockedByDeadline() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(true);
        when(lizenzComponent.getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID);

        Assertions.assertThat(actual).isNotNull();
        // veranstaltungComponent must NOT be consulted for Ligaleiter
        verify(veranstaltungComponent, never()).findById(anyLong());
        verify(lizenzComponent).getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID);
    }

    @Test
    public void downloadLizenz_noDeadlineSetAllowsSportleiter() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        // no deadline configured
        when(veranstaltungComponent.findById(VERANSTALTUNG_ID))
                .thenReturn(veranstaltungWithDeadline(null));
        when(lizenzComponent.getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(lizenzComponent).getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID);
    }

    @Test
    public void downloadLizenz_successWithLigaleiter() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(true);
        when(lizenzComponent.getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(dsbMannschaftComponent).findById(TEAM_ID);
        verify(requiresOnePermissionAspect).hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID);
        verify(lizenzComponent).getLizenzPDFasByteArray(DSB_MITGLIED_ID, TEAM_ID);
    }

    @Test
    public void downloadLizenz_teamNotFound() {
        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(null);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID))
                .withMessageContaining("Team not found");

        verify(dsbMannschaftComponent).findById(TEAM_ID);
        verify(lizenzComponent, never()).getLizenzPDFasByteArray(anyLong(), anyLong());
    }

    @Test
    public void downloadLizenz_noPermission() {
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadLizenz(DSB_MITGLIED_ID, TEAM_ID))
                .withMessageContaining("User does not have permission to download license for team");

        verify(dsbMannschaftComponent).findById(TEAM_ID);
        verify(lizenzComponent, never()).getLizenzPDFasByteArray(anyLong(), anyLong());
    }

    // ---- downloadLizenzenPdf ----

    @Test
    public void downloadLizenzenPdf_successWithSportleiter() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(MANSCHAFTS_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        when(dsbMannschaftComponent.findById(MANSCHAFTS_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        when(veranstaltungComponent.findById(VERANSTALTUNG_ID))
                .thenReturn(veranstaltungWithDeadline(pastDeadline()));
        when(lizenzComponent.getMannschaftsLizenzenPDFasByteArray(MANSCHAFTS_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenzenPdf(MANSCHAFTS_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(dsbMannschaftComponent).findById(MANSCHAFTS_ID);
        verify(requiresOnePermissionAspect).hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID);
        verify(lizenzComponent).getMannschaftsLizenzenPDFasByteArray(MANSCHAFTS_ID);
    }

    @Test
    public void downloadLizenzenPdf_sportleiterBlockedBeforeDeadline() {
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(MANSCHAFTS_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        when(dsbMannschaftComponent.findById(MANSCHAFTS_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        when(veranstaltungComponent.findById(VERANSTALTUNG_ID))
                .thenReturn(veranstaltungWithDeadline(futureDeadline()));

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadLizenzenPdf(MANSCHAFTS_ID))
                .matches(e -> e.getErrorCode() == ErrorCode.PDF_DOWNLOAD_BEFORE_MELDEDEADLINE);

        verify(lizenzComponent, never()).getMannschaftsLizenzenPDFasByteArray(anyLong());
    }

    @Test
    public void downloadLizenzenPdf_ligaleiterNotBlockedByDeadline() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(MANSCHAFTS_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        when(dsbMannschaftComponent.findById(MANSCHAFTS_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(true);
        when(lizenzComponent.getMannschaftsLizenzenPDFasByteArray(MANSCHAFTS_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenzenPdf(MANSCHAFTS_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(veranstaltungComponent, never()).findById(anyLong());
        verify(lizenzComponent).getMannschaftsLizenzenPDFasByteArray(MANSCHAFTS_ID);
    }

    @Test
    public void downloadLizenzenPdf_successWithLigaleiter() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(MANSCHAFTS_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        when(dsbMannschaftComponent.findById(MANSCHAFTS_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(true);
        when(lizenzComponent.getMannschaftsLizenzenPDFasByteArray(MANSCHAFTS_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadLizenzenPdf(MANSCHAFTS_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(dsbMannschaftComponent).findById(MANSCHAFTS_ID);
        verify(requiresOnePermissionAspect).hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID);
        verify(lizenzComponent).getMannschaftsLizenzenPDFasByteArray(MANSCHAFTS_ID);
    }

    @Test
    public void downloadLizenzenPdf_teamNotFound() {
        when(dsbMannschaftComponent.findById(MANSCHAFTS_ID)).thenReturn(null);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadLizenzenPdf(MANSCHAFTS_ID))
                .withMessageContaining("Team not found");

        verify(dsbMannschaftComponent).findById(MANSCHAFTS_ID);
        verify(lizenzComponent, never()).getMannschaftsLizenzenPDFasByteArray(anyLong());
    }

    @Test
    public void downloadLizenzenPdf_noPermission() {
        final DsbMannschaftDO mannschaft = new DsbMannschaftDO(MANSCHAFTS_ID, "Test Team", VEREIN_ID, 1L, 1L, VERANSTALTUNG_ID, 1L, 2024L);

        when(dsbMannschaftComponent.findById(MANSCHAFTS_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadLizenzenPdf(MANSCHAFTS_ID))
                .withMessageContaining("User does not have permission to download licenses for team");

        verify(dsbMannschaftComponent).findById(MANSCHAFTS_ID);
        verify(lizenzComponent, never()).getMannschaftsLizenzenPDFasByteArray(anyLong());
    }

    // ---- downloadRueckennummernPdf ----

    @Test
    public void downloadRueckennummernPdf_sportleiterBlockedBeforeDeadline() {
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        when(veranstaltungComponent.findById(VERANSTALTUNG_ID))
                .thenReturn(veranstaltungWithDeadline(futureDeadline()));

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadRueckennummernPdf(TEAM_ID))
                .matches(e -> e.getErrorCode() == ErrorCode.PDF_DOWNLOAD_BEFORE_MELDEDEADLINE);

        verify(rueckennummernComponent, never()).getMannschaftsRueckennummernPDFasByteArray(anyLong());
    }

    @Test
    public void downloadRueckennummernPdf_sportleiterAllowedAfterDeadline() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        when(veranstaltungComponent.findById(VERANSTALTUNG_ID))
                .thenReturn(veranstaltungWithDeadline(pastDeadline()));
        when(rueckennummernComponent.getMannschaftsRueckennummernPDFasByteArray(TEAM_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadRueckennummernPdf(TEAM_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(rueckennummernComponent).getMannschaftsRueckennummernPDFasByteArray(TEAM_ID);
    }

    @Test
    public void downloadRueckennummernPdf_ligaleiterNotBlockedByDeadline() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(true);
        when(rueckennummernComponent.getMannschaftsRueckennummernPDFasByteArray(TEAM_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadRueckennummernPdf(TEAM_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(veranstaltungComponent, never()).findById(anyLong());
        verify(rueckennummernComponent).getMannschaftsRueckennummernPDFasByteArray(TEAM_ID);
    }

    @Test
    public void downloadRueckennummernPdf_noDeadlineSetAllowsSportleiter() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        when(veranstaltungComponent.findById(VERANSTALTUNG_ID))
                .thenReturn(veranstaltungWithDeadline(null));
        when(rueckennummernComponent.getMannschaftsRueckennummernPDFasByteArray(TEAM_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadRueckennummernPdf(TEAM_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(rueckennummernComponent).getMannschaftsRueckennummernPDFasByteArray(TEAM_ID);
    }

    // ---- downloadRueckennummerPdf (single member) ----

    @Test
    public void downloadRueckennummerPdf_sportleiterBlockedBeforeDeadline() {
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        when(veranstaltungComponent.findById(VERANSTALTUNG_ID))
                .thenReturn(veranstaltungWithDeadline(futureDeadline()));

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> DownloadService.downloadRueckennummerPdf(TEAM_ID, DSB_MITGLIED_ID))
                .matches(e -> e.getErrorCode() == ErrorCode.PDF_DOWNLOAD_BEFORE_MELDEDEADLINE);

        verify(rueckennummernComponent, never()).getRueckennummerPDFasByteArray(anyLong(), anyLong());
    }

    @Test
    public void downloadRueckennummerPdf_sportleiterAllowedAfterDeadline() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(true);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(false);
        when(veranstaltungComponent.findById(VERANSTALTUNG_ID))
                .thenReturn(veranstaltungWithDeadline(pastDeadline()));
        when(rueckennummernComponent.getRueckennummerPDFasByteArray(TEAM_ID, DSB_MITGLIED_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadRueckennummerPdf(TEAM_ID, DSB_MITGLIED_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(rueckennummernComponent).getRueckennummerPDFasByteArray(TEAM_ID, DSB_MITGLIED_ID);
    }

    @Test
    public void downloadRueckennummerPdf_ligaleiterNotBlockedByDeadline() {
        final byte[] testPdf = new byte[0];
        final DsbMannschaftDO mannschaft = mannschaft();

        when(dsbMannschaftComponent.findById(TEAM_ID)).thenReturn(mannschaft);
        when(requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, VEREIN_ID))
                .thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, VERANSTALTUNG_ID))
                .thenReturn(true);
        when(rueckennummernComponent.getRueckennummerPDFasByteArray(TEAM_ID, DSB_MITGLIED_ID)).thenReturn(testPdf);

        final ResponseEntity<InputStreamResource> actual = DownloadService.downloadRueckennummerPdf(TEAM_ID, DSB_MITGLIED_ID);

        Assertions.assertThat(actual).isNotNull();
        verify(veranstaltungComponent, never()).findById(anyLong());
        verify(rueckennummernComponent).getRueckennummerPDFasByteArray(TEAM_ID, DSB_MITGLIED_ID);
    }

}
