package de.bogenliga.application.business.dsbmannschaft.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.MannschaftBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

@Repository
public class DsbMannschaftDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(DsbMannschaftDAO.class);

    // table name in the database
    private static final String TABLE = "mannschaft";
    // business entity parameter names


    private static final String MANNSCHAFT_BE_ID = "id";
    private static final String MANNSCHAFT_BE_TEAMID = "vereinId";
    private static final String MANNSCHAFT_BE_NUMBER = "nummer";
    private static final String MANNSCHAFT_BE_EVENTID = "veranstaltungId";
    private static final String MANNSCHAFT_BE_USER_ID = "benutzerId";
    private static final String MANNSCHAFT_BE_SORTIERUNG = "sortierung";

    private static final String MANNSCHAFT_TABLE_ID = "mannschaft_id";
    private static final String MANNSCHAFT_TABLE_TEAMID = "mannschaft_verein_id";
    private static final String MANNSCHAFT_TABLE_NUMBER = "mannschaft_nummer";
    private static final String MANNSCHAFT_TABLE_EVENTID = "mannschaft_veranstaltung_id";
    private static final String MANNSCHAFT_TABLE_USER_ID = "mannschaft_benutzer_id";
    private static final String MANNSCHAFT_TABLE_SORTIERUNG = "mannschaft_sortierung";


    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<MannschaftBE> MANNSCHAFT = new BusinessEntityConfiguration<>(
            MannschaftBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM mannschaft"
                    + " ORDER BY mannschaft_id";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM mannschaft"
                    + " WHERE mannschaft_id = ?";

    private static final String FIND_ALL_BY_VEREINS_ID =
            "SELECT * "
                    + " FROM mannschaft"
                    + " WHERE mannschaft_verein_id = ?"
                    + " ORDER BY mannschaft_nummer";

    private static final String FIND_ALL_BY_VERANSTALTUNGS_ID =
            "SELECT * "
                    + " FROM mannschaft"
                    + " WHERE " + MANNSCHAFT_TABLE_EVENTID +" = ?"
                    + " ORDER BY "+MANNSCHAFT_TABLE_SORTIERUNG;

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public DsbMannschaftDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(MANNSCHAFT_TABLE_ID, MANNSCHAFT_BE_ID);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_TEAMID, MANNSCHAFT_BE_TEAMID);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_NUMBER, MANNSCHAFT_BE_NUMBER);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_SORTIERUNG, MANNSCHAFT_BE_SORTIERUNG);

        columnsToFieldsMap.put(MANNSCHAFT_TABLE_USER_ID, MANNSCHAFT_BE_USER_ID);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_EVENTID, MANNSCHAFT_BE_EVENTID);


        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all dsbmitglied entries
     */
    public List<MannschaftBE> findAll() {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL);
    }


    /**
     * Return all dsbmannschaft entries with the given vereinsId
     *
     * @param id from the verein
     * @return all dbsmannschaft entries with the given vereinsId
     */

    public List<MannschaftBE> findAllByVereinsId(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_VEREINS_ID, id);}


    /**
     * Return all dsbmannschaft entries with the given Veranstaltungs-Id
     *
     * @param id from the Veranstaltung
     * @return all dbsmannschaft entries with the given Veranstaltungs-Id
     */

    public List<MannschaftBE> findAllByVeranstaltungsId(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_VERANSTALTUNGS_ID, id);}

    /**
     * Return dsbmitglied entry with specific id
     *
     * @param id
     */
    public MannschaftBE findById(final long id) {
        return basicDao.selectSingleEntity(MANNSCHAFT, FIND_BY_ID, id);
    }


    /**
     * Create a new dsbmitglied entry
     *
     * @param mannschaftBE
     * @param currentDsbMannschaftId
     *
     * @return Business Entity corresponding to the created dsbmitglied entry
     */
    public MannschaftBE create(final MannschaftBE mannschaftBE, final long currentDsbMannschaftId) {
        basicDao.setCreationAttributes(mannschaftBE, currentDsbMannschaftId);

        return basicDao.insertEntity(MANNSCHAFT, mannschaftBE);
    }


    /**
     * Update an existing dsbmitglied entry
     *
     * @param mannschaftBE
     * @param currentDsbMannschaftId
     *
     * @return Business Entity corresponding to the updated dsbmitglied entry
     */
    public MannschaftBE update(final MannschaftBE mannschaftBE, final long currentDsbMannschaftId) {
        basicDao.setModificationAttributes(mannschaftBE, currentDsbMannschaftId);

        return basicDao.updateEntity(MANNSCHAFT, mannschaftBE, MANNSCHAFT_BE_ID);
    }


    /**
     * Delete existing mannschaft entry
     *
     * @param mannschaftBE
     * @param currentDsbMannschaftId
     */
    public void delete(final MannschaftBE mannschaftBE, final long currentDsbMannschaftId) {
        basicDao.setModificationAttributes(mannschaftBE, currentDsbMannschaftId);

        basicDao.deleteEntity(MANNSCHAFT, mannschaftBE, MANNSCHAFT_BE_ID);
    }

}
