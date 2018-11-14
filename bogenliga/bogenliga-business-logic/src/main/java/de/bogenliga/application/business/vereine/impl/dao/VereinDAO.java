package de.bogenliga.application.business.vereine.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the Verein entity in the database
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
@Repository
public class VereinDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(VereinDAO.class);

    // table name in the database
    private static final String TABLE = "verein";

    //business entity parameter names
    private static final String VEREIN_BE_ID = "vereinId";
    private static final String VEREIN_BE_NAME = "vereinName";
    private static final String VEREIN_BE_DSB_IDENTIFIER = "vereinDsbIdentifier";
    private static final String VEREIN_BE_REGION_ID = "vereinRegionId";

    private static final String VEREIN_TABLE_ID = "verein_id";
    private static final String VEREIN_TABLE_NAME = "verein_name";
    private static final String VEREIN_TABLE_DSB_IDENTIFIER = "verein_dsb_identifier";
    private static final String VEREIN_TABLE_REGION_ID = "verein_region_id";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<VereinBE> VEREIN = new BusinessEntityConfiguration<>(
            VereinBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    // SQL Queries
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM verein"
                    + " ORDER BY verein_id";
    private final BasicDAO basicDao;


    @Autowired
    public VereinDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(VEREIN_TABLE_ID, VEREIN_BE_ID);
        columnsToFieldsMap.put(VEREIN_TABLE_NAME, VEREIN_BE_NAME);
        columnsToFieldsMap.put(VEREIN_TABLE_DSB_IDENTIFIER, VEREIN_BE_DSB_IDENTIFIER);
        columnsToFieldsMap.put(VEREIN_TABLE_REGION_ID, VEREIN_BE_REGION_ID);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * @return Returns all Vereine
     */
    public List<VereinBE> findAll() {
        return basicDao.selectEntityList(VEREIN, FIND_ALL);
    }


    public VereinBE create(final VereinBE vereinBE, final long currentDsbMitgliedId) {
        basicDao.setCreationAttributes(vereinBE, currentDsbMitgliedId);

        return basicDao.insertEntity(VEREIN, vereinBE);
    }
}
