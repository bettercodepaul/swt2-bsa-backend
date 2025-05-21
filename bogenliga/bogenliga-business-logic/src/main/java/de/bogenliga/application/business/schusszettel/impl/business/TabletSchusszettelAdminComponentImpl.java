package de.bogenliga.application.business.schusszettel.impl.business;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelAdminComponent;
import de.bogenliga.application.business.schusszettel.api.types.TabletSessionInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TabletSessionSingDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;

/**
 * Admin‐side component for managing tablet‐sessions (no shooter/passe logic).
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Initialize / delete / re‐tokenize sessions</li>
 *   <li>List all sessions for a competition, including team names and next opponent names</li>
 * </ul>
 * </p>
 * @author Marty Lauterbach
 */
@Service
public class TabletSchusszettelAdminComponentImpl implements TabletSchusszettelAdminComponent {

    private static final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final TabletSchusszettelDAO sessionDAO;
    private final MatchComponent matchComponent;
    private final DsbMannschaftComponent mannschaftComponent;
    private final VereinComponent vereinComponent;

    @Autowired
    public TabletSchusszettelAdminComponentImpl(final TabletSchusszettelDAO sessionDAO,
                                                final MatchComponent matchComponent,
                                                final DsbMannschaftComponent mannschaftComponent,
                                                final VereinComponent vereinComponent) {
        this.sessionDAO        = sessionDAO;
        this.matchComponent    = matchComponent;
        this.mannschaftComponent = mannschaftComponent;
        this.vereinComponent     = vereinComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeForWettkampf(final long wettkampfId) {
        try {
            sessionDAO.deleteByWettkampfId(wettkampfId);

            final List<MatchDO> allMatches = matchComponent.findByWettkampfId(wettkampfId);
            if (allMatches.isEmpty()) {
                throw new BusinessException(
                        ErrorCode.ENTITY_NOT_FOUND_ERROR,
                        "No matches found for wettkampf " + wettkampfId);
            }

            allMatches.forEach(m -> {
                if (m.getMannschaftId() == null) {
                    throw new BusinessException(
                            ErrorCode.ENTITY_NOT_FOUND_ERROR,
                            "Invalid match data (missing team) for wettkampf " + wettkampfId);
                }
            });

            final Set<Long> teamIds = allMatches.stream()
                    .map(MatchDO::getMannschaftId)
                    .collect(Collectors.toSet());

            for (final Long teamId : teamIds) {
                final List<MatchDO> teamMatches = allMatches.stream()
                        .filter(m -> Objects.equals(m.getMannschaftId(), teamId))
                        .sorted(Comparator.comparingLong(MatchDO::getNr))
                        .toList();

                final MatchDO firstMatch = teamMatches.get(0);
                final long opponentId = findOpponentTeamId(firstMatch, teamId);

                final TabletSchusszettelEntity session = new TabletSchusszettelEntity();
                session.setWettkampfId(wettkampfId);
                session.setTeamId(teamId);
                session.setCurrentMatchId(firstMatch.getId());
                session.setCurrentMatchNumber(Math.toIntExact(firstMatch.getNr()));
                session.setCurrentPasseNumber(1);
                session.setStatus(STATUS_SCHUETZENMELDUNG);
                session.setGegnerTeamId(opponentId);
                session.setToken(generateUrlSafeToken());

                sessionDAO.createSession(session, -1L);
            }

        } catch (final BusinessException be) {
            throw be;
        } catch (final Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "initializeForWettkampf failed for wettkampf " + wettkampfId +
                            ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteForWettkampf(final long wettkampfId) {
        try {
            sessionDAO.deleteByWettkampfId(wettkampfId);
        } catch (final BusinessException be) {
            throw be;
        } catch (final Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "deleteForWettkampf failed for wettkampf " + wettkampfId +
                            ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsForWettkampf(final long wettkampfId) {
        try {
            return sessionDAO.existsByWettkampfId(wettkampfId);
        } catch (final BusinessException be) {
            throw be;
        } catch (final Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "existsForWettkampf failed for wettkampf " + wettkampfId +
                            ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reTokenize(final long wettkampfId, final long teamId) {
        try {
            final TabletSchusszettelEntity session = sessionDAO
                    .findByWettkampfUndTeam(wettkampfId, teamId)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.NO_PERMISSION_ERROR,
                            "Invalid tablet session"));

            final String newToken = generateUrlSafeToken();
            session.setToken(newToken);
            sessionDAO.setToken(wettkampfId, teamId, newToken, -1L);

        } catch (final BusinessException be) {
            throw be;
        } catch (final Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "reTokenize failed for wettkampf " + wettkampfId +
                            ", team " + teamId + ": " + e.getMessage());
        }
    }

    /**
     * Lists all tablet‐sessions for a competition, including team & opponent club names.
     */
    @Override
    public TabletSessionInfoDO generateSchusszettelSessions(final long wettkampfId) {

        // load all sessions
        final List<TabletSchusszettelEntity> entities =
                sessionDAO.findByWettkampfId(wettkampfId);

        // map into DTOs
        final TabletSessionSingDO[] singDOs = entities.stream()
                .map(e -> {
                    // lookup own team name
                    final DsbMannschaftDO team = mannschaftComponent.findById(e.getTeamId());
                    final VereinDO vTeam = vereinComponent.findById(team.getVereinId());
                    final String teamName = vTeam.getName()
                            + (team.getNummer() > 1 ? " " + team.getNummer() : "");

                    // lookup next opponent name (may stay null)
                    String opponentName = null;
                    if (e.getGegnerTeamId() != null) {
                        final DsbMannschaftDO opp = mannschaftComponent.findById(e.getGegnerTeamId());
                        final VereinDO vOpp = vereinComponent.findById(opp.getVereinId());
                        opponentName = vOpp.getName()
                                + (opp.getNummer() > 1 ? " " + opp.getNummer() : "");
                    }

                    return new TabletSessionSingDO(
                            e.getTeamId(),
                            teamName,
                            e.getStatus(),
                            e.getToken(),
                            e.getCurrentPasseNumber(),
                            opponentName
                    );
                })
                .toArray(TabletSessionSingDO[]::new);

        // assemble info
        final TabletSessionInfoDO info = new TabletSessionInfoDO();
        info.setWettkampfId(wettkampfId);
        info.setTabletSessionSingDOs(singDOs);
        return info;
    }

    /**
     * 16‐byte, URL‐safe token without padding.
     */
    private String generateUrlSafeToken() {
        final byte[] buf = new byte[16];
        SECURE_RANDOM.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }

    /**
     * Find opponent by matching round & pairing.
     */
    private long findOpponentTeamId(final MatchDO m, final long own) {
        return matchComponent.findByWettkampfId(m.getWettkampfId()).stream()
                .filter(o ->
                        Objects.equals(o.getNr(), m.getNr()) &&
                                Objects.equals(o.getBegegnung(), m.getBegegnung()) &&
                                !Objects.equals(o.getMannschaftId(), own))
                .findFirst()
                .map(MatchDO::getMannschaftId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INTERNAL_ERROR,
                        "Opponent not found for match " + m.getId()));
    }
}
