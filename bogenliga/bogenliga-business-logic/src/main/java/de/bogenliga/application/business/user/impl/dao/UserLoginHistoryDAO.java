package de.bogenliga.application.business.user.impl.dao;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.user.impl.entity.UserFailedSignInAttemptsBE;
import de.bogenliga.application.business.user.impl.entity.UserSignInHistoryBE;
import de.bogenliga.application.business.user.impl.types.SignInResult;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the user sign in history entity in the database.
 *
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class UserLoginHistoryDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginHistoryDAO.class);

    // table name in the database
    private static final String TABLE = "benutzer_login_verlauf";

    // business entity parameter names
    // user failed sign in attempts
    private static final String USER_BE_ID = "userId";
    private static final String USER_TABLE_ID = "benutzer_id";
    private static final String USER_SIGN_IN_BE_FAILED_LOGIN_ATTEMPTS = "failedLoginAttempts";
    private static final String USER_SIGN_IN_TABLE_FAILED_LOGIN_ATTEMPTS = "failed_login_attempts";

    // user sign in history
    private static final String USER_SIGN_IN_HISTORY_BE_USER = "signInUserId";
    private static final String USER_SIGN_IN_HISTORY_TABLE_USER = "benutzer_login_verlauf_benutzer_id";
    private static final String USER_SIGN_IN_HISTORY_BE_RESULT = "signInResult";
    private static final String USER_SIGN_IN_HISTORY_TABLE_RESULT = "benutzer_login_verlauf_login_ergebnis";
    private static final String USER_SIGN_IN_HISTORY_BE_TIMESTAMP = "signInAtUtc";
    private static final String USER_SIGN_IN_HISTORY_TABLE_TIMESTAMP = "benutzer_login_verlauf_timestamp";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<UserFailedSignInAttemptsBE> USER_SIGN_IN_ATTEMPTS = new BusinessEntityConfiguration<>(
            UserFailedSignInAttemptsBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private static final BusinessEntityConfiguration<UserSignInHistoryBE> USER_SIGN_IN = new BusinessEntityConfiguration<>(
            UserSignInHistoryBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    // SELECT benutzer by id
    // COUNT failed login attempts for a given interval
    // the failed login attempts will be 0, if the last login was successful
    private static final String FIND_SIGNIN_INFO_BY_ID =
            "SELECT b.benutzer_id, COUNT(bv.benutzer_login_verlauf_benutzer_id) AS failed_login_attempts"
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
                    + " WHERE b.benutzer_id = ? " // userId
                    + " GROUP BY b.benutzer_id,  bv.benutzer_login_verlauf_benutzer_id;";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public UserLoginHistoryDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(USER_TABLE_ID, USER_BE_ID);
        // join with the "benutzer_login_verlauf" table
        columnsToFieldsMap.put(USER_SIGN_IN_TABLE_FAILED_LOGIN_ATTEMPTS, USER_SIGN_IN_BE_FAILED_LOGIN_ATTEMPTS);

        // user sign in history
        columnsToFieldsMap.put(USER_SIGN_IN_HISTORY_TABLE_RESULT, USER_SIGN_IN_HISTORY_BE_RESULT);
        columnsToFieldsMap.put(USER_SIGN_IN_HISTORY_TABLE_TIMESTAMP, USER_SIGN_IN_HISTORY_BE_TIMESTAMP);
        columnsToFieldsMap.put(USER_SIGN_IN_HISTORY_TABLE_USER, USER_SIGN_IN_HISTORY_BE_USER);

        return columnsToFieldsMap;
    }


    public UserFailedSignInAttemptsBE findSignInUserInformationByEmail(final long userId,
                                                                       final long timeRangeInSeconds) {
        return basicDao.selectSingleEntity(USER_SIGN_IN_ATTEMPTS, FIND_SIGNIN_INFO_BY_ID,
                SignInResult.LOGIN_FAILED.name(),
                timeRangeInSeconds,
                SignInResult.LOGIN_SUCCESS.name(),
                timeRangeInSeconds,
                userId);
    }


    public UserSignInHistoryBE create(final UserSignInHistoryBE userSignInHistoryBE) {
        return basicDao.insertEntity(USER_SIGN_IN, userSignInHistoryBE);
    }

}
