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
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImpl;
import de.bogenliga.application.business.lizenz.api.LizenzComponent;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.business.lizenz.impl.dao.LizenzDAO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.business.lizenz.impl.mapper.LizenzMapper;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.impl.mapper.MannschaftsmitgliedMapper;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.vereine.impl.business.VereinComponentImpl;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
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
    private final VereinComponentImpl vereinComponent;
    private final DsbMitgliedComponentImpl dsbMitgliedComponent;

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
    public LizenzComponentImpl(final LizenzDAO lizenzDAO, final VereinComponentImpl vereinComponent,
                               final DsbMitgliedComponentImpl dsbMitglied) {
        this.lizenzDAO = lizenzDAO;
        this.vereinComponent = vereinComponent;
        this.dsbMitgliedComponent = dsbMitglied;
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
    public byte[] getLizenzPDFasByteArray(long dsbMitgliedID) {
        byte[] result;
        DsbMitgliedDO mitgliedDO= dsbMitgliedComponent.findById(dsbMitgliedID);
        LizenzDO lizenzDO = new LizenzDO();
        result = generateDoc(mitgliedDO, lizenzDO).toByteArray();
        return result;
    }

    private ByteArrayOutputStream generateDoc(DsbMitgliedDO mitglied, LizenzDO lizenzDO) {
        ByteArrayOutputStream ret;
        try (final ByteArrayOutputStream result = new ByteArrayOutputStream();
             final PdfWriter writer = new PdfWriter(result);
             final PdfDocument pdfDocument = new PdfDocument(writer);
             final Document doc = new Document(pdfDocument, PageSize.A4)) {
            generateLizenzPage(doc, lizenzDO, mitglied);
            doc.close();
            ret = result;
        } catch (final IOException e) {
           throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "PDF Dokument konnte nicht erstellt werden: " + e);
        }
        return ret;
    }


        private void generateLizenzPage(Document doc, LizenzDO lizenzDO, DsbMitgliedDO mitgliedDO){


        final Table tableHead = new Table(UnitValue.createPercentArray(3), true);
        final Table secondTable = new Table(UnitValue.createPercentArray(1), true);
        final Table thirdTable = new Table(UnitValue.createPercentArray(1), true);
        final Table fourthTable = new Table(UnitValue.createPercentArray(1), true);
        final Table fifthTable = new Table(UnitValue.createPercentArray(1), true);


        tableHead
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Lizenz").setBold().setFontSize(15.0F))
                );


        secondTable
               // .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Lizenznummer: " + lizenzDO.getLizenznummer()).setBold().setFontSize(12.0F))
                //)
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Name: " + mitgliedDO.getNachname()).setBold().setFontSize(12.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Vorname: " + mitgliedDO.getVorname()).setBold().setFontSize(12.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Verein: " + vereinComponent.findById(mitgliedDO.getVereinsId()).getName()).setBold().setFontSize(12.0F))
                )
                .addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Liga: " + vereinComponent).setBold().setFontSize(12.0F))
                );

        thirdTable
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Wettkampftage teilgenommen:").setBold().setFontSize(13.0F))
                );

        fourthTable
                .addCell(new Cell().setHeight(25.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Unterschrift:").setBold().setFontSize(13.0F))
                )
                .addCell(new Cell().setHeight(25.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Unterschrift:").setBold().setFontSize(13.0F))
                )
                .addCell(new Cell().setHeight(25.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Unterschrift:").setBold().setFontSize(13.0F))
                )
                .addCell(new Cell().setHeight(25.0F))
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Unterschrift:").setBold().setFontSize(13.0F))
                );

        fifthTable
                .addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Unterschrift Schütze:").setBold().setFontSize(13.0F))
                );
        doc.add(tableHead);
            doc.add(secondTable);
            doc.add(thirdTable);
            doc.add(fourthTable);
            doc.add(fifthTable);
    }
}
