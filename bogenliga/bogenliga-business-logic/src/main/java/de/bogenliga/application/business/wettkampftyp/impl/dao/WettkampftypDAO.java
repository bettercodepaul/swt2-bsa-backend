package de.bogenliga.application.business.wettkampftyp.impl.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampftypBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the wettkampftyp entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Marvin Holm, Daniel Schott
 */
@Repository
public class WettkampftypDAO implements DataAccessObject {
    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(
            WettkampftypDAO.class);

    // table name in the database
    private static final String TABLE = "wettkampftyp";

    //business entity parameter names
    private static final String WETTKAMPFTYP_BE_WETTKAMPFTYP_ID = "wettkampftypID";
    private static final String WETTKAMPFTYP_BE_WETTKAMPFTYP_NAME= "wettkampftypName";

    private static final String WETTKAMPFTYP_TABLE_WETTKAMPFTYP_ID = "wettkampftyp_id";
    private static final String WETTKAMPFTYP_TABLE_WETTKAMPFTYP_NAME= "wettkampftyp_name";


    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<WettkampftypBE> WETTKAMPFTYP = new BusinessEntityConfiguration<>(
            WettkampftypBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM wettkampftyp"
                    + " ORDER BY wettkampftyp_id";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM wettkampftyp "
                    + " WHERE wettkampftyp_id = ?";

    private final BasicDAO basicDao;

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public WettkampftypDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }




    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(WETTKAMPFTYP_TABLE_WETTKAMPFTYP_ID, WETTKAMPFTYP_BE_WETTKAMPFTYP_ID);
        columnsToFieldsMap.put(WETTKAMPFTYP_TABLE_WETTKAMPFTYP_NAME, WETTKAMPFTYP_BE_WETTKAMPFTYP_NAME);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }

    /**
     * Return all Wettkampftyp entries
     */
    public List<WettkampftypBE> findAll() {
        return basicDao.selectEntityList(WETTKAMPFTYP, FIND_ALL);
    }


    /**
     * Return Wettkampftyp entry with specific id
     *
     * @param id
     */
    public WettkampftypBE findById(final long id) {
        return basicDao.selectSingleEntity(WETTKAMPFTYP, FIND_BY_ID, id);
    }


    /**
     * Create a new Wettkampftyp entry
     *
     * @param wettkampftypBE
     * @param currentWettkampftypId
     * @return Business Entity corresponding to the created wettkampftyp entry
     */
    public WettkampftypBE create(final WettkampftypBE wettkampftypBE, final long currentWettkampftypId) {
        basicDao.setCreationAttributes(wettkampftypBE, currentWettkampftypId);

        return basicDao.insertEntity(WETTKAMPFTYP, wettkampftypBE);
    }


    /**
     * Update an existing Wettkampftyp entry
     *
     * @param wettkampftypBE
     * @param currentWettkampftypId
     * @return Business Entity corresponding to the updated wettkampftyp entry
     */
    public WettkampftypBE update(final WettkampftypBE wettkampftypBE, final long currentWettkampftypId) {
        basicDao.setModificationAttributes(wettkampftypBE, currentWettkampftypId);

        return basicDao.updateEntity(WETTKAMPFTYP, wettkampftypBE, WETTKAMPFTYP_BE_WETTKAMPFTYP_ID);
    }


    /**
     * Delete existing wettkampftyp entrycreated_at_utc
     *
     * @param wettkampftypBE
     * @param currentWettkampftypId
     */
    public void delete(final WettkampftypBE wettkampftypBE, final long currentWettkampftypId) {
        basicDao.setModificationAttributes(wettkampftypBE, currentWettkampftypId);

        basicDao.deleteEntity(WETTKAMPFTYP, wettkampftypBE, WETTKAMPFTYP_BE_WETTKAMPFTYP_ID);
    }

}
