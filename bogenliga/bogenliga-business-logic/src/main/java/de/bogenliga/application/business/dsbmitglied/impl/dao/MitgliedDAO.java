package de.bogenliga.application.business.dsbmitglied.impl.dao;

import de.bogenliga.application.business.dsbmitglied.impl.entity.MitgliedBE;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
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
 * DataAccessObject for the dsbmitglied entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class MitgliedDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(MitgliedDAO.class);

    // table name in the database
    private static final String TABLE = "dsb_mitglied";
    // business entity parameter names

    private static final String DSBMITGLIED_BE_ID = "dsbMitgliedId";
    private static final String DSBMITGLIED_BE_FORENAME = "dsbMitgliedVorname";
    private static final String DSBMITGLIED_BE_SURNAME = "dsbMitgliedNachname";
    private static final String DSBMITGLIED_BE_BIRTHDATE = "dsbMitgliedGeburtsdatum";
    private static final String DSBMITGLIED_BE_NATIONALITY = "dsbMitgliedNationalitaet";
    private static final String DSBMITGLIED_BE_MEMBERNUMBER = "dsbMitgliedMitgliedsnummer";
    private static final String DSBMITGLIED_BE_CLUB_ID = "dsbMitgliedVereinsId";
    private static final String DSBMITGLIED_BE_USER_ID = "dsbMitgliedUserId";

    private static final String DSBMITGLIED_TABLE_ID = "dsb_mitglied_id";
    private static final String DSBMITGLIED_TABLE_FORENAME = "dsb_mitglied_vorname";
    private static final String DSBMITGLIED_TABLE_SURNAME = "dsb_mitglied_nachname";
    private static final String DSBMITGLIED_TABLE_BIRTHDATE = "dsb_mitglied_geburtsdatum";
    private static final String DSBMITGLIED_TABLE_NATIONALITY  = "dsb_mitglied_nationalitaet";
    private static final String DSBMITGLIED_TABLE_MEMBERNUMBER = "dsb_mitglied_mitgliedsnummer";
    private static final String DSBMITGLIED_TABLE_CLUB_ID = "dsb_mitglied_verein_id";
    private static final String DSBMITGLIED_TABLE_USER_ID = "dsb_mitglied_benutzer_id";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<MitgliedBE> DSBMITGLIED = new BusinessEntityConfiguration<>(
            MitgliedBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM dsb_mitglied"
                    + " ORDER BY dsb_mitglied_id";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM dsb_mitglied "
                    + " WHERE dsb_mitglied_id = ?";

    private static final String FIND_BY_USER_ID =
            "SELECT * "
                    + " FROM dsb_mitglied "
                    + " WHERE dsb_mitglied_benutzer_id = ?";



    private static final String FIND_DSB_KAMPFRICHTER =
            "SELECT * FROM dsb_mitglied" +
                    " WHERE dsb_mitglied_id = (" +
                    " SELECT lizenz_dsb_mitglied_id" +
                    " FROM lizenz" +
                    " WHERE lizenz_dsb_mitglied_id = ?" +
                    " AND lizenz_typ = 'Kampfrichter'" +
                    " )";

    private static final String FIND_ALL_BY_TEAM_ID =
            "SELECT * FROM dsb_mitglied" +
                    " WHERE dsb_mitglied_id IN (" +
                    " SELECT mannschaftsmitglied_dsb_mitglied_id" +
                    " FROM mannschaftsmitglied" +
                    " WHERE mannschaftsmitglied_mannschaft_id = ?)";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public MitgliedDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(DSBMITGLIED_TABLE_ID, DSBMITGLIED_BE_ID);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_FORENAME, DSBMITGLIED_BE_FORENAME);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_SURNAME, DSBMITGLIED_BE_SURNAME);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_BIRTHDATE, DSBMITGLIED_BE_BIRTHDATE);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_NATIONALITY, DSBMITGLIED_BE_NATIONALITY);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_MEMBERNUMBER, DSBMITGLIED_BE_MEMBERNUMBER);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_CLUB_ID, DSBMITGLIED_BE_CLUB_ID);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_USER_ID, DSBMITGLIED_BE_USER_ID);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     *
     * @param id from DsbMitglied
     * @return boolean if dsbMitglied has a lizenz
     */
    public Boolean hasKampfrichterLizenz(final long id) {
        return (basicDao.selectSingleEntity(DSBMITGLIED, FIND_DSB_KAMPFRICHTER, id)==null) ? false : true;
    }


    /**
     * Return all dsbmitglied entries
     */
    public List<MitgliedBE> findAll() {
        return basicDao.selectEntityList(DSBMITGLIED, FIND_ALL);
    }


    /**
     *
     * @param id id of the team, in which the dsmitglied entries are used
     * @return list of all dsbmitglied entries with the given id
     */
    public List<MitgliedBE> findAllByTeamId(final long id) {
        return basicDao.selectEntityList(DSBMITGLIED, FIND_ALL_BY_TEAM_ID, id);
    }

    /**
     * Return dsbmitglied entry with specific id
     *
     * @param id
     */
    public MitgliedBE findById(final long id) {
        return basicDao.selectSingleEntity(DSBMITGLIED, FIND_BY_ID, id);
    }

    /**
     * Return dsbmitglied entry with specific user id
     *
     * @param id
     */
    public MitgliedBE findByUserId(final long id) {
        return basicDao.selectSingleEntity(DSBMITGLIED, FIND_BY_USER_ID, id);
    }


    /**
     * Create a new dsbmitglied entry
     *
     * @param mitgliedBE
     * @param currentDsbMitgliedId
     * @return Business Entity corresponding to the created dsbmitglied entry
     */
    public MitgliedBE create(final MitgliedBE mitgliedBE, final long currentDsbMitgliedId) {
        basicDao.setCreationAttributes(mitgliedBE, currentDsbMitgliedId);

        return basicDao.insertEntity(DSBMITGLIED, mitgliedBE);
    }


    /**
     * Update an existing dsbmitglied entry
     *
     * @param mitgliedBE
     * @param currentDsbMitgliedId
     * @return Business Entity corresponding to the updated dsbmitglied entry
     */
    public MitgliedBE update(final MitgliedBE mitgliedBE, final long currentDsbMitgliedId) {
        basicDao.setModificationAttributes(mitgliedBE, currentDsbMitgliedId);

        MitgliedBE updatedMitgliedBE = basicDao.updateEntity(DSBMITGLIED, mitgliedBE, DSBMITGLIED_BE_ID);
        // Check if DsbMitgliedUserId is Null. If it is null then add the corresponding userId to DsbMitglied
        if(updatedMitgliedBE.getDsbMitgliedUserId() == null) {
            UserDAO UserDAO = new UserDAO(basicDao);
            UserBE UserBE = UserDAO.findByDsbMitgliedId(updatedMitgliedBE.getDsbMitgliedId());
            updatedMitgliedBE.setDsbMitgliedUserId(UserBE.getUserId());
            updatedMitgliedBE = basicDao.updateEntity(DSBMITGLIED, updatedMitgliedBE, DSBMITGLIED_BE_ID);
        }

        return updatedMitgliedBE;
    }


    /**
     * Delete existing dsbmitglied entrycreated_at_utc
     *
     * @param mitgliedBE
     * @param currentDsbMitgliedId
     */
    public void delete(final MitgliedBE mitgliedBE, final long currentDsbMitgliedId) {
        basicDao.setModificationAttributes(mitgliedBE, currentDsbMitgliedId);

        basicDao.deleteEntity(DSBMITGLIED, mitgliedBE, DSBMITGLIED_BE_ID);
    }
}
