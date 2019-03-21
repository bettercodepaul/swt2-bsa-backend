package de.bogenliga.application.business.user.impl.dao;

import de.bogenliga.application.business.user.impl.entity.UserRoleBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * DataAccessObject for the user entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class UserRoleDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleDAO.class);

    // table name in the database
    private static final String TABLE = "benutzer_rolle";
    // business entity parameter names

    private static final String USER_BE_ID = "userId";
    private static final String ROLE_BE_ID = "roleId";

    private static final String USER_TABLE_ID = "benutzer_rolle_benutzer_id";
    private static final String ROLE_TABLE_ID = "benutzer_rolle_rolle_id";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<UserRoleBE> USERROLE = new BusinessEntityConfiguration<>(
            UserRoleBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);


    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public UserRoleDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }



    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(USER_TABLE_ID, USER_BE_ID);
        columnsToFieldsMap.put(ROLE_TABLE_ID, ROLE_BE_ID);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Create a new user entry
     *
     * @param userRoleBE
     * @param currentUserId
     * @return Business Entity corresponding to the created user entry
     */
    public UserRoleBE create(final UserRoleBE userRoleBE, final long currentUserId) {
        basicDao.setCreationAttributes(userRoleBE, currentUserId );

        return basicDao.insertEntity(USERROLE, userRoleBE);
    }


    /**
     * Update an existing user entry
     *
     * @param userRoleBE
     * @param currentUserId
     * @return Business Entity corresponding to the updated user entry
     */
    public UserRoleBE update(final UserRoleBE userRoleBE, final long currentUserId) {
        basicDao.setModificationAttributes(userRoleBE, currentUserId);

        return basicDao.updateEntity(USERROLE, userRoleBE, USER_BE_ID);
    }


    /**
     * Delete existing user entry
     *
     * @param userRoleBE
     * @param currentUserId
     */
    public void delete(final UserRoleBE userRoleBE, final long currentUserId) {
        basicDao.setModificationAttributes(userRoleBE, currentUserId);

        basicDao.deleteEntity(USERROLE, userRoleBE, USER_BE_ID);
    }

    /**
     * Private constructor
     */
    private UserRoleDAO() {
        this.basicDao = null;
    }
        // empty private constructor

}
