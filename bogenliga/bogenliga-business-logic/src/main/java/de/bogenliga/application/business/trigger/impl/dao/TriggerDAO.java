package de.bogenliga.application.business.trigger.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * TODO [AL] class documentation
 *
 * @author Maximilian Fronmueller
 */
@Repository
public class TriggerDAO implements DataAccessObject {

    //define logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(
            de.bogenliga.application.business.trigger.impl.dao.TriggerDAO.class);

    //table name in the DB
    private static final String TABLE = "altsystem_aenderung";

    //business entity parameters
    private static final String TRIGGER_BE_ID = "Id";
    private static final String TRIGGER_BE_KATEGORIE = "kategorie";
    private static final String TRIGGER_BE_ALTSYSTEMID = "altsystemId";
    private static final String TRIGGER_BE_OPERATION = "changeOperation";
    private static final String TRIGGER_BE_STATUS = "changeStatus";
    private static final String TRIGGER_BE_NACHRICHT = "nachricht";
    private static final String TRIGGER_BE_RUNATUTC = "runAtUtc";

    private static final String TRIGGER_TABLE_ID = "aenderung_id";
    private static final String TRIGGER_TABLE_KATEGORIE = "kategorie";
    private static final String TRIGGER_TABLE_ALTSYSTEMID = "altsystem_id";
    private static final String TRIGGER_TABLE_OPERATION = "operation_name";
    private static final String TRIGGER_TABLE_STATUS = "status_name";
    private static final String TRIGGER_TABLE_NACHRICHT = "nachricht";
    private static final String TRIGGER_TABLE_RUNATUTC = "run_at_utc";

    private static final BusinessEntityConfiguration<TriggerBE> TRIGGER = new BusinessEntityConfiguration<>(
            TriggerBE.class, TABLE, getColumsToFieldsMap(), LOGGER);


    /**
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM altsystem_aenderung"
                    + "     LEFT JOIN altsystem_aenderung_operation op ON altsystem_aenderung.operation = op.operation_id"
                    + "     LEFT JOIN altsystem_aenderung_status st ON altsystem_aenderung.status = st.status_id"
                    + " ORDER BY aenderung_id";


    private final BasicDAO basicDAO;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDAO to handle the commonly used DB operations
     */

    @Autowired
    public TriggerDAO(final BasicDAO basicDAO) {
        this.basicDAO = basicDAO;
    }


    private static Map<String, String> getColumsToFieldsMap() {
        final Map<String, String> columnsToFieldMap = new HashMap<>();

        columnsToFieldMap.put(TRIGGER_TABLE_ID, TRIGGER_BE_ID);
        columnsToFieldMap.put(TRIGGER_TABLE_KATEGORIE, TRIGGER_BE_KATEGORIE);
        columnsToFieldMap.put(TRIGGER_TABLE_ALTSYSTEMID, TRIGGER_BE_ALTSYSTEMID);
        columnsToFieldMap.put(TRIGGER_TABLE_OPERATION, TRIGGER_BE_OPERATION);
        columnsToFieldMap.put(TRIGGER_TABLE_STATUS, TRIGGER_BE_STATUS);
        columnsToFieldMap.put(TRIGGER_TABLE_NACHRICHT, TRIGGER_BE_NACHRICHT);
        columnsToFieldMap.put(TRIGGER_TABLE_RUNATUTC, TRIGGER_BE_RUNATUTC);

        columnsToFieldMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return columnsToFieldMap;

    }


    /**
     * Return all the 'Trigger' entries
     *
     * @return List with 'Trigger' Business Entities
     */

    public List<TriggerBE> findAll() {
        return basicDAO.selectEntityList(TRIGGER, FIND_ALL);
    }
}
