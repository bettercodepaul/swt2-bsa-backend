package de.bogenliga.application.business.lizenz.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
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
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.lizenz.api.LizenzComponent;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.business.lizenz.impl.dao.LizenzDAO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.business.lizenz.impl.mapper.LizenzMapper;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.validation.Preconditions;


@Component
public class LizenzComponentImpl implements LizenzComponent {

    private static final String PRECONDITION_LIZENZ = "LizenzDO must not be null";
    private static final String PRECONDITION_CURRENT_USER_ID = "Current User ID must not be null";
    private static final String PRECONDITION_CURRENT_USER_ID_NEGATIVE = "Current User ID must not be negativ";

    private static final String PRECONDITION_LIZENZ_LIZENZTYP = "LizenzDO_Lizenztyp must not be null";
    private static final String PRECONDITION_LIZENZ_DSBMITGLIED = "LizenzDO_DSBMITGLIED must not be null";
    private static final String PRECONDITION_LIZENZ_ID = "LizenzDO_LizenzID must not be null";

    private static final String PRECONDITION_LIZENZ_LIZENZNUMMER = "LizenzDO_lizenznummer must not be null";
    private static final String PRECONDITION_LIZENZ_DISZIPLIN = "LizenzDO_Disziplin must not be null";
    private static final String PRECONDITION_LIZENZ_REGION = "LizenzDO_Region must not be null";


    private final LizenzDAO lizenzDAO;
    private final VereinComponent vereinComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final DsbMannschaftComponent mannschaftComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final WettkampfComponent wettkampfComponent;


    public void checkCurrentUserPreconditions(final Long id) {
        Preconditions.checkNotNull(id, PRECONDITION_CURRENT_USER_ID);
        Preconditions.checkArgument(id >= 0, PRECONDITION_CURRENT_USER_ID_NEGATIVE);
    }


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param lizenzDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public LizenzComponentImpl(final LizenzDAO lizenzDAO, final VereinComponent vereinComponent,
                               final DsbMitgliedComponent dsbMitglied, final DsbMannschaftComponent mannschaftComponent,
                               final VeranstaltungComponent veranstaltungComponent, final WettkampfComponent wettkampfComponent) {
        this.lizenzDAO = lizenzDAO;
        this.vereinComponent = vereinComponent;
        this.dsbMitgliedComponent = dsbMitglied;
        this.mannschaftComponent = mannschaftComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.wettkampfComponent = wettkampfComponent;
    }


    @Override
    public List<LizenzDO> findAll() {
        final List<LizenzBE> lizenzBEList = this.lizenzDAO.findAll();
        return lizenzBEList.stream().map(LizenzMapper.toLizenzDO).collect(
                Collectors.toList());
    }


    @Override
    public List<LizenzDO> findByDsbMitgliedId(long id) {
        final List<LizenzBE> LizenzBEList = lizenzDAO.findByDsbMitgliedId(id);
        return LizenzBEList.stream().map(LizenzMapper.toLizenzDO).collect(
                Collectors.toList());

    }

    private void checkPrecondition(LizenzDO lizenzDO, long currentUserId) {
        Preconditions.checkNotNull(lizenzDO, PRECONDITION_LIZENZ);
        Preconditions.checkArgument(currentUserId >= 0, PRECONDITION_CURRENT_USER_ID);
        Preconditions.checkNotNull(lizenzDO.getLizenztyp(), PRECONDITION_LIZENZ_LIZENZTYP);
        Preconditions.checkNotNull(lizenzDO.getLizenzDsbMitgliedId(), PRECONDITION_LIZENZ_DSBMITGLIED);
        if(lizenzDO.getLizenztyp().equals("Liga")) {
            Preconditions.checkNotNull(lizenzDO.getLizenznummer(), PRECONDITION_LIZENZ_LIZENZNUMMER);
            Preconditions.checkNotNull(lizenzDO.getLizenzRegionId(), PRECONDITION_LIZENZ_REGION);
            Preconditions.checkNotNull(lizenzDO.getLizenzDisziplinId(), PRECONDITION_LIZENZ_DISZIPLIN);
        }
    }

    @Override
    public LizenzDO create(LizenzDO lizenzDO, long currentUserId) {

        this.checkCurrentUserPreconditions(currentUserId);
        this.checkPrecondition(lizenzDO, currentUserId);

        LizenzBE lizenzBE = lizenzDAO.create(LizenzMapper.toLizenzBE.apply(lizenzDO), currentUserId);
        return LizenzMapper.toLizenzDO.apply(lizenzBE);
    }


    @Override
    public LizenzDO update(LizenzDO lizenzDO, long currentUserId) {

        this.checkCurrentUserPreconditions(currentUserId);
        this.checkPrecondition(lizenzDO,currentUserId);


        LizenzBE lizenzBE = this.lizenzDAO.update(LizenzMapper.toLizenzBE.apply(lizenzDO), currentUserId);
        return LizenzMapper.toLizenzDO.apply(lizenzBE);
    }


    @Override
    public void delete(LizenzDO lizenzDO, long currentUserId) {

        Preconditions.checkNotNull(lizenzDO.getLizenzId(), PRECONDITION_LIZENZ_ID);
        this.checkCurrentUserPreconditions(currentUserId);

        LizenzBE lizenzBE = LizenzMapper.toLizenzBE.apply(lizenzDO);
        lizenzDAO.delete(lizenzBE, currentUserId);
    }


    @Override
    public byte[] getLizenzPDFasByteArray(long dsbMitgliedID, long teamID) {
        byte[] result;
        DsbMitgliedDO mitgliedDO= dsbMitgliedComponent.findById(dsbMitgliedID);
        DsbMannschaftDO mannschaftDO = mannschaftComponent.findById(teamID);
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(mannschaftDO.getVeranstaltungId());
        List<WettkampfDO> wettkampfDOList = wettkampfComponent.findAllByVeranstaltungId(veranstaltungDO.getVeranstaltungID());
        LizenzBE lizenz = lizenzDAO.findByDsbMitgliedIdAndDisziplinId(mitgliedDO.getId(), wettkampfDOList.get(0).getWettkampfDisziplinId());
        System.out.println(lizenz);
        result = generateDoc(mitgliedDO, lizenz, veranstaltungDO).toByteArray();
        return result;
    }

    private ByteArrayOutputStream generateDoc(DsbMitgliedDO mitglied, LizenzBE lizenz, VeranstaltungDO veranstaltung) {
        ByteArrayOutputStream ret;
        try (final ByteArrayOutputStream result = new ByteArrayOutputStream();
             final PdfWriter writer = new PdfWriter(result);
             final PdfDocument pdfDocument = new PdfDocument(writer);
             final Document doc = new Document(pdfDocument, PageSize.A4)) {
            generateLizenzPage(doc, lizenz, mitglied, veranstaltung);
            doc.close();
            ret = result;
        } catch (final IOException e) {
           throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "PDF Dokument konnte nicht erstellt werden: " + e);
        }
        return ret;
    }


        private void generateLizenzPage(Document doc, LizenzBE lizenz, DsbMitgliedDO mitgliedDO, VeranstaltungDO veranstaltung){


        final Table tableHead = new Table(UnitValue.createPercentArray(1), true);
        final Table secondTable = new Table(UnitValue.createPercentArray(1), true);
        final Table thirdTable = new Table(UnitValue.createPercentArray(1), true);
        final Table fourthTable = new Table(UnitValue.createPercentArray(6), true);
        final Table fifthTable = new Table(UnitValue.createPercentArray(1), true);

        DottedLine line = new DottedLine(1.5F);


        tableHead
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Lizenz").setBold().setFontSize(25.0F))
                );


        secondTable
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Lizenznummer: " + lizenz.getLizenznummer()).setBold().setFontSize(15.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Name: " + mitgliedDO.getNachname()).setBold().setFontSize(12.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Vorname: " + mitgliedDO.getVorname()).setBold().setFontSize(12.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Verein: " + vereinComponent.findById(mitgliedDO.getVereinsId()).getName()).setBold().setFontSize(12.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Liga: " + veranstaltung.getVeranstaltungName()).setBold().setFontSize(12.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Sportjahr: " + veranstaltung.getVeranstaltungSportJahr()).setBold().setFontSize(12.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER));

        thirdTable
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Wettkampftage teilgenommen:").setBold().setFontSize(15.0F))
                );

        fourthTable
                //1. Zeile
                .addCell(new Cell().setHeight(50.0F).add(new Paragraph("1").setBold().setFontSize(25.0F)).setTextAlignment(TextAlignment.CENTER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Unterschrift:").setBold().setFontSize(13.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))

                //2. Zeile
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))

                //3.Zeile
                .addCell(new Cell().setHeight(50.0F).add(new Paragraph("2").setBold().setFontSize(25.0F)).setTextAlignment(TextAlignment.CENTER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Unterschrift:").setBold().setFontSize(13.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))

                //4.Zeile
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))

                //5.Zeile
                .addCell(new Cell().setHeight(50.0F).add(new Paragraph("3").setBold().setFontSize(25.0F)).setTextAlignment(TextAlignment.CENTER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Unterschrift:").setBold().setFontSize(13.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))

                //6.Zeile
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))

                //7.Zeile
                .addCell(new Cell().setHeight(50.0F).add(new Paragraph("4").setBold().setFontSize(25.0F)).setTextAlignment(TextAlignment.CENTER))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Unterschrift:").setBold().setFontSize(13.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))

                //8.Zeile
                .addCell(new Cell().setBorder(Border.NO_BORDER).setBorderTop(new SolidBorder(Border.SOLID)))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER))
                .addCell(new Cell().setBorder(Border.NO_BORDER));

        fifthTable
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Unterschrift Schütze:").setBold().setFontSize(15.0F))
                );
        doc
            .add(new Div().setPaddings(10.0F, 10.0F, 10.0F, 10.0F).setMargins(10.5F, 0.0F, 2.5F, 0.0F)
                    .add(tableHead)
                    .add(new Div().setPaddings(10.0F, 0.0F, 10.0F, 0.0F).setMargins(10.5F, 0.0F, 10.5F, 0.0F)
                            .add(secondTable))
                    .add(thirdTable)
                    .add(new Div().setPaddings(10.0F, 0.0F, 10.0F, 0.0F).setMargins(2.5F, 0.0F, 15.5F, 0.0F)
                        .add(fourthTable)
                    )
                    .add(new Div().setPaddings(10.0F, 0.0F, 10.0F, 0.0F).setMargins(2.5F, 0.0F, 15.5F, 0.0F)
                    .add(fifthTable)))
                    .add(new LineSeparator(line));

    }
}
