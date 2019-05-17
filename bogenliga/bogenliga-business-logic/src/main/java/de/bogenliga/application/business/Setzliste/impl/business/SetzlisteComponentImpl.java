package de.bogenliga.application.business.Setzliste.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
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
import de.bogenliga.application.business.Setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.Setzliste.impl.dao.SetzlisteDAO;
import de.bogenliga.application.business.Setzliste.impl.entity.SetzlisteBE;
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
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link SetzlisteComponent}
 */
@Component
public class SetzlisteComponentImpl implements SetzlisteComponent {

    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";
    private static final Logger LOGGER = LoggerFactory.getLogger(SetzlisteComponentImpl.class);

    private final SetzlisteDAO setzlisteDAO;
    private final MatchComponent matchComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;


    //Structure of setzliste
    //index 1: Match
    //index 2: Scheibe
    private final int[][] SETZLISTE_STRUCTURE = {
            {5, 4, 2, 7, 1, 8, 3, 6},
            {3, 5, 8, 4, 7, 1, 6, 2},
            {4, 7, 1, 6, 2, 5, 8, 3},
            {8, 2, 7, 3, 6, 4, 1, 5},
            {7, 6, 5, 8, 3, 2, 4, 1},
            {1, 3, 4, 2, 8, 6, 5, 7},
            {2, 1, 6, 5, 4, 3, 7, 8}};

    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param setzlisteDAO to access the database and return setzliste representations
     */
    @Autowired
    public SetzlisteComponentImpl(final SetzlisteDAO setzlisteDAO, final MatchComponent matchComponent,
                                  final WettkampfComponent wettkampfComponent, final VeranstaltungComponent veranstaltungComponent,
                                  final DsbMannschaftComponent dsbMannschaftComponent, VereinComponent vereinComponent) {
        this.setzlisteDAO = setzlisteDAO;
        this.matchComponent = matchComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
    }


    /**
     * Generates a pdf as binary document
     * @param wettkampfid ID for the competition
     * @return document
     */
    @Override
    public byte[] getPDFasByteArray(final long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        final List<SetzlisteBE> setzlisteBEList = setzlisteDAO.getTableByWettkampfID(wettkampfid);
        byte[] bResult = null;
        try (final ByteArrayOutputStream result = new ByteArrayOutputStream();
             final PdfWriter writer = new PdfWriter(result);
             final PdfDocument pdfDocument = new PdfDocument(writer);
             final Document doc = new Document(pdfDocument, PageSize.A4.rotate())) {

            generateDoc(doc, setzlisteBEList);

            bResult = result.toByteArray();
            LOGGER.debug("Setzliste erstellt");

        } catch (final IOException e) {
            LOGGER.error("PDF Setzliste konnte nicht erstellt werden: " + e);
        }

        return bResult;
    }


    /**
     * <p>Creates matches in database based on the structure of Setzliste if matches don't exist
     *
     * </p>
     * @param wettkampfid ID for the competition
     */
    @Override
    public void generateMatchesBySetzliste(final long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        final List<SetzlisteBE> setzlisteBEList = setzlisteDAO.getTableByWettkampfID(wettkampfid);
        if (!setzlisteBEList.isEmpty()){
            List<MatchDO> matchDOList = matchComponent.findByWettkampfId(wettkampfid);
            if (matchDOList.isEmpty()){
                //itarate thorugh matches
                for (int i = 0; i < SETZLISTE_STRUCTURE.length; i++){
                    //iterate through target boards
                    for (int j = 0; i < SETZLISTE_STRUCTURE[i].length; i++) {
                        final long begegnung = Math.round((float) (j + 1) / 2);
                        final long currentTeamID = getTeamIDByTablePos(SETZLISTE_STRUCTURE[i][j], setzlisteBEList);
                        MatchDO newMatchDO = new MatchDO(null, (long) i + 1, wettkampfid, currentTeamID, begegnung, (long) j + 1, null, null);
                        matchComponent.create(newMatchDO, (long) 0);
                    }
                }
            }
            else{
                LOGGER.error("matches existieren in db");
                throw new BusinessException(ErrorCode.UNEXPECTED_ERROR, "Matches existieren bereits");
            }
        }
        else{
            LOGGER.error("Setzliste leer");
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "Der Wettkampf oder die Tabelleneinträge existieren noch nicht");
        }
    }


    /**
     * <p>writes a document with a table containing information from given Setzliste
     * </p>
     * @param doc document to write
     * @param setzlisteBEList list with data for the doc
     */
    private void generateDoc(Document doc, List<SetzlisteBE> setzlisteBEList){

        //description
        final DateFormat sdF = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat sdF2 = new SimpleDateFormat("dd.MM.yyyy");
        WettkampfDO wettkampfDO = wettkampfComponent.findById(setzlisteBEList.get(0).getWettkampfid());
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(wettkampfDO.getVeranstaltungsId());
        String dateFormatted = null;
        try {
            dateFormatted = sdF2.format(sdF.parse(wettkampfDO.getDatum()));
        } catch (final ParseException e) {
            LOGGER.error("Error: ", e);
        }
        doc.add(new Paragraph("Setzliste " +
                wettkampfDO.getWettkampfTag() + ". Wettkampf " + veranstaltungDO.getVeranstaltungName()));
        doc.add(new Paragraph("am " + dateFormatted + " in"));
        doc.add(new Paragraph(wettkampfDO.getWettkampfOrt() + ", " + wettkampfDO.getWettkampfBeginn() + " Uhr"));

        doc.add(new Paragraph(""));
        doc.add(new Paragraph(""));

        //Create table
        final Table table = new Table(new float[]{40, 150, 40, 150, 40, 150, 40, 150, 40});

        //Table header
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Match")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Scheibe 1 + 2")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("M.Pkte")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Scheibe 3 + 4")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("M.Pkte")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Scheibe 5 + 6")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("M.Pkte")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Scheibe 7 + 8")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("M.Pkte")));


        //Create Setzliste content on base of SETZLISTE_STRUCTURE array
        for (int i = 0; i < SETZLISTE_STRUCTURE.length; i++) {
            table.addCell(new Cell(2, 1).add(new Paragraph(Integer.toString(i + 1))));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    SETZLISTE_STRUCTURE[i][0] + " " + getTeamName(SETZLISTE_STRUCTURE[i][0], setzlisteBEList)
                            + "\n " + SETZLISTE_STRUCTURE[i][1] + " " + getTeamName(SETZLISTE_STRUCTURE[i][1],
                            setzlisteBEList))));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    SETZLISTE_STRUCTURE[i][2] + " " + getTeamName(SETZLISTE_STRUCTURE[i][2], setzlisteBEList)
                            + "\n " + SETZLISTE_STRUCTURE[i][3] + " " + getTeamName(SETZLISTE_STRUCTURE[i][3],
                            setzlisteBEList))));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    SETZLISTE_STRUCTURE[i][4] + " " + getTeamName(SETZLISTE_STRUCTURE[i][4], setzlisteBEList)
                            + "\n " + SETZLISTE_STRUCTURE[i][5] + " " + getTeamName(SETZLISTE_STRUCTURE[i][5],
                            setzlisteBEList))));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    SETZLISTE_STRUCTURE[i][6] + " " + getTeamName(SETZLISTE_STRUCTURE[i][6], setzlisteBEList)
                            + "\n " + SETZLISTE_STRUCTURE[i][7] + " " + getTeamName(SETZLISTE_STRUCTURE[i][7],
                            setzlisteBEList))));
            table.addCell(new Cell().setHeight(15));

            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
        }

        doc.add(table);
        doc.close();
    }

    /**
     * help function to get table entry
     * @param tablepos Postition in table
     * @param setzlisteBEList list with data
     * @return index if found, otherwise -1
     */
    private long getTeamIDByTablePos(final int tablepos, final List<SetzlisteBE> setzlisteBEList) {
        for (int i = 0; i < setzlisteBEList.size(); i++) {
            if (setzlisteBEList.get(i).getLigatabelleTabellenplatz() == tablepos) {
                LOGGER.debug(setzlisteBEList.get(i).getLigatabelleTabellenplatz().toString());
                LOGGER.debug(setzlisteBEList.get(i).toString());
                return setzlisteBEList.get(i).getMannschaftid();
            }
        }
        return -1;
    }

    /**
     * help function to get table entry
     * @param tabellenplatz index in table
     * @param setzlisteBEList list with data
     * @return index if found, otherwise -1
     */
    private int getTableEntry(final int tabellenplatz, final List<SetzlisteBE> setzlisteBEList) {
        for (int i = 0; i < setzlisteBEList.size(); i++) {
            if (setzlisteBEList.get(i).getLigatabelleTabellenplatz() == tabellenplatz) {
                return i;
            }
        }
        return -1;
    }

    /**
     * help funktion to get team name
     * @param tabellenplatz index in table
     * @param setzlisteBEList list with data
     * @return name of the team
     */
    private String getTeamName(final int tabellenplatz, final List<SetzlisteBE> setzlisteBEList) {
        final long teamID = getTeamIDByTablePos(tabellenplatz,setzlisteBEList);
        if (teamID == -1) {
            LOGGER.error("Cannot find Mannschaftsname.");
            return "Error";
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
