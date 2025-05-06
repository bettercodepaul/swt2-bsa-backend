package de.bogenliga.application.business.schusszettel.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import de.bogenliga.application.business.schusszettel.impl.entity.MatchShooterEntity;
import de.bogenliga.application.business.schusszettel.impl.business.TabletSchusszettelComponentImpl.MatchShooter;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * DAO for the passe table – manages shooter assignments per match.
 *
 * @author Marty Lauterbach, mklemmingen
 */
@Repository
public class MatchShooterDAO implements DataAccessObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchShooterDAO.class);

    private static final String TABLE = "passe";
    private static final String COL_MATCH_ID    = "passe_match_id";
    private static final String COL_SHOOTER_ID  = "passe_dsb_mitglied_id";
    private static final String COL_LFDNR       = "passe_lfdnr";

    private static final BusinessEntityConfiguration<MatchShooterEntity> PASE_CFG =
            new BusinessEntityConfiguration<>(MatchShooterEntity.class,
                    TABLE,
                    getColumnsToFieldsMap(),
                    LOGGER);

    private final BasicDAO basicDao;

    public MatchShooterDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> map = new HashMap<>();
        map.put(COL_MATCH_ID,   "matchId");
        map.put(COL_SHOOTER_ID, "dsbMitgliedId");
        map.put(COL_LFDNR,      "lfdnr");
        map.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return map;
    }

    /**
     * Validates that the given member can be added as a shooter,
     * but does NOT actually insert anything.
     */
    public void assignToMatch(final long matchId, final long dsbMitgliedId) {
        // Check they’re on the right team
        final String checkTeamSql = new QueryBuilder()
                .select("COUNT(*)")
                .from("mannschaftsmitglied")
                // subselect to find the team for this match
                .where("mannschaftsmitglied_mannschaft_id = ( "
                        + "SELECT match_mannschaft_id FROM match WHERE match_id = :matchId)")
                .andEquals("mannschaftsmitglied_dsb_mitglied_id")
                .compose().toString();

        final Map<String, Object> params = new HashMap<>();
        params.put("matchId",      matchId);
        params.put("dsbMitgliedId", dsbMitgliedId);

        final int onTeam = basicDao.selectSingleValue(Integer.class, checkTeamSql, params);
        if (onTeam == 0) {
            throw new IllegalArgumentException("Member is not part of this match’s team");
        }

        // 2) Count how many distinct shooters already
        final String countSql = new QueryBuilder()
                .select("COUNT(DISTINCT " + COL_SHOOTER_ID + ")")
                .from(TABLE)
                .whereEquals(COL_MATCH_ID)
                .compose().toString();

        final int count = basicDao.selectSingleValue(Integer.class,
                countSql,
                matchId);
        if (count >= 3) {
            throw new IllegalStateException("Match already has 3 shooters assigned");
        }

        // 3) Check they’re not already in that list
        final String existsSql = new QueryBuilder()
                .select("COUNT(*)")
                .from(TABLE)
                .whereEquals(COL_MATCH_ID)
                .andEquals(COL_SHOOTER_ID)
                .compose().toString();

        final int already = basicDao.selectSingleValue(Integer.class,
                existsSql,
                matchId,
                dsbMitgliedId);
        if (already > 0) {
            throw new IllegalStateException("This member is already assigned to the match");
        }

        // no INSERT here – will happen when the first passe is inserted
    }

    /**
     * Retrieves all distinct shooters with their order number.
     */
    public List<MatchShooter> findByMatchId(final long matchId) {
        // window-function + grouping is a bit special, so we build raw SQL here
        final String sql =
                "SELECT " + COL_SHOOTER_ID + "," +
                        "       ROW_NUMBER() OVER (ORDER BY " + COL_LFDNR + ") AS rueckennummer " +
                        "  FROM " + TABLE + " " +
                        " WHERE " + COL_MATCH_ID + " = :matchId " +
                        " GROUP BY " + COL_SHOOTER_ID + ", " + COL_LFDNR + " " +
                        " ORDER BY " + COL_LFDNR;

        return basicDao.getNamedParameterJdbcTemplate().query(
                sql,
                Map.of("matchId", matchId),
                (rs, rowNum) -> {
                    final MatchShooter shooter = new MatchShooter();
                    shooter.setDsbMitgliedId(rs.getLong(COL_SHOOTER_ID));
                    shooter.setRueckennummer(rs.getInt("rueckennummer"));
                    return shooter;
                });
    }

    /**
     * deleteByMatchId is not supported.
     */
    @Override
    public Optional<?> findById(final Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<?> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object create(Object entity, Long currentUserId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object update(Object entity, Long currentUserId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}