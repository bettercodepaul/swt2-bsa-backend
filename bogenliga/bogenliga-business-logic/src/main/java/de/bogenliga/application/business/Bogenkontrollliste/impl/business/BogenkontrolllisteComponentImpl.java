package de.bogenliga.application.business.Bogenkontrollliste.impl.business;

import java.util.Hashtable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.layout.Document;
import de.bogenliga.application.business.Bogenkontrollliste.api.BogenkontrolllisteComponent;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class BogenkontrolllisteComponentImpl implements BogenkontrolllisteComponent {


    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";
    private static final String PRECONDITION_DOCUMENT = "doc cannot be null";
    private static final String PRECONDITION_TEAM_MAPPING = "TeamMemberMapping cannot be empty";
    private static final String PRECONDITION_WETTKAMPFDO = "wettkampfDO cannot be null";

    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final MatchComponent matchComponent;


    @Autowired
    public BogenkontrolllisteComponentImpl(final DsbMannschaftComponent dsbMannschaftComponent,
                                           final VereinComponent vereinComponent,
                                           final WettkampfComponent wettkampfComponent,
                                           final VeranstaltungComponent veranstaltungComponent,
                                           final MatchComponent matchComponent) {
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.matchComponent = matchComponent;
    }

    @Override
    public byte[] getBogenkontrolllistePDFasByteArray(long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        WettkampfDO wettkampfDO = wettkampfComponent.findById(wettkampfid);

        return new byte[0];
    }

    /**
     * <p>writes a Schusszettel document for the Wettkamnpf
     * </p>
     * @param doc document to write
     */
    private void generateBogenkontrolllisteDoc(Document doc, WettkampfDO wettkampfDO, Hashtable<String, List<DsbMitgliedDO>> TeamMemberMapping) {
        Preconditions.checkNotNull(doc, PRECONDITION_DOCUMENT);
        Preconditions.checkNotNull(wettkampfDO, PRECONDITION_WETTKAMPFDO);
        Preconditions.checkArgument(!TeamMemberMapping.isEmpty(), PRECONDITION_TEAM_MAPPING);
    }
}
