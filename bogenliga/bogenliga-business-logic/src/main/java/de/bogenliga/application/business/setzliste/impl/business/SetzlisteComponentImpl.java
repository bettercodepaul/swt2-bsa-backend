package de.bogenliga.application.business.setzliste.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import de.bogenliga.application.business.setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.setzliste.impl.dao.SetzlisteDAO;
import de.bogenliga.application.business.setzliste.impl.entity.SetzlisteBE;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link SetzlisteComponent}
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 */
@Component
public class SetzlisteComponentImpl implements SetzlisteComponent {

    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";
    private static final String SETZLISTE_MPKTE = "M.Pkte";
    private static final Logger LOGGER = LoggerFactory.getLogger(SetzlisteComponentImpl.class);

    private final SetzlisteDAO setzlisteDAO;
    private final MatchComponent matchComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;

    /**
     * Structure of setzliste for team size 8,6,4
     * dim 1, row: Match
     * dim 2: Teams (with Scheibe as columns)
     *
     * Every two teams in each match form one Begegnung.
     *
     * Constructor returns array from enum as matrix.
     */

    private enum SETZLISTE_STRUCTURE_SIZE_8_6_4 {
        SETZLISTE_STRUCTURE_8TEAM (new int[][] {
                {5, 4, 2, 7, 1, 8, 3, 6},
                {3, 5, 8, 4, 7, 1, 6, 2},
                {4, 7, 1, 6, 2, 5, 8, 3},
                {8, 2, 7, 3, 6, 4, 1, 5},
                {7, 6, 5, 8, 3, 2, 4, 1},
                {1, 3, 4, 2, 8, 6, 5, 7},
                {2, 1, 6, 5, 4, 3, 7, 8}}),

        SETZLISTE_STRUCTURE_6TEAM (new int[][] {
                {2, 5, 1, 6, 3, 4},
                {6, 3, 2, 4, 5, 1},
                {1, 2, 5, 3, 4, 6},
                {5, 4, 3, 1, 6, 2},
                {4, 1, 6, 5, 2, 3}}),

        SETZLISTE_STRUCTURE_4TEAM (new int[][] {
                {1, 4, 2, 3},
                {2, 4, 3, 1},
                {4, 3, 1, 2},
                {4, 1, 2, 3},
                {1, 3, 4, 2},
                {3, 4, 2, 1}});


        int[][] SETZLISTE_STRUCTURE;
        SETZLISTE_STRUCTURE_SIZE_8_6_4(int[][] SETZLISTE_STRUCTURE) {
            this.SETZLISTE_STRUCTURE = SETZLISTE_STRUCTURE;
        }
    }
    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param setzlisteDAO to access the database and return setzliste representations
     */
    @Autowired
    public SetzlisteComponentImpl(SetzlisteDAO setzlisteDAO, MatchComponent matchComponent,
                                  WettkampfComponent wettkampfComponent, VeranstaltungComponent veranstaltungComponent,
                                  DsbMannschaftComponent dsbMannschaftComponent, VereinComponent vereinComponent) {
        this.setzlisteDAO = setzlisteDAO;
        this.matchComponent = matchComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
    }


    @Override
    public byte[] getPDFasByteArray(long wettkampfID) {
        Preconditions.checkArgument(wettkampfID >= 0, PRECONDITION_WETTKAMPFID);

        List<SetzlisteBE> setzlisteBEList = setzlisteDAO.getTableByWettkampfID(wettkampfID);
        byte[] bResult;
        if (!setzlisteBEList.isEmpty()) {
            try (ByteArrayOutputStream result = new ByteArrayOutputStream();
                 PdfWriter writer = new PdfWriter(result);
                 PdfDocument pdfDocument = new PdfDocument(writer);
                 Document doc = new Document(pdfDocument, PageSize.A4.rotate())) {

                generateDoc(doc, setzlisteBEList);

                bResult = result.toByteArray();
                LOGGER.debug("Setzliste erstellt");

            } catch (IOException e) {
                LOGGER.error("PDF Setzliste konnte nicht erstellt werden: " + e);
                throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                        "PDF Setzliste konnte nicht erstellt werden: " + e);
            }
        }
        else{
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "Der Wettkampf mit der ID " + wettkampfID +" oder die Tabelleneinträge vom vorherigen Wettkampftag existieren noch nicht");
        }
        return bResult;
    }

    /**
     * Generates list with all competing teams, forms Begegnungen and assigns them to match and Scheibe.
     * @param wettkampfID Key number of a Wettkampf
     * @return matchDOList a list of all matches and Begegnungen in structure of SETZLISTE_STRUCTURE
     * */
    @Override
    public List<MatchDO> generateMatchesBySetzliste(long wettkampfID) {
        Preconditions.checkArgument(wettkampfID >= 0, PRECONDITION_WETTKAMPFID);

        List<MatchDO> matchDOList = matchComponent.findByWettkampfId(wettkampfID);
        List<SetzlisteBE> setzlisteBEList = setzlisteDAO.getTableByWettkampfID(wettkampfID);
        if (setzlisteBEList.isEmpty()){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "Der Wettkampf mit der ID " + wettkampfID +" oder die Tabelleneinträge vom vorherigen Wettkampftag existieren noch nicht");
        }
        int indexStructure = numberOfTeams(setzlisteBEList);
        if (matchDOList.isEmpty()){
            //itarate through matches
            for (int i = 0; i < SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].SETZLISTE_STRUCTURE.length; i++){
                //iterate through target boards
                for (int j = 0; j < SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].SETZLISTE_STRUCTURE[i].length; j++) {
                    long begegnung = Math.round((float) (j + 1) / 2);
                    long currentTeamID = getTeamIDByTablePos(SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].SETZLISTE_STRUCTURE[i][j], setzlisteBEList);
                    MatchDO newMatchDO = new MatchDO(null, (long) i + 1, wettkampfID, currentTeamID, begegnung, (long) j + 1, null, null,null,null,null,null,null);
                    matchDOList.add(matchComponent.create(newMatchDO, (long) 0));
                }
            }
        }
        else{
            LOGGER.debug("Matches existieren bereits");
        }
        return matchDOList;
    }

    /**
     * <p>writes a document with a table containing information from given Setzliste
     * </p>
     * @param doc document to write
     * @param setzlisteBEList list with data for the doc
     */
    private void generateDoc(Document doc, List<SetzlisteBE> setzlisteBEList){

        doc.setFontSize(9.2f);
        int numberOfTeams = setzlisteBEList.size();

        // description
        DateFormat sdF2 = new SimpleDateFormat("dd.MM.yyyy");
        WettkampfDO wettkampfDO = wettkampfComponent.findById(setzlisteBEList.get(0).getWettkampfID());
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(wettkampfDO.getWettkampfVeranstaltungsId());
        String dateFormatted = sdF2.format(wettkampfDO.getWettkampfDatum());
        doc.add(new Paragraph("Setzliste " +
                wettkampfDO.getWettkampfTag() + ". Wettkampf " + veranstaltungDO.getVeranstaltungName()));
        doc.add(new Paragraph("am " + dateFormatted + " in"));
        doc.add(new Paragraph(wettkampfDO.getWettkampfStrasse() + ", " +  wettkampfDO.getWettkampfPlz() + " " + wettkampfDO.getWettkampfOrtsname() + ", " + wettkampfDO.getWettkampfBeginn() + " Uhr"));

        doc.add(new Paragraph(""));

        //Create table
        Table table = new Table(new float[]{40, 150, 40, 150, 40, 150, 40, 150, 40});

        //adjust table height to fit on a single A4 page (129 is fixed value for height of the first 4 doc Paragraphs)
        table.setHeight(PageSize.A4.getWidth() - 129 - doc.getBottomMargin());

        //Table header
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Match")));

        for (int i = 1; i <= numberOfTeams/2; i++) {
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Scheibe " + (i * 2 - 1) + " + " + (i * 2))));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(SETZLISTE_MPKTE)));
        }

        int mpkteSpacing = 25;
        int indexStructure = numberOfTeams(setzlisteBEList);

        //Create Setzliste content on base of SETZLISTE_STRUCTURE array
        // iterate through the number of matches
        for (int i = 0; i < SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].SETZLISTE_STRUCTURE.length; i++) {
            table.addCell(new Cell(2, 1).add(new Paragraph(Integer.toString(i + 1))).setHeight(table.getHeight().getValue() / 8));
            // iterate through the number of teams
            for (int j = 0; j < numberOfTeams/2 ; j++) {

                table.addCell(new Cell(2, 1).add(new Paragraph(getTeamsCellParagraph(i, (j * 2), (j * 2 + 1), setzlisteBEList)))
                        .setHeight(table.getHeight().getValue() / 8));
                table.addCell(new Cell().setHeight(mpkteSpacing));
            }
            table.addCell(new Cell().setHeight(mpkteSpacing));
            table.addCell(new Cell().setHeight(mpkteSpacing));
            table.addCell(new Cell().setHeight(mpkteSpacing));
            table.addCell(new Cell().setHeight(mpkteSpacing));
        }

        doc.add(table);
        doc.close();
    }

    /**
     * Helper function for generateDoc(), that gets two Teams that compete in a Match on the Setzliste
     * @param index Match no.
     * @param pos1 Position of first Team in SETZTLISTE_STRUCTURE
     * @param pos2 Position of second Team in SETZTLISTE_STRUCTURE
     * @param setzlisteBEList the List with data for generateDoc
     * @return String with 2 Teams where each team has two lines of space with a line width of X chars
     * */
    private String getTeamsCellParagraph(int index, int pos1, int pos2, List<SetzlisteBE> setzlisteBEList) {

        int indexStructure = numberOfTeams(setzlisteBEList);

        String firstTwoLines = SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].SETZLISTE_STRUCTURE[index][pos1] + " " + getTeamName(SETZLISTE_STRUCTURE_SIZE_8_6_4.SETZLISTE_STRUCTURE_8TEAM.SETZLISTE_STRUCTURE[index][pos1], setzlisteBEList);
        if (firstTwoLines.length() <= 26) {
            firstTwoLines += "\n";
        }
        return firstTwoLines + "\n" +
                SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].SETZLISTE_STRUCTURE[index][pos2] + " " + getTeamName(SETZLISTE_STRUCTURE_SIZE_8_6_4.SETZLISTE_STRUCTURE_8TEAM.SETZLISTE_STRUCTURE[index][pos2], setzlisteBEList);
    }

    /**
     * help function to get table entry
     * @param tablePos Postition in table
     * @param setzlisteBEList list with data
     * @return index if found, otherwise -1
     */
    private long getTeamIDByTablePos(int tablePos, List<SetzlisteBE> setzlisteBEList) {
        for (SetzlisteBE setzlisteBE : setzlisteBEList) {
            if (setzlisteBE.getLigatabelleTabellenplatz() == tablePos) {
                return setzlisteBE.getMannschaftID();
            }
        }
        return -1;
    }

    /**
     * Returns specified index for the correct team size structure.
     * @param setzlisteBEList List of all competing teams in Wettkampf with context to Ligaplatz
     * @return index for size of Match
     * */
    private int numberOfTeams(List<SetzlisteBE> setzlisteBEList){
        int numberOfTeams = setzlisteBEList.size();
        if (numberOfTeams >= 7 && numberOfTeams <= 8){
            return 0;

        } else if (numberOfTeams >= 5 && numberOfTeams <= 6){
            return 1;

        } else if (numberOfTeams >= 3 && numberOfTeams <= 4){
            return 2;

        } else{
            LOGGER.debug("Number of teams can not form match ups.");
        }
        return numberOfTeams;
    }

    /**
     * help funktion to get team name
     * @param tablePos index in table
     * @param setzlisteBEList list with data
     * @return name of the team
     */
    private String getTeamName(int tablePos, List<SetzlisteBE> setzlisteBEList) {
        long teamID = getTeamIDByTablePos(tablePos,setzlisteBEList);
        if (teamID == -1) {
            LOGGER.error("Cannot find team for tablepos");
            return "ERROR";
        } else {
            DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(teamID);
            VereinDO vereinDO = vereinComponent.findById(dsbMannschaftDO.getVereinId());
            if (dsbMannschaftDO.getNummer() > 1) {
                return vereinDO.getName() + " " + dsbMannschaftDO.getNummer();
            } else {
                return vereinDO.getName();
            }
        }
    }
}
