package de.bogenliga.application.business.sportjahr.impl.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.sportjahr.impl.entity.SportjahrBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.tx.TransactionManager;

/**
 * DataAccessObject for the sportjahr entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Marcel Schneider
 */
@Repository
public class SportjahrDAO implements DataAccessObject {
    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(
            SportjahrDAO.class);

    //business entity parameter names
    private static final String SPORTJAHR_BE_ID = "sportjahrID";
    private static final String SPORTJAHR_BE_SPORTJAHR_LIGA_ID = "sportjahr_liga_id";
    private static final String SPORTJAHR_BE_SPORTJAHR_REGIONNAME = "sportjahr:region_name";
    private static final String SPORTJAHR_BE_SPORTJAHR_LIGA_UEBERGEORDNET_ID = "liga_uebergeordnet_id";
    private static final String SPORTJAHR_BE_SPORTJAHR_LIGALEITER_ID = "ligaleiter_id";

    private static final String SPORTJAHR_TABLE_ID = "sportjahrID";
    private static final String SPORTJAHR_TABLE_LIGA_ID = "sportjahr_liga_id";
    private static final String SPORTJAHR_TABLE_REGIONNAME = "sportjahr:region_name";
    private static final String SPORTJAHR_TABLE_LIGA_UEBERGEORDNET_ID = "liga_uebergeordnet_id";
    private static final String SPORTJAHR_TABLE_LIGALEITER_ID = "ligaleiter_id";

    private static final String TABLE = "Liga";


    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<SportjahrBE> SPORTJAHR = new BusinessEntityConfiguration<>(
            SportjahrBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM liga"
                    + " ORDER BY liga_id";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM liga "
                    + " WHERE liga_id = ?";

    private final BasicDAO basicDao;

    private final TransactionManager transactionManager;
    private final QueryRunner run = new QueryRunner();


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao           to handle the commonly used database operations
     * @param transactionManager to handle custom database transactions
     */
    @Autowired
    public SportjahrDAO(final BasicDAO basicDao, final TransactionManager transactionManager) {
        this.basicDao = basicDao;
        this.transactionManager = transactionManager;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(SPORTJAHR_TABLE_ID, SPORTJAHR_BE_ID);
        columnsToFieldsMap.put(SPORTJAHR_TABLE_LIGA_ID, SPORTJAHR_BE_SPORTJAHR_LIGA_ID);
        columnsToFieldsMap.put(SPORTJAHR_TABLE_REGIONNAME, SPORTJAHR_BE_SPORTJAHR_REGIONNAME);
        columnsToFieldsMap.put(SPORTJAHR_TABLE_LIGA_UEBERGEORDNET_ID, SPORTJAHR_BE_SPORTJAHR_LIGA_UEBERGEORDNET_ID);
        columnsToFieldsMap.put(SPORTJAHR_TABLE_LIGALEITER_ID, SPORTJAHR_BE_SPORTJAHR_LIGALEITER_ID);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all Sportjahr entries
     */
    public List<SportjahrBE> findAll() {
        return basicDao.selectEntityList(SPORTJAHR, FIND_ALL);

    }


    /**
     * Return Sportjahr entry with specific id
     *
     * @param id
     */
    public SportjahrBE findById(final long id) {
        return basicDao.selectSingleEntity(SPORTJAHR, FIND_BY_ID, id);
    }


    /**
     * Create a new Sportjahr entry
     *
     * @param sportjahrBE
     * @param currentSportjahrId
     *
     * @return Business Entity corresponding to the created sportjahr entry
     */
    public SportjahrBE create(final SportjahrBE sportjahrBE, final long currentSportjahrId) {
        basicDao.setCreationAttributes(sportjahrBE, currentSportjahrId);

        return basicDao.insertEntity(SPORTJAHR, sportjahrBE);
    }


    /**
     * Update an existing Sportjahr entry
     *
     * @param sportjahrBE
     * @param currentSportjahrId
     *
     * @return Business Entity corresponding to the updated sportjahr entry
     */
    public SportjahrBE update(final SportjahrBE sportjahrBE, final long currentSportjahrId) {
        basicDao.setModificationAttributes(sportjahrBE, currentSportjahrId);

        return basicDao.updateEntity(SPORTJAHR, sportjahrBE, SPORTJAHR_BE_ID);
    }


    /**
     * Delete existing sportjahr entrycreated_at_utc
     *
     * @param sportjahrBE
     * @param currentSportjahrId
     */
    public void delete(final SportjahrBE sportjahrBE, final long currentSportjahrId) {
        basicDao.setModificationAttributes(sportjahrBE, currentSportjahrId);

        basicDao.deleteEntity(SPORTJAHR, sportjahrBE, SPORTJAHR_BE_ID);
    }

}
