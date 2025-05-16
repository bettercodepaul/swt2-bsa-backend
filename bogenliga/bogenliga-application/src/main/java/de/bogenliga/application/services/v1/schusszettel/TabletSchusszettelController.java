package de.bogenliga.application.services.v1.schusszettel;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.services.v1.schusszettel.mapper.TabletSatzEingabeMapper;
import de.bogenliga.application.services.v1.schusszettel.mapper.TabletSchuetzenMeldungMapper;
import de.bogenliga.application.services.v1.schusszettel.mapper.TabletSchusszettelMapper;
import de.bogenliga.application.services.v1.schusszettel.model.SatzEingabeDTO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzenMeldungDTO;
import de.bogenliga.application.services.v1.schusszettel.model.TabletSchusszettelDTO;
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

    private enum EingabeTyp {
        SATZEINGABE,
        SCHUETZENMELDUNG
    }

    @Autowired
    public TabletSchusszettelController(TabletSchusszettelComponent component,
                                        ObjectMapper objectMapper) {
        this.component = component;
        this.objectMapper = objectMapper;
    }

    /**
     * Holt den aktuellen Zustand des Tablets (Status + Teams + Schützen + Ergebnisse).
     */
    @GetMapping
    public ResponseEntity<?> getSchusszettel(
            @RequestParam String token,
            @RequestParam Long wettkampfid,
            @RequestParam Long teamid) {
        try {
            TabletSchusszettelDO businessDO = component.getStatus(wettkampfid, teamid, token);
            TabletSchusszettelDTO dto = TabletSchusszettelMapper.toDTO(businessDO);
            return ResponseEntity.ok(dto);

        } catch (BusinessException e) {
            // NO_PERMISSION_ERROR or other business errors
            return ResponseEntity.status(403).body(Map.of(
                    "error", e.getMessage()
            ));

        } catch (TechnicalException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Ein unerwarteter Fehler ist aufgetreten"
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> postEingabe(
            @RequestParam String token,
            @RequestParam Long wettkampfid,
            @RequestParam Long teamid,
            @RequestBody Map<String, Object> payload) {
        try {
            // 1) Validiere, ob 'typ' existiert
            Object typObj = payload.get("typ");
            if (typObj == null) {
                throw new BusinessException(
                        ErrorCode.INVALID_ARGUMENT_ERROR,
                        "Eingabetyp fehlt"
                );
            }
            String typRaw = typObj.toString();

            EingabeTyp typ;
            try {
                typ = EingabeTyp.valueOf(typRaw);
            } catch (IllegalArgumentException e) {
                // 2) Ungültiger Typ
                throw new BusinessException(
                        ErrorCode.INVALID_ARGUMENT_ERROR,
                        "Unbekannter Eingabetyp: " + typRaw
                );
            }

            // 3) Dispatch je nach Typ
            switch (typ) {
                case SATZEINGABE -> {
                    SatzEingabeDTO dto = objectMapper.convertValue(payload, SatzEingabeDTO.class);
                    SatzEingabeDO doObj = TabletSatzEingabeMapper.toDO(dto);
                    component.submitSatz(wettkampfid, teamid, token, doObj);
                }
                case SCHUETZENMELDUNG -> {
                    SchuetzenMeldungDTO dto = objectMapper.convertValue(payload, SchuetzenMeldungDTO.class);
                    SchuetzenMeldungDO doObj = TabletSchuetzenMeldungMapper.toDO(dto);
                    component.submitSchuetzen(wettkampfid, teamid, token, doObj);
                }
                default -> {
                    // Sollte nie passieren, da valueOf abgefangen wird
                    throw new BusinessException(
                            ErrorCode.INVALID_ARGUMENT_ERROR,
                            "Unbekannter Eingabetyp: " + typRaw
                    );
                }
            }

            // Erfolgreiche Speicherung
            return ResponseEntity.ok(Map.of("message", "Eingabe gespeichert"));

        } catch (BusinessException e) {
            // Validierungsfehler → 400
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));

        } catch (TechnicalException e) {
            // Interner Fehler → 500
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage()
            ));

        } catch (Exception e) {
            // Unerwarteter Fehler → 500
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Ein unerwarteter Fehler ist aufgetreten"
            ));
        }
    }
}
