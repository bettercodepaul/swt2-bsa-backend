package de.bogenliga.application.services.v1.schusszettel;

import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDTO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDTO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST-Controller für den Tablet-Schusszettel.
 *
 * Bietet GET- und POST-Endpunkte zur Statusabfrage und Eingabeübermittlung.
 *
 * @author Marty Lauterbach
 */
@RestController
@RequestMapping("/api/tablet-schusszettel")
public class TabletSchusszettelController {

    private final TabletSchusszettelComponent component;

    @Autowired
    public TabletSchusszettelController(TabletSchusszettelComponent component) {
        this.component = component;
    }

    /**
     * Holt aktuellen Status und relevante Daten für ein Team-Tablet.
     */
    @GetMapping
    public ResponseEntity<TabletSchusszettelDTO> getSchusszettel(
            @RequestParam("token") String token,
            @RequestParam("wettkampfid") Long wettkampfId,
            @RequestParam("teamid") Long teamId) {

        TabletSchusszettelDTO dto = component.getStatus(wettkampfId, teamId, token);
        return ResponseEntity.ok(dto);
    }

    /**
     * POST-Verarbeitung für Schützenmeldung oder Satz-Eingabe.
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> postEingabe(
            @RequestParam("token") String token,
            @RequestParam("wettkampfid") Long wettkampfId,
            @RequestParam("teamid") Long teamId,
            @RequestBody Map<String, Object> payload) {

        String typ = (String) payload.get("typ");
        if ("SATZEINGABE".equals(typ)) {
            SatzEingabeDTO dto = SatzEingabeDTO.fromMap(payload);
            component.submitSatz(wettkampfId, teamId, token, dto);
        } else if ("SCHUETZENMELDUNG".equals(typ)) {
            SchuetzenMeldungDTO dto = SchuetzenMeldungDTO.fromMap(payload);
            component.submitSchuetzen(wettkampfId, teamId, token, dto);
        }

        return ResponseEntity.ok(Map.of("message", "Eingabe gespeichert"));
    }
}
