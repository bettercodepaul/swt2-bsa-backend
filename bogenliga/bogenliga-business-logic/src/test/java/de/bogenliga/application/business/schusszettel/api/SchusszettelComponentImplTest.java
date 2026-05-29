package de.bogenliga.application.business.schusszettel.api;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SchusszettelComponentImplTest {

    @Mock private MatchComponent matchComponent;
    @Mock private PasseComponent passeComponent;
    @Mock private DsbMannschaftComponent dsbMannschaftComponent;
    @Mock private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    @Mock private VereinComponent vereinComponent;
    @Mock private WettkampfComponent wettkampfComponent;
    @Mock private VeranstaltungComponent veranstaltungComponent;
    @Mock private TabletSchusszettelDAO tabletSchusszettelDAO;

    private SchusszettelComponentImpl underTest;

    private static final long WETTKAMPF_ID = 10L;
    private static final long TEAM_ID_1 = 101L;
    private static final long TEAM_ID_2 = 102L;
    private static final long VEREIN_ID_1 = 201L;
    private static final long VEREIN_ID_2 = 202L;
    private static final long VERANSTALTUNG_ID = 5L;

    @Before
    public void setUp() throws Exception {
        underTest = new SchusszettelComponentImpl(
            matchComponent, passeComponent, dsbMannschaftComponent,
            mannschaftsmitgliedComponent, vereinComponent, wettkampfComponent,
            veranstaltungComponent, tabletSchusszettelDAO
        );
        setFrontendUrl("http://localhost:4200");
        setupCommonMocks();
    }

    private void setFrontendUrl(String url) throws Exception {
        Field field = SchusszettelComponentImpl.class.getDeclaredField("frontendUrl");
        field.setAccessible(true);
        field.set(underTest, url);
    }

    private void setupCommonMocks() {
        WettkampfDO wettkampfDO = new WettkampfDO();
        wettkampfDO.setWettkampfTag(1L);
        wettkampfDO.setWettkampfVeranstaltungsId(VERANSTALTUNG_ID);
        lenient().when(wettkampfComponent.findById(WETTKAMPF_ID)).thenReturn(wettkampfDO);

        DsbMannschaftDO mannschaft1 = new DsbMannschaftDO();
        mannschaft1.setId(TEAM_ID_1);
        mannschaft1.setVereinId(VEREIN_ID_1);
        mannschaft1.setNummer(1L);
        lenient().when(dsbMannschaftComponent.findById(TEAM_ID_1)).thenReturn(mannschaft1);

        DsbMannschaftDO mannschaft2 = new DsbMannschaftDO();
        mannschaft2.setId(TEAM_ID_2);
        mannschaft2.setVereinId(VEREIN_ID_2);
        mannschaft2.setNummer(1L);
        lenient().when(dsbMannschaftComponent.findById(TEAM_ID_2)).thenReturn(mannschaft2);

        VereinDO verein1 = new VereinDO();
        verein1.setId(VEREIN_ID_1);
        verein1.setName("BSG Reutlingen");
        lenient().when(vereinComponent.findById(VEREIN_ID_1)).thenReturn(verein1);

        VereinDO verein2 = new VereinDO();
        verein2.setId(VEREIN_ID_2);
        verein2.setName("BSG Stuttgart");
        lenient().when(vereinComponent.findById(VEREIN_ID_2)).thenReturn(verein2);

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungGroesse(8);
        lenient().when(veranstaltungComponent.findById(VERANSTALTUNG_ID)).thenReturn(veranstaltungDO);
    }

    private MatchDO[] createTestMatches() {
        MatchDO match1 = new MatchDO(1L, 1L, WETTKAMPF_ID, TEAM_ID_1, 1L, 1L, 0L, 0L, null, null, null, null, null);
        MatchDO match2 = new MatchDO(2L, 1L, WETTKAMPF_ID, TEAM_ID_2, 1L, 2L, 0L, 0L, null, null, null, null, null);
        return new MatchDO[]{match1, match2};
    }

    @Test
    public void getAllSchusszettelPDFasByteArray_negativeId_throwsBusinessException() {
        assertThatThrownBy(() -> underTest.getAllSchusszettelPDFasByteArray(-1L))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void getAllSchusszettelPDFasByteArray_emptyMatchList_throwsBusinessException() {
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> underTest.getAllSchusszettelPDFasByteArray(WETTKAMPF_ID))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void generateSchusszettelPage_withSessionPresent_includesQrCode() throws Exception {
        MatchDO[] matches = createTestMatches();

        TabletSchusszettelEntity session = new TabletSchusszettelEntity();
        session.setToken("test-token-abc");
        when(tabletSchusszettelDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID_1)).thenReturn(Optional.of(session));
        when(tabletSchusszettelDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID_2)).thenReturn(Optional.empty());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc, PageSize.A4)) {
            underTest.generateSchusszettelPage(doc, matches);
        }

        verify(tabletSchusszettelDAO).findByWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID_1);
        verify(tabletSchusszettelDAO).findByWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID_2);
    }

    @Test
    public void generateSchusszettelPage_withNoSession_skipsQrCode() throws Exception {
        MatchDO[] matches = createTestMatches();
        when(tabletSchusszettelDAO.findByWettkampfUndTeam(anyLong(), anyLong())).thenReturn(Optional.empty());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc, PageSize.A4)) {
            underTest.generateSchusszettelPage(doc, matches);
        }

        verify(tabletSchusszettelDAO).findByWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID_1);
        verify(tabletSchusszettelDAO).findByWettkampfUndTeam(WETTKAMPF_ID, TEAM_ID_2);
    }

    @Test
    public void generateSchusszettelPage_withPlatzhalterTeam1_skipsFirstIteration() throws Exception {
        MatchDO match1 = new MatchDO(1L, 1L, WETTKAMPF_ID, TEAM_ID_1, 1L, 1L, 0L, 0L, null, null, null, null, null);
        MatchDO match2 = new MatchDO(2L, 1L, WETTKAMPF_ID, TEAM_ID_2, 1L, 2L, 0L, 0L, null, null, null, null, null);

        DsbMannschaftDO platzhalter = new DsbMannschaftDO();
        platzhalter.setId(TEAM_ID_1);
        platzhalter.setVereinId(99L);
        platzhalter.setNummer(1L);
        when(dsbMannschaftComponent.findById(TEAM_ID_1)).thenReturn(platzhalter);

        VereinDO platzhalterVerein = new VereinDO();
        platzhalterVerein.setId(99L);
        platzhalterVerein.setName("Platzhalter");
        when(vereinComponent.findById(99L)).thenReturn(platzhalterVerein);
        when(tabletSchusszettelDAO.findByWettkampfUndTeam(anyLong(), anyLong())).thenReturn(Optional.empty());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc, PageSize.A4)) {
            underTest.generateSchusszettelPage(doc, new MatchDO[]{match1, match2});
        }
    }

    @Test
    public void generateSchusszettelPage_withPlatzhalterTeam2_skipsSecondIteration() throws Exception {
        MatchDO match1 = new MatchDO(1L, 1L, WETTKAMPF_ID, TEAM_ID_1, 1L, 1L, 0L, 0L, null, null, null, null, null);
        MatchDO match2 = new MatchDO(2L, 1L, WETTKAMPF_ID, TEAM_ID_2, 1L, 2L, 0L, 0L, null, null, null, null, null);

        DsbMannschaftDO platzhalter = new DsbMannschaftDO();
        platzhalter.setId(TEAM_ID_2);
        platzhalter.setVereinId(99L);
        platzhalter.setNummer(1L);
        when(dsbMannschaftComponent.findById(TEAM_ID_2)).thenReturn(platzhalter);

        VereinDO platzhalterVerein = new VereinDO();
        platzhalterVerein.setId(99L);
        platzhalterVerein.setName("Platzhalter");
        when(vereinComponent.findById(99L)).thenReturn(platzhalterVerein);
        when(tabletSchusszettelDAO.findByWettkampfUndTeam(anyLong(), anyLong())).thenReturn(Optional.empty());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc, PageSize.A4)) {
            underTest.generateSchusszettelPage(doc, new MatchDO[]{match1, match2});
        }
    }
}
