package de.bogenliga.application.business.schusszettel.impl.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSessionEntity;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;


/**
 * DAO für die schusszettel_tablet_session Tabelle.
 * Verwaltet Authentifizierung und Spielstatus pro Tablet-Team.
 *
 * Implementierung der Methoden für Persistenz:
 * - setCurrentMatchId
 * - setCurrentPasseNumber
 * - deleteByWettkampfId
 * - existsByWettkampfId
 *
 * Tabellenstruktur:
 * schusszettel_tablet_session (
 *   id BIGSERIAL PRIMARY KEY,
 *   token TEXT NOT NULL UNIQUE,
 *   team_id BIGINT NOT NULL REFERENCES mannschaft(mannschaft_id),
 *   wettkampf_id BIGINT NOT NULL REFERENCES wettkampf(wettkampf_id),
 *   current_match_id BIGINT REFERENCES match(match_id),
 *   current_passe_number INTEGER DEFAULT 1,
 *   status VARCHAR(50) NOT NULL,
 *   last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 *   gegner_team_id BIGINT REFERENCES mannschaft(mannschaft_id)
 * );
 *
 * Indexe:
 * CREATE INDEX idx_tablet_session_token ON schusszettel_tablet_session(token);
 * CREATE INDEX idx_tablet_session_lookup ON schusszettel_tablet_session(wettkampf_id, team_id);
 *
 * @author Marty Lauterbach, mklemmingen
 */
@Repository
public class TabletSessionDAO implements DataAccessObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSessionDAO.class);
    private static final String TABLE = "schusszettel_tablet_session";

    private static final String TABLE_ID = "id";
    private static final String TABLE_TOKEN = "token";
    private static final String TABLE_TEAM_ID = "team_id";
    private static final String TABLE_WETTKAMPF_ID = "wettkampf_id";
    private static final String TABLE_MATCH_ID = "current_match_id";
    private static final String TABLE_PASSE_NR = "current_passe_number";
    private static final String TABLE_STATUS = "status";
    private static final String TABLE_LAST_UPDATED = "last_updated";
    private static final String TABLE_GEGENR_TEAM_ID = "gegner_team_id";

    private static final BusinessEntityConfiguration<TabletSessionEntity> TABLET_SESSION =
            new BusinessEntityConfiguration<>(TabletSessionEntity.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private final BasicDAO basicDao;

    public TabletSessionDAO(BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    private static Map<String, String> getColumnsToFieldsMap() {
        Map<String, String> map = new HashMap<>();
        map.put(TABLE_ID, "id");
        map.put(TABLE_TOKEN, "token");
        map.put(TABLE_TEAM_ID, "teamId");
        map.put(TABLE_WETTKAMPF_ID, "wettkampfId");
        map.put(TABLE_MATCH_ID, "currentMatchId");
        map.put(TABLE_PASSE_NR, "currentPasseNumber");
        map.put(TABLE_STATUS, "status");
        map.put(TABLE_LAST_UPDATED, "lastUpdated");
        map.put(TABLE_GEGENR_TEAM_ID, "gegnerTeamId");
        map.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return map;
    }


    /**
     * Setzt die aktuelle Match-ID in der Session-Tabelle.
     */
    public void setCurrentMatchId(long wettkampfId, long teamId, long matchId) {
        String sql = new QueryBuilder()
                .update(TABLE)
                .set(TABLE_MATCH_ID)
                .setExpression(TABLE_LAST_UPDATED + " = now()")
                .whereEquals(TABLE_WETTKAMPF_ID)
                .andEquals(TABLE_TEAM_ID)
                .compose().toString();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(TABLE_MATCH_ID, matchId)
                .addValue(TABLE_WETTKAMPF_ID, wettkampfId)
                .addValue(TABLE_TEAM_ID, teamId);
        basicDao.getNamedParameterJdbcTemplate().update(sql, params);
    }


    /**
     * Setzt die aktuelle Passe-Nummer in der Session-Tabelle.
     */
    public void setCurrentPasseNumber(long wettkampfId, long teamId, Integer currentPasseNumber) {
        String sql = new QueryBuilder()
                .update(TABLE)
                .set(TABLE_PASSE_NR)
                .setExpression(TABLE_LAST_UPDATED + " = now()")
                .whereEquals(TABLE_WETTKAMPF_ID)
                .andEquals(TABLE_TEAM_ID)
                .compose().toString();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(TABLE_PASSE_NR, currentPasseNumber)
                .addValue(TABLE_WETTKAMPF_ID, wettkampfId)
                .addValue(TABLE_TEAM_ID, teamId);
        basicDao.getNamedParameterJdbcTemplate().update(sql, params);
    }

    /**
     * Liest eine Session über Token, Wettkampf und Team.
     */
    public Optional<TabletSessionEntity> findByToken(long wettkampfId, long teamId, String token) {
        String sql = new QueryBuilder()
                .selectAll()
                .from(TABLE)
                .whereEquals(TABLE_TOKEN)
                .andEquals(TABLE_WETTKAMPF_ID)
                .andEquals(TABLE_TEAM_ID)
                .compose().toString();

        return basicDao.selectEntityList(TABLET_SESSION, sql, token, wettkampfId, teamId).stream().findFirst();
    }

    /**
     * Liest eine Session über Wettkampf und Team.
     */
    public Optional<TabletSessionEntity> findByWettkampfUndTeam(Long wettkampfId, Long teamId) {
        String sql = new QueryBuilder()
                .selectAll()
                .from(TABLE)
                .whereEquals(TABLE_WETTKAMPF_ID)
                .andEquals(TABLE_TEAM_ID)
                .compose().toString();

        return basicDao.selectEntityList(TABLET_SESSION, sql, wettkampfId, teamId).stream().findFirst();
    }

    /**
     * Erstellt eine neue Tablet-Session.
     */
    public TabletSessionEntity createSession(TabletSessionEntity entity, Long currentUserId) {
        basicDao.setCreationAttributes(entity, currentUserId);
        return basicDao.insertEntity(TABLET_SESSION, entity);
    }

    /**
     * Aktualisiert den Status in der Session-Entität.
     */
    public TabletSessionEntity updateStatus(TabletSessionEntity entity, Long currentUserId) {
        basicDao.setModificationAttributes(entity, currentUserId);
        return basicDao.updateEntity(TABLET_SESSION, entity, TABLE_ID);
    }


    /**
     * Löscht alle Sessions zu einem Wettkampf.
     */
    public void deleteByWettkampfId(long wettkampfId) {
        String sql = new QueryBuilder()
                .delete()
                .from(TABLE)
                .whereEquals(TABLE_WETTKAMPF_ID)
                .compose().toString();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(TABLE_WETTKAMPF_ID, wettkampfId);
        basicDao.getNamedParameterJdbcTemplate().update(sql, params);
    }


    /**
     * Prüft, ob Sessions für einen Wettkampf existieren.
     */
    public boolean existsByWettkampfId(long wettkampfId) {
        String sql = new QueryBuilder()
                .selectCount()
                .from(TABLE)
                .whereEquals(TABLE_WETTKAMPF_ID)
                .compose().toString();

        Integer count = basicDao.getNamedParameterJdbcTemplate()
                .queryForObject(sql,
                        new MapSqlParameterSource().addValue(TABLE_WETTKAMPF_ID, wettkampfId),
                        Integer.class);
        return count != null && count > 0;
    }
}
