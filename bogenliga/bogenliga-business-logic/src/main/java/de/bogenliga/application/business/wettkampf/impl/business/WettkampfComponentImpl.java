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
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedExtendedBE;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(WettkampfComponentImpl.class);

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
  
    private final LigaComponent ligaComponent;
    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final VereinComponent vereinComponent;
    private VeranstaltungComponent veranstaltungComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    
    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param wettkampfDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public WettkampfComponentImpl(final WettkampfDAO wettkampfDAO,
                                  final LigaComponent ligaComponent,
                                  final MatchComponent matchComponent,
                                  final PasseComponent passeComponent,
                                  final MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                  final DsbMitgliedComponent dsbMitgliedComponent,
                                  final DsbMannschaftComponent dsbMannschaftComponent,
                                  final VereinComponent vereinComponent,
                                  final MannschaftsmitgliedDAO mannschaftsmitgliedDAO,
                                  final VeranstaltungDAO veranstaltungDAO) {
        this.wettkampfDAO = wettkampfDAO;
        this.ligaComponent = ligaComponent;
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.mannschaftsmitgliedDAO = mannschaftsmitgliedDAO;
        this.veranstaltungDAO = veranstaltungDAO;
    }

    @Autowired
    public void setVeranstaltungComponent(final VeranstaltungComponent veranstaltungComponent){
        this.veranstaltungComponent = veranstaltungComponent;
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


    //Erstellt die grundstruktur der pdf und ruft dann passend entweder generateEinzel zur erstellung der einzelstatistik oder generateGesammt zur erstellung der Gesammtstatistik auf
    public void generateDoc(Document doc, String header, List<WettkampfBE> wettkampflisteBEList,long veranstaltungsid,long mannschaftsid,int jahr)
    {
        Preconditions.checkArgument(header.equals("Einzelstatistik") || header.equals("Gesamtstatistik"),"Invalid Header!");
        VeranstaltungBE selectedVeranstaltung = veranstaltungDAO.findById(veranstaltungsid);

        doc.setFontSize(20.0f);
        doc.add(new Paragraph(header).setBold());
        doc.setFontSize(9.2f);
        doc.add(new Paragraph("Veranstaltung: " + selectedVeranstaltung.getVeranstaltung_name()));
        doc.add(new Paragraph("Mannschaft: " + getTeamName(mannschaftsid)));
        doc.add(new Paragraph("Jahr: " +Integer.toString(jahr)));
        doc.add(new Paragraph(""));

        if(header.equals("Einzelstatistik"))
        {
            generateEinzel(doc, wettkampflisteBEList, mannschaftsid);
        }
        else if(header.equals( "Gesamtstatistik"))
        {
            generateGesammt(doc, wettkampflisteBEList, mannschaftsid);
        }

        doc.close();
    }

    //liefert eine liste mit den matchnummern der passen die übergeben wurden
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

    //ermitelt team name anhand id
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

    @Override
    public List<Long> getAllowedMitglieder(long wettkampfid){
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_MSG_WETTKAMPF_ID);

        List<Long> allowedList=new ArrayList<>();

        for(int i=1; i <= 8; i++){
            MatchDO matchDO = matchComponent.findByWettkampfIDMatchNrScheibenNr(wettkampfid, 1L, (long) i);
            List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = mannschaftsmitgliedComponent.findAllSchuetzeInTeam(matchDO.getMannschaftId());
            List<DsbMitgliedDO> dsbMitgliedDOList = new ArrayList<>();
            List<LigaDO> ligen=ligaComponent.findAll();
            int count = 0;
            for(MannschaftsmitgliedDO mannschaftsmitglied: mannschaftsmitgliedDOList){
                DsbMitgliedDO dsbMitglied=dsbMitgliedComponent.findById(mannschaftsmitglied.getDsbMitgliedId());
                long thisLiga=this.veranstaltungComponent.findById(findById(matchDO.getWettkampfId()).getWettkampfVeranstaltungsId()).getVeranstaltungLigaID();

                //finde Stufe der aktuellen Liga
                int thisLigaStufe = 0;
                int currentLiga=(int)thisLiga;
                while(currentLiga != 0){
                    if(ligen.get(currentLiga-1).getLigaUebergeordnetId()!=null) {
                        currentLiga = ligen.get(currentLiga-1).getLigaUebergeordnetId().intValue();
                        thisLigaStufe++;
                    }else{currentLiga=0;}
                }
                long thisWettkamptag=findById(matchDO.getWettkampfId()).getWettkampfTag();
                long thisSportjahr=this.veranstaltungComponent.findById(findById(matchDO.getWettkampfId()).getWettkampfVeranstaltungsId()).getVeranstaltungSportJahr();
                boolean darfSchiessen=true;

                //find highest Liga and check if mitglied has already shot on this Wettkampftag
                List<MannschaftsmitgliedDO> mitgliedIn=this.mannschaftsmitgliedComponent.findByMemberId(dsbMitglied.getId());
                for(MannschaftsmitgliedDO mitglied: mitgliedIn){
                    List<WettkampfDO> wettkaempfe = findAllWettkaempfeByMannschaftsId(mitglied.getMannschaftId());
                    for (WettkampfDO wettkampf : wettkaempfe) {

                        //check Sportjahr of Veranstaltung
                        long wettkampfSportjahr = this.veranstaltungComponent.findById(
                                wettkampf.getWettkampfVeranstaltungsId()).getVeranstaltungSportJahr();
                        if (thisSportjahr == wettkampfSportjahr) {
                            long liga=this.veranstaltungComponent.findById(
                                    wettkampf.getWettkampfVeranstaltungsId()).getVeranstaltungLigaID();

                            //finde Stufe der Liga dieses Wettkampfes, wenn das Mannschaftsmitglied mindestens 2 mal eingesetzt wurde
                            if(mitglied.getDsbMitgliedEingesetzt()>=2) {
                                currentLiga=(int)liga;
                                int ligaStufe=0;
                                while(currentLiga != 0){
                                    if(ligen.get(currentLiga-1).getLigaUebergeordnetId()!=null) {
                                        currentLiga = ligen.get(currentLiga-1).getLigaUebergeordnetId().intValue();
                                        ligaStufe++;
                                    }else{currentLiga=0;}
                                }
                                darfSchiessen=(thisLigaStufe <= ligaStufe) && darfSchiessen;
                            }
                            List<PasseDO> passen=passeComponent.findByWettkampfIdAndMitgliedId(wettkampf.getId(),dsbMitglied.getId());
                            darfSchiessen = !(thisWettkamptag == wettkampf.getWettkampfTag() && !passen.isEmpty()) && darfSchiessen;
                        }
                    }
                }
                dsbMitgliedDOList.add(dsbMitglied);

                if(darfSchiessen){
                    allowedList.add(dsbMitglied.getId());
                    LOGGER.info("Teammitglied {} {} wurde gefunden", dsbMitgliedDOList.get(count).getNachname(), dsbMitgliedDOList.get(count).getVorname());
                }else{
                    LOGGER.info("Teammitglied {} {} konnte nicht hinzugefügt werden, da es schon in einer höheren Liga oder am selben Wettkampftag geschossen hat.", dsbMitglied.getNachname(), dsbMitglied.getVorname());
                }
                count++;
            }
        }

        return allowedList;
    }

    @Override
    public byte[] getPDFasByteArray(String name, long veranstaltungsid,long manschaftsid,int jahr){
        Preconditions.checkArgument(manschaftsid >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        List<WettkampfBE> wettkampflisteBEList = wettkampfDAO.findAllWettkaempfeByMannschaftsId(manschaftsid);

        byte[] bResult;
        if (!wettkampflisteBEList.isEmpty()) {
            try (ByteArrayOutputStream result = new ByteArrayOutputStream();
                 PdfWriter writer = new PdfWriter(result);
                 PdfDocument pdfDocument = new PdfDocument(writer);
                 Document doc = new Document(pdfDocument, PageSize.A4)) {

                pdfDocument.getDocumentInfo().setTitle(name+".pdf");
                generateDoc(doc, name , wettkampflisteBEList,veranstaltungsid, manschaftsid, jahr);

                bResult = result.toByteArray();
                LOGGER.debug("{} erstellt",name);
            } catch(IOException e){
                LOGGER.error("PDF {} konnte nicht erstellt werden: {}",name , e);
                throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                        "PDF"+ name +"konnte nicht erstellt werden: " + e);
            }
        }
        else
        {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "Der Wettkampf mit der ID " + manschaftsid +" oder die Tabelleneinträge vom vorherigen Wettkampftag existieren noch nicht");
        }
        return bResult;
    }

    //Generiert Tabelle für Einzelstatistik
    public void generateEinzel(Document doc, List<WettkampfBE> wettkampflisteBEList, long mannschaftsid)
    {
        for(WettkampfBE wettkampf : wettkampflisteBEList)
        {
            List<MannschaftsmitgliedExtendedBE> mitglied = mannschaftsmitgliedDAO.findAllSchuetzeInTeamEingesetzt(mannschaftsid);

            Table table = new Table(new float[]{100, 150, 100, 250});
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Rückennummer").setBold()));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Schütze").setBold()));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Match").setBold()));
            table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Dürchschnittlicher Pfeilwert pro Match").setBold()));

            for (MannschaftsmitgliedExtendedBE schuetze : mitglied)
            {
                List<PasseDO> passen = passeComponent.findByWettkampfIdAndMitgliedId(wettkampf.getId(),schuetze.getDsbMitgliedId());
                List<Long> passennummern = getNummern(passen);
                for(Long nummer:passennummern)
                {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(schuetze.getRueckennummer()))));
                    table.addCell(new Cell().add(new Paragraph(schuetze.getDsbMitgliedVorname() + " " + schuetze.getDsbMitgliedNachname())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(nummer))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(calcAverageEinzel(passen,nummer)))));
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
    }

    //Generiert Tabelle für Gesammtstatistik
    void generateGesammt(Document doc, List<WettkampfBE> wettkampflisteBEList, long mannschaftsid)
    {
        List<MannschaftsmitgliedExtendedBE> mitglied = mannschaftsmitgliedDAO.findAllSchuetzeInTeamEingesetzt(mannschaftsid);

        Table table = new Table(new float[]{100, 150, 250});
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Rückennummer").setBold()));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Schütze").setBold()));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Dürchschnittlicher Pfeilwert pro Match").setBold()));

        for(MannschaftsmitgliedExtendedBE schuetze : mitglied)
        {
            float average = -1;
            for(WettkampfBE wettkampf : wettkampflisteBEList)
            {
                List<PasseDO> passen = passeComponent.findByWettkampfIdAndMitgliedId(wettkampf.getId(), schuetze.getDsbMitgliedId());
                if(!passen.isEmpty())
                {
                    if(average == -1)
                        average = 0;

                    average += calcAverage(passen);
                }
            }
            if (average != -1)
            {
                        table.addCell(new Cell().add(new Paragraph(String.valueOf(schuetze.getRueckennummer()))));
                        table.addCell(new Cell().add(new Paragraph(schuetze.getDsbMitgliedVorname() + " " + schuetze.getDsbMitgliedNachname())));
                        table.addCell(new Cell().add(new Paragraph(String.valueOf(average))));
            }
        }
        if(table.getNumberOfRows() > 1)
        {
            doc.setFontSize(9.2f);
            doc.add(table);
            doc.add(new Paragraph(""));
        }
    }

    //berechnet den durchscnittlichen pfeilwert der passen die zur übergebenen matchnr gehören
    float calcAverageEinzel(List<PasseDO> passen ,long nummer)
    {
        List<PasseDO> neu = new ArrayList<>();
        for(PasseDO passe : passen)
        {
            if(passe.getPasseMatchNr() == nummer)
            {
                neu.add(passe);
            }
        }
        return calcAverage(neu);
    }
    public WettkampfDO findWT0byVeranstaltungsId(long veranstaltungsId){
        WettkampfBE wettkampfBE= wettkampfDAO.findWT0byVeranstaltungsId(veranstaltungsId);
        return  WettkampfMapper.toWettkampfDO.apply(wettkampfBE);
    }

    //berechnet den durchscnittlichen pfeilwert aller übergebenen passen
    public float calcAverage( List<PasseDO> passen)
    {
        float average = 0;
        int count = 0;

        for(PasseDO passe : passen)
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
        average = average / count;
        return average;
    }

}
