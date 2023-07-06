package de.bogenliga.application.business.bogenkontrollliste.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import de.bogenliga.application.business.bogenkontrollliste.api.BogenkontrolllisteComponent;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(BogenkontrolllisteComponentImpl.class);

    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";
    private static final String PRECONDITION_DOCUMENT = "doc cannot be null";
    private static final String PRECONDITION_TEAM_MAPPING = "TeamMemberMapping cannot be empty";
    private static final String PRECONDITION_WETTKAMPFDO = "wettkampfDO cannot be null";
    private static final String PRECONDITION_VERANSTALTUNGSNAME = "veranstaltungsName cannot be null";

    private static final String AUFFUELLMANNSCHAFT_NAME = "Auffüllmannschaft";

    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private final LigaComponent ligaComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;


    @Autowired
    public BogenkontrolllisteComponentImpl(final DsbMannschaftComponent dsbMannschaftComponent,
                                           final VereinComponent vereinComponent,
                                           final LigaComponent ligaComponent,
                                           final WettkampfComponent wettkampfComponent,
                                           final VeranstaltungComponent veranstaltungComponent,
                                           final MatchComponent matchComponent,
                                           final PasseComponent passeComponent,
                                           final MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                           final DsbMitgliedComponent dsbMitgliedComponent) {
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.ligaComponent = ligaComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
    }


    @Override
    public byte[] getBogenkontrolllistePDFasByteArray(long wettkampfid) {
        byte[] bResult;
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        // Initialisierung Dsb Mitglied Hashmap und DsbMitglied, boolean Hashmap.
        HashMap<String, List<DsbMitgliedDO>> teamMemberMapping = new HashMap<>();
        HashMap<DsbMitgliedDO, Boolean> allowedMapping = new HashMap<>();

        // Informationen sammeln Wettkampf und zugehoerige Veranstaltung
        WettkampfDO wettkampfDO = wettkampfComponent.findById(wettkampfid);
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(wettkampfDO.getWettkampfVeranstaltungsId());

        // Veranstaltungsname einer konketen Veranstalung
        String eventName = veranstaltungDO.getVeranstaltungName();

        // Fuer Jedes Match eines wettkampfs -> List.add alle Schuetzen
        for (int i = 1; i <= veranstaltungDO.getVeranstaltungGroesse(); i++) {
            MatchDO matchDO = matchComponent.findByWettkampfIDMatchNrScheibenNr(wettkampfid, 1L, (long) i);
            String teamName = getTeamName(matchDO.getMannschaftId());
            LOGGER.info("Teamname {} wurde gefunden ", teamName);
            List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = mannschaftsmitgliedComponent.findAllSchuetzeInTeam(
                    matchDO.getMannschaftId());

            List<DsbMitgliedDO> dsbMitgliedDOList = new ArrayList<>();
            List<LigaDO> ligen = this.ligaComponent.findAll();

            int count = 0;
            for (MannschaftsmitgliedDO mannschaftsmitglied : mannschaftsmitgliedDOList) {
                DsbMitgliedDO dsbMitglied = dsbMitgliedComponent.findById(mannschaftsmitglied.getDsbMitgliedId());
                long thisLiga = this.veranstaltungComponent.findById(this.wettkampfComponent.findById(
                        matchDO.getWettkampfId()).getWettkampfVeranstaltungsId()).getVeranstaltungLigaID();
                // Find step of the actual League
                int thisLigaStufe = 0;
                int currentLiga = (int) thisLiga;

                // Finde und Setze die Ligastufe falls diese nicht 0 ist -> ansonsten thisLigaStufe = 0
                while (currentLiga != 0) {
                    if (ligen.get(currentLiga - 1).getLigaUebergeordnetId() != null) {
                        currentLiga = ligen.get(currentLiga - 1).getLigaUebergeordnetId().intValue();
                        thisLigaStufe++;
                    } else {
                        currentLiga = 0;
                    }
                }

                // Finde Wettkampftag über Matchcomponenet-> wettkampfID -> .getWettkampftag
                long thisWettkamptag = this.wettkampfComponent.findById(matchDO.getWettkampfId()).getWettkampfTag();

                // Finde thisSportjahr über veranstaltungsComponend -> match -> wettkampfID -> WettkampfveranstaltungsId -> getVeranstaltungSportJahr
                long thisSportjahr = this.veranstaltungComponent.findById(this.wettkampfComponent.findById(
                        matchDO.getWettkampfId()).getWettkampfVeranstaltungsId()).getVeranstaltungSportJahr();
                boolean darfSchiessen = true;

                //Finde die höchste Liga und prüfe ob das Mitglied bereits am Wettkapftag geschossen hat
                List<MannschaftsmitgliedDO> mitgliedIn = this.mannschaftsmitgliedComponent.findByMemberId(
                        dsbMitglied.getId());

                // Finde alle Wettkämpfe einer Mannschaft
                for (MannschaftsmitgliedDO mitglied : mitgliedIn) {
                    List<WettkampfDO> wettkaempfe = this.wettkampfComponent.findAllWettkaempfeByMannschaftsId(
                            mitglied.getMannschaftId());

                    // für jeden Wettkampf einer Mannschaft
                    for (WettkampfDO wettkampf : wettkaempfe) {

                        //Prüfe das Sportjahr der Veranstaltung
                        long wettkampfSportjahr = this.veranstaltungComponent.findById(
                                wettkampf.getWettkampfVeranstaltungsId()).getVeranstaltungSportJahr();

                        if (thisSportjahr == wettkampfSportjahr) {
                            long liga = this.veranstaltungComponent.findById(
                                    wettkampf.getWettkampfVeranstaltungsId()).getVeranstaltungLigaID();

                            //finde Stufe der Liga dieses Wettkampfes, wenn das Mannschaftsmitglied mindestens 2 mal eingesetzt wurde
                            if (mitglied.getDsbMitgliedEingesetzt() >= 2) {
                                currentLiga = (int) liga;
                                int ligaStufe = 0;
                                while (currentLiga != 0) {
                                    if (ligen.get(currentLiga - 1).getLigaUebergeordnetId() != null) {
                                        currentLiga = ligen.get(currentLiga - 1).getLigaUebergeordnetId().intValue();
                                        ligaStufe++;
                                    } else {
                                        currentLiga = 0;
                                    }
                                }
                                darfSchiessen = (thisLigaStufe <= ligaStufe) && darfSchiessen;
                            }
                            List<PasseDO> passen = passeComponent.findByWettkampfIdAndMitgliedId(wettkampf.getId(),
                                    dsbMitglied.getId());
                            darfSchiessen = !(thisWettkamptag == wettkampf.getWettkampfTag() && !passen.isEmpty()) && darfSchiessen;
                        }
                    }
                }
                // Füge mitglieder mit schusserlaubnis hinzu
                dsbMitgliedDOList.add(dsbMitglied);
                allowedMapping.put(dsbMitglied, darfSchiessen);
                if (darfSchiessen) {
                    LOGGER.info("Teammitglied {} {} wurde gefunden", dsbMitgliedDOList.get(count).getNachname(),
                            dsbMitgliedDOList.get(count).getVorname());
                } else {
                    LOGGER.info(
                            "Teammitglied {} {} konnte nicht hinzugefügt werden, da es schon in einer höheren Liga oder am selben Wettkampftag geschossen hat.",
                            dsbMitglied.getNachname(), dsbMitglied.getVorname());
                }
                count++;
            }
            teamMemberMapping.put(teamName, dsbMitgliedDOList);


        }
        // Erstelle den byte output Stream
        try (final ByteArrayOutputStream result = new ByteArrayOutputStream();
             final PdfWriter writer = new PdfWriter(result);
             final PdfDocument pdfDocument = new PdfDocument(writer);
             final Document doc = new Document(pdfDocument, PageSize.A4)) {

            generateBogenkontrolllisteDoc(doc, wettkampfDO, teamMemberMapping, eventName, allowedMapping, veranstaltungDO.getVeranstaltungGroesse());

            bResult = result.toByteArray();
            return bResult;

        } catch (IOException e) {
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                    "Bogenkontrollliste PDF konnte nicht erstellt werden: " + e);
        }
    }


    /**
     * Generates the Document
     *
     * @param doc               Doc to write
     * @param wettkampfDO       WettkampfDO for competition info
     * @param teamMemberMapping Key: TeamName String, Value: List of DSBMitgliedDO (Contains shooters)
     */
    private void generateBogenkontrolllisteDoc(Document doc, WettkampfDO wettkampfDO,
                                               HashMap<String, List<DsbMitgliedDO>> teamMemberMapping,
                                               String veranstaltungsName,
                                               HashMap<DsbMitgliedDO, Boolean> allowedMapping, int veranstaltungGroesse) {
        Preconditions.checkNotNull(doc, PRECONDITION_DOCUMENT);
        Preconditions.checkNotNull(wettkampfDO, PRECONDITION_WETTKAMPFDO);
        Preconditions.checkArgument(!teamMemberMapping.isEmpty(), PRECONDITION_TEAM_MAPPING);
        Preconditions.checkNotNull(veranstaltungsName, PRECONDITION_VERANSTALTUNGSNAME);
        String[] teamNameList = new String[veranstaltungGroesse];
        int numberOfMatches = numberOfMatches(veranstaltungGroesse);
        int i = 0;

        LOGGER.info("Es wurden {} Teams gefunden", teamMemberMapping.size());
        for (String key : teamMemberMapping.keySet()) {
            teamNameList[i] = key;
            i++;
        }

        //Table for the entire document
        final Table docTable = new Table(UnitValue.createPercentArray(1), true);

        //Table for the page title
        final Table pageTitle = new Table(UnitValue.createPercentArray(1), true);
        pageTitle.addCell(addTitle(wettkampfDO, veranstaltungsName));

        //Add page title on every page
        docTable.addHeaderCell(new Cell().setBorder(Border.NO_BORDER)
                .add(pageTitle));
        //Iterate through all the teams
        for (int manschaftCounter = 0; manschaftCounter < veranstaltungGroesse; manschaftCounter++) {

            if(teamNameList[manschaftCounter].startsWith(AUFFUELLMANNSCHAFT_NAME)){
                continue;
            }

            //Create table for each team
            final Table tableBody = new Table(UnitValue.createPercentArray(3), true).setKeepTogether(true);
            //Create column headers
            final Table tableFirstRowFirstPart = new Table(UnitValue.createPercentArray(new float[]{25.0F, 75.0F}),
                    true);
            final Table tableFirstRowSecondPart = new Table(UnitValue.createPercentArray(numberOfMatches), true);
            final Table tableFirstRowThirdPart = new Table(UnitValue.createPercentArray(1), true);

            //Add content to column headers

            tableFirstRowFirstPart
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                            .add(new Paragraph("Anw.").setFontSize(10.0F))
                    )
                    .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                            .add(new Paragraph(teamNameList[manschaftCounter]).setBold()).setFontSize(10.0F))
            ;

            String[] teams = {"M1", "M2", "M3", "M4", "M5", "M6", "M7"};

            for (int team = 0; team < numberOfMatches; team++) {
                tableFirstRowSecondPart.addCell(
                    new Cell()
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(teams[team]).setFontSize(10.0F))
                );
            }

            tableFirstRowThirdPart.addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph("Bemerkungen zur Bogenkontrolle").setFontSize(10.0F))
            )
            ;

            //Add column headers to team table
            tableBody
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

            LOGGER.info("Für Team {} wurden {} Mitglieder gefunden", manschaftCounter,
                    teamMemberMapping.get(teamNameList[manschaftCounter]).size());
            //Iterate through playerlist of each team, if no player was found, add additional information

            for (int mitgliedCounter = 1; mitgliedCounter <= teamMemberMapping.get(
                    teamNameList[manschaftCounter]).size(); mitgliedCounter++) {
                //Create columns for player content
                final Table tableBodyFirstPart = new Table(UnitValue.createPercentArray(new float[]{25.0F, 75.0F}),
                        true);
                final Table tableBodySecondPart = new Table(UnitValue.createPercentArray(numberOfMatches), true);
                final Table tableBodyThirdPart = new Table(UnitValue.createPercentArray(1), true);
//                final Table tableBodyForthPart = new Table(UnitValue.createPercentArray(new float[]{25.0F, 75.0F}), true);

                //Create tables for checkboxes
                final Table tableCheckbox1 = new Table(UnitValue.createPercentArray(new float[]{40.0F, 60.0F}), true);
                final Table tableCheckbox2 = new Table(UnitValue.createPercentArray(new float[]{75.0F, 25.0F}), true);

                tableCheckbox1
                        .addCell(new Cell().setHeight(10.0F))
                        .addCell(new Cell().setBorder(Border.NO_BORDER))
                        .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                        .addCell(new Cell().setBorder(Border.NO_BORDER))
                ;

                tableCheckbox2
                        .addCell(new Cell().setHeight(10.0F))
                        .addCell(new Cell().setBorder(Border.NO_BORDER))
                        .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                        .addCell(new Cell().setBorder(Border.NO_BORDER))
                ;

                //Add content to player columns
                if (allowedMapping.get(teamMemberMapping.get(
                        teamNameList[manschaftCounter]).get(
                        mitgliedCounter - 1))) {

                    tableBodyFirstPart
                            .addCell(new Cell().setBorder(Border.NO_BORDER)
                                    .add(tableCheckbox1.setBorder(Border.NO_BORDER)))
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                                    .add(new Paragraph(mitgliedCounter + " " + teamMemberMapping.get(
                                            teamNameList[manschaftCounter]).get(
                                            mitgliedCounter - 1).getNachname() + ", " + teamMemberMapping.get(
                                            teamNameList[manschaftCounter]).get(
                                            mitgliedCounter - 1).getVorname()).setBold().setFontSize(10.0F)))
                    ;
                } else {
                    tableBodyFirstPart
                            .addCell(new Cell().setBorder(Border.NO_BORDER)
                                    .add(tableCheckbox1.setBorder(Border.NO_BORDER)))
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                                    .add(new Paragraph(mitgliedCounter + " " + teamMemberMapping.get(
                                            teamNameList[manschaftCounter]).get(
                                            mitgliedCounter - 1).getNachname() + ", " + teamMemberMapping.get(
                                            teamNameList[manschaftCounter]).get(
                                            mitgliedCounter - 1).getVorname()).setBold().setLineThrough().setFontSize(
                                            10.0F)))
                    ;
                }

                for (int match = 0; match < numberOfMatches; match++) {
                    tableBodySecondPart.addCell(
                            new Cell().setBorder(Border.NO_BORDER)
                                    .add(tableCheckbox2.setBorder(Border.NO_BORDER))
                    );
                }

                tableBodyThirdPart
                        .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                                .add(new Paragraph(" ").setFontSize(10.0F))
                        )
                ;
/*                tableBodyForthPart
                        .addCell(new Cell().setBorder(Border.NO_BORDER)
                                .add(tableCheckbox1.setBorder(Border.NO_BORDER)))
                        .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                                .add(new Paragraph((mitgliedCounter + 1) + " ").setBold().setFontSize(10.0F)))
                ;*/

                //Add player columns to team tables

                //No border top in ferst line
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

                //Add border top to other lines
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

                //Add a Border to the last line with an empty cell
                if (mitgliedCounter == teamMemberMapping.get(teamNameList[manschaftCounter]).size()) {

                    //Add Cell without Name if less than 10 Teams
                    /*if (mitgliedCounter <= 10) {
                        tableBody.addCell(
                                        new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID))
                                                .add(tableBodyForthPart)
                                )
                                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(
                                                new SolidBorder(Border.SOLID))
                                        .add(tableBodySecondPart)
                                )
                                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(
                                                new SolidBorder(Border.SOLID))
                                        .add(tableBodyThirdPart)
                                );
                    }*/
                    tableBody.addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID))
                            )
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID))
                            )
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID))
                            )


                    ;
                    //new Empty Space under Teams
                    tableBody
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                                    .add(new Paragraph(" ").setBold().setLineThrough().setFontSize(10.0F)))
                            .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                                    .add(new Paragraph(" ").setFontSize(10.0F)))

                            .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                                    .add(new Paragraph(" ").setBold().setLineThrough().setFontSize(10.0F)));


                }
            }

            //Add team table to the document table
            docTable
                    .setPaddings(10.0F, 10.0F, 0.0F, 10.0F).setBorder(Border.NO_BORDER)
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(tableBody));
        }

        //Add document table to document
        doc
                .add(new Div().setPaddings(10.0F, 10.0F, 0.0F, 10.0F).setBorder(Border.NO_BORDER)
                        .add(docTable).setBorder(Border.NO_BORDER)
                )
        ;

        doc.close();
    }

    /**
     * Returns number of Matches for given size.
     * @param veranstaltungGroesse How many competing Teams
     * @return number of Matches for given veranstaltungGroesse
     * */
    private int numberOfMatches(int veranstaltungGroesse) {
        if (veranstaltungGroesse == 8 || veranstaltungGroesse == 6) {
            return veranstaltungGroesse - 1;
        } else {
            return 6;
        }
    }

    /**
     * function to add title and date at the top of each page
     *
     * @param wettkampfDO DO of current Wettkampf veranstaltungsName name of the current Veranstaltung
     *
     * @return cell containing the title and the date
     */
    private static Cell addTitle(WettkampfDO wettkampfDO, String veranstaltungsName) {
        return new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(
                        "Bogenkontrolle / " + wettkampfDO.getWettkampfTag() + ". Bogenligawettkampf / " + veranstaltungsName)
                        .setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(14.0F))
                .add(new Paragraph("am " + wettkampfDO.getWettkampfDatum())
                        .setTextAlignment(TextAlignment.RIGHT).setFontSize(10.0F));
    }


    /**
     * help function to get team name
     *
     * @param teamID ID of the team
     *
     * @return name of the team
     */
    private String getTeamName(long teamID) {
        Preconditions.checkArgument(teamID >= 0, "TeamID cannot be Negative");
        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(teamID);
        VereinDO vereinDO = vereinComponent.findById(dsbMannschaftDO.getVereinId());
        if (dsbMannschaftDO.getNummer() > 1) {
            return vereinDO.getName() + " " + dsbMannschaftDO.getNummer();
        } else {
            return vereinDO.getName();
        }
    }
}
