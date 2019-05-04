package de.bogenliga.application.business.user.impl.dao;

import de.bogenliga.application.business.match.impl.dao.QueryBuilder;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.user.impl.entity.UserRoleBE;
import de.bogenliga.application.business.user.impl.entity.UserRoleExtBE;
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
import javax.management.Query;

/**
 * DataAccessObject for the user entity in the database.
 *
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

    private static final String USER_TABLE = "benutzer";

    private static final String ROLE_TABLE = "rolle";
    // business entity parameter names

    private static final String USER_BE_ID = "userId";
    private static final String ROLE_BE_ID = "roleId";
    private static final String USER_BE_EMAIL = "userEmail";
    private static final String ROLE_BE_NAME = "roleName";

    private static final String USER_TABLE_ID = "benutzer_rolle_benutzer_id";
    private static final String USER_TABLE_USER_ID = "benutzer_id";
    private static final String ROLE_TABLE_ID = "benutzer_rolle_rolle_id";
    private static final String ROLE_TABLE_ROLE_ID = "rolle_id";
    private static final String USER_TABLE_EMAIL = "benutzer_email";
    private static final String ROLE_TABLE_NAME = "rolle_name";

    private static final String USER_EMAIL_QUERY_FUNCTION = "upper";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<UserRoleExtBE> USERROLE = new BusinessEntityConfiguration<>(
            UserRoleExtBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /**
     * SQL queries
     */
    private static final String[] selectedField = {
            TABLE, USER_TABLE_ID, USER_TABLE, USER_TABLE_EMAIL, TABLE, ROLE_TABLE_ID, ROLE_TABLE, ROLE_TABLE_NAME
    };

    private static final String FIND_ALL = new QueryBuilder()
            .selectFieldsWithTableNames(selectedField)
            .from(TABLE).andFrom(USER_TABLE).andFrom(ROLE_TABLE)
            .whereEquals(TABLE, USER_TABLE_ID, USER_TABLE, USER_TABLE_USER_ID)
            .andEquals(TABLE, ROLE_TABLE_ID, ROLE_TABLE, ROLE_TABLE_ROLE_ID)
            .compose().toString();

    private static final String FIND_BY_ID = new QueryBuilder()
            .selectFieldsWithTableNames(selectedField)
            .from(TABLE).andFrom(USER_TABLE).andFrom(ROLE_TABLE)
            .whereEquals(QueryBuilder.withAlias(TABLE, USER_TABLE_ID))
            .andEquals(TABLE, USER_TABLE_ID, USER_TABLE, USER_TABLE_USER_ID)
            .andEquals(TABLE, ROLE_TABLE_ID, ROLE_TABLE, ROLE_TABLE_ROLE_ID)
            .compose().toString();

    private static final String FIND_BY_EMAIL = new QueryBuilder()
            .selectFieldsWithTableNames(selectedField)
            .from(TABLE).andFrom(USER_TABLE).andFrom(ROLE_TABLE)
            .whereEqualsRaw(
                    QueryBuilder.applyFunction(QueryBuilder.withAlias(USER_TABLE, USER_TABLE_EMAIL), USER_EMAIL_QUERY_FUNCTION),
                    QueryBuilder.applyFunction(QueryBuilder.SQL_VALUE_PLACEHOLDER, USER_EMAIL_QUERY_FUNCTION)
            )
            .andEquals(TABLE, USER_TABLE_ID, USER_TABLE, USER_TABLE_USER_ID)
            .andEquals(TABLE, ROLE_TABLE_ID, ROLE_TABLE, ROLE_TABLE_ROLE_ID)
            .compose().toString();


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
    public UserRoleExtBE findById(final long id) {

        return basicDao.selectSingleEntity(USERROLE, FIND_BY_ID, id);
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
