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

    public Optional<TabletSessionEntity> findByWettkampfUndTeam(Long wettkampfId, Long teamId) {
        String sql = new QueryBuilder()
                .selectAll()
                .from(TABLE)
                .whereEquals(TABLE_WETTKAMPF_ID)
                .andEquals(TABLE_TEAM_ID)
                .compose().toString();

        return basicDao.selectEntityList(TABLET_SESSION, sql, wettkampfId, teamId).stream().findFirst();
    }

    public TabletSessionEntity createSession(TabletSessionEntity entity, Long currentUserId) {
        basicDao.setCreationAttributes(entity, currentUserId);
        return basicDao.insertEntity(TABLET_SESSION, entity);
    }

    public TabletSessionEntity updateStatus(TabletSessionEntity entity, Long currentUserId) {
        basicDao.setModificationAttributes(entity, currentUserId);
        return basicDao.updateEntity(TABLET_SESSION, entity, TABLE_ID);
    }


    public void deleteByWettkampfId(long wettkampfId) {
        // An allen Stellen, an denen die WettkampfID genannt wird: delete
        // TODO
    }


    public boolean existsByWettkampfId(long wettkampfId) {
        // True, falls es einen eintrag für wettkampfId gibt
        // False, falls es keine eintrag/einträge für die wettkampfId gibt
        // TODO
    }
}
