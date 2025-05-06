package de.bogenliga.application.services.v1.schusszettel;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.services.v1.schusszettel.mapper.TabletSatzEingabeMapper;
import de.bogenliga.application.services.v1.schusszettel.mapper.TabletSchuetzenMeldungMapper;
import de.bogenliga.application.services.v1.schusszettel.mapper.TabletSchusszettelMapper;
import de.bogenliga.application.services.v1.schusszettel.model.SatzEingabeDTO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzenMeldungDTO;
import de.bogenliga.application.services.v1.schusszettel.model.TabletSchusszettelDTO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST-Controller für den Tablet-Schusszettel.
 * Bietet GET- und POST-Endpunkte zur Statusabfrage und Eingabeübermittlung.
 *
 * @author Marty Lauterbach
 * @author Youmna Samouneh
 */
@RestController
@RequestMapping("/api/tablet-schusszettel")
public class TabletSchusszettelController {

    private final TabletSchusszettelComponent component;
    private final ObjectMapper objectMapper;

    @Autowired
    public TabletSchusszettelController(TabletSchusszettelComponent component, ObjectMapper objectMapper) {
        this.component = component;
        this.objectMapper = objectMapper;
    }

    /**
     * Holt den aktuellen Zustand des Tablets (Status + Teams + Schützen + Ergebnisse).
     */
    @GetMapping
    public ResponseEntity<TabletSchusszettelDTO> getSchusszettel(
            @RequestParam("token") String token,
            @RequestParam("wettkampfid") Long wettkampfId,
            @RequestParam("teamid") Long teamId) {

        TabletSchusszettelDO tabletDO = component.getStatus(wettkampfId, teamId, token);
        TabletSchusszettelDTO dto = TabletSchusszettelMapper.toDTO(tabletDO);
        return ResponseEntity.ok(dto);
    }

    /**
     * Verarbeitet entweder eine Satz-Eingabe oder eine Schützenmeldung.
     */
    @PostMapping
    public ResponseEntity<?> postEingabe(
            @RequestParam("token") String token,
            @RequestParam("wettkampfid") Long wettkampfId,
            @RequestParam("teamid") Long teamId,
            @RequestBody Map<String, Object> payload) {

        String typ = (String) payload.get("typ");

        if ("SATZEINGABE".equals(typ)) {
            SatzEingabeDTO dto = objectMapper.convertValue(payload, SatzEingabeDTO.class);
            SatzEingabeDO satzDO = TabletSatzEingabeMapper.toDO(dto);
            component.submitSatz(wettkampfId, teamId, token, satzDO);
        } else if ("SCHUETZENMELDUNG".equals(typ)) {
            SchuetzenMeldungDTO dto = objectMapper.convertValue(payload, SchuetzenMeldungDTO.class);
            SchuetzenMeldungDO meldungDO = TabletSchuetzenMeldungMapper.toDO(dto);
            component.submitSchuetzen(wettkampfId, teamId, token, meldungDO);
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Ungültiger Typ: " + typ));
        }

        return ResponseEntity.ok(Map.of("message", "Eingabe gespeichert"));


    }
}
