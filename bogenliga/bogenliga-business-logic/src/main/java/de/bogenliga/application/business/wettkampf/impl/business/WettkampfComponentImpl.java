package de.bogenliga.application.business.wettkampf.impl.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedExtendedBE;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
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
    private final MannschaftsmitgliedDAO mannschaftsmitgliedDAO;
    private final PasseComponent passeComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private static final Logger LOGGER = LoggerFactory.getLogger(WettkampfComponentImpl.class);
    
    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param wettkampfDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public WettkampfComponentImpl(final WettkampfDAO wettkampfDAO,final VeranstaltungDAO veranstaltungDAO, final MannschaftsmitgliedDAO mannschaftsmitgliedDAO
                                    ,final DsbMannschaftComponent dsbMannschaftComponent, final VereinComponent vereinComponent, final PasseComponent passeComponent) {
        this.wettkampfDAO = wettkampfDAO;
        this.veranstaltungDAO = veranstaltungDAO;
        this.mannschaftsmitgliedDAO = mannschaftsmitgliedDAO;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.passeComponent = passeComponent;
    }



    @Override
    public WettkampfDO create(final WettkampfDO wettkampfDO, final long currentUserID) {
        checkParams(wettkampfDO, currentUserID);

        final WettkampfBE wettkampfBE = WettkampfMapper.toWettkampfBE.apply(wettkampfDO);

        final WettkampfBE persistedWettkampfBe = wettkampfDAO.create(wettkampfBE, currentUserID);

        return WettkampfMapper.toWettkampfDO.apply(persistedWettkampfBe);
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


    @Override
    public List<WettkampfDO> findAll() {
        final List<WettkampfBE> wettkampfBEList = wettkampfDAO.findAll();
        return wettkampfBEList.stream().map(WettkampfMapper.toWettkampfDO).collect(Collectors.toList());
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
        List<WettkampfBE> wettkampflisteBEList = wettkampfDAO.findAllWettkaempfeByMannschaftsId(manschaftsid);

        byte[] bResult;
        if (!wettkampflisteBEList.isEmpty()) {
            try (ByteArrayOutputStream result = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(result);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document doc = new Document(pdfDocument, PageSize.A4)) {

                pdfDocument.getDocumentInfo().setTitle("Einzelstatistik.pdf");
                generateDoc(doc, wettkampflisteBEList,veranstaltungsid, manschaftsid, jahr);

                bResult = result.toByteArray();
                LOGGER.debug("Einzelstatistik erstellt");
            } catch(IOException e){
                LOGGER.error("PDF Einzelstatistik konnte nicht erstellt werden: {0}" , e);
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

    public void generateDoc(Document doc,List<WettkampfBE> wettkampflisteBEList,long veranstaltungsid,long mannschaftsid,int jahr)
    {
        VeranstaltungBE selectedVeranstaltung = veranstaltungDAO.findById(veranstaltungsid);

        doc.setFontSize(20.0f);
        doc.add(new Paragraph("Einzelstatistik").setBold());
        doc.setFontSize(9.2f);
        doc.add(new Paragraph(selectedVeranstaltung.getVeranstaltung_name()));
        doc.add(new Paragraph(getTeamName(mannschaftsid)));
        doc.add(new Paragraph(Integer.toString(jahr)));
        doc.add(new Paragraph(""));

        for(WettkampfBE wettkampf : wettkampflisteBEList)
        {
            List<MannschaftsmitgliedExtendedBE> mitglied = mannschaftsmitgliedDAO.findAllSchuetzeInTeamEingesetzt(mannschaftsid);

            Table table = new Table(new float[]{40, 150, 150, 250});
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Match").setBold()));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Mannschaft").setBold()));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Schütze").setBold()));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Dürchschnittlicher Pfeilwert pro Match").setBold()));

            for (MannschaftsmitgliedExtendedBE schuetze : mitglied)
            {
                    List<PasseDO> passen = passeComponent.findByWettkampfIdAndMitgliedId(wettkampf.getId(),schuetze.getDsbMitgliedId());
                    List<Long> passennummern = getNummern(passen);
                    for(Long nummer:passennummern)
                    {
                        if (!passen.isEmpty())
                        {
                            table.addCell(new Cell().add(new Paragraph(String.valueOf(nummer))));
                            table.addCell(new Cell().add(new Paragraph(getTeamName(mannschaftsid))));
                            table.addCell(new Cell().add(new Paragraph(schuetze.getDsbMitgliedVorname() + " " + schuetze.getDsbMitgliedNachname())));
                            table.addCell(new Cell().add(new Paragraph(String.valueOf(calcAverage(passen,nummer)))));
                        }
                    }
            }
            if(table.getNumberOfRows() > 1)
            {
                doc.setFontSize(12.0f);
                doc.add(new Paragraph("Wettkampftag " + wettkampf.getWettkampfTag()).setBold());
                doc.setFontSize(9.2f);
                doc.add(table);
                doc.add(new Paragraph(""));
            }
        }
        doc.close();
    }
    public List<Long> getNummern(List<PasseDO> passen)
    {
        List<Long> passennummern = new LinkedList<>();
        for(PasseDO passe : passen)
        {
            if(!passennummern.contains(passe.getPasseMatchNr()))
            {
                passennummern.add(passe.getPasseMatchNr());
            }
        }
        return passennummern;
    }
    public String getTeamName(long teamID) {
        Preconditions.checkArgument(teamID >= 0,"TeamID cannot be Negative");
        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(teamID);
        VereinDO vereinDO = vereinComponent.findById(dsbMannschaftDO.getVereinId());
        if (dsbMannschaftDO.getNummer() >= 1) {
            return vereinDO.getName() + " " + dsbMannschaftDO.getNummer();
        } else {
            return vereinDO.getName();
        }
    }

    public float calcAverage( List<PasseDO> passen ,long nummer)
    {
        float average = 0;
        int count = 0;

        for(PasseDO passe : passen)
        {
            if(passe.getPasseMatchNr()==nummer)
            {
                if (passe.getPfeil1() != null) {
                    average += passe.getPfeil1();
                    count++;
                }
                if (passe.getPfeil2() != null) {
                    average += passe.getPfeil2();
                    count++;
                }
                if (passe.getPfeil3() != null) {
                    average += passe.getPfeil3();
                    count++;
                }
                if (passe.getPfeil4() != null) {
                    average += passe.getPfeil4();
                    count++;
                }
                if (passe.getPfeil5() != null) {
                    average += passe.getPfeil5();
                    count++;
                }
                if (passe.getPfeil6() != null) {
                    average += passe.getPfeil6();
                    count++;
                }
            }

        }
        average = average / count;
        return average;
    }
}
