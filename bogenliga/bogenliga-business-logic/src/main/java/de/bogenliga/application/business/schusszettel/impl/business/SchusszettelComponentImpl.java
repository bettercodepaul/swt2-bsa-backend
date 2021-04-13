package de.bogenliga.application.business.schusszettel.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.api.SchusszettelComponent;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * * Implementation of {@link SchusszettelComponent}
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 * @author Robin Müller, robin.mueller@student.reutlingen-university.de
 * @author Jonas Müller, jonas_dominik.mueller@student.reutlingen-university.de
 * @author Maximilian Gysau, maximilian_alexander.gysau@reutlingen-university.de
 */
@Component
public class SchusszettelComponentImpl implements SchusszettelComponent {

    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";
    private static final String SCHUSSZETTEL_SATZ1 = "1. Satz";
    private static final String SCHUSSZETTEL_SATZ2 = "2. Satz";
    private static final String SCHUSSZETTEL_SATZ3 = "3. Satz";
    private static final String SCHUSSZETTEL_SATZ4 = "4. Satz";
    private static final String SCHUSSZETTEL_SATZ5 = "5. Satz";
    private static final String SCHUSSZETTEL_SUMME = "Summe";
    private static final String SCHUSSZETTEL_PFEIL1 = "Pfeil 1";
    private static final String SCHUSSZETTEL_PFEIL2 = "Pfeil 2";
    private static final String SCHUSSZETTEL_UNTERSCHRIFT = "Unterschrift";

    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfComponent wettkampfComponent;

    @Autowired
    public SchusszettelComponentImpl(final MatchComponent matchComponent,
                                     final PasseComponent passeComponent,
                                     final DsbMannschaftComponent dsbMannschaftComponent,
                                     final MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                     final VereinComponent vereinComponent,
                                     final WettkampfComponent wettkampfComponent) {
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfComponent = wettkampfComponent;
    }

    @Override
    public byte[] getAllSchusszettelPDFasByteArray(long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        List<MatchDO> matchDOList = matchComponent.findByWettkampfId(wettkampfid);

        byte[] bResult;
        if (!matchDOList.isEmpty()) {
            bResult = generateDoc(matchDOList).toByteArray();
        }else{
            throw new BusinessException(ErrorCode.UNEXPECTED_ERROR, "Matches für den Wettkampf noch nicht erzeugt");
        }
        return bResult;
    }

    @Override
    public byte[] getFilledSchusszettelPDFasByteArray(long matchId1, long matchId2) {
        MatchDO match1 = matchComponent.findById(matchId1);
        MatchDO match2 = matchComponent.findById(matchId2);

        List<PasseDO> passen1 = passeComponent.findByMatchId(match1.getId());
        List<PasseDO> passen2 = passeComponent.findByMatchId(match2.getId());

        return generateFilledDoc(match1, match2, passen1, passen2).toByteArray();
    }


    /**
     * <p>Creates a Schusszettel with the values from the database filled in
     * </p>
     */
    private ByteArrayOutputStream generateFilledDoc(MatchDO match1, MatchDO match2, List<PasseDO> passen1, List<PasseDO> passen2) {
        ByteArrayOutputStream ret;
        try (final ByteArrayOutputStream result = new ByteArrayOutputStream();
             final PdfWriter writer = new PdfWriter(result);
             final PdfDocument pdfDocument = new PdfDocument(writer);
             final Document doc = new Document(pdfDocument, PageSize.A4)) {

            generateFilledSchusszettelPage(doc, new MatchDO[] {match1, match2}, new List[]{passen1, passen2});
            doc.close();
            ret = result;

        } catch (final IOException e) {
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                    "PDF Dokument konnte nicht erstellt werden: " + e);
        }
        return ret;

    }

    /**
     * <p>Creates the pdf document for Schusszettel with the values from the database filled in
     * </p>
     * @param doc document to write
     */
    private void generateFilledSchusszettelPage(Document doc, MatchDO[] matchDOs, List<PasseDO>[] passenDOs) {

        Long wettkampfTag = wettkampfComponent.findById(matchDOs[0].getWettkampfId()).getWettkampfTag();
        String[] mannschaftName = { getMannschaftsNameByID(matchDOs[0].getMannschaftId()), getMannschaftsNameByID(matchDOs[1].getMannschaftId())};

        // Generate special settings for some parts
        Border specialBorder = new SolidBorder(Border.SOLID);
        specialBorder.setWidth(1.5F);

        DottedLine cutterDottedLine = new DottedLine(0.5F);

        // Keep track of Satz-sums of both matches to calculate Satzpunkte later
        Integer[][] matchSatzSums = new Integer[2][5];
        // Keep track of Satzpunkt-Cells to fill in Satzpunkte later
        Cell[][] matchSatzpunktCells = new Cell[2][10];

        // Keep track of all table components to build doc when Satzpunkte are figured out
        // (itext makes document unwritable after components have been added to it, so everything needs to be ready beforehand)
        Table[] matchHeaders = new Table[2];
        Table[] matchFirstRows = new Table[2];
        Table[] matchSecondRows = new Table[2];
        Table[] matchThirdRows = new Table[2];

        // Create a map with all Passen of a Schuetze from unorganized list of all passen of match
        for (int i = 1; i <= 2; i++) {
            Map<Long, List<PasseDO>> schuetzenPasseMap = new HashMap<>();
            for (PasseDO passeDO : passenDOs[i-1]) {
                // Only pick 3 schuetzen
                if (!schuetzenPasseMap.containsKey(passeDO.getPasseDsbMitgliedId())) {
                    if (schuetzenPasseMap.size() < 3) {
                        schuetzenPasseMap.put(passeDO.getPasseDsbMitgliedId(), new ArrayList<>());
                        schuetzenPasseMap.get(passeDO.getPasseDsbMitgliedId()).add(passeDO);
                    }
                } else {
                    // Only add 5 passen
                    if (schuetzenPasseMap.get(passeDO.getPasseDsbMitgliedId()).size() < 5) {
                        schuetzenPasseMap.get(passeDO.getPasseDsbMitgliedId()).add(passeDO);
                    }
                }
            }

            // Helper variables
            MatchDO currentMatch = matchDOs[i-1];
            MatchDO otherMatch = matchDOs[(i == 1 ? 2 : 1) - 1];
            // Skip matches with no passen
            if (passenDOs[i-1].isEmpty()) {
                continue;
            }

            // Generate tables and store references in array in outer scope
            matchHeaders[i-1] = new Table(UnitValue.createPercentArray(3), true);
            matchFirstRows[i-1]  = new Table(UnitValue.createPercentArray(2), true);
            final Table tableFirstRowFirstPart = new Table(UnitValue.createPercentArray(2), true);
            final Table tableFirstRowSecondPart = new Table(UnitValue.createPercentArray(7), true);
            matchSecondRows[i-1] = new Table(UnitValue.createPercentArray(new float[] { 10.0F, 80.0F, 10.0F }), true);
            final Table tableSecondRowFirstPart = new Table(UnitValue.createPercentArray(1), true);
            final Table tableSecondRowSecondPart = new Table(UnitValue.createPercentArray(10), true);
            final Table tableSecondRowThirdPart = new Table(UnitValue.createPercentArray(1), true);
            matchThirdRows[i-1]  = new Table(UnitValue.createPercentArray(2), true);

            // Table head
            matchHeaders[i-1]
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                            .add(new Paragraph(mannschaftName[i - 1]).setBold().setFontSize(getDynamicFontSize(mannschaftName[i - 1], 12.0F)))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                            .add(new Paragraph(wettkampfTag + ". Wettkampf").setBold().setFontSize(12.0F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT)
                            .add(new Paragraph("Scheibe " + currentMatch.getScheibenNummer()).setBold().setFontSize(12.0F))
                    )
            ;

            // First row
            // First part
            tableFirstRowFirstPart
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(mannschaftName[0]).setBold().setFontSize(getDynamicFontSize(mannschaftName[0], 15.0F)))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(16.0F)
                            .add(new Paragraph(matchDOs[0].getNr() + ". Match"))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph("gegen").setBold().setFontSize(14.0F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(mannschaftName[1]).setBold().setFontSize(getDynamicFontSize(mannschaftName[1], 15.0F)))
                    )
            ;

            // Second part
            tableFirstRowSecondPart
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ1).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ2).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ3).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ4).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ5).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SUMME).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph("Match").setFontSize(7.5F))
                    );

            // Save references for Satzpunkte for team1
            for (int j = 0; j < 5; j++) {
                Cell cell = new Cell().setHeight(20.0F);
                tableFirstRowSecondPart
                        .addCell(cell);
                matchSatzpunktCells[i-1][j] = cell;
            }

            // Add summed Satzpunkte and Matchpunkte for team1
            tableFirstRowSecondPart
                    .addCell(
                            new Cell().setHeight(20.0F).setBorder(specialBorder)
                                    .add(new Paragraph(currentMatch.getSatzpunkte().toString()))
                    )
                    .addCell(
                            new Cell().setHeight(20.0F).setBorder(specialBorder)
                                    .add(new Paragraph(currentMatch.getMatchpunkte().toString()))
                    );

            // Save references for Satzpunkte for team2
            for (int j = 5; j < 10; j++) {
                Cell cell = new Cell().setHeight(20.0F);
                tableFirstRowSecondPart
                        .addCell(cell);
                matchSatzpunktCells[i-1][j] = cell;
            }

            tableFirstRowSecondPart
                    // Add summed Satzpunkte and Matchpunkte for team2
                    .addCell(
                            new Cell().setHeight(20.0F).setBorder(specialBorder)
                                    .add(new Paragraph(otherMatch.getSatzpunkte().toString()))
                    )
                    .addCell(
                            new Cell().setHeight(20.0F).setBorder(specialBorder)
                                    .add(new Paragraph(otherMatch.getMatchpunkte().toString()))
                    )
                    // Add seven cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))

            ;

            // Second row
            // First part
            tableSecondRowFirstPart
                    .addCell(new Cell(2,1).setTextAlignment(TextAlignment.CENTER).setHeight(29.0F)
                            .add(new Paragraph("Schütze").setFontSize(8.0F))
                    )
            ;

            // Fill in Schuetzennummer column
            for (Map.Entry<Long, List<PasseDO>> entry : schuetzenPasseMap.entrySet()) {
                // Get matching mannschaftsMitgliedDO to dsb_mitglied_id for this match to get Rueckennummer
                MannschaftsmitgliedDO mannschaftsmitgliedDO = mannschaftsmitgliedComponent
                        .findByMemberAndTeamId(currentMatch.getMannschaftId(), entry.getKey());
                // Add schutzen Rueckennummern
                String rueckennummer = mannschaftsmitgliedDO.getRueckennummer() == null
                        ? "NaN"
                        : mannschaftsmitgliedDO.getRueckennummer().toString();
                tableSecondRowFirstPart.addCell(new Cell().setHeight(20.0F)
                        .add(new Paragraph(rueckennummer).setFontSize(8.0F)));
                // Sort each Schuetzen's passen by lfdnr
                entry.getValue().sort(Comparator.comparing(PasseDO::getPasseLfdnr));
            }

            // Add one cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
            tableSecondRowFirstPart.addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)));

            // Second part
            tableSecondRowSecondPart
                    .addCell(new Cell(1,2).setBorderBottom(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph("1. Satz/ Pfeile").setFontSize(8.0F))
                    )
                    .addCell(new Cell(1,2).setBorderBottom(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph("2. Satz/ Pfeile").setFontSize(8.0F))
                    )
                    .addCell(new Cell(1,2).setBorderBottom(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph("3. Satz/ Pfeile").setFontSize(8.0F))
                    )
                    .addCell(new Cell(1,2).setBorderBottom(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph("4. Satz/ Pfeile").setFontSize(8.0F))
                    )
                    .addCell(new Cell(1,2).setBorderBottom(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph("5. Satz/ Pfeile").setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL1).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL2).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL1).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL2).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL1).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL2).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL1).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL2).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL1).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL2).setFontSize(8.0F))
                    );

            // Add sum array and initialize with 0
            Integer[] sums = new Integer[5];
            Arrays.fill(sums, 0);

            // Fill in Passen for each Schuetze
            for (Map.Entry<Long, List<PasseDO>> entry : schuetzenPasseMap.entrySet()) {
                // Iterate over all 5 Passen
                for (int j = 0; j < 5; j++) {
                    // Add passe data to table, if there's no more data available, fill in zeroes
                    if (entry.getValue().size() > j)  {
                        Integer pfeil1 = entry.getValue().get(j).getPfeil1();
                        Integer pfeil2 = entry.getValue().get(j).getPfeil2();
                        String pfeil1String = pfeil1 == null ? "" : pfeil1.toString();
                        String pfeil2String = pfeil2 == null ? "" : pfeil2.toString();
                        tableSecondRowSecondPart.addCell(new Cell().setHeight(20.0F).add(new Paragraph(pfeil1String).setFontSize(8.0F)));
                        tableSecondRowSecondPart.addCell(new Cell().setHeight(20.0F).add(new Paragraph(pfeil2String).setFontSize(8.0F)));
                        // Add score to sum for this passe
                        sums[j] += (pfeil1 == null ? 0 : pfeil1) + (pfeil2 == null ? 0 : pfeil2);
                    } else {
                        tableSecondRowSecondPart.addCell(new Cell().setHeight(20.0F).add(new Paragraph("0").setFontSize(8.0F)));
                        tableSecondRowSecondPart.addCell(new Cell().setHeight(20.0F).add(new Paragraph("0").setFontSize(8.0F)));
                    }

                }
            }


            // Turn all Strafpunkte into "0" if null
            String[] strafpunkte = new String[] {
                    currentMatch.getStrafPunkteSatz1() == null ? "0" : currentMatch.getStrafPunkteSatz1().toString(),
                    currentMatch.getStrafPunkteSatz2() == null ? "0" : currentMatch.getStrafPunkteSatz2().toString(),
                    currentMatch.getStrafPunkteSatz3() == null ? "0" : currentMatch.getStrafPunkteSatz3().toString(),
                    currentMatch.getStrafPunkteSatz4() == null ? "0" : currentMatch.getStrafPunkteSatz4().toString(),
                    currentMatch.getStrafPunkteSatz5() == null ? "0" : currentMatch.getStrafPunkteSatz5().toString()
            };

            //Save sum with including the Strafpunkte
            for (int j = 0; j < 5; j++) {
                sums[j] -= Integer.parseInt(strafpunkte[j]) ;
            }

            // Save sums to array in outside scope
            matchSatzSums[i-1] = sums;

            // Add sum cells
            for (Integer sum : sums) {
                tableSecondRowSecondPart
                        .addCell(
                                new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                                        .setHeight(20.0F)
                                        .add(new Paragraph(SCHUSSZETTEL_SUMME).setFontSize(10.0F))
                        )
                        .addCell(
                                new Cell().setHeight(20.0F).add(new Paragraph(sum.toString()).setFontSize(10.0F))
                        );
            }

            tableSecondRowSecondPart
                    // Add ten cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
            ;


            // Add Fehlerpunkte
            // Third part
            tableSecondRowThirdPart
                    .addCell(new Cell(2,1).setTextAlignment(TextAlignment.CENTER).setHeight(29.0F)
                            .add(new Paragraph("Fehler-punkte").setFontSize(8.0F))
                            .add(new Paragraph("punkte").setFontSize(8.0F))
                    )
                    .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph("1. Satz\n" + strafpunkte[0]).setFontSize(6.0F))
                    )
                    .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph("2. Satz\n" + strafpunkte[1]).setFontSize(6.0F))
                    )
                    .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph("3. Satz\n" + strafpunkte[2]).setFontSize(6.0F))
                    )
                    .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph("4. Satz\n" + strafpunkte[3]).setFontSize(6.0F))
                    )
                    .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph("5. Satz\n" + strafpunkte[4]).setFontSize(6.0F))
                    )
                    // Add one cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
            ;

            // Third row
            matchThirdRows[i-1]
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(mannschaftName[0]).setBold().setFontSize(getDynamicFontSize(mannschaftName[0], 12.0F)))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(mannschaftName[1]).setBold().setFontSize(getDynamicFontSize(mannschaftName[1], 12.0F)))
                    )
                    // Two empty cells for text input
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph("\n"))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph("\n"))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_UNTERSCHRIFT).setFontSize(10.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(50.0F)))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_UNTERSCHRIFT).setFontSize(10.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(50.0F)))
                    )
            ;

            // Add subtables to main tables
            matchFirstRows[i-1]
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableFirstRowFirstPart)
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableFirstRowSecondPart)
                    )
            ;

            matchSecondRows[i-1]
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableSecondRowFirstPart)
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableSecondRowSecondPart)
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableSecondRowThirdPart)
                    )
            ;
        }

        if (matchSatzSums[1][0] != null) {
            // Compare Pfeile from Saetze and assign Satzpunkte
            Integer[][] satzpunkte = new Integer[2][5];
            Integer[] match1Sums = matchSatzSums[0];
            Integer[] match2Sums = matchSatzSums[1];

            // Point distribution logic of 2 teams:
            // A > B => A gets 2, B gets 0
            // A = B, A & B have scored in Satz => A gets 1, B gets 1
            // A = B, A & B have not scored in Satz (aka Satz was not played) => A gets 0, B gets 0
            // A < B => A gets 0, B gets 2
            for (int i = 0; i < 5; i++) {
                if (match1Sums[i] > match2Sums[i]) {
                    satzpunkte[0][i] = 2;
                    satzpunkte[1][i] = 0;
                } else if (match1Sums[i].equals(match2Sums[i])) {
                    if (match1Sums[i] == 0) {
                        satzpunkte[0][i] = 0;
                        satzpunkte[1][i] = 0;
                    } else {
                        satzpunkte[0][i] = 1;
                        satzpunkte[1][i] = 1;
                    }
                } else {
                    satzpunkte[0][i] = 0;
                    satzpunkte[1][i] = 2;
                }
            }

            // Fill Satzpunkte cells with data
            for (int i = 0; i < 2; i++) {
                Cell[] satzpunkteCells = matchSatzpunktCells[i];
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 5; k++) {
                        Cell satzpunktCell = satzpunkteCells[k + 5 * j];
                        satzpunktCell.add(new Paragraph(satzpunkte[(i + j) % 2][k].toString()));
                    }
                }
            }
        }

        // Build page with both matches and separator
        for (int i = 0; i < 2; i++) {
            // If there's no 2nd match data, break
            if (matchHeaders[i] == null) {
                break;
            }
            // Add separator
            if (i == 1) {
                doc.add(new LineSeparator(cutterDottedLine).setMargins(25.0F, 1.0F, 25.0F, 1.0F));
            }
            // Add all to document
            doc
                    .add(matchHeaders[i])
                    .add(new Div().setPaddings(10.0F, 10.0F, 10.0F, 10.0F).setMargins(2.5F, 0.0F, 2.5F, 0.0F).setBorder(new SolidBorder(Border.SOLID))
                            .add(matchFirstRows[i])
                            .add(matchSecondRows[i])
                            .add(matchThirdRows[i])
                    )
            ;
        }
    }


    /**
     * <p>writes a Schusszettel document for the Wettkamnpf
     * </p>
     */
    private ByteArrayOutputStream generateDoc(List<MatchDO> matchDOList) {
        ByteArrayOutputStream ret;
        try (final ByteArrayOutputStream result = new ByteArrayOutputStream();
             final PdfWriter writer = new PdfWriter(result);
             final PdfDocument pdfDocument = new PdfDocument(writer);
             final Document doc = new Document(pdfDocument, PageSize.A4)) {

            //iterate through matches
            for (long i = 1; i<=7; i++){
                //iterate through begegnungen
                for(long k = 1; k<=4; k++){
                    MatchDO[] matchesBegegnung = getMatchDOsForPage(matchDOList , i, k);
                    if(matchesBegegnung[0] != null && matchesBegegnung[1] != null) {
                        generateSchusszettelPage(doc, matchesBegegnung);
                        if(i != 7){
                            doc.add(new AreaBreak());
                        }
                    }
                }
            }
            doc.close();
            ret = result;

        } catch (final IOException e) {
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                    "PDF Dokument konnte nicht erstellt werden: " + e);
        }
        return ret;

    }

    /**
     * <p>internal function to return two matches for one Schusszettel page
     * </p>
     * @param matchDOList list of matches for competition
     */
    private MatchDO[] getMatchDOsForPage(List<MatchDO> matchDOList, long matchNr, long begegnung){
        MatchDO[] ret = new MatchDO[2];
        long startScheibenNrBegegnung = (begegnung * 2) - 1 ;
        for(MatchDO match : matchDOList){
            if(match.getNr() == matchNr && match.getBegegnung() == begegnung){
                if(match.getScheibenNummer() == startScheibenNrBegegnung){
                    ret[0] = match;
                }
                if(match.getScheibenNummer() == startScheibenNrBegegnung + 1){
                    ret[1] = match;
                }
            }
        }
        return ret;
    }

    /**
     * <p>writes a Schusszettel document for the Wettkamnpf
     * </p>
     * @param doc document to write
     */
    private void generateSchusszettelPage(Document doc, MatchDO[] matchDOs) {
        Long wettkampfTag = wettkampfComponent.findById(matchDOs[0].getWettkampfId()).getWettkampfTag();
        String[] mannschaftName = { getMannschaftsNameByID(matchDOs[0].getMannschaftId()), getMannschaftsNameByID(matchDOs[1].getMannschaftId())};

        // Generate special settings for some parts
        Border specialBorder = new SolidBorder(Border.SOLID);
        specialBorder.setWidth(1.5F);

        DottedLine cutterDottedLine = new DottedLine(0.5F);

        for (int i = 1; i <= 2; i++) {
            //Blank lines before second half
            if (i == 2) {
                for(int j = 0; j <= 2; j++) {
                    if (j == 1) {
                        doc.add(new LineSeparator(cutterDottedLine));
                    } else {
                        doc.add(new Paragraph("\n"));
                    }
                }
            }

            // Generate tables
            final Table tableHead = new Table(UnitValue.createPercentArray(3), true);
            final Table tableFirstRow = new Table(UnitValue.createPercentArray(2), true);
            final Table tableFirstRowFirstPart = new Table(UnitValue.createPercentArray(2), true);
            final Table tableFirstRowSecondPart = new Table(UnitValue.createPercentArray(7), true);
            final Table tableSecondRow = new Table(UnitValue.createPercentArray(new float[] { 10.0F, 80.0F, 10.0F }), true);
            final Table tableSecondRowFirstPart = new Table(UnitValue.createPercentArray(1), true);
            final Table tableSecondRowSecondPart = new Table(UnitValue.createPercentArray(10), true);
            final Table tableSecondRowThirdPart = new Table(UnitValue.createPercentArray(1), true);
            final Table tableThirdRow = new Table(UnitValue.createPercentArray(2), true);

            // Table head
            tableHead
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                            .add(new Paragraph(mannschaftName[i - 1]).setBold().setFontSize(getDynamicFontSize(mannschaftName[i - 1], 12.0F)))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                            .add(new Paragraph(wettkampfTag + ". Wettkampf").setBold().setFontSize(12.0F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT)
                            .add(new Paragraph("Scheibe " + matchDOs[i - 1].getScheibenNummer()).setBold().setFontSize(12.0F))
                    )
            ;

            // First row
            // First part
            tableFirstRowFirstPart
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(mannschaftName[0]).setBold().setFontSize(getDynamicFontSize(mannschaftName[0], 15.0F)))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(16.0F)
                            .add(new Paragraph(matchDOs[0].getNr() + ". Match"))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph("gegen").setBold().setFontSize(14.0F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(mannschaftName[1]).setBold().setFontSize(getDynamicFontSize(mannschaftName[1], 15.0F)))
                    )
            ;

            // Second part
            tableFirstRowSecondPart
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ1).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ2).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ3).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ4).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ5).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_SUMME).setFontSize(7.5F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph("Match").setFontSize(7.5F))
                    )
                    // Add fourteen cells for text input
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F).setBorder(specialBorder))
                    .addCell(new Cell().setHeight(20.0F).setBorder(specialBorder))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F).setBorder(specialBorder))
                    .addCell(new Cell().setHeight(20.0F).setBorder(specialBorder))
                    // Add seven cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))

            ;

            // Second row
            // First part
            tableSecondRowFirstPart
                    .addCell(new Cell(2,1).setTextAlignment(TextAlignment.CENTER).setHeight(29.0F)
                            .add(new Paragraph("Schütze").setFontSize(8.0F))
                    )
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    // Add one cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
            ;

            // Second part
            tableSecondRowSecondPart
                    .addCell(new Cell(1,2).setBorderBottom(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph("1. Satz/ Pfeile").setFontSize(8.0F))
                    )
                    .addCell(new Cell(1,2).setBorderBottom(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph("2. Satz/ Pfeile").setFontSize(8.0F))
                    )
                    .addCell(new Cell(1,2).setBorderBottom(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph("3. Satz/ Pfeile").setFontSize(8.0F))
                    )
                    .addCell(new Cell(1,2).setBorderBottom(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph("4. Satz/ Pfeile").setFontSize(8.0F))
                    )
                    .addCell(new Cell(1,2).setBorderBottom(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph("5. Satz/ Pfeile").setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL1).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL2).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL1).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL2).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL1).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL2).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL1).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL2).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL1).setFontSize(8.0F))
                    )
                    .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                            .add(new Paragraph(SCHUSSZETTEL_PFEIL2).setFontSize(8.0F))
                    )
                    // Add thirty cells for text input
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph(SCHUSSZETTEL_SUMME).setFontSize(10.0F))
                    )
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph(SCHUSSZETTEL_SUMME).setFontSize(10.0F))
                    )
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph(SCHUSSZETTEL_SUMME).setFontSize(10.0F))
                    )
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph(SCHUSSZETTEL_SUMME).setFontSize(10.0F))
                    )
                    .addCell(new Cell().setHeight(20.0F))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph(SCHUSSZETTEL_SUMME).setFontSize(10.0F))
                    )
                    .addCell(new Cell().setHeight(20.0F))
                    // Add ten cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
            ;

            // Third part
            tableSecondRowThirdPart
                    .addCell(new Cell(2,1).setTextAlignment(TextAlignment.CENTER).setHeight(29.0F)
                            .add(new Paragraph("Fehler-punkte").setFontSize(8.0F))
                            .add(new Paragraph("punkte").setFontSize(8.0F))
                    )
                    .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ1).setFontSize(5.0F))
                    )
                    .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ1).setFontSize(5.0F))
                    )
                    .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ1).setFontSize(5.0F))
                    )
                    .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ1).setFontSize(5.0F))
                    )
                    .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                            .add(new Paragraph(SCHUSSZETTEL_SATZ1).setFontSize(5.0F))
                    )
                    // Add one cells more because of a bug in the pdf framework which leads to the last cells not showing the border downwards.
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
            ;

            // Third row
            tableThirdRow
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(mannschaftName[0]).setBold().setFontSize(getDynamicFontSize(mannschaftName[0], 12.0F)))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(mannschaftName[1]).setBold().setFontSize(getDynamicFontSize(mannschaftName[1], 12.0F)))
                    )
                    // Two empty cells for text input
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph("\n"))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph("\n"))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_UNTERSCHRIFT).setFontSize(10.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(50.0F)))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(new Paragraph(SCHUSSZETTEL_UNTERSCHRIFT).setFontSize(10.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(50.0F)))
                    )
            ;

            // Add subtables to main tables
            tableFirstRow
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableFirstRowFirstPart)
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableFirstRowSecondPart)
                    )
            ;

            tableSecondRow
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableSecondRowFirstPart)
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableSecondRowSecondPart)
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableSecondRowThirdPart)
                    )
            ;

            // Add all to document
            doc
                    .add(tableHead)
                    .add(new Div().setPaddings(10.0F, 10.0F, 10.0F, 10.0F).setMargins(2.5F, 0.0F, 2.5F, 0.0F).setBorder(new SolidBorder(Border.SOLID))
                            .add(tableFirstRow)
                            .add(tableSecondRow)
                            .add(tableThirdRow)
                    )
            ;
        }
    }

    private String getMannschaftsNameByID(long mannschaftID){
        String mannschaftName;
        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(mannschaftID);
        VereinDO vereinDO = vereinComponent.findById(dsbMannschaftDO.getVereinId());

        if (dsbMannschaftDO.getNummer() > 1) {
            mannschaftName = vereinDO.getName() + " " + dsbMannschaftDO.getNummer();
        } else {
            mannschaftName = vereinDO.getName();
        }
        return mannschaftName;
    }

    // This method returns a dynamic font size, dependant on the length of the given text and the intended font size.
    // The font size will only be changed if the length of the given text is greater then 15.
    // If the text length is greater then 15 the given text wont fit in his dedicated field and we have to alter the
    // given font size. This is achieved with the help of the hyperbolic function "f(x) = 175f / x" with
    // "x = length of text". This function alters the font size to fit nicely in his dedicated field
    // if the text length increases.
    private float getDynamicFontSize(String text, Float fontSize){
        if(text.length() < 15){
            return  fontSize;
        }
        return 175f  / text.length();
    }
}