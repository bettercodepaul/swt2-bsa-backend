package de.bogenliga.application.business.Bogenkontrollliste.impl.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.Bogenkontrollliste.api.BogenkontrolllisteComponent;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import com.itextpdf.layout.Document;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class BogenkontrolllisteComponentImpl implements BogenkontrolllisteComponent {


    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";

    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;

    @Autowired
    public BogenkontrolllisteComponentImpl(final DsbMannschaftComponent dsbMannschaftComponent,
                                           final VereinComponent vereinComponent,
                                           final WettkampfComponent wettkampfComponent,
                                           final VeranstaltungComponent veranstaltungComponent) {
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
    }

    @Override
    public byte[] getBogenkontrolllistePDFasByteArray(long wettkampfid) {
        return new byte[0];
    }

    /**
     * <p>writes a Schusszettel document for the Wettkamnpf
     * </p>
     * @param doc document to write
     */
    private void generateBogenkontrolllistePage(Document doc) {}
}
