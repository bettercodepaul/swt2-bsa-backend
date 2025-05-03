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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST-Controller für den Tablet-Schusszettel.
 * Bietet GET- und POST-Endpunkte zur Statusabfrage und Eingabeübermittlung.
 *
 * @author Marty Lauterbach
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

        // TODO @Youmna
        // Exclaimer: Die component ist der zentrale Einstiegspunkt zur Business-Logik. Nutz die Mapper am besten immer sauber zur Trennung zwischen REST-Modellen (DTO) und Business-Layer (DO). Für Exception Handling, HTTP-Codes und Tests lohnt ein Blick auf andere Controller im Projekt.
        // 1. Rufe die Component-Methode getStatus(...) mit den Parametern auf.
        // 2. Mappe das zurückgegebene DO-Objekt mit TabletSchusszettelMapper.toDTO(...)
        // 3. Rückgabe: ResponseEntity.ok(dto);

        return null; // Platzhalter
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

        // TODO @Youmna
        // 1. Lese das Feld "typ" aus dem payload-Map
        // 2. Wenn typ == "SATZEINGABE":
        //    a. Konvertiere payload mit objectMapper zu SatzEingabeDTO
        //    b. Mappe zu DO mit TabletSatzEingabeMapper.toDO(...)
        //    c. Übergib an component.submitSatz(...)
        //
        // 3. Wenn typ == "SCHUETZENMELDUNG":
        //    a. Konvertiere payload zu SchuetzenMeldungDTO
        //    b. Mappe zu DO mit TabletSchuetzenMeldungMapper.toDO(...)
        //    c. Übergib an component.submitSchuetzen(...)
        //
        // 4. Falls typ unbekannt → gib BAD_REQUEST mit Fehlermeldung zurück
        //
        // 5. Erfolgsfall: gib { "message": "Eingabe gespeichert" } zurück

        return null; // Platzhalter
    }
}
