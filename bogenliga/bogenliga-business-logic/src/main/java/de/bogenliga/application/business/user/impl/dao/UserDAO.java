package de.bogenliga.application.business.user.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.user.impl.entity.UserFailedSignInAttemptsBE;
import de.bogenliga.application.business.user.impl.types.SignInResult;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the user entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
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
    private static final String USER_SIGN_IN_BE_FAILED_LOGIN_ATTEMPTS = "failedLoginAttempts";

    private static final String USER_TABLE_ID = "benutzer_id";
    private static final String USER_TABLE_EMAIL = "benutzer_email";
    private static final String USER_TABLE_SALT = "benutzer_salt";
    private static final String USER_TABLE_PASSWORD = "benutzer_password";
    private static final String USER_TABLE_FAILED_LOGIN_ATTEMPTS = "failed_login_attempts";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<UserBE> USER = new BusinessEntityConfiguration<>(
            UserBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private static final BusinessEntityConfiguration<UserFailedSignInAttemptsBE> USER_SIGN_IN = new BusinessEntityConfiguration<>(
            UserFailedSignInAttemptsBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM benutzer";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM benutzer "
                    + " WHERE benutzer_id = ?";

    private static final String FIND_BY_EMAIL =
            "SELECT * "
                    + " FROM benutzer "
                    + " WHERE benutzer_email = ?";

    // SELECT benutzer with email address
    // COUNT failed login attempts for a given interval
    // the failed login attempts will be 0, if the last login was successful
    private static final String FIND_SIGNIN_INFO_BY_EMAIL =
            "SELECT b.*, COUNT(bv.benutzer_login_verlauf_benutzer_id) AS failed_login_attempts"
                    + " FROM benutzer b"
                    + "        LEFT OUTER JOIN"
                    + "          (SELECT benutzer_login_verlauf_benutzer_id"
                    + "           FROM benutzer_login_verlauf"
                    + "           WHERE benutzer_login_verlauf_login_ergebnis = ? " // failed login result
                    + "               AND (benutzer_login_verlauf_timestamp"
                    + "                 BETWEEN (now() AT TIME ZONE 'utc')::timestamp - (interval '1s') * ? "// interval
                    + "                 AND now()::timestamp)"
                    + "               AND benutzer_login_verlauf_benutzer_id NOT IN " // <-
                    + "                 (SELECT benutzer_login_verlauf_benutzer_id"
                    + "                  FROM benutzer_login_verlauf"
                    + "                  WHERE  benutzer_login_verlauf_login_ergebnis = ? " // successful login result
                    + "                    AND (benutzer_login_verlauf_timestamp"
                    + "                      BETWEEN (now() AT TIME ZONE 'utc')::timestamp - (interval '1s') * ? " // i
                    + "                      AND now()::timestamp)"
                    + "                  ORDER BY benutzer_login_verlauf_timestamp DESC"
                    + "                  LIMIT 1)) bv"
                    + "        ON (b.benutzer_id = bv.benutzer_login_verlauf_benutzer_id)"
                    + " WHERE b.benutzer_email = ? " // email
                    + " GROUP BY b.benutzer_id,  bv.benutzer_login_verlauf_benutzer_id;";

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


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(USER_TABLE_ID, USER_BE_ID);
        columnsToFieldsMap.put(USER_TABLE_EMAIL, USER_BE_EMAIL);
        columnsToFieldsMap.put(USER_TABLE_SALT, USER_BE_SALT);
        columnsToFieldsMap.put(USER_TABLE_PASSWORD, USER_BE_PASSWORD);

        // join with the "benutzer_login_verlauf" table
        columnsToFieldsMap.put(USER_TABLE_FAILED_LOGIN_ATTEMPTS, USER_SIGN_IN_BE_FAILED_LOGIN_ATTEMPTS);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }

    public List<UserBE> findAll() {
        return basicDao.selectEntityList(USER, FIND_ALL);
    }


    public UserBE findById(final long id) {
        return basicDao.selectSingleEntity(USER, FIND_BY_ID, id);
    }


    public UserBE findByEmail(final String email) {
        return basicDao.selectSingleEntity(USER, FIND_BY_EMAIL, email);
    }


    public UserFailedSignInAttemptsBE findSignInUserInformationByEmail(final String email,
                                                                       final long timeRangeInSeconds) {
        return basicDao.selectSingleEntity(USER_SIGN_IN, FIND_SIGNIN_INFO_BY_EMAIL,
                SignInResult.LOGIN_FAILED.name(),
                timeRangeInSeconds,
                SignInResult.LOGIN_SUCCESS.name(),
                timeRangeInSeconds,
                email);
    }

    public UserBE create(final UserBE configurationBE) {
        return basicDao.insertEntity(USER, configurationBE);
    }


    public UserBE update(final UserBE configurationBE) {
        return basicDao.updateEntity(USER, configurationBE, USER_BE_ID);
    }


    public void delete(final UserBE configurationBE) {
        basicDao.deleteEntity(USER, configurationBE, USER_BE_ID);
    }


}
