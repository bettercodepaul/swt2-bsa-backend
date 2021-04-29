package de.bogenliga.application.business.disziplin.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.disziplin.impl.entity.DisziplinBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * @author Marcel Neumann
 * @author Robin Mueller
 * @author Nick Kerschagel
 */
@Repository
public class DisziplinDAO implements DataAccessObject {

    // define logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(DisziplinDAO.class);

    // table name in the DB
    private static final String TABLE = "disziplin";

    // business entity parameters
    private static final String DISZIPLIN_BE_ID = "id";
    private static final String DISZIPLIN_BE_NAME = "name";

    // table columns
    private static final String DISZIPLIN_TABLE_ID = "disziplin_id";
    private static final String DISZIPLIN_TABLE_NAME = "disziplin_name";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<DisziplinBE> DISZIPLIN = new BusinessEntityConfiguration<>(
            DisziplinBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private final BasicDAO basicDao;

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public DisziplinDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(DISZIPLIN_TABLE_ID, DISZIPLIN_BE_ID);
        columnsToFieldsMap.put(DISZIPLIN_TABLE_NAME, DISZIPLIN_BE_NAME);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }

    /**
     * SQL queries
     */
    private static final String FIND_ALL = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .orderBy(DISZIPLIN_TABLE_ID)
            .compose().toString();

    private static final String FIND_BY_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(DISZIPLIN_TABLE_ID)
            .orderBy(DISZIPLIN_TABLE_ID)
            .compose().toString();



    /**
     * Return a specific disziplin.
     *
     * @return match with given id
     */
    public DisziplinBE findById(final Long matchId) {
        return basicDao.selectSingleEntity(DISZIPLIN, FIND_BY_ID, matchId);
    }


    /**
     * Return all entries.
     *
     * @return list of all disciplins in the database; empty list, if no match is found
     */
    public List<DisziplinBE> findAll() {
        return basicDao.selectEntityList(DISZIPLIN, FIND_ALL);
    }

    /**
     * Create a new disziplin entry
     *
     * @param disziplinBE Business Entity representation to persist
     * @param currentUserId ID of the current user
     *
     * @return Business Entity corresponding to the created disziplin entry
     */
    public DisziplinBE create(final DisziplinBE disziplinBE, final Long currentUserId) {
        basicDao.setCreationAttributes(disziplinBE, currentUserId);
        return basicDao.insertEntity(DISZIPLIN, disziplinBE);
    }

    /**
     * Update an existing match entry
     *
     * @param disziplinBE Business Entity representation to update
     * @param currentUserId ID of current user
     *
     * @return Business Entity corresponding to the updated match entry
     */
    public DisziplinBE update(final DisziplinBE disziplinBE, final Long currentUserId) {
        basicDao.setModificationAttributes(disziplinBE, currentUserId);
        return basicDao.updateEntity(DISZIPLIN, disziplinBE, DISZIPLIN_BE_ID);
    }

    /**
     * Delete existing match
     *
     * @param disziplinBE Business Entity representation to delete
     * @param currentUserId ID of current user
     */
    public void delete(final DisziplinBE disziplinBE, final Long currentUserId) {
        basicDao.setModificationAttributes(disziplinBE, currentUserId);
        basicDao.deleteEntity(DISZIPLIN, disziplinBE, DISZIPLIN_BE_ID);
    }
}