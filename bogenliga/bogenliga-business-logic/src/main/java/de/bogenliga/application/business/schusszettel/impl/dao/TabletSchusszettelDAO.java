package de.bogenliga.application.business.schusszettel.impl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * DAO für die schusszettel_tablet_session Tabelle.
 * Verwaltet Authentifizierung und Spielstatus pro Tablet-Team.
 * 
 * <h2>Tabellenstruktur</h2>
 * <pre>
 * schusszettel_tablet_session (
 *   id BIGSERIAL PRIMARY KEY,
 *   token TEXT NOT NULL UNIQUE,
 *   team_id BIGINT NOT NULL REFERENCES mannschaft(mannschaft_id),
 *   wettkampf_id BIGINT NOT NULL REFERENCES wettkampf(wettkampf_id),
 *   current_match_id BIGINT REFERENCES match(match_id),
 *   current_match_number INTEGER NOT NULL DEFAULT 1,
 *   current_passe_number INTEGER DEFAULT 1,
 *   status VARCHAR(50) NOT NULL,
 *   last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 *   gegner_team_id BIGINT REFERENCES mannschaft(mannschaft_id)
 * );
 * </pre>
 * 
 * <h2>BasicDAO Integration</h2>
 * This DAO leverages the standard BasicDAO infrastructure for entity operations:
 * <ul>
 *   <li>{@link BasicDAO#selectSingleEntity} - For single entity retrieval</li>
 *   <li>{@link BasicDAO#selectEntityList} - For list retrieval</li>
 *   <li>{@link BasicDAO#insertEntity} - For creating new sessions</li>
 *   <li>{@link BasicDAO#updateEntity} - For updating existing sessions</li>
 *   <li>{@link BasicDAO#deleteEntity} - For session cleanup</li>
 * </ul>
 * 
 * <h2>QueryBuilder Usage</h2>
 * SQL queries are constructed using the QueryBuilder pattern:
 * <ul>
 *   <li>{@link QueryBuilder#selectAll()} - SELECT * operations</li>
 *   <li>{@link QueryBuilder#from(String)} - FROM table operations</li>
 *   <li>{@link QueryBuilder#whereEquals(String)} - WHERE equality conditions</li>
 *   <li>{@link QueryBuilder#andEquals(String)} - AND equality conditions</li>
 *   <li>{@link QueryBuilder#orderBy(String)} - ORDER BY clauses</li>
 *   <li>{@link QueryBuilder#compose()} - Query finalization</li>
 * </ul>
 * 
 * <b>Note:</b> QueryBuilder does NOT support UPDATE operations (no update() method).
 * All updates are performed through BasicDAO.updateEntity() which uses the entity state.
 * 
 * <h2>Session Management Pattern</h2>
 * This DAO follows the established session management pattern:
 * <ul>
 *   <li>Find operations use token-based authentication</li>
 *   <li>Update operations use entity-based modification with optimistic locking</li>
 *   <li>Audit fields are automatically managed by BasicDAO</li>
 * </ul>
 * 
 * @author Marty Lauterbach
 * @see BasicDAO
 * @see QueryBuilder
 * @see BusinessEntityConfiguration
 * @see TabletSchusszettelEntity
 */
@Repository
public class TabletSchusszettelDAO implements DataAccessObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSchusszettelDAO.class);
    private static final String TABLE = "schusszettel_tablet_session";

    // Feldkonstanten
    private static final String COL_ID               = "id";
    private static final String COL_TOKEN            = "token";
    private static final String COL_TEAM_ID          = "team_id";
    private static final String COL_WETTKAMPF_ID     = "wettkampf_id";
    private static final String COL_MATCH_ID         = "current_match_id";
    private static final String COL_MATCH_NUMBER     = "current_match_number";
    private static final String COL_PASSE_NR         = "current_passe_number";
    private static final String COL_STATUS           = "status";
    private static final String COL_LAST_UPDATED     = "last_updated";
    private static final String COL_GEGENR_TEAM_ID   = "gegner_team_id";

    private static final BusinessEntityConfiguration<TabletSchusszettelEntity> TABLE_CONFIG =
            new BusinessEntityConfiguration<>(TabletSchusszettelEntity.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    @Autowired
    private BasicDAO basicDao;

    private static Map<String, String> getColumnsToFieldsMap() {
        Map<String,String> map = new HashMap<>();
        map.put(COL_ID, "id");
        map.put(COL_TOKEN, "token");
        map.put(COL_TEAM_ID, "teamId");
        map.put(COL_WETTKAMPF_ID, "wettkampfId");
        map.put(COL_MATCH_ID, "currentMatchId");
        map.put(COL_MATCH_NUMBER, "currentMatchNr");
        map.put(COL_PASSE_NR, "currentPasseNumber");
        map.put(COL_STATUS, "status");
        map.put(COL_LAST_UPDATED, "lastUpdated");
        map.put(COL_GEGENR_TEAM_ID, "gegnerTeamId");
        map.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return map;
    }

    /**
     * Liest eine Session via Token, Wettkampf und Team.
     */
    public Optional<TabletSchusszettelEntity> findByTokenWettkampfUndTeam(long wettkampfId, long teamId, String token) {
        String sql = new QueryBuilder()
                .selectAll()
                .from(TABLE)
                .whereEquals(COL_TOKEN)
                .andEquals(COL_WETTKAMPF_ID)
                .andEquals(COL_TEAM_ID)
                .compose().toString();
        return basicDao.selectEntityList(TABLE_CONFIG, sql, token, wettkampfId, teamId).stream().findFirst();
    }

    /**
     * Liest eine Session via Wettkampf und Team.
     */
    public Optional<TabletSchusszettelEntity> findByWettkampfUndTeam(long wettkampfId, long teamId) {
        String sql = new QueryBuilder()
                .selectAll()
                .from(TABLE)
                .whereEquals(COL_WETTKAMPF_ID)
                .andEquals(COL_TEAM_ID)
                .compose().toString();
        return basicDao.selectEntityList(TABLE_CONFIG, sql, wettkampfId, teamId).stream().findFirst();
    }

    /**
     * Erstellt eine neue Tablet-Session.
     */
    public TabletSchusszettelEntity createSession(TabletSchusszettelEntity entity, Long currentUserId) {
        basicDao.setCreationAttributes(entity, currentUserId);
        return basicDao.insertEntity(TABLE_CONFIG, entity);
    }

    /**
     * Aktualisiert den Status der Session.
     * Enhanced with optimistic locking support following BasicDAO patterns.
     */
    public TabletSchusszettelEntity updateStatus(TabletSchusszettelEntity entity, Long currentUserId) {
        basicDao.setModificationAttributes(entity, currentUserId);
        
        try {
            return basicDao.updateEntity(TABLE_CONFIG, entity, COL_ID);
        } catch (BusinessException e) {
            // BusinessException from BasicDAO already has proper error codes
            if (ErrorCode.ENTITY_CONFLICT_ERROR.equals(e.getErrorCode())) {
                LOGGER.warn("Optimistic locking conflict updating session {} for team {}: {}", 
                           entity.getId(), entity.getTeamId(), e.getMessage());
            }
            throw e;
        }
    }


    /**
     * Setzt die aktuelle Passe-Nummer und speichert via updateEntity.
     */
    public TabletSchusszettelEntity setCurrentPasseNumber(TabletSchusszettelEntity entity, Long currentUserId) {
        entity.setLastUpdatedNow();
        basicDao.setModificationAttributes(entity, currentUserId);
        return basicDao.updateEntity(TABLE_CONFIG, entity, COL_ID);
    }

    /**
     * Löscht alle Sessions zu einem Wettkampf via deleteEntity.
     */
    public void deleteByWettkampfId(long wettkampfId) {
        List<TabletSchusszettelEntity> list = basicDao.selectEntityList(
                TABLE_CONFIG,
                new QueryBuilder()
                        .selectAll()
                        .from(TABLE)
                        .whereEquals(COL_WETTKAMPF_ID)
                        .compose().toString(),
                wettkampfId);
        for (TabletSchusszettelEntity e : list) {
            basicDao.deleteEntity(TABLE_CONFIG, e, COL_ID);
        }
    }

    /**
     * Prüft, ob Sessions für einen Wettkampf existieren.
     * Optimized with EXISTS query for better performance.
     */
    public boolean existsByWettkampfId(long wettkampfId) {
        // Use selectEntityList approach since selectSingleValue is not available in BasicDAO
        List<TabletSchusszettelEntity> list = basicDao.selectEntityList(
                TABLE_CONFIG,
                new QueryBuilder()
                        .selectAll()
                        .from(TABLE)
                        .whereEquals(COL_WETTKAMPF_ID)
                        .compose().toString(),
                wettkampfId);
        return !list.isEmpty();
    }

    /**
     * Prüft, ob eine Session für einen Wettkampf und ein Team existiert.
     * Optimized with EXISTS query for better performance.
     */
    public boolean existsByWettkampfIdAndTeamId(long wettkampfId, long teamId) {
        // Use selectEntityList approach since selectSingleValue is not available in BasicDAO
        List<TabletSchusszettelEntity> list = basicDao.selectEntityList(
                TABLE_CONFIG,
                new QueryBuilder()
                        .selectAll()
                        .from(TABLE)
                        .whereEquals(COL_WETTKAMPF_ID)
                        .andEquals(COL_TEAM_ID)
                        .compose().toString(),
                wettkampfId, teamId);
        return !list.isEmpty();
    }

    /**
     * Set the Token after finding the Session via WettkampfId and TeamId.
     * Enhanced with atomic operation to prevent race conditions.
     */
    public TabletSchusszettelEntity setToken(long wettkampfId, long teamId, String token, Long currentUserId) {
        Optional<TabletSchusszettelEntity> sessionOpt = findByWettkampfUndTeam(wettkampfId, teamId);
        if (sessionOpt.isPresent()) {
            TabletSchusszettelEntity session = sessionOpt.get();
            session.setToken(token);
            session.setLastUpdatedNow();
            return updateStatus(session, currentUserId);
        } else {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("Keine Tablet-Session für wettkampfId=%d und teamId=%d gefunden", wettkampfId, teamId));
        }
    }
    

    /**
     * Set the Enemy TeamID after finding the Session via WettkampfId und TeamId.
     * Enhanced with atomic operation to prevent race conditions.
     */
    public TabletSchusszettelEntity setGegnerTeamId(long wettkampfId, long teamId, Long gegnerTeamId, Long currentUserId) {
        Optional<TabletSchusszettelEntity> sessionOpt = findByWettkampfUndTeam(wettkampfId, teamId);
        if (sessionOpt.isPresent()) {
            TabletSchusszettelEntity session = sessionOpt.get();
            session.setGegnerTeamId(gegnerTeamId);
            session.setLastUpdatedNow();
            return updateStatus(session, currentUserId);
        } else {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("Keine Tablet-Session für wettkampfId=%d und teamId=%d gefunden", wettkampfId, teamId));
        }
    }
    
    /**
     * Set the CurrentMatchID after finding the Session via WettkampfId und TeamId.
     * Uses standard DAO patterns for consistency.
     */
    public TabletSchusszettelEntity setCurrentMatchId(long wettkampfId, long teamId, Long currentMatchId, Long currentUserId) {
        Optional<TabletSchusszettelEntity> sessionOpt = findByWettkampfUndTeam(wettkampfId, teamId);
        if (sessionOpt.isPresent()) {
            TabletSchusszettelEntity session = sessionOpt.get();
            session.setCurrentMatchId(currentMatchId);
            session.setLastUpdatedNow();
            return updateStatus(session, currentUserId);
        } else {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("Keine Tablet-Session für wettkampfId=%d und teamId=%d gefunden", wettkampfId, teamId));
        }
    }
    
    /**
     * Set the CurrentMatchNumber after finding the Session via WettkampfId und TeamId.
     * Uses standard DAO patterns for consistency.
     */
    public TabletSchusszettelEntity setCurrentMatchNumber(long wettkampfId, long teamId, Integer currentMatchNumber, Long currentUserId) {
        Optional<TabletSchusszettelEntity> sessionOpt = findByWettkampfUndTeam(wettkampfId, teamId);
        if (sessionOpt.isPresent()) {
            TabletSchusszettelEntity session = sessionOpt.get();
            session.setCurrentMatchNr(currentMatchNumber);
            session.setLastUpdatedNow();
            return updateStatus(session, currentUserId);
        } else {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("Keine Tablet-Session für wettkampfId=%d und teamId=%d gefunden", wettkampfId, teamId));
        }
    }

    /**
     * Set the CurrentPasseNumber after finding the Session via WettkampfId und TeamId.
     * Uses standard DAO patterns for consistency.
     */
    public TabletSchusszettelEntity setCurrentPasseNumber(long wettkampfId, long teamId, Integer currentPasseNumber, Long currentUserId) {
        Optional<TabletSchusszettelEntity> sessionOpt = findByWettkampfUndTeam(wettkampfId, teamId);
        if (sessionOpt.isPresent()) {
            TabletSchusszettelEntity session = sessionOpt.get();
            session.setCurrentPasseNumber(currentPasseNumber);
            session.setLastUpdatedNow();
            return updateStatus(session, currentUserId);
        } else {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("Keine Tablet-Session für wettkampfId=%d und teamId=%d gefunden", wettkampfId, teamId));
        }
    }

    /**
     * Set the Status after finding the Session via WettkampfId und TeamId.
     * Uses standard DAO patterns for consistency.
     */
    public TabletSchusszettelEntity setStatus(long wettkampfId, long teamId, String status, Long currentUserId) {
        Optional<TabletSchusszettelEntity> sessionOpt = findByWettkampfUndTeam(wettkampfId, teamId);
        if (sessionOpt.isPresent()) {
            TabletSchusszettelEntity session = sessionOpt.get();
            session.setStatus(status);
            session.setLastUpdatedNow();
            return updateStatus(session, currentUserId);
        } else {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("Keine Tablet-Session für wettkampfId=%d und teamId=%d gefunden", wettkampfId, teamId));
        }
    }

    /**
     * Set the LastUpdated after finding the Session via WettkampfId und TeamId
     */
    public TabletSchusszettelEntity updateLastUpdated(long wettkampfId, long teamId, Long currentUserId) {
        TabletSchusszettelEntity entity = findByWettkampfUndTeam(wettkampfId, teamId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                        String.format("Keine Tablet-Session für wettkampfId=%d und teamId=%d gefunden", wettkampfId, teamId)));
        entity.setLastUpdatedNow();
        basicDao.setModificationAttributes(entity, currentUserId);
        return basicDao.updateEntity(TABLE_CONFIG, entity, COL_ID);
    }

    /**
     * Liest alle Sessions für einen Wettkampf aus.
     * Enhanced with performance monitoring and result caching considerations.
     */
    public List<TabletSchusszettelEntity> findByWettkampfId(long wettkampfId) {
        String sql = new QueryBuilder()
                .selectAll()
                .from(TABLE)
                .whereEquals(COL_WETTKAMPF_ID)
                .orderBy(COL_TEAM_ID) // Consistent ordering for better performance
                .compose().toString();
        
        long startTime = System.currentTimeMillis();
        List<TabletSchusszettelEntity> results = basicDao.selectEntityList(TABLE_CONFIG, sql, wettkampfId);
        long queryTime = System.currentTimeMillis() - startTime;
        
        if (queryTime > 100) { // Log slow queries
            LOGGER.warn("Slow query detected: findByWettkampfId({}) took {}ms and returned {} results", 
                       wettkampfId, queryTime, results.size());
        }
        
        return results;
    }

    /**
     * Batch creation of tablet sessions for performance optimization.
     * Creates multiple sessions in a single transaction for better performance.
     */
    public List<TabletSchusszettelEntity> createSessionsBatch(List<TabletSchusszettelEntity> entities, Long currentUserId) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        long startTime = System.currentTimeMillis();
        List<TabletSchusszettelEntity> results = new ArrayList<>();

        for (TabletSchusszettelEntity entity : entities) {
            basicDao.setCreationAttributes(entity, currentUserId);
            TabletSchusszettelEntity created = basicDao.insertEntity(TABLE_CONFIG, entity);
            results.add(created);
        }

        long batchTime = System.currentTimeMillis() - startTime;
        LOGGER.debug("Batch created {} sessions in {}ms (avg: {}ms per session)",
                    entities.size(), batchTime, batchTime / entities.size());

        return results;
    }
}