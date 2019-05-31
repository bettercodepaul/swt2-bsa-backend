package de.bogenliga.application.business.Schusszettel.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import de.bogenliga.application.business.Schusszettel.api.SchusszettelComponent;
import de.bogenliga.application.business.Setzliste.impl.business.SetzlisteComponentImpl;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * * Implementation of {@link SchusszettelComponent}
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 * @author Robin Müller, robin.mueller@student.reutlingen-university.de
 */
@Component
public class SchusszettelComponentImpl implements SchusszettelComponent {

    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";
    private static final Logger LOGGER = LoggerFactory.getLogger(SetzlisteComponentImpl.class);

    private final MatchComponent matchComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfComponent wettkampfComponent;


    private long WettkampfID;


    @Autowired
    public SchusszettelComponentImpl(final MatchComponent matchComponent,
                                     final DsbMannschaftComponent dsbMannschaftComponent,
                                     final VereinComponent vereinComponent,
                                     final WettkampfComponent wettkampfComponent) {
        this.matchComponent = matchComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfComponent = wettkampfComponent;
    }


    @Override
    public byte[] getAllSchusszettelPDFasByteArray(long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        this.WettkampfID = wettkampfid;
        List<MatchDO> matchDOList = matchComponent.findByWettkampfId(wettkampfid);

        byte[] bResult;
        if (matchDOList.size() != 0) {
            bResult = generateDoc(matchDOList).toByteArray();
        }else{
            throw new BusinessException(ErrorCode.UNEXPECTED_ERROR, "Matches für den Wettkampf noch nicht erzeugt");
        }
        return bResult;
    }

    /**
     * <p>writes a Schusszettel document for the Wettkamnpf
     * </p>
     */
    private ByteArrayOutputStream generateDoc(List<MatchDO> matchDOList) {
        ByteArrayOutputStream ret;
        try (final ByteArrayOutputStream result = new ByteArrayOutputStream();
             final PdfWriter writer = new PdfWriter(result);
             final PdfDocument pdfDocument = new PdfDocument(writer);
             final Document doc = new Document(pdfDocument, PageSize.A4.rotate())) {

            //iterate through matches
            for (long i = 1; i<=7; i++){
                //iterate through begegnungen
                for(long k = 1; k<=4; k++){
                    generateSchusszettelPage(doc, getMatchDOsForPage(matchDOList , i, k));
                }
            }

            ret = result;

        } catch (final IOException e) {
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                    "PDF Dokument konnte nicht erstellt werden: " + e);
        }
        return ret;

    }

    /**
     * <p>internal function to return two matches for one Schusszettel page
     * </p>
     * @param matchDOList list of matches for competition
     */
    private MatchDO[] getMatchDOsForPage(List<MatchDO> matchDOList, long matchNr, long begegnung){
        MatchDO[] ret = new MatchDO[2];
        long startScheibenNrBegegnung = (begegnung * 2) - 1 ;
        for(MatchDO match : matchDOList){
            if(match.getNr() == matchNr && match.getBegegnung() == begegnung){
                if(match.getScheibenNummer() == startScheibenNrBegegnung){
                    ret[0] = match;
                }
                if(match.getScheibenNummer() == startScheibenNrBegegnung + 1){
                    ret[1] = match;
                }
            }
        }
        return ret;
    }

    /**
     * <p>writes a Schusszettel document for the Wettkamnpf
     * </p>
     * @param doc document to write
     */
    private void generateSchusszettelPage(Document doc, MatchDO[] matchDOS) {

    }
}
