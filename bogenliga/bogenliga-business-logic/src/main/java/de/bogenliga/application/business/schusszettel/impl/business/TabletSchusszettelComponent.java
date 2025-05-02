package de.bogenliga.application.business.schusszettel.api;

import de.bogenliga.application.services.v1.schusszettel.model.*;

/**
 * Interface der TabletSchusszettelComponent Businesslogik.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public interface TabletSchusszettelComponent {

    TabletSchusszettelDTO getStatus(long wettkampfid, long teamid, String token);

    void submitSatz(long wettkampfid, long teamid, String token, SatzEingabeDTO dto);

    void submitSchuetzen(long wettkampfid, long teamid, String token, SchuetzenMeldungDTO dto);
}