package de.bogenliga.application.business.user.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedWithoutVereinsnameBE;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the user entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@Repository
public class UserDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    // table name in the database
    private static final String TABLE = "benutzer";
    // business entity parameter names

    private static final String USER_BE_ID = "userId";
    private static final String USER_BE_EMAIL = "userEmail";
    private static final String USER_BE_SALT = "userSalt";
    private static final String USER_BE_PASSWORD = "userPassword";
    private static final String USER_BE_USING2FA = "using2FA";
    private static final String USER_BE_SECRET = "secret";
    private static final String USER_BE_DSB_MITGLIED_ID = "dsbMitgliedId";

    private static final String USER_BE_ACTIVE = "active";

    private static final String USER_TABLE_ID = "benutzer_id";
    private static final String USER_TABLE_EMAIL = "benutzer_email";
    private static final String USER_TABLE_SALT = "benutzer_salt";
    private static final String USER_TABLE_PASSWORD = "benutzer_password";
    private static final String USER_TABLE_USING2FA = "benutzer_using_2fa";
    private static final String USER_TABLE_SECRET = "benutzer_secret";
    private static final String USER_TABLE_DSB_MITGLIED_ID = "benutzer_dsb_mitglied_id";

    private static final String USER_TABLE_ACTIVE = "benutzer_active";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<UserBE> USER = new BusinessEntityConfiguration<>(
            UserBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM benutzer"
                    + " WHERE benutzer_active = TRUE";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM benutzer "
                    + " WHERE benutzer_id = ?";

    private static final String FIND_BY_EMAIL =
            "SELECT * "
                    + " FROM benutzer "
                    + " WHERE upper(benutzer_email) = upper(?)"
                    + " AND benutzer_active = TRUE";

    private static final String FIND_BY_DSBMITGLIED_ID =
            "SELECT * "
                    + " FROM benutzer "
                    + " WHERE benutzer_dsb_mitglied_id = ?";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public UserDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    /**
     * Return all user entries
     */
    public List<UserBE> findAll() {
        return basicDao.selectEntityList(USER, FIND_ALL);
    }


    /**
     * Return user entry with specific id
     *
     * @param id
     */
    public UserBE findById(final long id) {
        return basicDao.selectSingleEntity(USER, FIND_BY_ID, id);
    }

    /**
     * Return user entry with specific email adress
     *
     * @param email
     */
    public UserBE findByEmail(final String email) {
        return basicDao.selectSingleEntity(USER, FIND_BY_EMAIL, email);
    }

    /**
     * Return user entry with specific dsbMitgliedId
     *
     * @param dsbMitgliedId
     */
    public UserBE findByDsbMitgliedId(final long dsbMitgliedId) {
        return basicDao.selectSingleEntity(USER, FIND_BY_DSBMITGLIED_ID, dsbMitgliedId);
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(USER_TABLE_ID, USER_BE_ID);
        columnsToFieldsMap.put(USER_TABLE_EMAIL, USER_BE_EMAIL);
        columnsToFieldsMap.put(USER_TABLE_SALT, USER_BE_SALT);
        columnsToFieldsMap.put(USER_TABLE_PASSWORD, USER_BE_PASSWORD);
        columnsToFieldsMap.put(USER_TABLE_USING2FA, USER_BE_USING2FA);
        columnsToFieldsMap.put(USER_TABLE_SECRET, USER_BE_SECRET);
        columnsToFieldsMap.put(USER_TABLE_ACTIVE, USER_BE_ACTIVE);
        columnsToFieldsMap.put(USER_TABLE_DSB_MITGLIED_ID, USER_BE_DSB_MITGLIED_ID);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Create a new user entry
     *
     * @param userBE
     * @param currentUserId
     *
     * @return Business Entity corresponding to the created user entry
     */
    public UserBE create(final UserBE userBE, final long currentUserId) {
        basicDao.setCreationAttributes(userBE, currentUserId);
        UserBE persistedUser = basicDao.insertEntity(USER, userBE);

        // Save UserId in the column dsb_mitglied_benutzer_id of entity dsb_mitglied
        DsbMitgliedDAO dsbMitgliedDAO = new DsbMitgliedDAO(basicDao);
        DsbMitgliedBE dsbMitgliedBE = dsbMitgliedDAO.findById(persistedUser.getDsbMitgliedId());
        if(dsbMitgliedBE != null) {
            dsbMitgliedBE.setDsbMitgliedUserId(persistedUser.getUserId());
            dsbMitgliedDAO.update((DsbMitgliedWithoutVereinsnameBE) dsbMitgliedBE, dsbMitgliedBE.getDsbMitgliedId());
        }

        return persistedUser;
    }


    /**
     * Update an existing user entry
     *
     * @param userBE
     * @param currentUserId
     *
     * @return Business Entity corresponding to the updated user entry
     */
    public UserBE update(final UserBE userBE, final long currentUserId) {
        basicDao.setModificationAttributes(userBE, currentUserId);

        return basicDao.updateEntity(USER, userBE, USER_BE_ID);
    }


    /**
     * Delete existing user entry
     *
     * @param userBE
     * @param currentUserId
     */
    public void delete(final UserBE userBE, final long currentUserId) {
        basicDao.setModificationAttributes(userBE, currentUserId);

        basicDao.deleteEntity(USER, userBE, USER_BE_ID);
    }
}
