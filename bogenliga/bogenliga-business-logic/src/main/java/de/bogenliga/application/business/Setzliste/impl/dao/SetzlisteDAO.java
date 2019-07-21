package de.bogenliga.application.business.Setzliste.impl.dao;

import de.bogenliga.application.business.Setzliste.impl.entity.SetzlisteBE;
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

/**
 * DataAccessObject for the Setzliste entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class SetzlisteDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(SetzlisteDAO.class);

    // table name in the database
    private static final String TABLE = "veranstaltung";

    // business entity parameter names
    private static final String SETZLISTE_BE_TABELLENPLATZ = "ligatabelleTabellenplatz";
    private static final String SETZLISTE_BE_WETTKAMPF_ID = "wettkampfid";
    private static final String SETZLISTE_BE_MANNSCHAFT_ID = "mannschaftid";

    // table columns
    private static final String LIGATABELLE_TABLE_TABELLENPLATZ = "ligatabelle_tabellenplatz";
    private static final String WETTKAMPF_TABLE_ID = "wettkampf_id";
    private static final String MANNSCHAFT_TABLE_ID = "mannschaft_id";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<SetzlisteBE> SETZLISTE = new BusinessEntityConfiguration<>(
            SetzlisteBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */

    private static final String GET_TABLE_BY_WETTKAMPF_ID = "SELECT lt.ligatabelle_tabellenplatz, lt.ligatabelle_mannschaft_id, wk.wettkampf_id"
            + " FROM ligatabelle as lt"
            + " INNER JOIN wettkampf AS wk ON lt.ligatabelle_veranstaltung_id = wk.wettkampf_veranstaltung_id"
            + " WHERE wk.wettkampf_id = ?"
            + " AND lt.ligatabelle_wettkampf_tag = wk.wettkampf_tag - 1"
            + " AND lt.ligatabelle_veranstaltung_id = wk.wettkampf_veranstaltung_id"
            + " ORDER BY lt.ligatabelle_tabellenplatz";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public SetzlisteDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(WETTKAMPF_TABLE_ID, SETZLISTE_BE_WETTKAMPF_ID);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_ID, SETZLISTE_BE_MANNSCHAFT_ID);
        columnsToFieldsMap.put(LIGATABELLE_TABLE_TABELLENPLATZ, SETZLISTE_BE_TABELLENPLATZ);
        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all setzliste entries
     */
    public List<SetzlisteBE> getTableByWettkampfID(long wettkampfid) {
        return basicDao.selectEntityList(SETZLISTE, GET_TABLE_BY_WETTKAMPF_ID, wettkampfid);
    }
}
