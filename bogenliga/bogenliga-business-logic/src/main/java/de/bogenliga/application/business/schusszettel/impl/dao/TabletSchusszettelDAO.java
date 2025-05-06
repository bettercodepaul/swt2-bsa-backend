package de.bogenliga.application.business.schusszettel.impl.dao;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO für den Tablet-Schusszettel. (loads/saves SatzEingaben by match/team/passe.)
 * Verwaltet Zugriff auf die Datenbanktabelle mit Satz-Eingaben.
 *
 * @author Marty Lauterbach, mklemmingen
 */
@Repository
public class TabletSchusszettelDAO implements DataAccessObject {

    // Define logger
    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSchusszettelDAO.class);

    // SQL statements
    private static final String FIND_BY_WETTKAMPF_AND_TEAM =
            "SELECT * FROM schusszettel_tablet_session " +
            "WHERE wettkampf_id = :wettkampfId AND team_id = :teamId";

    private static final String INSERT_SATZ_EINGABE =
            "INSERT INTO schusszettel_tablet_session " +
            "(token, team_id, wettkampf_id, current_match_id, current_passe_number, status, gegner_team_id) " +
            "VALUES " +
            "(:token, :teamId, :wettkampfId, :currentMatchId, :currentPasseNumber, :status, :gegnerTeamId) " +
            "ON CONFLICT (token) DO UPDATE SET " +
            "current_match_id = :currentMatchId, " +
            "current_passe_number = :currentPasseNumber, " +
            "status = :status, " +
            "last_updated = CURRENT_TIMESTAMP";

    private static final String DELETE_ALL_FOR_WETTKAMPF_TEAM =
            "DELETE FROM schusszettel_tablet_session " +
            "WHERE wettkampf_id = :wettkampfId AND team_id = :teamId";

    private static final String DELETE_BY_WETTKAMPF_ID =
            "DELETE FROM schusszettel_tablet_session " +
            "WHERE wettkampf_id = :wettkampfId";

    private static final String EXISTS_BY_WETTKAMPF_ID =
            "SELECT COUNT(*) FROM schusszettel_tablet_session " +
            "WHERE wettkampf_id = :wettkampfId";

    // DAO dependency
    private final BasicDAO basicDAO;

    // Entity configuration
    private final BusinessEntityConfiguration<TabletSchusszettelEntity> tabletSchusszettelConfig;

    /**
     * Constructor
     */
    @Autowired
    public TabletSchusszettelDAO(final BasicDAO basicDAO) {
        this.basicDAO = basicDAO;
        this.tabletSchusszettelConfig = new BusinessEntityConfiguration<>(TabletSchusszettelEntity.class);
    }

    /**
     * Find all schusszettel entries for a specific wettkampf and team
     *
     * @param wettkampfId ID of the wettkampf
     * @param teamId ID of the team
     * @return List of TabletSchusszettelEntity
     */
    public List<TabletSchusszettelEntity> findByWettkampfUndTeam(Long wettkampfId, Long teamId) {
        Map<String, Object> params = new HashMap<>();
        params.put("wettkampfId", wettkampfId);
        params.put("teamId", teamId);

        return basicDAO.selectEntityList(FIND_BY_WETTKAMPF_AND_TEAM, params, tabletSchusszettelConfig);
    }

    /**
     * Save a new entry or update an existing one in the schusszettel_tablet_session table
     *
     * @param entity the entity to save
     */
    public void saveSatzEingabe(TabletSchusszettelEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", entity.getToken());
        params.put("teamId", entity.getTeamId());
        params.put("wettkampfId", entity.getWettkampfId());
        params.put("currentMatchId", entity.getCurrentMatchId());
        params.put("currentPasseNumber", entity.getCurrentPasseNumber());
        params.put("status", entity.getStatus());
        params.put("gegnerTeamId", entity.getGegnerTeamId());

        basicDAO.insertOrUpdateEntity(INSERT_SATZ_EINGABE, params, tabletSchusszettelConfig);
    }

    /**
     * Delete all entries for a specific wettkampf and team
     *
     * @param wettkampfId ID of the wettkampf
     * @param teamId ID of the team
     */
    public void deleteAllForWettkampfTeam(Long wettkampfId, Long teamId) {
        Map<String, Object> params = new HashMap<>();
        params.put("wettkampfId", wettkampfId);
        params.put("teamId", teamId);

        basicDAO.executeUpdate(DELETE_ALL_FOR_WETTKAMPF_TEAM, params);
    }

    /**
     * Initialize necessary entries for a wettkampf
     *
     * @param wettkampfId ID of the wettkampf
     * @param teamId ID of the team
     */
    public void initializeForWettkampf(long wettkampfId, long teamId) {
        // No specific initialization beyond creating entries is needed
        // This would be implemented if we needed to pre-populate data
        LOGGER.info("Initialized schusszettel for wettkampf {} and team {}", wettkampfId, teamId);
    }

    /**
     * Delete all entries for a specific wettkampf
     *
     * @param wettkampfId ID of the wettkampf
     */
    public void deleteByWettkampfId(long wettkampfId) {
        Map<String, Object> params = new HashMap<>();
        params.put("wettkampfId", wettkampfId);

        basicDAO.executeUpdate(DELETE_BY_WETTKAMPF_ID, params);
    }

    /**
     * Check if entries exist for a specific wettkampf
     *
     * @param wettkampfId ID of the wettkampf
     * @return true if entries exist, false otherwise
     */
    public boolean existsByWettkampfId(long wettkampfId) {
        Map<String, Object> params = new HashMap<>();
        params.put("wettkampfId", wettkampfId);

        return basicDAO.selectSingleValue(EXISTS_BY_WETTKAMPF_ID, params, Long.class) > 0;
    }
}
