package de.bogenliga.application.business.trigger.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.impl.entity.TriggerChangeOperationBE;
import de.bogenliga.application.business.user.impl.entity.UserRoleBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * DataAccessObject for the trigger change operation in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@Repository
public class TriggerChangeOperationDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(TriggerChangeOperationDAO.class);

    // table name in the database
    private static final String TABLE = "altsystem_aenderung_operation";
    // business entity parameter names

    private static final String OPERATION_BE_ID = "id";
    private static final String OPERATION_BE_NAME = "name";

    private static final String OPERATION_TABLE_ID = "operation_id";
    private static final String OPERATION_TABLE_NAME = "operation_name";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<TriggerChangeOperationBE> TRIGGER_CHANGE_OPERATION = new BusinessEntityConfiguration<>(
            TriggerChangeOperationBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);


    private final BasicDAO basicDao;


    /**
     * Query for createOrUpdate userRoles
     */
    private static final String FIND_BY_NAME = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(OPERATION_TABLE_NAME)
            .compose().toString();

    private static final String FIND_BY_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(OPERATION_TABLE_ID)
            .compose().toString();

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public TriggerChangeOperationDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    public TriggerChangeOperationBE findByEnum(final TriggerChangeOperation operation) {
        return basicDao.selectSingleEntity(TRIGGER_CHANGE_OPERATION, FIND_BY_NAME, operation.name());
    }

    public TriggerChangeOperationBE findById(final Long id) {
        return basicDao.selectSingleEntity(TRIGGER_CHANGE_OPERATION, FIND_BY_ID, id);
    }

    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(OPERATION_TABLE_ID, OPERATION_BE_ID);
        columnsToFieldsMap.put(OPERATION_TABLE_NAME, OPERATION_BE_NAME);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }
}
