package de.bogenliga.application.business.Bogenkontrollliste.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.css.Rect;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import de.bogenliga.application.business.Bogenkontrollliste.api.BogenkontrolllisteComponent;
import de.bogenliga.application.business.Setzliste.impl.business.SetzlisteComponentImpl;
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
 * Class to generate Bogenkontrollliste
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 * @author Sebastian Eckl, sebastian_alois.eckl@student.reutlingen-university.de
 */
@Component
public class BogenkontrolllisteComponentImpl implements BogenkontrolllisteComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetzlisteComponentImpl.class);

    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";
    private static final String PRECONDITION_DOCUMENT = "doc cannot be null";
    private static final String PRECONDITION_TEAM_MAPPING = "TeamMemberMapping cannot be empty";
    private static final String PRECONDITION_WETTKAMPFDO = "wettkampfDO cannot be null";
    private static final String PRECONDITION_VERANSTALTUNGSNAME =  "veranstaltungsName cannot be null";

    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final MatchComponent matchComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;


    @Autowired
    public BogenkontrolllisteComponentImpl(final DsbMannschaftComponent dsbMannschaftComponent,
                                           final VereinComponent vereinComponent,
                                           final WettkampfComponent wettkampfComponent,
                                           final VeranstaltungComponent veranstaltungComponent,
                                           final MatchComponent matchComponent,
                                           final MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                           final DsbMitgliedComponent dsbMitgliedComponent) {
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.matchComponent = matchComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
    }

    @Override
    public byte[] getBogenkontrolllistePDFasByteArray(long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);


        Hashtable<String, List<DsbMitgliedDO>> TeamMemberMapping = new Hashtable<>();

        // Collect Information
        WettkampfDO wettkampfDO = wettkampfComponent.findById(wettkampfid);
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(wettkampfDO.getVeranstaltungsId());

        String eventName = veranstaltungDO.getVeranstaltungName();

        for(int i=1; i <= 8; i++){
            MatchDO matchDO = matchComponent.findByWettkampfIDMatchNrScheibenNr(wettkampfid, 1L, (long) i);
            String TeamName = getTeamName(matchDO.getMannschaftId());
            List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = mannschaftsmitgliedComponent.findAllSchuetzeInTeam(matchDO.getMannschaftId());
            List<DsbMitgliedDO> dsbMitgliedDOList = new ArrayList<>();
            for(MannschaftsmitgliedDO mannschaftsmitglied: mannschaftsmitgliedDOList){
                dsbMitgliedDOList.add(dsbMitgliedComponent.findById(mannschaftsmitglied.getDsbMitgliedId()));
            }
            TeamMemberMapping.put(TeamName,dsbMitgliedDOList);


        }
        try (ByteArrayOutputStream result = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(result);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document doc = new Document(pdfDocument, PageSize.A4)) {

            generateBogenkontrolllisteDoc(doc, wettkampfDO, TeamMemberMapping, eventName);

            return result.toByteArray();

        } catch (IOException e) {
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                    "Bogenkontrollliste PDF konnte nicht erstellt werden: " + e);
        }
    }


    /**
     * Generates the Document
     *
     * @param doc Doc to write
     * @param wettkampfDO WettkampfDO for competition info
     * @param TeamMemberMapping Key: TeamName String, Value: List of DSBMitgliedDO (Contains shooters)
     */
    private void generateBogenkontrolllisteDoc(Document doc, WettkampfDO wettkampfDO, Hashtable<String, List<DsbMitgliedDO>> TeamMemberMapping, String veranstaltungsName) {
        Preconditions.checkNotNull(doc, PRECONDITION_DOCUMENT);
        Preconditions.checkNotNull(wettkampfDO, PRECONDITION_WETTKAMPFDO);
        Preconditions.checkArgument(!TeamMemberMapping.isEmpty(), PRECONDITION_TEAM_MAPPING);
        Preconditions.checkNotNull(veranstaltungsName, PRECONDITION_VERANSTALTUNGSNAME);
        String[] teamNameList = new String[8];
        int i = 0;

        for (String key : TeamMemberMapping.keySet()) {
            teamNameList[i] = key;
            i++;
        }

        final Table PageTitle = new Table(UnitValue.createPercentArray(1), true);
        PageTitle.addCell(addTitle(wettkampfDO, veranstaltungsName));

        doc.add(PageTitle);

        for (int manschaftCounter = 0; manschaftCounter < 8; manschaftCounter++) {
            final Table tableFirstRow = new Table(UnitValue.createPercentArray(3), true);
            final Table tableFirstRowFirstPart = new Table(UnitValue.createPercentArray(new float[] { 25.0F, 75.0F}), true);
            final Table tableFirstRowSecondPart = new Table(UnitValue.createPercentArray(7), true);
            final Table tableFirstRowThirdPart = new Table(UnitValue.createPercentArray(1), true);

            tableFirstRowFirstPart
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph("Anw.").setFontSize(10.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph(teamNameList[manschaftCounter]).setBold()).setFontSize(10.0F))
            ;

            tableFirstRowSecondPart
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph("M1").setFontSize(10.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph("M2").setFontSize(10.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph("M3").setFontSize(10.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph("M4").setFontSize(10.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph("M5").setFontSize(10.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph("M6").setFontSize(10.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph("M7").setFontSize(10.0F))
                )
            ;

            tableFirstRowThirdPart
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph("Bemerkungen zur Bogenkontrolle").setFontSize(10.0F))
                )
            ;

            tableFirstRow
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(tableFirstRowFirstPart)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(tableFirstRowSecondPart)
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(tableFirstRowThirdPart)
                )
            ;

            doc
                .add(new Div().setPaddings(10.0F, 10.0F, 1.0F, 10.0F).setMargins(2.5F, 0.0F, 1.0F, 0.0F).setBorder(Border.NO_BORDER)
                    .add(tableFirstRow).setBorder(Border.NO_BORDER)
                )
            ;

            for (int mitgliedCounter = 1; mitgliedCounter < TeamMemberMapping.get(teamNameList[manschaftCounter]).size() + 1; mitgliedCounter++) {
                final Table tableBody = new Table(UnitValue.createPercentArray(3), true);
                final Table tableBodyFirstPart = new Table(UnitValue.createPercentArray(new float[] { 25.0F, 75.0F}), true);
                final Table tableBodySecondPart = new Table(UnitValue.createPercentArray(7), true);
                final Table tableBodyThirdPart = new Table(UnitValue.createPercentArray(1), true);

                final Table tableCheckbox1 = new Table(UnitValue.createPercentArray(1), false);
                final Table tableCheckbox2 = new Table(UnitValue.createPercentArray(new float[] {75.0F, 25.0F}), true);

                tableCheckbox1
                        .addCell(new Cell().setHeight(10.0F).setWidth(10.0F))
                        .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(10.0F))
                ;

                tableCheckbox2
                        .addCell(new Cell().setHeight(10.0F).setWidth(0.7F))
                        .addCell(new Cell().setBorder(Border.NO_BORDER))
                        .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)).setWidth(0.7F))
                        .addCell(new Cell().setBorder(Border.NO_BORDER))
                ;

                tableBodyFirstPart
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(tableCheckbox1.setBorder(Border.NO_BORDER)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(mitgliedCounter + " " + TeamMemberMapping.get(
                                teamNameList[manschaftCounter]).get(mitgliedCounter - 1).getNachname() + ", " + TeamMemberMapping.get(
                                teamNameList[manschaftCounter]).get(mitgliedCounter - 1).getVorname()).setBold().setFontSize(10.0F)))
                ;

                tableBodySecondPart
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(tableCheckbox2.setBorder(Border.NO_BORDER)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(tableCheckbox2.setBorder(Border.NO_BORDER)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(tableCheckbox2.setBorder(Border.NO_BORDER)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(tableCheckbox2.setBorder(Border.NO_BORDER)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(tableCheckbox2.setBorder(Border.NO_BORDER)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(tableCheckbox2.setBorder(Border.NO_BORDER)))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                        .add(tableCheckbox2.setBorder(Border.NO_BORDER))
                    )
                ;

                tableBodyThirdPart
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(" ").setFontSize(10.0F))
                    )
                ;

                if (mitgliedCounter == 1) {
                    tableBody
                            .addCell(new Cell().setBorder(Border.NO_BORDER)
                                    .add(tableBodyFirstPart)
                            )
                            .addCell(new Cell().setBorder(Border.NO_BORDER)
                                    .add(tableBodySecondPart)
                            )
                            .addCell(new Cell().setBorder(Border.NO_BORDER)
                                    .add(tableBodyThirdPart)
                            )
                    ;
                }

                else {
                    tableBody
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID))
                                    .add(tableBodyFirstPart)
                            )
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID))
                                    .add(tableBodySecondPart)
                            )
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID))
                                    .add(tableBodyThirdPart)
                            )
                    ;
                }


                if (mitgliedCounter == TeamMemberMapping.get(teamNameList[manschaftCounter]).size()){
                    tableBody
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID))
                            )
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID))
                            )
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID))
                            )
                    ;
                }

                doc
                    .add(new Div().setPaddings(0.0F, 10.0F, 0.0F, 10.0F).setBorder(Border.NO_BORDER)
                            .add(tableBody).setBorder(Border.NO_BORDER)
                    )
                ;
            }
        }

        doc.close();
    }

    /**
     * function to add title and date at the top of each page
     *
     * @param wettkampfDO DO of current Wettkampf
     *        veranstaltungsName name of the current Veranstaltung
     * @return cell containing the title and the date
     */
    private static Cell addTitle(WettkampfDO wettkampfDO, String veranstaltungsName){
        return new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Bogenkontrolle / "+wettkampfDO.getWettkampfTag()+". Bogenligawettkampf / "+veranstaltungsName)
                        .setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(14.0F))
                .add(new Paragraph("am "+wettkampfDO.getDatum())
                        .setTextAlignment(TextAlignment.RIGHT).setFontSize(10.0F));
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
