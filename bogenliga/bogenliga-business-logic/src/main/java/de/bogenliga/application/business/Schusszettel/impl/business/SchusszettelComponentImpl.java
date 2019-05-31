package de.bogenliga.application.business.Schusszettel.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import de.bogenliga.application.business.Schusszettel.api.SchusszettelComponent;
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

        for (int i = 1; i <= 2; i++) {
            // Generate tables
            final Table tableHead = new Table(UnitValue.createPercentArray(3), true);
            final Table tableFirstRow = new Table(UnitValue.createPercentArray(2), true);
            final Table tableFirstRowFirstPart = new Table(UnitValue.createPercentArray(2), true);
            final Table tableFirstRowSecondPart = new Table(UnitValue.createPercentArray(7), true);
            final Table tableSecondRow = new Table(UnitValue.createPercentArray(1), true);
            final Table tableThirdRow = new Table(UnitValue.createPercentArray(2), true);

            // Table head
            tableHead
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                   .add(new Paragraph(mannschaftName[i - 1]).setBold().setFontSize(12.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                    .add(new Paragraph(wettkampfTag + ". Wettkampf").setBold().setFontSize(12.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT)
                    .add(new Paragraph("Scheibe " + matchDOs[0].getScheibenNummer()).setBold().setFontSize(12.0F))
                )
            ;

            // First row

            // First part
            tableFirstRowFirstPart.setWidth(UnitValue.createPercentValue(100.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("\n"))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                   .add(new Paragraph(mannschaftName[i - 1]).setBold())
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(16.0F)
                    .add(new Paragraph(matchDOs[0].getNr() + ". Match"))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("gegen").setBold())
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("\n"))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(mannschaftName[i % 2]).setBold())
                )
            ;

            // Second part
            tableFirstRowSecondPart.setWidth(UnitValue.createPercentValue(100.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("1.S.Pkte").setFontSize(5.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("2.S.Pkte").setFontSize(5.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("3.S.Pkte").setFontSize(5.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("4.S.Pkte").setFontSize(5.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("5.S.Pkte").setFontSize(5.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("Summe").setFontSize(5.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("Matchpkte").setFontSize(5.0F))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
                .addCell(new Cell().setBorder(new SolidBorder(Border.SOLID)).setHeight(15.0F)
                    .add(new Paragraph(""))
                )
            ;

            // Second row

            // Third row
            tableThirdRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(mannschaftName[0]).setBold())
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph(mannschaftName[1]).setBold())
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("\n"))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("\n"))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("Unterschrift").setFontSize(10.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(new Paragraph("Unterschrift").setFontSize(10.0F))
                )
            ;

            // Add subtables to main table
            tableFirstRow.setHeight(UnitValue.createPercentValue(10.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(tableFirstRowFirstPart)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(tableFirstRowSecondPart)
                )
            ;

            // Add to all to document
            doc
                .add(new Div().setMargin(10.0F)
                    .add(tableHead)
                    .add(new Div().setBorder(new SolidBorder(Border.SOLID))
                        .add(tableFirstRow)
                        .add(tableSecondRow)
                        .add(tableThirdRow)
                    )
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
}