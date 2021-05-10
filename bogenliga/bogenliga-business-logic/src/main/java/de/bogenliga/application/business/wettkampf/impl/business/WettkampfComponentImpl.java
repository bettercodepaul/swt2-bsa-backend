package de.bogenliga.application.business.wettkampf.impl.business;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAO;
import de.bogenliga.application.business.setzliste.impl.business.SetzlisteComponentImpl;
import de.bogenliga.application.business.setzliste.impl.entity.SetzlisteBE;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.dao.WettkampfDAO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.business.wettkampf.impl.mapper.WettkampfMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link WettkampfComponent}
 *
 * Autor: Marvin Holm, Daniel Schott
 */
@Component
public class WettkampfComponentImpl implements WettkampfComponent {

    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "wettkampfID must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID = "wettkampfVeranstaltungsID must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_DATUM = "wettkampfDatum must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_BEGINN = "wettkampfBeginn must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_TAG = "wettkampfTag must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_DISZIPLIN_ID = "wettkampfDisziplinID must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_WETTKAMPFTYP_ID = "wettkampfTypID must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_USER_ID = "CurrentUserID must not be null and must not be negative";

    private final WettkampfDAO wettkampfDAO;
    private final VeranstaltungDAO veranstaltungDAO;
    private final DsbMannschaftDAO dsbMannschaftDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(WettkampfComponentImpl.class);
    
    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param wettkampfDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public WettkampfComponentImpl(final WettkampfDAO wettkampfDAO,final VeranstaltungDAO veranstaltungDAO,final DsbMannschaftDAO dsbMannschaftDAO) {
        this.wettkampfDAO = wettkampfDAO;
        this.veranstaltungDAO = veranstaltungDAO;
        this.dsbMannschaftDAO = dsbMannschaftDAO;
    }


    @Override
    public List<WettkampfDO> findAll() {
        final List<WettkampfBE> wettkampfBEList = wettkampfDAO.findAll();
        return wettkampfBEList.stream().map(WettkampfMapper.toWettkampfDO).collect(Collectors.toList());
    }


    // Do we need this method for anything or does it purely exist because it has to implement the interfaces method?
    //TODO - hier fehlt die Implementierung, es wird explizit aus dem Match-Service aus aufgerufen.
    @Override
    public List<WettkampfDO> findByAusrichter(long id) {
        return new ArrayList<>();
    }


    @Override
    public WettkampfDO findById(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_WETTKAMPF_ID);

        final WettkampfBE result = wettkampfDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return WettkampfMapper.toWettkampfDO.apply(result);
    }


    @Override
    public List<WettkampfDO> findAllWettkaempfeByMannschaftsId(long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_WETTKAMPF_ID);

        final List<WettkampfBE> wettkampfBEList = wettkampfDAO.findAllWettkaempfeByMannschaftsId(id);
        return wettkampfBEList.stream().map(WettkampfMapper.toWettkampfDO).collect(Collectors.toList());
    }


    @Override
    public List<WettkampfDO> findAllByVeranstaltungId(long veranstaltungId) {
        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID);

        final List<WettkampfBE> wettkampfBEList = this.wettkampfDAO.findAllByVeranstaltungId(veranstaltungId);
        return wettkampfBEList.stream().map(WettkampfMapper.toWettkampfDO).collect(Collectors.toList());
    }


    @Override
    public WettkampfDO create(final WettkampfDO wettkampfDO, final long currentUserID) {
        checkParams(wettkampfDO, currentUserID);

        final WettkampfBE wettkampfBE = WettkampfMapper.toWettkampfBE.apply(wettkampfDO);

        final WettkampfBE persistedWettkampfBe = wettkampfDAO.create(wettkampfBE, currentUserID);

        return WettkampfMapper.toWettkampfDO.apply(persistedWettkampfBe);
    }


    public WettkampfDO createWT0(long veranstaltungID, final long currentUserID) {
        Preconditions.checkNotNull(veranstaltungID, PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID);
        Preconditions.checkArgument(veranstaltungID >= 0, PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID);
        Preconditions.checkArgument(currentUserID >= 0, PRECONDITION_MSG_WETTKAMPF_USER_ID);

        final WettkampfBE persistedWettkampfBe = wettkampfDAO.createWettkampftag0(veranstaltungID, currentUserID);

        return WettkampfMapper.toWettkampfDO.apply(persistedWettkampfBe);
    }


    @Override
    public WettkampfDO update(final WettkampfDO wettkampfDO, final long currentUserID) {
        checkParams(wettkampfDO, currentUserID);
        Preconditions.checkArgument(wettkampfDO.getId() >= 0, PRECONDITION_MSG_WETTKAMPF_ID);

        final WettkampfBE wettkampfBE = WettkampfMapper.toWettkampfBE.apply(wettkampfDO);

        final WettkampfBE persistedWettkampfBe = wettkampfDAO.update(wettkampfBE, currentUserID);

        return WettkampfMapper.toWettkampfDO.apply(persistedWettkampfBe);
    }


    @Override
    public void delete(final WettkampfDO wettkampfDO, final long currentUserID) {
        Preconditions.checkNotNull(wettkampfDO, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkArgument(wettkampfDO.getId() >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkArgument(currentUserID >= 0, PRECONDITION_MSG_WETTKAMPF_USER_ID);

        final WettkampfBE wettkampfBE = WettkampfMapper.toWettkampfBE.apply(wettkampfDO);

        wettkampfDAO.delete(wettkampfBE, currentUserID);
    }





    private void checkParams(final WettkampfDO wettkampfDO, final long currentUserID) {
        Preconditions.checkNotNull(wettkampfDO, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkNotNull(wettkampfDO.getWettkampfVeranstaltungsId(), PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID);
        Preconditions.checkArgument(wettkampfDO.getWettkampfVeranstaltungsId() >= 0, PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID);
        Preconditions.checkNotNull(wettkampfDO.getWettkampfDatum(), PRECONDITION_MSG_WETTKAMPF_DATUM);
        Preconditions.checkNotNull(wettkampfDO.getWettkampfBeginn(), PRECONDITION_MSG_WETTKAMPF_BEGINN);
        Preconditions.checkNotNull(wettkampfDO.getWettkampfTag(), PRECONDITION_MSG_WETTKAMPF_TAG);
        Preconditions.checkNotNull(wettkampfDO.getWettkampfDisziplinId(), PRECONDITION_MSG_WETTKAMPF_DISZIPLIN_ID);
        Preconditions.checkArgument(wettkampfDO.getWettkampfDisziplinId() >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkNotNull(wettkampfDO.getWettkampfTypId(), PRECONDITION_MSG_WETTKAMPF_WETTKAMPFTYP_ID);
        Preconditions.checkArgument(wettkampfDO.getWettkampfTypId() >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkArgument(currentUserID >= 0, PRECONDITION_MSG_WETTKAMPF_USER_ID);
    }

    @Override
    public byte[] getEinzelstatistikPDFasByteArray(long veranstaltungsid,long manschaftsid,int jahr){
        Preconditions.checkArgument(manschaftsid >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        System.out.println(veranstaltungsid);
        System.out.println(manschaftsid);
        System.out.println(jahr);
        List<WettkampfBE> wettkampflisteBEList = wettkampfDAO.findAllWettkaempfeByMannschaftsId(manschaftsid);

        byte[] bResult;
        if (!wettkampflisteBEList.isEmpty()) {
            try (ByteArrayOutputStream result = new ByteArrayOutputStream();
                 PdfWriter writer = new PdfWriter(result);
                 PdfDocument pdfDocument = new PdfDocument(writer);
                 Document doc = new Document(pdfDocument, PageSize.A4)) {

                generateDoc(doc, wettkampflisteBEList,veranstaltungsid, manschaftsid, jahr);

                bResult = result.toByteArray();
                LOGGER.debug("Einzelstatistik erstellt");
            } catch(IOException e){
                LOGGER.error("PDF Einzelstatistik konnte nicht erstellt werden: " + e);
                throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                        "PDF Einzelstatistik konnte nicht erstellt werden: " + e);
            }
        }
        else
        {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "Der Wettkampf mit der ID " + manschaftsid +" oder die Tabelleneinträge vom vorherigen Wettkampftag existieren noch nicht");
        }

        return bResult;

    }

    private void generateDoc(Document doc,List<WettkampfBE> wettkampflisteBEList,long veranstaltungsid,long manschaftsid,int jahr) {

        doc.setFontSize(9.2f);

        // description
        DateFormat sdF2 = new SimpleDateFormat("dd.MM.yyyy");
        doc.add(new Paragraph("Einzelstatistik"));
        doc.add(new Paragraph(veranstaltungDAO.findById(veranstaltungsid).getVeranstaltung_name()));
        doc.add(new Paragraph(Integer.toString(jahr)));

        doc.add(new Paragraph(""));

        doc.close();
    }

}
