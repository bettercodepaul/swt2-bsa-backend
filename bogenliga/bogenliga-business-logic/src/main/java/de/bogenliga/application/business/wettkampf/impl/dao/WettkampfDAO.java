package de.bogenliga.application.business.wettkampf.impl.dao;


import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
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
 * DataAccessObject for the wettkampf entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Marvin Holm, Daniel Schott
 */
@Repository
public class WettkampfDAO implements DataAccessObject {
    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(WettkampfDAO.class);

    // table name in the database
    private static final String TABLE = "wettkampf";

    //business entity parameter names
    private static final String WETTKAMPF_BE_ID = "id";
    private static final String WETTKAMPF_BE_VERANSTALTUNGS_ID= "veranstaltungsId";
    private static final String WETTKAMPF_BE_WETTKAMPF_DATUM= "datum";
    private static final String WETTKAMPF_BE_WETTKAMPF_ORT= "wettkampfOrt";
    private static final String WETTKAMPF_BE_WETTKAMPF_BEGINN = "wettkampfBeginn";
    private static final String WETTKAMPF_BE_WETTKAMPF_TAG = "wettkampfTag";
    private static final String WETTKAMPF_BE_WETTKAMPF_DISZIPLIN_ID = "wettkampfDisziplinId";
    private static final String WETTKAMPF_BE_WETTKAMPF_WETTKAMPFTYP_ID= "wettkampfTypId";

    private static final String WETTKAMPF_TABLE_ID = "wettkampf_id";
    private static final String WETTKAMPF_TABLE_VERANSTALTUNGS_ID= "wettkampf_veranstaltung_id";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_DATUM= "wettkampf_datum";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_ORT= "wettkampf_ort";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_BEGINN = "wettkampf_beginn";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_TAG = "wettkampf_tag";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_DISZIPLIN_ID = "wettkampf_disziplin_id";
    private static final String WETTKAMPF_TABLE_WETTKAMPF_WETTKAMPFTYP_ID= "wettkampf_wettkampftyp_id";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<WettkampfBE> WETTKAMPF = new BusinessEntityConfiguration<>(
            WettkampfBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM wettkampf"
                    + " ORDER BY wettkampf_id";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM wettkampf "
                    + " WHERE wettkampf_id = ?";

    private static final String FIND_ALL_BY_MANNSCHAFTS_ID =
            "SELECT * "
                    + " FROM wettkampf "
                    + " WHERE wettkampf_id IN ("
                    + " SELECT match_wettkampf_id"
                        + " FROM match"
                        + " WHERE match_mannschaft_id = ?)" +
                    "ORDER BY wettkampf_datum";

    private static final String FIND_ALL_BY_VERANSTALTUNG_ID =
            "SELECT *" +
                    " FROM wettkampf" +
                    " WHERE wettkampf_veranstaltung_id = ?" +
                    " order by wettkampf_datum";


    private final BasicDAO basicDao;

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public WettkampfDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }




    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(WETTKAMPF_TABLE_ID, WETTKAMPF_BE_ID);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_VERANSTALTUNGS_ID, WETTKAMPF_BE_VERANSTALTUNGS_ID);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_BEGINN, WETTKAMPF_BE_WETTKAMPF_BEGINN);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_DATUM, WETTKAMPF_BE_WETTKAMPF_DATUM);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_DISZIPLIN_ID, WETTKAMPF_BE_WETTKAMPF_DISZIPLIN_ID);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_ORT, WETTKAMPF_BE_WETTKAMPF_ORT);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_TAG, WETTKAMPF_BE_WETTKAMPF_TAG);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_WETTKAMPF_WETTKAMPFTYP_ID, WETTKAMPF_BE_WETTKAMPF_WETTKAMPFTYP_ID);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }

    /**
     * Return all Wettkampf entries
     */
    public List<WettkampfBE> findAll() {
        return basicDao.selectEntityList(WETTKAMPF, FIND_ALL);
    }


    /**
     * Return Wettkampf entry with specific id
     *
     * @param id
     */
    public WettkampfBE findById(final long id) {
        return basicDao.selectSingleEntity(WETTKAMPF, FIND_BY_ID, id);
    }

    /**
     * Return all Wettkampf entries according to the given mannschafts-Id
     */
    public List<WettkampfBE> findAllWettkaempfeByMannschaftsId(final long id) {
        return basicDao.selectEntityList(WETTKAMPF, FIND_ALL_BY_MANNSCHAFTS_ID, id);
    }

    public List<WettkampfBE> findAllByVeranstaltungId(final long id) {
        return basicDao.selectEntityList(WETTKAMPF, FIND_ALL_BY_VERANSTALTUNG_ID, id);
    }

    /**
     * Create a new Wettkampf entry
     *
     * @param wettkampfBE
     * @param currentWettkampfId
     * @return Business Entity corresponding to the created wettkampf entry
     */
    public WettkampfBE create(final WettkampfBE wettkampfBE, final long currentWettkampfId) {
        basicDao.setCreationAttributes(wettkampfBE, currentWettkampfId);

        return basicDao.insertEntity(WETTKAMPF, wettkampfBE);
    }


    /**
     * Update an existing Wettkampf entry
     *
     * @param wettkampfBE
     * @param currentWettkampfId
     * @return Business Entity corresponding to the updated wettkampf entry
     */
    public WettkampfBE update(final WettkampfBE wettkampfBE, final long currentWettkampfId) {
        basicDao.setModificationAttributes(wettkampfBE, currentWettkampfId);

        return basicDao.updateEntity(WETTKAMPF, wettkampfBE, WETTKAMPF_BE_ID);
    }


    /**
     * Delete existing wettkampf entrycreated_at_utc
     *
     * @param wettkampfBE
     * @param currentWettkampfId
     */
    public void delete(final WettkampfBE wettkampfBE, final long currentWettkampfId) {
        basicDao.setModificationAttributes(wettkampfBE, currentWettkampfId);

        basicDao.deleteEntity(WETTKAMPF, wettkampfBE, WETTKAMPF_BE_ID);
    }

}
