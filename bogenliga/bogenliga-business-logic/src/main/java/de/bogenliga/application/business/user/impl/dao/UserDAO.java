package de.bogenliga.application.business.user.impl.dao;

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
 * DataAccessObject for the user entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 */
@Repository
public class UserDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    // table name in the database
    private static final String TABLE = "t_benutzer";
    // business entity parameter names

    private static final String USER_BE_ID = "userId";
    private static final String USER_BE_EMAIL = "userEmail";
    private static final String USER_BE_SALT = "userSalt";
    private static final String USER_BE_PASSWORD = "userPassword";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<UserBE> USER = new BusinessEntityConfiguration<>(
            UserBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);
    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM t_benutzer";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM t_benutzer "
                    + " WHERE benutzer_id = ?";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao
     */
    @Autowired
    public UserDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put("benutzer_id", USER_BE_ID);
        columnsToFieldsMap.put("benutzer_email", USER_BE_EMAIL);
        columnsToFieldsMap.put("benutzer_salt", USER_BE_SALT);
        columnsToFieldsMap.put("benutzer_password", USER_BE_PASSWORD);
        return columnsToFieldsMap;
    }

    public List<UserBE> findAll() {
        return basicDao.selectEntityList(USER, FIND_ALL);
    }


    public UserBE findById(final int id) {
        return basicDao.selectSingleEntity(USER, FIND_BY_ID, id);
    }


    public UserBE create(final UserBE configurationBE) {
        return basicDao.insertEntity(USER, configurationBE);
    }


    public UserBE update(final UserBE configurationBE) {
        return basicDao.updateEntity(USER, configurationBE, USER_BE_ID, FIND_BY_ID);
    }


    public void delete(final UserBE configurationBE) {
        basicDao.deleteEntity(USER, configurationBE, USER_BE_ID);
    }
}
