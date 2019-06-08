package de.bogenliga.application.business.Meldezettel.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import de.bogenliga.application.business.Meldezettel.api.MeldezettelComponent;
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class MeldezettelComponentImpl implements MeldezettelComponent {

    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final DisziplinComponent disziplinComponent;

    @Autowired
    public MeldezettelComponentImpl(final DsbMannschaftComponent dsbMannschaftComponent,
                                     final VereinComponent vereinComponent,
                                     final WettkampfComponent wettkampfComponent,
                                    final VeranstaltungComponent veranstaltungComponent) {
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
    }
    @Override
    public byte[] getMeldezettelPDFasByteArray(long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        List<MatchDO> wettkampfDOList =
        List<MatchDO> vereinDOList =

        byte[] bResult;
        if (matchDOList.size() != 0) {
            bResult = generateDoc(matchDOList).toByteArray();
        }else{
            throw new BusinessException(ErrorCode.UNEXPECTED_ERROR, "Matches für den Wettkampf noch nicht erzeugt");
        }
        return bResult;
    }

    /**
     * <p>writes a Meldezettel document for the Wettkampf
     * </p>
     */
    private ByteArrayOutputStream generateDoc(List<MatchDO> matchDOList) {
        ByteArrayOutputStream ret;
        try (final ByteArrayOutputStream result = new ByteArrayOutputStream();
             final PdfWriter writer = new PdfWriter(result);
             final PdfDocument pdfDocument = new PdfDocument(writer);
             final Document doc = new Document(pdfDocument, PageSize.A4)) {

            //iterate through matches
            for (long i = 1; i<=7; i++){
                //iterate through begegnungen
                for(long k = 1; k<=4; k++){
                    MatchDO[] matchesBegegnung = getMatchDOsForPage(matchDOList , i, k);
                    if(matchesBegegnung[0] != null && matchesBegegnung[1] != null) {
                        generateSchusszettelPage(doc, matchesBegegnung);
                        if(i != 7){
                            doc.add(new AreaBreak());
                        }
                    }
                }
            }
            doc.close();
            ret = result;

        } catch (final IOException e) {
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                    "PDF Dokument konnte nicht erstellt werden: " + e);
        }
        return ret;

    }

    private void generateMeldezettelPage(Document doc, MatchDO[] matchDOs)
    {
        Long wettkampfTag = wettkampfComponent.findById(matchDOs[0].getWettkampfId()).getWettkampfTag();
        WettkampfDO wettkampfDO = wettkampfComponent.findById(matchDOs[0].getWettkampfId());
        String veranstaltungsName = veranstaltungComponent.findById(wettkampfDO.getVeranstaltungsId()).getVeranstaltungName();
        String disziplinsName = disziplinComponent.findById(wettkampfDO.getWettkampfDisziplinId()).get;
    }
}
