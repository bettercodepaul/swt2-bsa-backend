package de.bogenliga.application.business.Setzliste.impl.business;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import de.bogenliga.application.business.Setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.Setzliste.api.types.SetzlisteDO;
import de.bogenliga.application.business.Setzliste.impl.dao.SetzlisteDAO;
import de.bogenliga.application.business.Setzliste.impl.entity.SetzlisteBE;
import de.bogenliga.application.business.Setzliste.impl.mapper.SetzlisteMapper;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DsbMitgliedComponent}
 */
@Component
public class SetzlisteComponentImpl implements SetzlisteComponent {

    private static final String PRECONDITION_MSG_DSBMITGLIED = "DsbMitgliedDO must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_ID = "DsbMitgliedDO ID must not be negative";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VORNAME = "DsbMitglied vorname must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_NACHNAME = "DsbMitglied nachname must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_GEBURTSDATUM = "DsbMitglied geburtsdatum must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_NATIONALITAET = "DsbMitglied nationalitaet must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_MITGLIEDSNUMMER = "DsbMitglied mitgliedsnummer must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID = "DsbMitglied vereins id must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID_NEGATIVE = "DsbMitglied vereins id must not be negative";
    private static final String PRECONDITION_MSG_CURRENT_DSBMITGLIED = "Current dsbmitglied id must not be negative";

    private final SetzlisteDAO setzlisteDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(SetzlisteComponentImpl.class);


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param setzlisteDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public SetzlisteComponentImpl(final SetzlisteDAO setzlisteDAO) {
        this.setzlisteDAO = setzlisteDAO;
    }


    @Override
    public List<SetzlisteDO> getTable() {
        LOGGER.warn("### SetzlisteCompImpl!!!! ########");
        final List<SetzlisteBE> setzlisteBEList = setzlisteDAO.getTable();

        try (OutputStream result = new FileOutputStream(new File("tableForDennis.pdf"));
             PdfWriter writer = new PdfWriter(result);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document doc = new Document(pdfDocument, PageSize.A4.rotate())   )
        {

            //Beschreibung
            String date = setzlisteBEList.get(0).getWettkampfDatum().toString();
            doc.add(new Paragraph("Setzliste " + Integer.toString(setzlisteBEList.get(0).getWettkampfTag()) + ". Wettkampf Liganame"));
            doc.add(new Paragraph("am " + date + " in"));
            doc.add(new Paragraph(setzlisteBEList.get(0).getWettkampfOrt() + ", " + setzlisteBEList.get(0).getWettkampfBeginn() + " Uhr"));

            doc.add(new Paragraph(""));
            doc.add(new Paragraph(""));

            //Tabelle
            Table table = new Table(new float[] {40, 150, 40, 150, 40, 150, 40, 150, 40});

            //Überschrift
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Match")));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Scheibe 1 + 2")));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("M.Pkte")));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Scheibe 3 + 4")));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("M.Pkte")));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Scheibe 5 + 6")));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("M.Pkte")));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Scheibe 7 + 8")));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("M.Pkte")));


            //Zeile 1
            table.addCell(new Cell(2, 1).add(new Paragraph("1")));
            table.addCell(new Cell(2, 1).add(new Paragraph("5 " + setzlisteBEList.get(getTableEntry(1,5, setzlisteBEList)).getVereinName()
                    + "\n 4 " + setzlisteBEList.get(getTableEntry(1,4, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph("2 " + setzlisteBEList.get(getTableEntry(1,2, setzlisteBEList)).getVereinName()
                    + "\n 7 " + setzlisteBEList.get(getTableEntry(1,7, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph("1 " + setzlisteBEList.get(getTableEntry(1,1, setzlisteBEList)).getVereinName()
                    + "\n 8 " + setzlisteBEList.get(getTableEntry(1,8, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph("3 " + setzlisteBEList.get(getTableEntry(1,3, setzlisteBEList)).getVereinName()
                    + "\n 6 " + setzlisteBEList.get(getTableEntry(1,6, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));

            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));

            //Zeile 2
            table.addCell(new Cell(2, 1).add(new Paragraph("2")));
            table.addCell(new Cell(2, 1).add(new Paragraph("3 " + setzlisteBEList.get(getTableEntry(1,3, setzlisteBEList)).getVereinName()
                    + "\n 5 " + setzlisteBEList.get(getTableEntry(1,5, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph("8 " + setzlisteBEList.get(getTableEntry(1,8, setzlisteBEList)).getVereinName()
                    + "\n 4 " + setzlisteBEList.get(getTableEntry(1,4, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph("7 " + setzlisteBEList.get(getTableEntry(1,7, setzlisteBEList)).getVereinName()
                    + "\n 1 " + setzlisteBEList.get(getTableEntry(1,1, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph("6 " + setzlisteBEList.get(getTableEntry(1,6, setzlisteBEList)).getVereinName()
                    + "\n 2 " + setzlisteBEList.get(getTableEntry(1,2, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));

            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));

            //Zeile 3
            table.addCell(new Cell(2, 1).add(new Paragraph("3")));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    "4 " + setzlisteBEList.get(getTableEntry(1,4, setzlisteBEList)).getVereinName()
                    + "\n 7 " + setzlisteBEList.get(getTableEntry(1,7, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    "1 " + setzlisteBEList.get(getTableEntry(1,1, setzlisteBEList)).getVereinName()
                    + "\n 6 " + setzlisteBEList.get(getTableEntry(1,6, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    "2 " + setzlisteBEList.get(getTableEntry(1,2, setzlisteBEList)).getVereinName()
                    + "\n 5 " + setzlisteBEList.get(getTableEntry(1,5, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    "8 " + setzlisteBEList.get(getTableEntry(1,8, setzlisteBEList)).getVereinName()
                    + "\n 3 " + setzlisteBEList.get(getTableEntry(1,3, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));

            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));

            //Zeile 4
            table.addCell(new Cell(2, 1).add(new Paragraph("4")));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    "8 " + setzlisteBEList.get(getTableEntry(1,8, setzlisteBEList)).getVereinName()
                    + "\n 2 " + setzlisteBEList.get(getTableEntry(1,2, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    "7 " + setzlisteBEList.get(getTableEntry(1,7, setzlisteBEList)).getVereinName()
                    + "\n 3 " + setzlisteBEList.get(getTableEntry(1,3, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    "6 " + setzlisteBEList.get(getTableEntry(1,6, setzlisteBEList)).getVereinName()
                    + "\n 4 " + setzlisteBEList.get(getTableEntry(1,4, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell(2, 1).add(new Paragraph(
                    "1 " + setzlisteBEList.get(getTableEntry(1,1, setzlisteBEList)).getVereinName()
                    + "\n 5 " + setzlisteBEList.get(getTableEntry(1,5, setzlisteBEList)).getVereinName())));
            table.addCell(new Cell().setHeight(15));

            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            table.addCell(new Cell().setHeight(15));
            //Zeile n

            doc.add(table);
            doc.close();
            LOGGER.warn("### Setzliste erstellt ######");


        } catch(IOException e){
            LOGGER.error("PDF Setzliste konnte nicht erstellt werden: " + e);
        }


/*
        String dest = "pdfText2.pdf";
        PdfWriter writer = null;
        try {
            writer = new PdfWriter(dest);
        }
        catch (FileNotFoundException e){
            LOGGER.error("PDF konnte nicht erzeugt werden. Filepath not valid: " + dest);
        }
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());
        String date = setzlisteBEList.get(0).getWettkampfDatum().toString();
        document.add(new Paragraph("Setzliste " + Integer.toString(setzlisteBEList.get(0).getWettkampfTag()) + ". Wettkampf Liganame"));
        document.add(new Paragraph("am " + date + " in"));
        document.add(new Paragraph(setzlisteBEList.get(0).getWettkampfOrt() + ", " + setzlisteBEList.get(0).getWettkampfBeginn() + " Uhr"));

        Table table = new Table(9);
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Match")));
        table.addCell("Scheibe 1 + 2");
        table.addCell("M.Pkte");
        table.addCell("Scheibe 3 + 4");
        table.addCell("M.Pkte");
        table.addCell("Scheibe 5 + 6");
        table.addCell("M.Pkte");
        table.addCell("Scheibe 7 + 8");
        table.addCell("M.Pkte");

        table.addCell("1");
        table.addCell("5 " + setzlisteBEList.get(getTableEntry(1,5, setzlisteBEList)).getVereinName()
            + "\n 4 " + setzlisteBEList.get(getTableEntry(1,4, setzlisteBEList)).getVereinName());
        Table innerTable = new Table(1);
        innerTable.addCell("     ");
        innerTable.addCell("     ");
        table.addCell(innerTable);
        //   Cell cell = new Cell(2,1);
      //  cell.add(new Cell().setBorderBottom(new SolidBorder(1f)));
      //  cell.add(new Cell());
      //  cell.add(new Paragraph("    ").setBorderBottom(new SolidBorder(1f)));
      //  cell.add(new Paragraph("    "));
      //  table.addCell(cell);


        //Alternative: Für jedes Match 2 Reihen und für alle Nicht-M.Pkte Spalten new Cell(1,2) nutzen

        document.add(table);
        document.close();
*/


        return setzlisteBEList.stream().map(SetzlisteMapper.toSetzlisteDO).collect(Collectors.toList());
    }

    private int getTableEntry(int matchnr, int tabellenplatz, List<SetzlisteBE> setzlisteBEList){
        for (int i = 0; i < setzlisteBEList.size(); i++) {
            if(setzlisteBEList.get(i).getMatchNr()==matchnr){
                if(setzlisteBEList.get(i).getLigatabelleTabellenplatz()==tabellenplatz){
                    return i;
                }
            }
        }
        return 9999;
    }



//    @Override
//    public DsbMitgliedDO findById(final long id) {
//        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);
//
//        final SetzlisteBE result = setzlisteDAO.findById(id);
//
//        if (result == null) {
//            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
//                    String.format("No result found for ID '%s'", id));
//        }
//
//        return DsbMitgliedMapper.toDsbMitgliedDO.apply(result);
//    }


//    private void checkDsbMitgliedDO(final DsbMitgliedDO dsbMitgliedDO, final long currentDsbMitgliedId) {
//        Preconditions.checkNotNull(dsbMitgliedDO, PRECONDITION_MSG_DSBMITGLIED);
//        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_CURRENT_DSBMITGLIED);
//        Preconditions.checkNotNull(dsbMitgliedDO.getVorname(), PRECONDITION_MSG_DSBMITGLIED_VORNAME);
//        Preconditions.checkNotNull(dsbMitgliedDO.getNachname(), PRECONDITION_MSG_DSBMITGLIED_NACHNAME);
//        Preconditions.checkNotNull(dsbMitgliedDO.getGeburtsdatum(), PRECONDITION_MSG_DSBMITGLIED_GEBURTSDATUM);
//        Preconditions.checkNotNull(dsbMitgliedDO.getNationalitaet(), PRECONDITION_MSG_DSBMITGLIED_NATIONALITAET);
//        Preconditions.checkNotNull(dsbMitgliedDO.getMitgliedsnummer(), PRECONDITION_MSG_DSBMITGLIED_MITGLIEDSNUMMER);
//        Preconditions.checkNotNull(dsbMitgliedDO.getVereinsId(), PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID);
//        Preconditions.checkArgument(dsbMitgliedDO.getVereinsId() >= 0, PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID_NEGATIVE);
//    }
}
