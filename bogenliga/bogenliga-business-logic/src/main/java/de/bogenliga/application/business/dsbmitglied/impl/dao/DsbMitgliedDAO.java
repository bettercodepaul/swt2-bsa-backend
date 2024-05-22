package de.bogenliga.application.business.dsbmitglied.impl.dao;

import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
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
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Yann Philippczyk, BettercallPaul gmbh
 */
@Repository
public class DsbMitgliedDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(DsbMitgliedDAO.class);

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
    private static final String DSBMITGLIED_BE_CLUB_NAME = "dsbMitgliedVereinName";
    private static final String DSBMITGLIED_BE_USER_ID = "dsbMitgliedUserId";
    private static final String DSBMITGLIED_BE_DATE_OF_JOINING = "dsbMitgliedBeitrittsdatum";

    private static final String DSBMITGLIED_TABLE_ID = "dsb_mitglied_id";
    private static final String DSBMITGLIED_TABLE_FORENAME = "dsb_mitglied_vorname";
    private static final String DSBMITGLIED_TABLE_SURNAME = "dsb_mitglied_nachname";
    private static final String DSBMITGLIED_TABLE_BIRTHDATE = "dsb_mitglied_geburtsdatum";
    private static final String DSBMITGLIED_TABLE_NATIONALITY = "dsb_mitglied_nationalitaet";
    private static final String DSBMITGLIED_TABLE_MEMBERNUMBER = "dsb_mitglied_mitgliedsnummer";
    private static final String DSBMITGLIED_TABLE_DATE_OF_JOINING = "dsb_mitglied_beitrittsdatum";
    private static final String DSBMITGLIED_TABLE_CLUB_ID = "dsb_mitglied_verein_id";
    private static final String DSBMITGLIED_TABLE_CLUB_NAME = "verein_name";
    private static final String DSBMITGLIED_TABLE_USER_ID = "dsb_mitglied_benutzer_id";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<DsbMitgliedBE> DSBMITGLIED = new BusinessEntityConfiguration<>(
            DsbMitgliedBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT dsb_mitglied.*, verein.verein_name "
                    + " FROM dsb_mitglied"
                    + " JOIN verein ON dsb_mitglied.dsb_mitglied_verein_id = verein.verein_id"
                    + " ORDER BY dsb_mitglied.dsb_mitglied_id";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM dsb_mitglied "
                    + " WHERE dsb_mitglied_id = ?";

    private static final String FIND_BY_USER_ID =
            "SELECT * "
                    + " FROM dsb_mitglied "
                    + " WHERE dsb_mitglied_benutzer_id = ?";

    private static final String FIND_BY_SEARCH =
            "SELECT dsb_mitglied.*, verein.verein_name "
                    + " FROM dsb_mitglied"
                    + " JOIN verein ON dsb_mitglied.dsb_mitglied_verein_id = verein.verein_id"
                    + " WHERE CONCAT(LOWER(dsb_mitglied_vorname), "
                    + " ' ', LOWER(dsb_mitglied_nachname), ' ', LOWER(dsb_mitglied_mitgliedsnummer)) LIKE LOWER(?) ";


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

    private static final String FIND_ALL_NOT_IN_TEAM_ID =
            "SELECT * FROM dsb_mitglied " +
                    "WHERE dsb_mitglied_id NOT IN (SELECT mannschaftsmitglied_dsb_mitglied_id " +
                    "FROM mannschaftsmitglied " +
                    "WHERE mannschaftsmitglied_mannschaft_id = ?) and dsb_mitglied_verein_id = ?";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public DsbMitgliedDAO(final BasicDAO basicDao) {
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
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_DATE_OF_JOINING, DSBMITGLIED_BE_DATE_OF_JOINING);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_CLUB_ID, DSBMITGLIED_BE_CLUB_ID);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_CLUB_NAME, DSBMITGLIED_BE_CLUB_NAME);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_USER_ID, DSBMITGLIED_BE_USER_ID);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * @param id from DsbMitglied
     *
     * @return boolean if dsbMitglied has a lizenz
     */
    public Boolean hasKampfrichterLizenz(final long id) {
        return (basicDao.selectSingleEntity(DSBMITGLIED, FIND_DSB_KAMPFRICHTER, id) == null) ? false : true;
    }


    /**
     * Return all dsbmitglied entries
     */
    public List<DsbMitgliedBE> findAll() {
        return basicDao.selectEntityList(DSBMITGLIED, FIND_ALL);
    }


    /**
     * @param id id of the team, in which the dsmitglied entries are used
     *
     * @return list of all dsbmitglied entries with the given id
     */
    public List<DsbMitgliedBE> findAllByTeamId(final long id) {
        return basicDao.selectEntityList(DSBMITGLIED, FIND_ALL_BY_TEAM_ID, id);
    }


    public List<DsbMitgliedBE> findAllNotInTeamId(final long id, final long vereinId) {
        return basicDao.selectEntityList(DSBMITGLIED, FIND_ALL_NOT_IN_TEAM_ID, id, vereinId);
    }


    /**
     * Return dsbmitglied entry with specific id
     *
     * @param id
     */
    public DsbMitgliedBE findById(final long id) {
        return basicDao.selectSingleEntity(DSBMITGLIED, FIND_BY_ID, id);
    }


    /**
     * Return dsbmitglied entry with specific user id
     *
     * @param id
     */
    public DsbMitgliedBE findByUserId(final long id) {
        return basicDao.selectSingleEntity(DSBMITGLIED, FIND_BY_USER_ID, id);
    }


    /**
     * @param searchTerm
     *
     * @return dsbmitglied entries which contain the search term
     */
    public List<DsbMitgliedBE> findBySearch(final String searchTerm) {
        return basicDao.selectEntityList(DSBMITGLIED, FIND_BY_SEARCH, new StringBuilder()
                .append("%")
                .append(searchTerm)
                .append("%")
                .toString()
        );
    }


    /**
     * Create a new dsbmitglied entry
     *
     * @param dsbMitgliedBE
     * @param currentDsbMitgliedId
     *
     * @return Business Entity corresponding to the created dsbmitglied entry
     */
    public DsbMitgliedBE create(final DsbMitgliedBE dsbMitgliedBE, final long currentDsbMitgliedId) {
        basicDao.setCreationAttributes(dsbMitgliedBE, currentDsbMitgliedId);

        return basicDao.insertEntity(DSBMITGLIED, dsbMitgliedBE);
    }


    /**
     * Update an existing dsbmitglied entry
     *
     * @param dsbMitgliedBE
     * @param currentDsbMitgliedId
     *
     * @return Business Entity corresponding to the updated dsbmitglied entry
     */
    public DsbMitgliedBE update(final DsbMitgliedBE dsbMitgliedBE, final long currentDsbMitgliedId) {
        basicDao.setModificationAttributes(dsbMitgliedBE, currentDsbMitgliedId);

        DsbMitgliedBE updatedDsbMitgliedBE = basicDao.updateEntity(DSBMITGLIED, dsbMitgliedBE, DSBMITGLIED_BE_ID);
        // Check if DsbMitgliedUserId is Null. If it is null then add the corresponding userId to DsbMitglied
        UserDAO userDAO = new UserDAO(basicDao);
        UserBE userBE = userDAO.findByDsbMitgliedId(updatedDsbMitgliedBE.getDsbMitgliedId());
        if (updatedDsbMitgliedBE.getDsbMitgliedUserId() == null && userBE != null) {
            updatedDsbMitgliedBE.setDsbMitgliedUserId(userBE.getUserId());
            updatedDsbMitgliedBE = basicDao.updateEntity(DSBMITGLIED, updatedDsbMitgliedBE, DSBMITGLIED_BE_ID);
        }

        return updatedDsbMitgliedBE;
    }


    /**
     * Delete existing dsbmitglied entrycreated_at_utc
     *
     * @param dsbMitgliedBE
     * @param currentDsbMitgliedId
     */
    public void delete(final DsbMitgliedBE dsbMitgliedBE, final long currentDsbMitgliedId) {
        basicDao.setModificationAttributes(dsbMitgliedBE, currentDsbMitgliedId);

        basicDao.deleteEntity(DSBMITGLIED, dsbMitgliedBE, DSBMITGLIED_BE_ID);
    }
}
