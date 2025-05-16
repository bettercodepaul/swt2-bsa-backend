package de.bogenliga.application.business.schusszettel.impl.dao;

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

/**
 * DAO für die schusszettel_tablet_session Tabelle.
 * Verwaltet Authentifizierung und Spielstatus pro Tablet-Team.
 * Erweiterung um current_match_number nach Projektstandard.
 *
 * Tabellenstruktur:
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
 *
 * Indexe:
 * CREATE INDEX idx_tablet_session_token ON schusszettel_tablet_session(token);
 * CREATE INDEX idx_tablet_session_lookup ON schusszettel_tablet_session(wettkampf_id, team_id);
 *
 * SQL-Konstanten und DAO-Methoden nach Projektstandard.
 *
 * @author Marty Lauterbach
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
        map.put(COL_MATCH_NUMBER, "currentMatchNumber");
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
    public Optional<TabletSchusszettelEntity> findByToken(long wettkampfId, long teamId, String token) {
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
     */
    public TabletSchusszettelEntity updateStatus(TabletSchusszettelEntity entity, Long currentUserId) {
        basicDao.setModificationAttributes(entity, currentUserId);
        return basicDao.updateEntity(TABLE_CONFIG, entity, COL_ID);
    }

    /**
     * Setzt die aktuelle Match-ID und speichert via updateEntity.
     */
    public TabletSchusszettelEntity setCurrentMatchId(TabletSchusszettelEntity entity, Long currentUserId) {
        entity.setLastUpdatedNow();
        basicDao.setModificationAttributes(entity, currentUserId);
        return basicDao.updateEntity(TABLE_CONFIG, entity, COL_ID);
    }

    /**
     * Setzt die aktuelle Match-Nummer und speichert via updateEntity.
     */
    public TabletSchusszettelEntity setCurrentMatchNumber(TabletSchusszettelEntity entity, Long currentUserId) {
        entity.setLastUpdatedNow();
        basicDao.setModificationAttributes(entity, currentUserId);
        return basicDao.updateEntity(TABLE_CONFIG, entity, COL_ID);
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
     */
    public boolean existsByWettkampfId(long wettkampfId) {
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
}
