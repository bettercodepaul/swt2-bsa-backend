package de.bogenliga.application.business.user.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.user.impl.entity.UserRoleExtBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the user entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class UserRoleExtDAO extends UserRoleDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleExtDAO.class);

    // table name in the database
    private static final String TABLE = "benutzer_rolle";
    // business entity parameter names

    private static final String USER_BE_ID = "userId";
    private static final String ROLE_BE_ID = "roleId";
    private static final String USER_BE_EMAIL = "userEmail";
    private static final String ROLE_BE_NAME = "roleName";
    private static final String USER_BE_ACTIVE = "active";

    private static final String USER_TABLE_ID = "benutzer_rolle_benutzer_id";
    private static final String ROLE_TABLE_ID = "benutzer_rolle_rolle_id";
    private static final String USER_TABLE_EMAIL = "benutzer_email";
    private static final String ROLE_TABLE_NAME = "rolle_name";
    private static final String USER_TABLE_ACTIVE = "benutzer_active";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<UserRoleExtBE> USERROLE = new BusinessEntityConfiguration<>(
            UserRoleExtBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT benutzer_rolle.benutzer_rolle_benutzer_id, benutzer.benutzer_email, benutzer.benutzer_active, "
                    + " benutzer_rolle.benutzer_rolle_rolle_id, rolle.rolle_name "
                    + " FROM benutzer_rolle, benutzer, rolle "
                    + " WHERE benutzer_rolle.benutzer_rolle_benutzer_id = benutzer.benutzer_id "
                    + " AND benutzer_rolle.benutzer_rolle_rolle_id = rolle.rolle_id "
                    + " AND benutzer.benutzer_active = TRUE";

    private static final String FIND_BY_ID =
            "SELECT benutzer_rolle.benutzer_rolle_benutzer_id, benutzer.benutzer_email, benutzer.benutzer_active, "
                    + " benutzer_rolle.benutzer_rolle_rolle_id, rolle.rolle_name "
                    + " FROM benutzer_rolle, benutzer, rolle "
                    + " WHERE benutzer_rolle.benutzer_rolle_benutzer_id = ? "
                    + " AND benutzer_rolle.benutzer_rolle_benutzer_id = benutzer.benutzer_id "
                    + " AND benutzer_rolle.benutzer_rolle_rolle_id = rolle.rolle_id";


    private static final String FIND_BY_EMAIL =
            "SELECT benutzer_rolle.benutzer_rolle_benutzer_id, benutzer.benutzer_email, benutzer.benutzer_active, "
                    + " benutzer_rolle.benutzer_rolle_rolle_id, rolle.rolle_name "
                    + " FROM benutzer_rolle, benutzer, rolle "
                    + " WHERE upper(benutzer.benutzer_email) = upper(?) "
                    + " AND benutzer_rolle.benutzer_rolle_benutzer_id = benutzer.benutzer_id "
                    + " AND benutzer_rolle.benutzer_rolle_rolle_id = rolle.rolle_id"
                    + " AND benutzer.benutzer_active = TRUE";


    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public UserRoleExtDAO(final BasicDAO basicDao) {

        super(basicDao);
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(USER_TABLE_ID, USER_BE_ID);
        columnsToFieldsMap.put(ROLE_TABLE_ID, ROLE_BE_ID);
        columnsToFieldsMap.put(USER_TABLE_EMAIL, USER_BE_EMAIL);
        columnsToFieldsMap.put(ROLE_TABLE_NAME, ROLE_BE_NAME);
        columnsToFieldsMap.put(USER_TABLE_ACTIVE, USER_BE_ACTIVE);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all user entries
     */
    public List<UserRoleExtBE> findAll() {

        return basicDao.selectEntityList(USERROLE, FIND_ALL);
    }


    /**
     * Return user entry with specific id
     *
     * @param id - User Id the Role is to be searched for
     */
    public List<UserRoleExtBE> findById(final long id) {

        return basicDao.selectEntityList(USERROLE, FIND_BY_ID, id);
    }


    /**
     * Return user entry with specific email address
     *
     * @param email the role is to be searched for
     */
    public UserRoleExtBE findByEmail(final String email) {

        return basicDao.selectSingleEntity(USERROLE, FIND_BY_EMAIL, email);
    }


}
