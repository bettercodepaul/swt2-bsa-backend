package de.bogenliga.application.business.meldezettel.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import de.bogenliga.application.business.meldezettel.api.MeldezettelComponent;
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 *
 * @author Robin Müller, HSRT
 * @author Marcel Neumann, HSRT
 * @author Michael Hesse, HSRT
 */
@Component
public class MeldezettelComponentImpl implements MeldezettelComponent {
    private static final String PRECONDITION_WETTKAMPFID = "WettkampfID cannot be negative";
    private static final String PRECONDITION_DOCUMENT = "Doc cannot be null";
    private static final String PRECONDITION_WETTKAMPFTAG =  "WettkampfTag cannot be null";
    private static final String PRECONDITION_VERANSTALTUNGSNAME =  "VeranstaltungsName cannot be null";
    private static final String PRECONDITION_DISZIPLINNAME =  "DisziplinName cannot be null";
    private static final String PRECONDITION_WETTKAMPFDATUM =  "WettkampfDatum cannot be null";
    private static final String PRECONDITION_TEAM_MAPPING = "TeamMemberMapping cannot be empty";
    private static final String MELDEZETTEL_MATCH = "Match";
    private static final String MELDEZETTEL_SCHUETZEN = "Schützen";
    private static final String MELDEZETTEL_UNTERSCHRIFT ="Unterschrift des Mannschaftsführers";

    private final MatchComponent matchComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final DisziplinComponent disziplinComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;

    @Autowired
    public MeldezettelComponentImpl(final MatchComponent matchComponent,
                                    final DsbMannschaftComponent dsbMannschaftComponent,
                                    final VereinComponent vereinComponent,
                                    final WettkampfComponent wettkampfComponent,
                                    final VeranstaltungComponent veranstaltungComponent,
                                    final DisziplinComponent disziplinComponent,
                                    final MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                    final DsbMitgliedComponent dsbMitgliedComponent) {
        this.matchComponent = matchComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.disziplinComponent = disziplinComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
    }
    @Override
    public byte[] getMeldezettelPDFasByteArray(long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        Hashtable<String, List<DsbMitgliedDO>> teamMemberMapping = new Hashtable<>();

        // Collect Information
        WettkampfDO wettkampfDO = wettkampfComponent.findById(wettkampfid);
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(wettkampfDO.getWettkampfVeranstaltungsId());

        Long wettkampfTag = wettkampfDO.getWettkampfTag();
        String veranstaltungsName = veranstaltungDO.getVeranstaltungName();
        String disziplinsName = disziplinComponent.findById(wettkampfDO.getWettkampfDisziplinId()).getDisziplinName();
        Date wettkampfDatum = wettkampfDO.getWettkampfDatum();

        for (int i = 1; i <= 8; i++) {
            MatchDO matchDO = matchComponent.findByWettkampfIDMatchNrScheibenNr(wettkampfid, 1L, (long) i);
            String teamName = getTeamName(matchDO.getMannschaftId());
            List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = mannschaftsmitgliedComponent.findAllSchuetzeInTeam(matchDO.getMannschaftId());
            List<DsbMitgliedDO> dsbMitgliedDOList = new ArrayList<>();

            for (MannschaftsmitgliedDO mannschaftsmitglied : mannschaftsmitgliedDOList) {
                dsbMitgliedDOList.add(dsbMitgliedComponent.findById(mannschaftsmitglied.getDsbMitgliedId()));
            }

            teamMemberMapping.put(teamName, dsbMitgliedDOList);
        }

        try (ByteArrayOutputStream result = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(result);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document doc = new Document(pdfDocument, PageSize.A4)) {

            generateDoc(doc, wettkampfTag, veranstaltungsName, disziplinsName, wettkampfDatum, teamMemberMapping);
            doc.close();

            return result.toByteArray();
        } catch (IOException e) {
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "Meldezettel PDF konnte nicht erstellt werden: " + e);
        }
    }


    /**
     * <p>writes a Meldezettel document for the Wettkampf</p>
     */
    private void generateDoc(Document doc, Long wettkampfTag, String veranstaltungsName, String disziplinsName, Date wettkampfDatum, Hashtable<String, List<DsbMitgliedDO>> teamMemberMapping) {
        Preconditions.checkNotNull(doc, PRECONDITION_DOCUMENT);
        Preconditions.checkNotNull(wettkampfTag, PRECONDITION_WETTKAMPFTAG);
        Preconditions.checkNotNull(veranstaltungsName, PRECONDITION_VERANSTALTUNGSNAME);
        Preconditions.checkNotNull(disziplinsName, PRECONDITION_DISZIPLINNAME);
        Preconditions.checkNotNull(wettkampfDatum, PRECONDITION_WETTKAMPFDATUM);
        Preconditions.checkArgument(!teamMemberMapping.isEmpty(), PRECONDITION_TEAM_MAPPING);

        String[] teamNameList = new String[8];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        int i = 0;

        for (String key : teamMemberMapping.keySet()) {
            teamNameList[i] = key;
            i++;
        }

        for (int manschaftCounter = 0; manschaftCounter < 8; manschaftCounter++) {
            String header = wettkampfTag + ". WK " + veranstaltungsName + " " + disziplinsName + " am " + simpleDateFormat.format(wettkampfDatum);
            String verein = "Verein: " + teamNameList[manschaftCounter];

            final Table mainTable = new Table(UnitValue.createPercentArray(2), true);
            final Table mainTableFirstRowFirstPart = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFirstRowFirstPartFirstRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFirstRowFirstPartSecondRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFirstRowFirstPartThirdRow = new Table(UnitValue.createPercentArray(new float[] { 23.0F, 11.0F, 11.0F, 11.0F, 11.0F, 11.0F, 11.0F, 11.0F }), true);
            final Table mainTableFirstRowSecondPart = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFirstRowSecondPartFirstRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFirstRowSecondPartSecondRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFirstRowSecondPartThirdRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableSecondRowFirstPart = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableSecondRowFirstPartFirstRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableSecondRowFirstPartSecondRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableSecondRowFirstPartThirdRow = new Table(UnitValue.createPercentArray(5), true);
            final Table mainTableSecondRowSecondPart = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableSecondRowSecondPartFirstRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableSecondRowSecondPartSecondRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableSecondRowSecondPartThirdRow = new Table(UnitValue.createPercentArray(5), true);
            final Table mainTableThirdRowFirstPart = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableThirdRowFirstPartFirstRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableThirdRowFirstPartSecondRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableThirdRowFirstPartThirdRow = new Table(UnitValue.createPercentArray(5), true);
            final Table mainTableThirdRowSecondPart = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableThirdRowSecondPartFirstRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableThirdRowSecondPartSecondRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableThirdRowSecondPartThirdRow = new Table(UnitValue.createPercentArray(5), true);
            final Table mainTableFourthRowFirstPart = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFourthRowFirstPartFirstRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFourthRowFirstPartSecondRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFourthRowFirstPartThirdRow = new Table(UnitValue.createPercentArray(5), true);
            final Table mainTableFourthRowSecondPart = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFourthRowSecondPartFirstRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFourthRowSecondPartSecondRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFourthRowSecondPartThirdRow = new Table(UnitValue.createPercentArray(5), true);
            final Table mainTableFifthRowFirstPart = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFifthRowSecondPart = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFifthRowSecondPartFirstRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFifthRowSecondPartSecondRow = new Table(UnitValue.createPercentArray(1), true);
            final Table mainTableFifthRowSecondPartThirdRow = new Table(UnitValue.createPercentArray(5), true);

            // Fill first table in first row
            mainTableFirstRowFirstPartFirstRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(header).setFontSize(8.0F))
                )
            ;

            mainTableFirstRowFirstPartSecondRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(verein).setBold().setFontSize(10.0F))
                )
            ;

            mainTableFirstRowFirstPartThirdRow
                .addCell(new Cell(5, 1).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_MATCH).setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)).setBorderBottom(new SolidBorder(Border.SOLID)).setBorderLeft(new SolidBorder(Border.SOLID))
                    .add(new Paragraph("1").setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)).setBorderBottom(new SolidBorder(Border.SOLID))
                    .add(new Paragraph("2").setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)).setBorderBottom(new SolidBorder(Border.SOLID))
                    .add(new Paragraph("3").setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)).setBorderBottom(new SolidBorder(Border.SOLID))
                    .add(new Paragraph("4").setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)).setBorderBottom(new SolidBorder(Border.SOLID))
                    .add(new Paragraph("5").setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)).setBorderBottom(new SolidBorder(Border.SOLID))
                    .add(new Paragraph("6").setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)).setBorderBottom(new SolidBorder(Border.SOLID)).setBorderRight(new SolidBorder(Border.SOLID))
                    .add(new Paragraph("7").setBold().setFontSize(10.0F))
                )
                // Add twentyone cells for text input
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                .addCell(new Cell().setHeight(15.0F))
                // Add seven cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
            ;

            // Fill second table in first row
            mainTableFirstRowSecondPartFirstRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(header).setFontSize(8.0F))
                )
            ;

            mainTableFirstRowSecondPartSecondRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(new Paragraph(verein).setBold().setFontSize(10.0F))
                )
            ;

            mainTableFirstRowSecondPartThirdRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("Die Lizenz erhalten:").setFontSize(10.0F))
                )
                // Two empty cells for text input
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("\n"))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("\n"))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(new Paragraph(MELDEZETTEL_UNTERSCHRIFT).setFontSize(6.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(60.0F)))
                )
            ;

            // Fill first table in second row
            mainTableSecondRowFirstPartFirstRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(header).setFontSize(8.0F))
                )
            ;

            mainTableSecondRowFirstPartSecondRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(verein).setBold().setFontSize(10.0F))
                )
            ;

            mainTableSecondRowFirstPartThirdRow
                .addCell(new Cell(1,2).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_SCHUETZEN).setBold().setFontSize(10.0F))
                )
                // Add three cells for text input
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                // Add five cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_MATCH).setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph("7").setBold().setFontSize(15.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell(1,3).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_UNTERSCHRIFT).setFontSize(6.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(100.0F)))
                )
            ;

            // Fill second table in second row
            mainTableSecondRowSecondPartFirstRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(header).setFontSize(8.0F))
                )
            ;

            mainTableSecondRowSecondPartSecondRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(verein).setBold().setFontSize(10.0F))
                )
            ;

            mainTableSecondRowSecondPartThirdRow
                .addCell(new Cell(1,2).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                        .add(new Paragraph(MELDEZETTEL_SCHUETZEN).setBold().setFontSize(10.0F))
                )
                // Add three cells for text input
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                // Add five cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_MATCH).setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph("6").setBold().setFontSize(15.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell(1,3).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_UNTERSCHRIFT).setFontSize(6.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(100.0F)))
                )
            ;

            // Fill first table in third row
            mainTableThirdRowFirstPartFirstRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(header).setFontSize(8.0F))
                )
            ;

            mainTableThirdRowFirstPartSecondRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(verein).setBold().setFontSize(10.0F))
                )
            ;

            mainTableThirdRowFirstPartThirdRow
                .addCell(new Cell(1,2).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_SCHUETZEN).setBold().setFontSize(10.0F))
                )
                // Add three cells for text input
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                // Add five cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_MATCH).setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph("5").setBold().setFontSize(15.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell(1,3).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_UNTERSCHRIFT).setFontSize(6.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(100.0F)))
                )
            ;

            // Fill second table in third row
            mainTableThirdRowSecondPartFirstRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(header).setFontSize(8.0F))
                )
            ;

            mainTableThirdRowSecondPartSecondRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(verein).setBold().setFontSize(10.0F))
                )
            ;

            mainTableThirdRowSecondPartThirdRow
                .addCell(new Cell(1,2).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_SCHUETZEN).setBold().setFontSize(10.0F))
                )
                // Add three cells for text input
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                // Add five cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_MATCH).setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph("4").setBold().setFontSize(15.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell(1,3).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_UNTERSCHRIFT).setFontSize(6.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(100.0F)))
                )
            ;

            // Fill first table in fourth row
            mainTableFourthRowFirstPartFirstRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(header).setFontSize(8.0F))
                )
            ;

            mainTableFourthRowFirstPartSecondRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(verein).setBold().setFontSize(10.0F))
                )
            ;

            mainTableFourthRowFirstPartThirdRow
                .addCell(new Cell(1,2).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_SCHUETZEN).setBold().setFontSize(10.0F))
                )
                // Add three cells for text input
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                // Add five cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_MATCH).setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph("3").setBold().setFontSize(15.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell(1,3).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_UNTERSCHRIFT).setFontSize(6.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(100.0F)))
                )
            ;

            // Fill second table in fourth row
            mainTableFourthRowSecondPartFirstRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(header).setFontSize(8.0F))
                )
            ;

            mainTableFourthRowSecondPartSecondRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(verein).setBold().setFontSize(10.0F))
                )
            ;

            mainTableFourthRowSecondPartThirdRow
                .addCell(new Cell(1,2).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_SCHUETZEN).setBold().setFontSize(10.0F))
                )
                // Add three cells for text input
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                // Add five cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_MATCH).setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph("2").setBold().setFontSize(15.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell(1,3).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_UNTERSCHRIFT).setFontSize(6.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(100.0F)))
                )
            ;

            // Fill first table in fifth row
            mainTableFifthRowFirstPart
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("RNr.: " + teamNameList[manschaftCounter]).setBold().setFontSize(10.0F))
                )
            ;

            // Create table for Mitglieder with 2 fixed width columns
            final Table mitgliederTable = new Table(new float[]{150F, 150F});
            for (int mitgliedCounter = 1; mitgliedCounter < teamMemberMapping.get(teamNameList[manschaftCounter]).size() + 1; mitgliedCounter++) {
                DsbMitgliedDO mitgliedDO = teamMemberMapping.get(teamNameList[manschaftCounter]).get(mitgliedCounter - 1);
                mitgliederTable
                        .addCell(new Cell().setBorder(Border.NO_BORDER)
                                .add(new Paragraph(mitgliedCounter + ". " + mitgliedDO.getNachname() + ", " + mitgliedDO.getVorname()).setFontSize(8.0F))
                        )
                ;
            }
            // Add mitgliederTable to mainTable
            mainTableFifthRowFirstPart
                    .addCell(
                            new Cell().setBorder(Border.NO_BORDER)
                                    .add(mitgliederTable)
                    );

            // Fill second table in fifth row
            mainTableFifthRowSecondPartFirstRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(header).setFontSize(8.0F))
                )
            ;

            mainTableFifthRowSecondPartSecondRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(verein).setBold().setFontSize(10.0F))
                )
            ;

            mainTableFifthRowSecondPartThirdRow
                .addCell(new Cell(1,2).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_SCHUETZEN).setBold().setFontSize(10.0F))
                )
                // Add three cells for text input
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                .addCell(new Cell().setHeight(45.0F))
                // Add five cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_MATCH).setBold().setFontSize(10.0F))
                )
                .addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER)
                    .add(new Paragraph("1").setBold().setFontSize(15.0F))
                )
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setHeight(15.0F).setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell(1,3).setBorder(Border.NO_BORDER)
                    .add(new Paragraph(MELDEZETTEL_UNTERSCHRIFT).setFontSize(6.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(100.0F)))
                )
            ;

            // Add sub tables to main table parts
            mainTableFirstRowFirstPart
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFirstRowFirstPartFirstRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFirstRowFirstPartSecondRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFirstRowFirstPartThirdRow)
                )
            ;

            mainTableFirstRowSecondPart
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFirstRowSecondPartFirstRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFirstRowSecondPartSecondRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFirstRowSecondPartThirdRow)
                )
            ;

            mainTableSecondRowFirstPart
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableSecondRowFirstPartFirstRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableSecondRowFirstPartSecondRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableSecondRowFirstPartThirdRow)
                )
            ;

            mainTableSecondRowSecondPart
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableSecondRowSecondPartFirstRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableSecondRowSecondPartSecondRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableSecondRowSecondPartThirdRow)
                )
            ;

            mainTableThirdRowFirstPart
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableThirdRowFirstPartFirstRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableThirdRowFirstPartSecondRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableThirdRowFirstPartThirdRow)
                )
            ;

            mainTableThirdRowSecondPart
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableThirdRowSecondPartFirstRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableThirdRowSecondPartSecondRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableThirdRowSecondPartThirdRow)
                )
            ;

            mainTableFourthRowFirstPart
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFourthRowFirstPartFirstRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFourthRowFirstPartSecondRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFourthRowFirstPartThirdRow)
                )
            ;

            mainTableFourthRowSecondPart
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFourthRowSecondPartFirstRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFourthRowSecondPartSecondRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFourthRowSecondPartThirdRow)
                )
            ;

            // Insert 5.1 here

            mainTableFifthRowSecondPart
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFifthRowSecondPartFirstRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFifthRowSecondPartSecondRow)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(mainTableFifthRowSecondPartThirdRow)
                )
            ;

            // Add sub tables to main table
            mainTable
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(mainTableFirstRowFirstPart).setPaddingLeft(10.0F).setPaddingRight(20.0F).setPaddingBottom(15.0F)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(mainTableFirstRowSecondPart).setPaddingLeft(30.0F).setPaddingRight(5.0F).setPaddingBottom(15.0F)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(mainTableSecondRowFirstPart).setPaddingLeft(10.0F).setPaddingRight(20.0F).setPaddingBottom(15.0F)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(mainTableSecondRowSecondPart).setPaddingLeft(30.0F).setPaddingRight(5.0F).setPaddingBottom(15.0F)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(mainTableThirdRowFirstPart).setPaddingLeft(10.0F).setPaddingRight(20.0F).setPaddingBottom(15.0F)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(mainTableThirdRowSecondPart).setPaddingLeft(30.0F).setPaddingRight(5.0F).setPaddingBottom(15.0F)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(mainTableFourthRowFirstPart).setPaddingLeft(10.0F).setPaddingRight(20.0F).setPaddingBottom(15.0F)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(mainTableFourthRowSecondPart).setPaddingLeft(30.0F).setPaddingRight(5.0F).setPaddingBottom(15.0F)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(mainTableFifthRowFirstPart).setPaddingLeft(10.0F).setPaddingRight(20.0F)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(mainTableFifthRowSecondPart).setPaddingLeft(30.0F).setPaddingRight(5.0F)
                )
            ;

            // Add all to document
            doc.add(mainTable.setPaddings(10.0F, 10.0F, 10.0F, 10.0F).setMargins(2.5F, 0.0F, 2.5F, 0.0F)).setBorder(Border.NO_BORDER);

            if (manschaftCounter != 7) {
                doc.add(new AreaBreak());
            }
        }
    }


    /**
     * help function to get team name
     *
     * @param teamID ID of the team
     * @return name of the team
     */
    private String getTeamName(long teamID) {
        Preconditions.checkArgument(teamID >= 0,"TeamID cannot be Negative");
        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(teamID);
        VereinDO vereinDO = vereinComponent.findById(dsbMannschaftDO.getVereinId());
        if (dsbMannschaftDO.getNummer() > 1) {
            return vereinDO.getName() + " " + dsbMannschaftDO.getNummer();
        } else {
            return vereinDO.getName();
        }
    }
}