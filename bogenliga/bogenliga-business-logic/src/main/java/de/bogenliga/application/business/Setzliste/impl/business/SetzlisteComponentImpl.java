package de.bogenliga.application.business.Setzliste.impl.business;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import de.bogenliga.application.business.Setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.Setzliste.api.types.SetzlisteDO;
import de.bogenliga.application.business.Setzliste.impl.dao.SetzlisteDAO;
import de.bogenliga.application.business.Setzliste.impl.entity.SetzlisteBE;
import de.bogenliga.application.business.Setzliste.impl.mapper.SetzlisteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SetzlisteComponent}
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
     * @param setzlisteDAO to access the database and return setzliste representations
     */
    @Autowired
    public SetzlisteComponentImpl(final SetzlisteDAO setzlisteDAO) {
        this.setzlisteDAO = setzlisteDAO;
    }


    @Override
    public String getTable() {
        LOGGER.debug("Generate Setzliste");
        final List<SetzlisteBE> setzlisteBEList = setzlisteDAO.getTable();
        String fileName = "setzliste.pdf";
        try (OutputStream result = new FileOutputStream(new File("bogenliga/bogenliga-application/src/main/resources/" + fileName));
             PdfWriter writer = new PdfWriter(result);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document doc = new Document(pdfDocument, PageSize.A4.rotate())   )
        {

            //Structure of setzliste
            int[][] structure = {
                    { 5, 4, 2, 7, 1, 8, 3, 6 },
                    { 3, 5, 8, 4, 7, 1, 6, 2 },
                    { 4, 7, 1, 6, 2, 5, 8, 3 },
                    { 8, 2, 7, 3, 6, 4, 1, 5 },
                    { 7, 6, 5, 8, 3, 2, 4, 1 },
                    { 1, 3, 4, 2, 8, 6, 5, 7 },
                    { 2, 1, 6, 5, 4, 3, 7, 8 }};


            //Beschreibung
            String date = setzlisteBEList.get(0).getWettkampfDatum().toString();
            doc.add(new Paragraph("Setzliste " + Integer.toString(setzlisteBEList.get(0).getWettkampfTag()) + ". Wettkampf " + setzlisteBEList.get(0).getVeranstaltungName()));
            doc.add(new Paragraph("am " + date + " in"));
            doc.add(new Paragraph(setzlisteBEList.get(0).getWettkampfOrt() + ", " + setzlisteBEList.get(0).getWettkampfBeginn() + " Uhr"));

            doc.add(new Paragraph(""));
            doc.add(new Paragraph(""));

            //Create table
            Table table = new Table(new float[] {40, 150, 40, 150, 40, 150, 40, 150, 40});

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


            //Create Setzliste content on base of structure array
            for (int i = 0; i < structure.length ; i++){
                table.addCell(new Cell(2, 1).add(new Paragraph(Integer.toString(i+1))));
                table.addCell(new Cell(2, 1).add(new Paragraph(
                        Integer.toString(structure[i][0]) + " " + getMannschaftsname(structure[i][0], setzlisteBEList)
                                + "\n " + Integer.toString(structure[i][1]) + " " + getMannschaftsname(structure[i][1], setzlisteBEList))));
                table.addCell(new Cell().setHeight(15));
                table.addCell(new Cell(2, 1).add(new Paragraph(
                        Integer.toString(structure[i][2]) + " " + getMannschaftsname(structure[i][2], setzlisteBEList)
                                + "\n " + Integer.toString(structure[i][3]) + " " + getMannschaftsname(structure[i][3], setzlisteBEList))));
                table.addCell(new Cell().setHeight(15));
                table.addCell(new Cell(2, 1).add(new Paragraph(
                        Integer.toString(structure[i][4]) + " " + getMannschaftsname(structure[i][4], setzlisteBEList)
                                + "\n " + Integer.toString(structure[i][5]) + " " + getMannschaftsname(structure[i][5], setzlisteBEList))));
                table.addCell(new Cell().setHeight(15));
                table.addCell(new Cell(2, 1).add(new Paragraph(
                        Integer.toString(structure[i][6]) + " " + getMannschaftsname(structure[i][6], setzlisteBEList)
                                + "\n " + Integer.toString(structure[i][7]) + " " + getMannschaftsname(structure[i][7], setzlisteBEList))));
                table.addCell(new Cell().setHeight(15));

                table.addCell(new Cell().setHeight(15));
                table.addCell(new Cell().setHeight(15));
                table.addCell(new Cell().setHeight(15));
                table.addCell(new Cell().setHeight(15));
            }

            doc.add(table);
            doc.close();
            LOGGER.debug("Setzliste erstellt");


        } catch(IOException e){
            LOGGER.error("PDF Setzliste konnte nicht erstellt werden: " + e);
        }

        return fileName;
        //return setzlisteBEList.stream().map(SetzlisteMapper.toSetzlisteDO).collect(Collectors.toList());
    }

    private int getTableEntry(int tabellenplatz, List<SetzlisteBE> setzlisteBEList){
        for (int i = 0; i < setzlisteBEList.size(); i++) {
            if(setzlisteBEList.get(i).getLigatabelleTabellenplatz()==tabellenplatz){
                return i;
            }
        }
        return -1;
    }

    private String getMannschaftsname(int tabellenplatz, List<SetzlisteBE> setzlisteBEList){
        int rowIndex = getTableEntry(tabellenplatz, setzlisteBEList);
        if(rowIndex == -1){
            return "Fehler";
        }
        else {
            if (setzlisteBEList.get(rowIndex).getMannschaftNummer() > 1) {
                return setzlisteBEList.get(rowIndex).getVereinName()+ " " + setzlisteBEList.get(rowIndex).getMannschaftNummer();
            }
            else {
                return setzlisteBEList.get(rowIndex).getVereinName();
            }
        }
    }
}
