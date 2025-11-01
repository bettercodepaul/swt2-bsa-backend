package de.bogenliga.application.business.vereine.impl.dao;

import de.bogenliga.application.business.vereine.impl.entity.VereinBEext;
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
 * DataAccessObject for the Verein entity in the database
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
@Repository
public class VereinDAOext implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(VereinDAOext.class);

    // table name in the database
    private static final String TABLE = "verein";

    //business entity parameter names
    private static final String VEREIN_BE_ID = "vereinId";
    private static final String VEREIN_BE_NAME = "vereinName";
    private static final String VEREIN_BE_DSB_IDENTIFIER = "vereinDsbIdentifier";
    private static final String VEREIN_BE_REGION_ID = "vereinRegionId";
    private static final String VEREIN_BE_WEBSITE = "vereinWebsite";
    private static final String VEREIN_BE_DESCRIPTION = "vereinDescription";
    private static final String VEREIN_BE_ICON = "vereinIcon";
    private static final String VEREIN_BE_REGIONAME = "vereinRegionName";

    private static final String VEREIN_TABLE_ID = "verein_id";
    private static final String VEREIN_TABLE_NAME = "verein_name";
    private static final String VEREIN_TABLE_DSB_IDENTIFIER = "verein_dsb_identifier";
    private static final String VEREIN_TABLE_REGION_ID = "verein_region_id";
    private static final String VEREIN_TABLE_WEBSITE = "verein_website";
    private static final String VEREIN_TABLE_DESCRIPTION = "verein_description";
    private static final String VEREIN_TABLE_ICON = "verein_icon";
    private static final String VEREIN_TABLE_REGIONNAME = "region_name";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<VereinBEext> VEREIN_EXTENDED = new BusinessEntityConfiguration<>(
            VereinBEext.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    // SQL Queries
    private static final String FIND_ALL =
            "SELECT v.*, r.region_name "
                    + " FROM verein v"
                    + " JOIN region r on v.verein_region_id=r.region_id"
                    + " ORDER BY verein_id";

    private static final String FIND_BY_SEARCH =
            "SELECT v.*, r.region_name "
                    + " FROM verein v"
                    + " JOIN region r on v.verein_region_id=r.region_id "
                    + " WHERE CONCAT(LOWER(v.verein_name), ' ', "
                    + " LOWER(r.region_name), ' ', "
                    + " LOWER(v.verein_dsb_identifier)) LIKE LOWER(?) ";

    private static final String FIND_BY_ID =
            "SELECT v.*, r.region_name "
                    + " FROM verein v"
                    + " JOIN region r on v.verein_region_id=r.region_id"
                    + " WHERE v.verein_id = ?";

    private final BasicDAO basicDao;


    @Autowired
    public VereinDAOext(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap;
        columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(VEREIN_TABLE_ID, VEREIN_BE_ID);
        columnsToFieldsMap.put(VEREIN_TABLE_NAME, VEREIN_BE_NAME);
        columnsToFieldsMap.put(VEREIN_TABLE_DSB_IDENTIFIER, VEREIN_BE_DSB_IDENTIFIER);
        columnsToFieldsMap.put(VEREIN_TABLE_REGION_ID, VEREIN_BE_REGION_ID);
        columnsToFieldsMap.put(VEREIN_TABLE_WEBSITE, VEREIN_BE_WEBSITE);
        columnsToFieldsMap.put(VEREIN_TABLE_DESCRIPTION, VEREIN_BE_DESCRIPTION);
        columnsToFieldsMap.put(VEREIN_TABLE_ICON, VEREIN_BE_ICON);
        columnsToFieldsMap.put(VEREIN_TABLE_REGIONNAME, VEREIN_BE_REGIONAME);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * @return Returns all Vereine
     */
    public List<VereinBEext> findAll() {
        return basicDao.selectEntityList(VEREIN_EXTENDED, FIND_ALL);
    }

    public List<VereinBEext> findBySearch(final String searchTerm) {
        return basicDao.selectEntityList(VEREIN_EXTENDED, FIND_BY_SEARCH, new StringBuilder()
                                                                     .append("%")
                                                                     .append(searchTerm)
                                                                     .append("%")
                                                                     .toString()
        );
    }


    /**
     * Returns a "Verein" entry with a specific id
     *
     * @param vereinId Id of the verein that should be queried
     *
     * @return Returns the queried verein as Business Entity
     */
    public VereinBEext findById(final long vereinId) {
        return basicDao.selectSingleEntity(VEREIN_EXTENDED, FIND_BY_ID, vereinId);
    }

   }
