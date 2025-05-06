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
        final TabletSchusszettelDO businessDO = component.getStatus(wettkampfId, teamId, token);
        // *only* one mapping line here:
        final TabletSchusszettelDTO outerDTO = TabletSchusszettelMapper.toDTO(businessDO);
        return ResponseEntity.ok(outerDTO);
    }

    @PostMapping
    public ResponseEntity<?> postEingabe(
            @RequestParam("token") String token,
            @RequestParam("wettkampfid") Long wettkampfId,
            @RequestParam("teamid") Long teamId,
            @RequestBody Map<String, Object> payload) {

        try {
            String typRaw = (String) payload.get("typ");

            EingabeTyp typ;
            try {
                typ = EingabeTyp.valueOf(typRaw);
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new BusinessException(
                        ErrorCode.INVALID_ARGUMENT_ERROR,
                        "Ungültiger Eingabetyp: " + typRaw
                );
            }

            switch (typ) {
                case SATZEINGABE -> {
                    SatzEingabeDTO dto = objectMapper.convertValue(payload, SatzEingabeDTO.class);
                    SatzEingabeDO satzDO = TabletSatzEingabeMapper.toDO(dto);
                    component.submitSatz(wettkampfId, teamId, token, satzDO);
                }
                case SCHUETZENMELDUNG -> {
                    SchuetzenMeldungDTO dto = objectMapper.convertValue(payload, SchuetzenMeldungDTO.class);
                    SchuetzenMeldungDO meldungDO = TabletSchuetzenMeldungMapper.toDO(dto);
                    component.submitSchuetzen(wettkampfId, teamId, token, meldungDO);
                }
            }

            return ResponseEntity.ok(Map.of("message", "Eingabe gespeichert"));

        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getErrorCode().name(),
                    "message", e.getMessage()
            ));

        } catch (TechnicalException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getErrorCode().name(),
                    "message", e.getMessage()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", ErrorCode.UNEXPECTED_ERROR.name(),
                    "message", "Ein unerwarteter Fehler ist aufgetreten"
            ));
        }
    }
}
