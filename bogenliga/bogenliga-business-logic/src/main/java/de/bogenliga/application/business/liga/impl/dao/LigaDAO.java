package de.bogenliga.application.business.liga.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the liga entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Bruno Michael Cunha Teixeira, Bruno_Michael.Cunha_teixeira@Student.Reutlingen-University.de
 */
@Repository
public class LigaDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(LigaDAO.class);

    // table name in the database
    private static final String TABLE = "liga";

    //business entity parameter names
    private static final String LIGA_BE_ID = "ligaId";
    private static final String LIGA_BE_NAME = "ligaName";
    private static final String LIGA_BE_REGION_ID = "ligaRegionId";
    private static final String LIGA_BE_UEBERGEORDNET_ID = "ligaUebergeordnetId";
    private static final String LIGA_BE_VERANTWORTLICH_ID = "ligaVerantwortlichId";

    private static final String LIGA_TABLE_ID = "liga_id";
    private static final String LIGA_TABLE_NAME = "liga_name";
    private static final String LIGA_TABLE_REGION_ID = "liga_region_id";
    private static final String LIGA_TABLE_UEBERGEORDNET = "liga_uebergeordnet";
    private static final String LIGA_TABLE_VERANTWORTLICH = "liga_verantwortlich";

    //wrap all specific config parameters
    private static final BusinessEntityConfiguration<LigaBE> LIGA = new BusinessEntityConfiguration<>(
            LigaBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

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


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public LigaDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(LIGA_TABLE_ID, LIGA_BE_ID);
        columnsToFieldsMap.put(LIGA_TABLE_NAME, LIGA_BE_NAME);
        columnsToFieldsMap.put(LIGA_TABLE_REGION_ID, LIGA_BE_REGION_ID);
        columnsToFieldsMap.put(LIGA_TABLE_UEBERGEORDNET, LIGA_BE_UEBERGEORDNET_ID);
        columnsToFieldsMap.put(LIGA_TABLE_VERANTWORTLICH, LIGA_BE_VERANTWORTLICH_ID);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return liga entry with specific id
     *
     * @param id
     */
    public LigaBE findById(final long id) {
        return basicDao.selectSingleEntity(LIGA, FIND_BY_ID, id);
    }


    /**
     * Return all liga entries
     */
    public List<LigaBE> findAll() {
        return basicDao.selectEntityList(LIGA, FIND_ALL);
    }


    /**
     * @param ligaBE
     * @param currentDsbMitglied
     *
     * @return
     */
    public LigaBE update(final LigaBE ligaBE, final long currentDsbMitglied) {
        basicDao.setModificationAttributes(ligaBE, currentDsbMitglied);

        return basicDao.updateEntity(LIGA, ligaBE, LIGA_BE_ID);
    }


    public void delete(final LigaBE ligaBE, final long currentDsbMitglied) {
        basicDao.setModificationAttributes(ligaBE, currentDsbMitglied);

        basicDao.deleteEntity(LIGA, ligaBE, LIGA_BE_ID);
    }


    /**
     * Create a new dsbmitglied entry
     *
     * @param ligaBE
     * @param currentLigaId
     *
     * @return Business Entity corresponding to the created dsbmitglied entry
     */
    public LigaBE create(final LigaBE ligaBE, final long currentLigaId) {
        basicDao.setCreationAttributes(ligaBE, currentLigaId);

        return basicDao.insertEntity(LIGA, ligaBE);
    }
}
