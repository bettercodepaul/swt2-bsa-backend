package de.bogenliga.application.business.lizenz.impl.dao;

import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
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
 * DataAccessObject for the Lizenz/Dsbmitglied entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 * Functionality to find all Kampfrichter who are not in a Wettkampftag (wettkampfid) but have the Kampfrichter-Lizenz is implemented in KampfrichterDAO
 *
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class LizenzDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(LizenzDAO.class);

    // table name in the database
    private static final String TABLE = "lizenz";
    // business entity parameter names

    private static final String LIZENZ_BE_ID = "lizenzId";
    private static final String LIZENZ_BE_NUMBER = "lizenznummer";
    private static final String LIZENZ_BE_REGIONID = "lizenzRegionId";
    private static final String LIZENZ_BE_DSBMEMBERID = "lizenzDsbMitgliedId";
    private static final String LIZENZ_BE_TYP = "lizenztyp";
    private static final String LIZENZ_BE_DISCIPLINEID = "lizenzDisziplinId";

    private static final String LIZENZ_TABLE_ID = "lizenz_id";
    private static final String LIZENZ_TABLE_NUMBER = "lizenz_nummer";
    private static final String LIZENZ_TABLE_REGIONID = "lizenz_region_id";
    private static final String LIZENZ_TABLE_DSBMEMBERID = "lizenz_dsb_mitglied_id";
    private static final String LIZENZ_TABLE_TYP  = "lizenz_typ";
    private static final String LIZENZ_TABLE_DISCIPLINEID = "lizenz_disziplin_id";


    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<LizenzBE> LIZENZ = new BusinessEntityConfiguration<>(
            LizenzBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM lizenz"
                    + " ORDER BY lizenz_id";

    private static final String FIND_KAMPFRICHTER_BY_DSB_MITGLIED_ID =
            "SELECT * "
                    + " FROM lizenz"
                    + " WHERE lizenz_typ = 'Kampfrichter' AND lizenz_dsb_mitglied_id = ?";

    private static final String FIND_BY_DSB_MITGLIED_ID =
            "SELECT * "
                    + " FROM lizenz"
                    + " WHERE lizenz_dsb_mitglied_id = ?";

    private static final String FIND_BY_DSB_MITGLIED_ID_AND_DISZIPLIN_ID =
            "SELECT * " +
                    " FROM lizenz" +
                    " WHERE (lizenz_dsb_mitglied_id = ?" +
                    " AND lizenz_disziplin_id = ?)";


    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public LizenzDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(LIZENZ_TABLE_ID, LIZENZ_BE_ID);
        columnsToFieldsMap.put(LIZENZ_TABLE_NUMBER, LIZENZ_BE_NUMBER);
        columnsToFieldsMap.put(LIZENZ_TABLE_REGIONID, LIZENZ_BE_REGIONID);
        columnsToFieldsMap.put(LIZENZ_TABLE_DSBMEMBERID, LIZENZ_BE_DSBMEMBERID);
        columnsToFieldsMap.put(LIZENZ_TABLE_TYP, LIZENZ_BE_TYP);
        columnsToFieldsMap.put(LIZENZ_TABLE_DISCIPLINEID, LIZENZ_BE_DISCIPLINEID);


        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all Lizenz entries
     */
    public List<LizenzBE> findAll() {
        return basicDao.selectEntityList(LIZENZ, FIND_ALL);
    }


    /**
     * Return Lizenz entry with specific dsbmitglied_id
     *
     * @param id
     */
    public List<LizenzBE> findByDsbMitgliedId(final long id) {
        return basicDao.selectEntityList(LIZENZ, FIND_BY_DSB_MITGLIED_ID, id);
    }

    /**
     * Return Wettkampflizenz entry with specific dsbmitglied_id
     *
     * @param id
     */
    public LizenzBE findKampfrichterLizenzByDsbMitgliedId(final long id) {
        return basicDao.selectSingleEntity(LIZENZ, FIND_KAMPFRICHTER_BY_DSB_MITGLIED_ID, id);
    }

    public LizenzBE findByDsbMitgliedIdAndDisziplinId(final long dsbMitgliedId, final long disziplinId) {
        return basicDao.selectSingleEntity(LIZENZ, FIND_BY_DSB_MITGLIED_ID_AND_DISZIPLIN_ID, dsbMitgliedId, disziplinId);
    }



    /**
     * Create a new Lizenz entry
     *
     * @return Business Entity corresponding to the created dsbmitglied entry
     */
    public LizenzBE create(final LizenzBE lizenzBE, final long currentUserId) {

        basicDao.setCreationAttributes(lizenzBE, currentUserId);

        return basicDao.insertEntity(LIZENZ, lizenzBE);
    }


    /**
     * Update an existing kampfrichterlizenz entry
     *
     * @return Business Entity corresponding to the updated dsbmitglied entry
     */
    public LizenzBE update(final LizenzBE lizenzBE, final long currentUserId) {
        basicDao.setModificationAttributes(lizenzBE, currentUserId);

        return basicDao.updateEntity(LIZENZ, lizenzBE, LIZENZ_BE_ID);
    }


    /**
     * Delete existing kampfrichterlizenz entry
     *
     */
    public void delete(final LizenzBE lizenzBE, final long currentUserId) {
        basicDao.setModificationAttributes(lizenzBE, currentUserId);

        basicDao.deleteEntity(LIZENZ, lizenzBE, LIZENZ_BE_ID);
    }
}
