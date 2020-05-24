package de.bogenliga.application.business.schusszettel.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
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
 */
@Component
public class SchusszettelComponentImpl implements SchusszettelComponent {

    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";

    private final MatchComponent matchComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfComponent wettkampfComponent;

    @Autowired
    public SchusszettelComponentImpl(final MatchComponent matchComponent,
                                     final DsbMannschaftComponent dsbMannschaftComponent,
                                     final VereinComponent vereinComponent,
                                     final WettkampfComponent wettkampfComponent) {
        this.matchComponent = matchComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfComponent = wettkampfComponent;
    }

    @Override
    public byte[] getAllSchusszettelPDFasByteArray(long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        List<MatchDO> matchDOList = matchComponent.findByWettkampfId(wettkampfid);

        byte[] bResult;
        if (matchDOList.size() != 0) {
            bResult = generateDoc(matchDOList).toByteArray();
        }else{
            throw new BusinessException(ErrorCode.UNEXPECTED_ERROR, "Matches für den Wettkampf noch nicht erzeugt");
        }
        return bResult;
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
                    .add(new Paragraph("1. Satz").setFontSize(7.5F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("2. Satz").setFontSize(7.5F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("3. Satz").setFontSize(7.5F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("4. Satz").setFontSize(7.5F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("5. Satz").setFontSize(7.5F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("Summe").setFontSize(7.5F))
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
                    .add(new Paragraph("Pfeil 1").setFontSize(8.0F))
                )
                .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                    .add(new Paragraph("Pfeil 2").setFontSize(8.0F))
                )
                .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                    .add(new Paragraph("Pfeil 1").setFontSize(8.0F))
                )
                .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                    .add(new Paragraph("Pfeil 2").setFontSize(8.0F))
                )
                .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                    .add(new Paragraph("Pfeil 1").setFontSize(8.0F))
                )
                .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                    .add(new Paragraph("Pfeil 2").setFontSize(8.0F))
                )
                .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                    .add(new Paragraph("Pfeil 1").setFontSize(8.0F))
                )
                .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                    .add(new Paragraph("Pfeil 2").setFontSize(8.0F))
                )
                .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                    .add(new Paragraph("Pfeil 1").setFontSize(8.0F))
                )
                .addCell(new Cell().setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(12.5F)
                    .add(new Paragraph("Pfeil 2").setFontSize(8.0F))
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
                    .add(new Paragraph("Summe").setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(20.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                    .add(new Paragraph("Summe").setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(20.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                    .add(new Paragraph("Summe").setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(20.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                    .add(new Paragraph("Summe").setFontSize(10.0F))
                )
                .addCell(new Cell().setHeight(20.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                    .add(new Paragraph("Summe").setFontSize(10.0F))
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
                    .add(new Paragraph("1. Satz").setFontSize(5.0F))
                )
                .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                    .add(new Paragraph("2. Satz").setFontSize(5.0F))
                )
                .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                    .add(new Paragraph("3. Satz").setFontSize(5.0F))
                )
                .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                    .add(new Paragraph("4. Satz").setFontSize(5.0F))
                )
                .addCell(new Cell().setTextAlignment(TextAlignment.CENTER).setHeight(20.0F)
                    .add(new Paragraph("5. Satz").setFontSize(5.0F))
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
                    .add(new Paragraph("Unterschrift").setFontSize(10.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(50.0F)))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("Unterschrift").setFontSize(10.0F).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(UnitValue.createPercentValue(50.0F)))
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