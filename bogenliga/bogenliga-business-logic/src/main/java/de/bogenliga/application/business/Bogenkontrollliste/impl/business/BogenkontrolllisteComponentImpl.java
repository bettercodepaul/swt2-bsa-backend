package de.bogenliga.application.business.Bogenkontrollliste.impl.business;

import com.itextpdf.layout.Document;
import de.bogenliga.application.business.Bogenkontrollliste.api.BogenkontrolllisteComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class BogenkontrolllisteComponentImpl implements BogenkontrolllisteComponent {
    @Override
    public byte[] getBogenkontrolllistePDFasByteArray(long wettkampfid) {
        return new byte[0];
    }

    /**
     * <p>writes a Schusszettel document for the Wettkamnpf
     * </p>
     * @param doc document to write
     */
    private void generateBogenkontrolllistePage(Document doc, MatchDO[] matchDOs) {}
}
