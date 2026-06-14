package de.bogenliga.application.business.wettkampf.impl.dao;

import de.bogenliga.application.business.wettkampf.impl.entity.AnzeigenBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AnzeigenDAO implements DataAccessObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnzeigenDAO.class);

    private static final String TABLE = "anzeigen";

    private static final String ANZEIGEN_BE_ID = "id";
    private static final String ANZEIGEN_BE_PHYSISCHE_BILDSCHIRM_ID = "physischeBildschirmId";
    private static final String ANZEIGEN_BE_TABLE_TYP = "tableTyp";
    private static final String ANZEIGEN_BE_WETTKAMPF_ID = "wettkampfId";
    private static final String ANZEIGEN_BE_AKTUELLES_MATCH = "aktuellesMatch";

    private static final String ANZEIGEN_TABLE_ID = "anzeigen_id";
    private static final String ANZEIGEN_TABLE_PHYSISCHE_BILDSCHIRM_ID = "physische_bildschirm_id";
    private static final String ANZEIGEN_TABLE_TABLE_TYP = "table_typ";
    private static final String ANZEIGEN_TABLE_WETTKAMPF_ID = "wettkampf_id";
    private static final String ANZEIGEN_TABLE_AKTUELLES_MATCH = "aktuelles_match";

    private static final BusinessEntityConfiguration<AnzeigenBE> ANZEIGE = new BusinessEntityConfiguration<>(
            AnzeigenBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private final BasicDAO basicDao;

    @Autowired
    public AnzeigenDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(ANZEIGEN_TABLE_ID, ANZEIGEN_BE_ID);
        columnsToFieldsMap.put(ANZEIGEN_TABLE_PHYSISCHE_BILDSCHIRM_ID, ANZEIGEN_BE_PHYSISCHE_BILDSCHIRM_ID);
        columnsToFieldsMap.put(ANZEIGEN_TABLE_TABLE_TYP, ANZEIGEN_BE_TABLE_TYP);
        columnsToFieldsMap.put(ANZEIGEN_TABLE_WETTKAMPF_ID, ANZEIGEN_BE_WETTKAMPF_ID);
        columnsToFieldsMap.put(ANZEIGEN_TABLE_AKTUELLES_MATCH, ANZEIGEN_BE_AKTUELLES_MATCH);

        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }

    private static final String FIND_ALL = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .orderBy(ANZEIGEN_TABLE_ID)
            .compose().toString();

    private static final String FIND_BY_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(ANZEIGEN_TABLE_ID)
            .orderBy(ANZEIGEN_TABLE_ID)
            .compose().toString();

    private static final String FIND_BY_WETTKAMPF_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(ANZEIGEN_TABLE_WETTKAMPF_ID)
            .orderBy(ANZEIGEN_TABLE_ID)
            .compose().toString();

    private static final String FIND_BY_PHYSISCHE_BILDSCHIRM_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(ANZEIGEN_TABLE_PHYSISCHE_BILDSCHIRM_ID)
            .orderBy(ANZEIGEN_TABLE_ID)
            .compose().toString();

    /**
     * Return a specific anzeige.
     *
     * @return match with given id
     */
    public AnzeigenBE findById(final Long matchId) {
        return basicDao.selectSingleEntity(ANZEIGE, FIND_BY_ID, matchId);
    }

    /**
     * Return all entries with specific wettkampfId.
     *
     * @return list of all anzeigen with this wettkampfId; empty list, if no match is found
     */
    public List<AnzeigenBE> findByWettkampfId(Long wettkampfId) {
        return basicDao.selectEntityList(ANZEIGE, FIND_BY_WETTKAMPF_ID, wettkampfId);
    }
    /**
     * Return all entries with specific physischeBildschirmId.
     *
     * @return anzeige with this physischeBildschrimId; null if no match is found.
     */
    public AnzeigenBE findByPhysischeBildschirmId(String physischeBildschirmId) {
        return basicDao.selectSingleEntity(ANZEIGE, FIND_BY_PHYSISCHE_BILDSCHIRM_ID, physischeBildschirmId);
    }
    /**
     * Return all entries.
     *
     * @return list of all anzeigen in the database; empty list, if no match is found
     */
    public List<AnzeigenBE> findAll() {
        return basicDao.selectEntityList(ANZEIGE, FIND_ALL);
    }

    /**
     * Create a new anzeigen entry
     *
     * @param anzeigenBE Business Entity representation to persist
     * @param currentUserId ID of the current user
     *
     * @return Business Entity corresponding to the created anzeigen entry
     */
    public AnzeigenBE create(final AnzeigenBE anzeigenBE, final Long currentUserId) {
        basicDao.setCreationAttributes(anzeigenBE, currentUserId);
        return basicDao.insertEntity(ANZEIGE, anzeigenBE);
    }

    /**
     * Update an existing anzeigen entry
     *
     * @param anzeigenBE Business Entity representation to update
     * @param currentUserId ID of current user
     *
     * @return Business Entity corresponding to the updated anzeigen entry
     */
    public AnzeigenBE update(final AnzeigenBE anzeigenBE, final Long currentUserId) {
        basicDao.setModificationAttributes(anzeigenBE, currentUserId);
        return basicDao.updateEntity(ANZEIGE, anzeigenBE, ANZEIGEN_BE_ID);
    }

    /**
     * Delete existing anzeige
     *
     * @param anzeigenBE Business Entity representation to delete
     * @param currentUserId ID of current user
     */
    public void delete(final AnzeigenBE anzeigenBE, final Long currentUserId) {
        basicDao.setModificationAttributes(anzeigenBE, currentUserId);
        basicDao.deleteEntity(ANZEIGE, anzeigenBE, ANZEIGEN_BE_ID);
    }

    /**
     * Clears the anzeigen database which is usually scheduled at night
     * For security reasons, there is no basicDAO implementation of deleteAll since it could wipe other tables
     */
    public void deleteAll() {
        final String clear_anzeigen_database_sql = "DELETE FROM anzeigen";
        basicDao.executeQuery(clear_anzeigen_database_sql);
    }
}