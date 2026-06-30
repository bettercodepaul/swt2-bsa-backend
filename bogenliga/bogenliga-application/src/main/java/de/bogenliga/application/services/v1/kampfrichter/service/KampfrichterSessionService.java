package de.bogenliga.application.services.v1.kampfrichter.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.SchuetzeStammdatenDTO;
import de.bogenliga.application.business.kampfrichter.impl.dao.KampfrichterSessionDAO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterSessionEntity;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterMatchDTO;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterStrafpunkteRequestDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

@RestController
@RequestMapping("/v1/kampfrichter-session")
public class KampfrichterSessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KampfrichterSessionService.class);

    private final KampfrichterSessionDAO sessionDAO;
    private final MatchComponent matchComponent;
    private final DsbMannschaftComponent mannschaftComponent;
    private final TabletSchusszettelDAO tabletSessionDAO;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;

    @Autowired
    public KampfrichterSessionService(KampfrichterSessionDAO sessionDAO,
                                      MatchComponent matchComponent,
                                      DsbMannschaftComponent mannschaftComponent,
                                      TabletSchusszettelDAO tabletSessionDAO,
                                      MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                      DsbMitgliedComponent dsbMitgliedComponent) {
        this.sessionDAO = sessionDAO;
        this.matchComponent = matchComponent;
        this.mannschaftComponent = mannschaftComponent;
        this.tabletSessionDAO = tabletSessionDAO;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
    }

    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF})
    @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getOrCreateToken(@RequestParam Long wettkampfid,
                                                                final Principal principal) {
        long userId = UserProvider.getCurrentUserId(principal);
        Optional<KampfrichterSessionEntity> existing = sessionDAO.findByWettkampfId(wettkampfid);

        String token;
        if (existing.isPresent()) {
            token = existing.get().getToken();
        } else {
            KampfrichterSessionEntity entity = new KampfrichterSessionEntity();
            entity.setWettkampfId(wettkampfid);
            entity.setToken(UUID.randomUUID().toString());
            KampfrichterSessionEntity created = sessionDAO.create(entity, userId);
            token = created.getToken();
        }

        LOGGER.debug("getOrCreateToken for wettkampfid={}", wettkampfid);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF})
    @PostMapping(value = "/tokenize", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> regenerateToken(@RequestParam Long wettkampfid,
                                                               final Principal principal) {
        long userId = UserProvider.getCurrentUserId(principal);
        Optional<KampfrichterSessionEntity> existing = sessionDAO.findByWettkampfId(wettkampfid);

        String newToken = UUID.randomUUID().toString();
        if (existing.isPresent()) {
            KampfrichterSessionEntity entity = existing.get();
            entity.setToken(newToken);
            sessionDAO.update(entity, userId);
        } else {
            KampfrichterSessionEntity entity = new KampfrichterSessionEntity();
            entity.setWettkampfId(wettkampfid);
            entity.setToken(newToken);
            sessionDAO.create(entity, userId);
        }

        LOGGER.debug("regenerateToken for wettkampfid={}", wettkampfid);
        return ResponseEntity.ok(Map.of("token", newToken));
    }

    @GetMapping(value = "/matches", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<KampfrichterMatchDTO>> getMatches(@RequestParam Long wettkampfid,
                                                                 @RequestParam String token) {
        validateToken(wettkampfid, token);

        List<MatchDO> matches = matchComponent.findByWettkampfId(wettkampfid);

        // Load all tablet sessions once to enrich each match with meldung status
        Map<Long, String> teamStatusMap = tabletSessionDAO.findByWettkampfId(wettkampfid).stream()
                .filter(s -> s.getTeamId() != null && s.getStatus() != null)
                .collect(Collectors.toMap(
                        TabletSchusszettelEntity::getTeamId,
                        TabletSchusszettelEntity::getStatus,
                        (a, b) -> a
                ));

        List<KampfrichterMatchDTO> result = matches.stream()
                .map(match -> {
                    KampfrichterMatchDTO dto = new KampfrichterMatchDTO();
                    dto.setMatchId(match.getId());
                    dto.setNr(match.getNr());
                    dto.setBegegnung(match.getBegegnung());
                    dto.setMatchScheibennummer(match.getMatchScheibennummer());
                    dto.setMannschaftId(match.getMannschaftId());
                    dto.setMannschaftName(resolveMannschaftName(match.getMannschaftId()));
                    dto.setStrafPunkteSatz1(match.getStrafPunkteSatz1());
                    dto.setStrafPunkteSatz2(match.getStrafPunkteSatz2());
                    dto.setStrafPunkteSatz3(match.getStrafPunkteSatz3());
                    dto.setStrafPunkteSatz4(match.getStrafPunkteSatz4());
                    dto.setStrafPunkteSatz5(match.getStrafPunkteSatz5());
                    dto.setSessionStatus(teamStatusMap.getOrDefault(match.getMannschaftId(), "UNBEKANNT"));
                    dto.setSchuetzen(loadSchuetzen(match.getMannschaftId()));
                    return dto;
                })
                .collect(Collectors.toList());

        LOGGER.debug("getMatches for wettkampfid={}, returning {} matches", wettkampfid, result.size());
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/strafpunkte",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updateStrafpunkte(@RequestParam Long wettkampfid,
                                                                  @RequestParam String token,
                                                                  @RequestBody KampfrichterStrafpunkteRequestDTO request) {
        validateToken(wettkampfid, token);

        MatchDO match = matchComponent.findById(request.getMatchId());
        if (match == null || !wettkampfid.equals(match.getWettkampfId())) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Match nicht gefunden oder falsche Wettkampf-ID");
        }

        match.setStrafPunkteSatz1(request.getStrafPunkteSatz1());
        match.setStrafPunkteSatz2(request.getStrafPunkteSatz2());
        match.setStrafPunkteSatz3(request.getStrafPunkteSatz3());
        match.setStrafPunkteSatz4(request.getStrafPunkteSatz4());
        match.setStrafPunkteSatz5(request.getStrafPunkteSatz5());
        matchComponent.update(match, 0L);

        LOGGER.debug("updateStrafpunkte for matchId={}", request.getMatchId());
        return ResponseEntity.ok(Map.of("message", "Strafpunkte gespeichert"));
    }

    private void validateToken(Long wettkampfid, String token) {
        sessionDAO.findByWettkampfIdAndToken(wettkampfid, token)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_OFFLINE_TOKEN, "Ungültiger Token"));
    }

    private List<SchuetzeStammdatenDTO> loadSchuetzen(Long mannschaftId) {
        if (mannschaftId == null) {
            return List.of();
        }
        try {
            return mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(mannschaftId).stream()
                    .map(m -> {
                        DsbMitgliedDO mitglied = dsbMitgliedComponent.findById(m.getDsbMitgliedId());
                        SchuetzeStammdatenDTO dto = new SchuetzeStammdatenDTO();
                        dto.setSchuetzenId(mitglied.getId());
                        dto.setRueckennummer(Math.toIntExact(m.getRueckennummer()));
                        dto.setVorname(mitglied.getVorname());
                        dto.setNachname(mitglied.getNachname());
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("Could not load Schuetzen for mannschaftId={}: {}", mannschaftId, e.getMessage());
            return List.of();
        }
    }

    private String resolveMannschaftName(Long mannschaftId) {
        if (mannschaftId == null) {
            return "";
        }
        try {
            DsbMannschaftDO mannschaft = mannschaftComponent.findById(mannschaftId);
            return mannschaft != null ? mannschaft.getName() : "";
        } catch (Exception e) {
            LOGGER.warn("Could not resolve team name for mannschaftId={}", mannschaftId);
            return "";
        }
    }
}
