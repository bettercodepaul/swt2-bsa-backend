package de.bogenliga.application.business.rueckennummern.impl.business;

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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.rueckennummern.api.RueckennummernComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;

/**
 * Class to generate Rückennummer-pdf to print the labels of the teammembers for a wettkampf
 *
 * @author Jonas Winkler, jonas.winkler@student.reutlingen-university.de
 */
@Component
public class RueckennummernComponentImpl implements RueckennummernComponent {


    private static final Logger LOGGER = LoggerFactory.getLogger(RueckennummernComponentImpl.class);

    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final VereinComponent vereinComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VeranstaltungComponent veranstaltungComponent;


    @Autowired
    public RueckennummernComponentImpl(MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                       VereinComponent vereinComponent,
                                       DsbMitgliedComponent dsbMitgliedComponent,
                                       DsbMannschaftComponent dsbMannschaftComponent,
                                       VeranstaltungComponent veranstaltungComponent) {

        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.vereinComponent = vereinComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.veranstaltungComponent = veranstaltungComponent;
    }



    @Override
    public byte[] getRueckennummerPDFasByteArray(long dsbMannschaftsId, long dsbMitgliedId) {

        //Collect information
        MannschaftsmitgliedDO mannschaftsmitgliedDO = this.mannschaftsmitgliedComponent.findByMemberAndTeamId(dsbMannschaftsId, dsbMitgliedId);

        DsbMannschaftDO dsbMannschaftDO = this.dsbMannschaftComponent.findById(dsbMannschaftsId);
        VeranstaltungDO veranstaltungDO = this.veranstaltungComponent.findById(dsbMannschaftDO.getVeranstaltungId());

        DsbMitgliedDO dsbMitgliedDO = this.dsbMitgliedComponent.findById(dsbMitgliedId);
        VereinDO vereinDO = this.vereinComponent.findById(dsbMitgliedDO.getVereinsId());

        HashMap<String, List<String>> RueckennummerMapping = new HashMap<>();

        String Liganame = veranstaltungDO.getVeranstaltungName();
        String Verein = vereinDO.getName();
        String Schuetzenname = dsbMitgliedDO.getVorname() + ' ' + dsbMitgliedDO.getNachname();
        String Rueckennummer = mannschaftsmitgliedDO.getRueckennummer().toString();

        List<String> Schuetzendaten = new ArrayList<>();
        Schuetzendaten.add(Liganame);
        Schuetzendaten.add(Verein);
        Schuetzendaten.add(Schuetzenname);
        RueckennummerMapping.put(Rueckennummer,Schuetzendaten);

        try (ByteArrayOutputStream result = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(result);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document doc = new Document(pdfDocument, PageSize.A4)) {

            generateRueckennummernDoc(doc, RueckennummerMapping);

            return result.toByteArray();

        } catch (IOException e) {
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                    "Rueckennummern PDF konnte nicht erstellt werden: " + e);
        }

    }

    public byte[] getMannschaftsRueckennummernPDFasByteArray(long dsbMannschaftsId) {

        //Collect information
        DsbMannschaftDO dsbMannschaftDO = this.dsbMannschaftComponent.findById(dsbMannschaftsId);
        VeranstaltungDO veranstaltungDO = this.veranstaltungComponent.findById(dsbMannschaftDO.getVeranstaltungId());


        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOs = this.mannschaftsmitgliedComponent.findByTeamId(dsbMannschaftsId);

        HashMap<String, List<String>> RueckennummerMapping = new HashMap<>();

        String Liganame = veranstaltungDO.getVeranstaltungName();

        for(MannschaftsmitgliedDO mannschaftsmitgliedDO : mannschaftsmitgliedDOs) {
            DsbMitgliedDO dsbMitgliedDO = this.dsbMitgliedComponent.findById(mannschaftsmitgliedDO.getDsbMitgliedId());
            VereinDO vereinDO = this.vereinComponent.findById(dsbMitgliedDO.getVereinsId());

            String Verein = vereinDO.getName();
            String Schuetzenname = dsbMitgliedDO.getVorname() + ' ' + dsbMitgliedDO.getNachname();
            String Rueckennummer = mannschaftsmitgliedDO.getRueckennummer().toString();

            List<String> Schuetzendaten = new ArrayList<>();
            Schuetzendaten.add(Liganame);
            Schuetzendaten.add(Verein);
            Schuetzendaten.add(Schuetzenname);
            RueckennummerMapping.put(Rueckennummer,Schuetzendaten);
            LOGGER.info("Teammitglied {} mit Rückennummer {} gefunden",Schuetzenname,Rueckennummer);
        }

        try (ByteArrayOutputStream result = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(result);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document doc = new Document(pdfDocument, PageSize.A4)) {

            generateRueckennummernDoc(doc, RueckennummerMapping);

            return result.toByteArray();

        } catch (IOException e) {
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                    "Rueckennummern PDF konnte nicht erstellt werden: " + e);
        }
    }

    private void generateRueckennummernDoc(Document doc, HashMap<String, List<String>> RueckennummerMapping) {
        LOGGER.info("Es wurden {} Mannschaftsmitglieder gefunden", RueckennummerMapping.size());
        doc.setMargins(0,0,0,0);

        //Table for the entire document
        final Table docTable = new Table(UnitValue.createPercentArray(1), true).setBorder(Border.NO_BORDER);
        //iterate over all Mannschaftsmitglieder
        for(String rNummer : RueckennummerMapping.keySet())
        {
            String liga = RueckennummerMapping.get(rNummer).get(0);
            String verein = RueckennummerMapping.get(rNummer).get(1);
            String schuetze = RueckennummerMapping.get(rNummer).get(2);

            //create single RueckennummerDoc
            final Table singleDoc = new Table(UnitValue.createPercentArray(1), true)
                    .setBorder(Border.NO_BORDER)
                    .setMargins(50F,50F,50F,50F);
            final Table veranstaltung=new Table(UnitValue.createPercentArray(1),true)
                    .setBorder(Border.NO_BORDER);
            final Table schuetzenInfo=new Table(UnitValue.createPercentArray(1),true)
                    .setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE);

            float totalMargin=singleDoc.getMarginTop().getValue()+singleDoc.getMarginBottom().getValue();
            singleDoc.setHeight((PageSize.A4.getHeight()-totalMargin)/2);


            //fill with content of Mannschaftsmitglied
            final Cell ligaFeld = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph(liga).setFontSize(10.0F));
            final Cell vereinFeld = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)
                    .add(new Paragraph(verein).setFontSize(10.0F));
            final Cell schuetzeFeld = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                    .add(new Paragraph(schuetze).setBold().setFontSize(25.0F));
            final Cell rueckenNrFeld = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                    .add(new Paragraph(rNummer).setBold().setFontSize(100.0F));

            veranstaltung
                    .addCell(ligaFeld)
                    .addCell(vereinFeld);
            schuetzenInfo
                    .addCell(schuetzeFeld)
                    .addCell(rueckenNrFeld);
            singleDoc.addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(veranstaltung).setPaddingBottom(-50F))
                    .addCell(new Cell().setBorder(Border.NO_BORDER)
                            .add(schuetzenInfo));

            docTable.addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(singleDoc));

            //add empty 1/2 A4 page
            final Table emptySingleDoc = new Table(UnitValue.createPercentArray(1), true)
                    .setBorder(Border.NO_BORDER)
                    .setMargins(50F,50F,50F,50F);
            emptySingleDoc.setHeight((PageSize.A4.getHeight()-totalMargin)/2);
            docTable.addCell(new Cell().setBorder(Border.NO_BORDER)
                    .add(emptySingleDoc));
        }

        //Add document table to document
        doc.add(docTable);

        doc.close();
    }
}
