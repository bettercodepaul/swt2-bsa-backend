package de.bogenliga.application.business.wettkampf.impl.dao;


import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfOverviewBE;
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
 * DataAccessObject for the wettkampf overview entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Dennis Eitle
 */
@Repository
public class WettkampfOverviewDAO implements DataAccessObject {
    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(WettkampfOverviewDAO.class);

    // table name in the database
    private static final String TABLE = "veranstaltung";

    //business entity parameter names
    private static final String VERANSTALTUNG_BE_ID = "id";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_NAME= "ligaName";
    private static final String WETTKAMPF_BE_WETTKAMPF_DATUM= "datum";
    private static final String WETTKAMPF_BE_WETTKAMPF_ORT= "wettkampfOrt";
    private static final String WETTKAMPF_BE_WETTKAMPF_BEGINN = "wettkampfBeginn";
    private static final String WETTKAMPF_BE_WETTKAMPF_TAG = "wettkampfTag";
    private static final String WETTKAMPF_BE_WETTKAMPF_DISZIPLIN_ID = "disziplinID";
    private static final String DISZIPLIN_BE_NAME= "disziplinName";

    private static final String VERANSTALTUNG_TABLE_ID = "veranstaltung_id";
    private static final String VERANSTALTUNG_TABLE_VERANSTALTUNG_NAME= "veranstaltung_name";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_DATUM= "wettkampf_datum";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_ORT= "wettkampf_ort";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_BEGINN = "wettkampf_beginn";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_TAG = "wettkampf_tag";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_DISZIPLIN_ID = "wettkampf_disziplin_id";
    private static final String DISZIPLIN_TABLE_NAME= "disziplin_name";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<WettkampfOverviewBE> WETTKAMPF = new BusinessEntityConfiguration<>(
            WettkampfOverviewBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT vs.veranstaltung_id, vs.veranstaltung_name, wk.wettkampf_datum, wk.wettkampf_ort, wk.wettkampf_beginn, wk.wettkampf_tag, wk.wettkampf_disziplin_id, d.disziplin_name FROM veranstaltung as vs"
                + " INNER JOIN wettkampf AS wk ON vs.veranstaltung_id = wk.wettkampf_veranstaltung_id"
                + " INNER JOIN disziplin AS d ON wk.wettkampf_disziplin_id = d.disziplin_id";

    private final BasicDAO basicDao;

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public WettkampfOverviewDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }




    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_ID, VERANSTALTUNG_BE_ID);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_VERANSTALTUNG_NAME, VERANSTALTUNG_BE_VERANSTALTUNG_NAME);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_DATUM, WETTKAMPF_BE_WETTKAMPF_DATUM);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_ORT, WETTKAMPF_BE_WETTKAMPF_ORT);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_BEGINN, WETTKAMPF_BE_WETTKAMPF_BEGINN);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_TAG, WETTKAMPF_BE_WETTKAMPF_TAG);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_DISZIPLIN_ID, WETTKAMPF_BE_WETTKAMPF_DISZIPLIN_ID);
        columnsToFieldsMap.put(DISZIPLIN_TABLE_NAME, DISZIPLIN_BE_NAME);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }

    /**
     * Return all Wettkampf entries
     */
    public List<WettkampfOverviewBE> findAll() {
        return basicDao.selectEntityList(WETTKAMPF, FIND_ALL);
    }
}
