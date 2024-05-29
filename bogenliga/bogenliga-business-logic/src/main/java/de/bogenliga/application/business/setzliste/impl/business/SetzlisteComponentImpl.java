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
    private static final String PRECONDITION_USERID = "userId cannot be negative";
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


        int[][] setzlisteStructure;
        SETZLISTE_STRUCTURE_SIZE_8_6_4(int[][] setzlisteStructure) {
            this.setzlisteStructure = setzlisteStructure;
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
     * @param userId Key number of a current user
     * @return matchDOList a list of all matches and Begegnungen in structure of SETZLISTE_STRUCTURE
     * */
    @Override
    public List<MatchDO> generateMatchesBySetzliste(Long wettkampfID, Long userId) {

        Preconditions.checkArgument(wettkampfID >= 0, PRECONDITION_WETTKAMPFID);
        Preconditions.checkArgument(userId >= 0, PRECONDITION_USERID);

        List<MatchDO> matchDOList = matchComponent.findByWettkampfId(wettkampfID);
        List<SetzlisteBE> setzlisteBEList = setzlisteDAO.getTableByWettkampfID(wettkampfID);
        if (setzlisteBEList.isEmpty()){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "Der Wettkampf mit der ID " + wettkampfID +" oder die Tabelleneinträge vom vorherigen Wettkampftag existieren noch nicht");
        }

        int numberOfTeams = approvedNumberOfTeams(setzlisteBEList.size());
        int indexStructure = indexOfStructure(numberOfTeams);

        if (matchDOList.isEmpty()){
            //itarate through matches
            for (int i = 0; i < SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].setzlisteStructure.length; i++){
                //iterate through target boards
                for (int j = 0; j < SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].setzlisteStructure[i].length; j++) {
                    long begegnung = Math.round((float) (j + 1) / 2);
                    long currentTeamID = getTeamIDByTablePos(SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].setzlisteStructure[i][j], setzlisteBEList);
                    MatchDO newMatchDO = new MatchDO(null, (long) i + 1, wettkampfID, currentTeamID, begegnung, (long) j + 1, null, null,null,null,null,null,null);
                    matchDOList.add(matchComponent.create(newMatchDO, userId));
                }
            }
        }
        else{
            LOGGER.debug("Matches existieren bereits");
        }
        return matchDOList;
    }

    /**
     * writes a document with a table containing information from given Setzliste
     *
     * @param doc document to write
     * @param setzlisteBEList list with data for the doc
     */
    private void generateDoc(Document doc, List<SetzlisteBE> setzlisteBEList){

        doc.setFontSize(9.2f);
        int numberOfTeams = approvedNumberOfTeams(setzlisteBEList.size());

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

        // adjust table height dynamically to fit on a single A4 page
        float tableHeight = calculateTableHeight(numberOfTeams, doc);

        // Create dynamic Table
        Table table = createTable(numberOfTeams, tableHeight);

        // Add Table header
        addTableHeader(table, numberOfTeams);

        // Add Table content
        addTableContent(table, setzlisteBEList, numberOfTeams);

        doc.add(table);
        doc.close();
    }

    /**
     * calculates the specific height for the given size of team
     *
     * @param doc document to write
     * @param numberOfTeams How many competing Teams
     * @return calculated table height
     */
    private float calculateTableHeight(int numberOfTeams, Document doc) {

        float tableHeight;

        switch (numberOfTeams) {
            case 8:
                tableHeight = PageSize.A4.getWidth() - 129 - doc.getBottomMargin();
                break;
            case 6:
                tableHeight = PageSize.A4.getWidth() - 246 - doc.getBottomMargin();
                break;
            case 4:
                tableHeight = PageSize.A4.getWidth() - 187 - doc.getBottomMargin();
                break;
            default:
                tableHeight = PageSize.A4.getWidth() - doc.getBottomMargin();
        }

        return tableHeight;
    }

    /**
     * Creates the table with specific amount of columns
     *
     * @param numberOfTeams How many competing Teams
     * @param tableHeight the height needed for the document
     * @return created Table with specific height and column width
     */
    private Table createTable(int numberOfTeams, float tableHeight) {
// Max Columns for Table
        float[] coulumnWidth = new float[]{40, 150, 40, 150, 40, 150, 40, 150, 40};

        //Copys relevant amount of columns to fit the amount of begegnungen per Match (8 Teams = 4, 6Teams = 3, 4Teams = 2)
        float[] dynamicColumnAmount = new float[numberOfTeams + 1];
        System.arraycopy(coulumnWidth, 0, dynamicColumnAmount, 0, numberOfTeams + 1);
      
        return new Table(dynamicColumnAmount).setHeight(tableHeight);
    }

    /**
     * adds the Header into the Document
     *
     * @param table generated table with specific height and column width
     * @param numberOfTeams How many competing Teams
     */
    private void addTableHeader(Table table, int numberOfTeams) {

        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Match")));

        for (int i = 1; i <= numberOfTeams/2; i++) {
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Scheibe " + (i * 2 - 1) + " + " + (i * 2))));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(SETZLISTE_MPKTE)));
        }
    }

    /**
     * adds specific Content to the Document (Matchnumber, disc, Matchups, ...)
     *
     * @param table generated table with specific height and column width and header
     * @param setzlisteBEList list with data for the doc
     * @param numberOfTeams How many competing Teams
     */
    private void addTableContent(Table table, List<SetzlisteBE> setzlisteBEList, int numberOfTeams) {
        int mpkteSpacing = 25;
        int indexStructure = indexOfStructure(numberOfTeams);
        int correctSpacing;

        if (numberOfTeams == 8 || numberOfTeams == 4) {
            correctSpacing = 8;
        } else {
            correctSpacing = 6;
        }


        //Create Setzliste content on base of SETZLISTE_STRUCTURE array
        // iterate through the number of matches
        for (int i = 0; i < SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].setzlisteStructure.length; i++) {
            table.addCell(new Cell(2, 1).add(new Paragraph(Integer.toString(i + 1))).setHeight(table.getHeight().getValue() / correctSpacing));
            // iterate through the number of teams
            for (int j = 0; j < numberOfTeams/2 ; j++) {

                table.addCell(new Cell(2, 1).add(new Paragraph(getTeamsCellParagraph(i, (j * 2), (j * 2 + 1), setzlisteBEList, indexStructure)))
                        .setHeight(table.getHeight().getValue() / correctSpacing));
                table.addCell(new Cell().setHeight(mpkteSpacing));
            }
            for (int k = 1; k <= numberOfTeams/2; k++) {
                table.addCell(new Cell().setHeight(mpkteSpacing));
            }
        }
    }

    /**
     * Helper function for generateDoc(), that gets two Teams that compete in a Match on the Setzliste
     * @param index Match no.
     * @param pos1 Position of first Team in SETZTLISTE_STRUCTURE
     * @param pos2 Position of second Team in SETZTLISTE_STRUCTURE
     * @param setzlisteBEList the List with data for generateDoc
     * @return String with 2 Teams where each team has two lines of space with a line width of X chars
     * */
    private String getTeamsCellParagraph(int index, int pos1, int pos2, List<SetzlisteBE> setzlisteBEList, int indexStructure) {

        String firstTwoLines = SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].setzlisteStructure[index][pos1] + " " + getTeamName(SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].setzlisteStructure[index][pos1], setzlisteBEList);
        if (firstTwoLines.length() <= 26) {
            firstTwoLines += "\n";
        }

        return firstTwoLines + "\n" +
                SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].setzlisteStructure[index][pos2] + " " + getTeamName(SETZLISTE_STRUCTURE_SIZE_8_6_4.values()[indexStructure].setzlisteStructure[index][pos2], setzlisteBEList);
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
     * @param numberOfTeams How many competing Teams
     * @return index of Structure for given Team Size
     * */
    private int indexOfStructure(int numberOfTeams) {
        if (numberOfTeams == 8) {
            return 0;

        } else if (numberOfTeams == 6) {
            return 1;
        } else {
            return 2;
        }
    }


    /**
     * Checks if given size of competing Teams is correct
     *
     * @param numberOfTeams How many competing Teams
     * @return approved Number of Teams (8, 6 , 4)
     */
    private int approvedNumberOfTeams (int numberOfTeams){
        if (numberOfTeams == 8 || numberOfTeams == 6 || numberOfTeams == 4){
            return numberOfTeams;
        }
        throw new BusinessException(ErrorCode.INCORRECT_AMOUNT_OF_TEAMS, "Anzahl der Teams muss 8, 6 oder 4 betragen, Aktuelle Größe:" + numberOfTeams);
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
