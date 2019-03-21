package de.bogenliga.application.business.role.impl.dao;

import de.bogenliga.application.business.role.impl.entity.RoleBE;
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
 * DataAccessObject for the user permission entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class RoleDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOG = LoggerFactory.getLogger(RoleDAO.class);

    // table name in the database
    private static final String TABLE = "rolle";
    // business entity parameter names

    private static final String ROLE_BE_ID = "roleId";
    private static final String ROLE_NAME = "roleName";

    private static final String ROLE_TABLE_ID = "rolle_id";
    private static final String ROLE_TABLE_NAME = "rolle_name";



    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<RoleBE> ROLE = new BusinessEntityConfiguration<>(
            RoleBE.class, TABLE, getColumnsToFieldsMap(), LOG);


    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM rolle";

    private static final String FIND_BY_NAME =
            "SELECT * "
                    + " FROM rolle "
                    + " WHERE upper(rolle_name) = upper(?)";



    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public RoleDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(ROLE_TABLE_ID, ROLE_BE_ID);
        columnsToFieldsMap.put(ROLE_TABLE_NAME, ROLE_NAME);

        return columnsToFieldsMap;
    }

    /**
     * Return all role entries
     */
    public List<RoleBE> findAll() {
        return basicDao.selectEntityList(ROLE, FIND_ALL);
    }

    /**
     * Return user entry with specific email adress
     *
     * @param roleName - role name to search
     */
    public RoleBE findByName(final String roleName) {
        return basicDao.selectSingleEntity(ROLE, FIND_BY_NAME, roleName);
    }





}
