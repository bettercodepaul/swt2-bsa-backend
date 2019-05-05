package de.bogenliga.application.business.user.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.match.impl.dao.QueryBuilder;
import de.bogenliga.application.business.user.impl.entity.UserPermissionBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the user permission entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class UserPermissionDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOG = LoggerFactory.getLogger(UserPermissionDAO.class);

    // table name in the database
    private static final String TABLE = "benutzer";
    private static final String TABLE_ALIAS = "b";

    private static final String TABLE_RECHT = "recht";
    private static final String TABLE_RECHT_ALIAS = "r";
    private static final String TABLE_RECHT_TABLE_NAME = "recht_name";
    private static final String TABLE_RECHT_TABLE_ID = "recht_id";

    private static final String TABLE_ROLLE_RECHT = "rolle_recht";
    private static final String TABLE_ROLLE_RECHT_ALIAS = "rr";
    private static final String TABLE_ROLLE_RECHT_TABLE_ROLLE_RECHT_ROLLE_ID = "rolle_recht_rolle_id";
    private static final String TABLE_ROLLE_RECHT_TABLE_ROLLE_RECHT_RECHT_ID = "rolle_recht_recht_id";

    private static final String TABLE_USER_ROLLE = "benutzer_rolle";
    private static final String TABLE_USER_ROLLE_ALIAS = "br";
    private static final String TABLE_USER_ROLLE_TABLE_USER_ID = "benutzer_rolle_benutzer_id";
    private static final String TABLE_USER_ROLLE_TABLE_ROLLE_ID = "benutzer_rolle_rolle_id";

    // business entity parameter names

    private static final String USER_BE_ID = "userId";
    private static final String USER_PERMISSION_BE_PERMISSION_NAME = "permissionName";

    private static final String USER_TABLE_ID = "benutzer_id";
    private static final String USER_PERMISSION_TABLE_PERMISSION_NAME = "recht_name";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<UserPermissionBE> USER_PERMISSION = new BusinessEntityConfiguration<>(
            UserPermissionBE.class, TABLE, getColumnsToFieldsMap(), LOG);


    /**
     * SQL queries
     */
    private static final String FIND_BY_USER_ID = new QueryBuilder()
            .selectFieldsWithAliases(TABLE_ALIAS, USER_TABLE_ID, TABLE_RECHT_ALIAS, TABLE_RECHT_TABLE_NAME)
            .from(TABLE, TABLE_ALIAS)
                .joinLeft(TABLE_USER_ROLLE, TABLE_USER_ROLLE_ALIAS)
                .on(TABLE_ALIAS, USER_TABLE_ID, TABLE_USER_ROLLE_ALIAS, TABLE_USER_ROLLE_TABLE_USER_ID)
                .joinLeft(TABLE_ROLLE_RECHT, TABLE_ROLLE_RECHT_ALIAS)
                .on(TABLE_USER_ROLLE_ALIAS, TABLE_USER_ROLLE_TABLE_ROLLE_ID, TABLE_ROLLE_RECHT_ALIAS, TABLE_ROLLE_RECHT_TABLE_ROLLE_RECHT_ROLLE_ID)
                .joinLeft(TABLE_RECHT, TABLE_RECHT_ALIAS)
                .on(TABLE_RECHT_ALIAS, TABLE_RECHT_TABLE_ID, TABLE_ROLLE_RECHT_ALIAS, TABLE_ROLLE_RECHT_TABLE_ROLLE_RECHT_RECHT_ID)
            .whereEquals(USER_TABLE_ID)
            .groupBy(TABLE_RECHT_TABLE_NAME, USER_TABLE_ID)
            .orderByAsc(TABLE_RECHT_TABLE_NAME)
            .compose().toString();


    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public UserPermissionDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(USER_TABLE_ID, USER_BE_ID);
        columnsToFieldsMap.put(USER_PERMISSION_TABLE_PERMISSION_NAME, USER_PERMISSION_BE_PERMISSION_NAME);

        return columnsToFieldsMap;
    }


    public List<UserPermissionBE> findByUserId(final long userId) {
        return basicDao.selectEntityList(USER_PERMISSION, FIND_BY_USER_ID, userId);
    }


}
