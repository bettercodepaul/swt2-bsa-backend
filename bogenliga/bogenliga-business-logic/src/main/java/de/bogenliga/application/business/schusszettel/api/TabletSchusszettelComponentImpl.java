package de.bogenliga.application.business.schusszettel.api;

import de.bogenliga.application.services.v1.schusszettel.model.*;
import org.springframework.stereotype.Service;

/**
 * Implementierung der TabletSchusszettelComponent Businesslogik.
 *
 * @author Marty Lauterbach, mklemmingen
 */
@Service
public class TabletSchusszettelComponentImpl implements TabletSchusszettelComponent {

    @Override
    public TabletSchusszettelDTO getStatus(long wettkampfid, long teamid, String token) {
        // TODO: Zustand ermitteln und DTO befüllen
        return new TabletSchusszettelDTO();
    }

    @Override
    public void submitSatz(long wettkampfid, long teamid, String token, SatzEingabeDTO dto) {
        // TODO: Eingabe verarbeiten
    }

    @Override
    public void submitSchuetzen(long wettkampfid, long teamid, String token, SchuetzenMeldungDTO dto) {
        // TODO: Schützenmeldung verarbeiten
    }
}