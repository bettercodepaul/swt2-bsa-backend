package de.bogenliga.application.services.v1.schusszettel;

import de.bogenliga.application.services.v1.schusszettel.model.*;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST-Controller zur Bereitstellung der Tablet-Schusszettel-API.
 *
 * @author Marty Lauterbach, mklemmingen
 */
@RestController
@RequestMapping("/api/tablet-schusszettel")
public class TabletSchusszettelController {

    private final TabletSchusszettelComponent component;

    public TabletSchusszettelController(final TabletSchusszettelComponent component) {
        this.component = component;
    }

    @GetMapping
    public ResponseEntity<TabletSchusszettelDTO> getSchusszettel(
            @RequestParam long wettkampfid,
            @RequestParam long teamid,
            @RequestParam String token) {
        return ResponseEntity.ok(component.getStatus(wettkampfid, teamid, token));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> postEingabe(
            @RequestParam long wettkampfid,
            @RequestParam long teamid,
            @RequestParam String token,
            @RequestBody Object request) {

        if (request instanceof SatzEingabeDTO satz) {
            component.submitSatz(wettkampfid, teamid, token, satz);
        } else if (request instanceof SchuetzenMeldungDTO meldung) {
            component.submitSchuetzen(wettkampfid, teamid, token, meldung);
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Ungültiger Request"));
        }

        return ResponseEntity.ok(Map.of("message", "Eingabe erfolgreich gespeichert"));
    }
}