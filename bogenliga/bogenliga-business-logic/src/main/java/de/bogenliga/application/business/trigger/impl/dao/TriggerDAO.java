package de.bogenliga.application.business.trigger.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.impl.entity.RawTriggerBE;
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
    private static final String TRIGGER_BE_OPERATION_ID = "changeOperationId";
    private static final String TRIGGER_BE_STATUS_ID = "changeStatusId";
    private static final String TRIGGER_BE_NACHRICHT = "nachricht";
    private static final String TRIGGER_BE_RUNATUTC = "runAtUtc";

    private static final String TRIGGER_TABLE_ID = "aenderung_id";
    private static final String TRIGGER_TABLE_KATEGORIE = "kategorie";
    private static final String TRIGGER_TABLE_ALTSYSTEMID = "altsystem_id";
    private static final String TRIGGER_TABLE_OPERATION_NAME = "operation_name";
    private static final String TRIGGER_TABLE_STATUS_NAME = "status_name";
    private static final String TRIGGER_TABLE_OPERATION_ID = "operation";
    private static final String TRIGGER_TABLE_STATUS_ID = "status";
    private static final String TRIGGER_TABLE_NACHRICHT = "nachricht";
    private static final String TRIGGER_TABLE_RUNATUTC = "run_at_utc";

    private static final BusinessEntityConfiguration<TriggerBE> TRIGGER = new BusinessEntityConfiguration<>(
            TriggerBE.class, TABLE, getColumsToFieldsMapWithJoin(), LOGGER);

    private static final BusinessEntityConfiguration<RawTriggerBE> RAW_TRIGGER = new BusinessEntityConfiguration<>(
            RawTriggerBE.class, TABLE, getColumsToFieldsMap(), LOGGER);

    private static final String selectCount = "SELECT COUNT(*) ";

    /**
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM altsystem_aenderung"
                    + "     LEFT JOIN altsystem_aenderung_operation op"
                    + "         ON altsystem_aenderung.operation = op.operation_id"
                    + "     LEFT JOIN altsystem_aenderung_status st"
                    + "         ON altsystem_aenderung.status = st.status_id"
                    + " ORDER BY aenderung_id";

    private static final String FIND_ALL_LIMITED =
            "SELECT * "
                    + " FROM altsystem_aenderung"
                    + "     LEFT JOIN altsystem_aenderung_operation op"
                    + "         ON altsystem_aenderung.operation = op.operation_id"
                    + "     LEFT JOIN altsystem_aenderung_status st"
                    + "         ON altsystem_aenderung.status = st.status_id"
                    + "         AND st.status_name != 'SUCCESS'"
                    + " ORDER BY aenderung_id"
                    + " LIMIT 500";

    private static final String FIND_ALL_UNPROCESSED =
            "SELECT * "
                    + " FROM altsystem_aenderung"
                    + "     LEFT JOIN altsystem_aenderung_operation op"
                    + "         ON altsystem_aenderung.operation = op.operation_id"
                    + "     LEFT JOIN altsystem_aenderung_status st"
                    + "         ON altsystem_aenderung.status = st.status_id"
                    + "         AND st.status_name != 'SUCCESS'"
                    + "         where status != 4"
                    + " ORDER BY aenderung_id"
                    + " LIMIT 500";
    //TODO Alle neuen Abfragen nutzbringend einsetzen und bei Bedarf löschen
    private static final String FIND_ALL_COUNT =
            selectCount
                            + " FROM altsystem_aenderung"
                            + "     LEFT JOIN altsystem_aenderung_operation op"
                            + "         ON altsystem_aenderung.operation = op.operation_id"
                            + "     LEFT JOIN altsystem_aenderung_status st"
                            + "         ON altsystem_aenderung.status = st.status_id"
                            + " ORDER BY aenderung_id";
    private static final String FIND_UNPROCESSED_COUNT=
            selectCount
                            + " FROM altsystem_aenderung"
                            + "     LEFT JOIN altsystem_aenderung_operation op"
                            + "         ON altsystem_aenderung.operation = op.operation_id"
                            + "     LEFT JOIN altsystem_aenderung_status st"
                            + "         ON altsystem_aenderung.status = st.status_id"
                            + "         AND st.status_name != 'SUCCESS'"
                            + "         where status != 4"
                            + " LIMIT 500";
    private static final String FIND_SUCCEEDED_COUNT =
            selectCount
                            + " FROM altsystem_aenderung"
                            + "     LEFT JOIN altsystem_aenderung_operation op"
                            + "         ON altsystem_aenderung.operation = op.operation_id"
                            + "     LEFT JOIN altsystem_aenderung_status st"
                            + "         ON altsystem_aenderung.status = st.status_id"
                            + "         where status = 4";
    private static final String FIND_NEW_COUNT =
            selectCount
                            + " FROM altsystem_aenderung"
                            + "     LEFT JOIN altsystem_aenderung_operation op"
                            + "         ON altsystem_aenderung.operation = op.operation_id"
                            + "     LEFT JOIN altsystem_aenderung_status st"
                            + "         ON altsystem_aenderung.status = st.status_id"
                            + "         where status = 1";
    private static final String FIND_IN_PROGRESS_COUNT =
            selectCount
                            + " FROM altsystem_aenderung"
                            + "     LEFT JOIN altsystem_aenderung_operation op"
                            + "         ON altsystem_aenderung.operation = op.operation_id"
                            + "     LEFT JOIN altsystem_aenderung_status st"
                            + "         ON altsystem_aenderung.status = st.status_id"
                            + "         where status = 2";

    private static final String FIND_ERRORS_COUNT =
            selectCount
                            + " FROM altsystem_aenderung"
                            + "     LEFT JOIN altsystem_aenderung_operation op"
                            + "         ON altsystem_aenderung.operation = op.operation_id"
                            + "     LEFT JOIN altsystem_aenderung_status st"
                            + "         ON altsystem_aenderung.status = st.status_id"
                            + "         where status = 3";

    //TODO Bis hier hin

    private final BasicDAO basicDAO;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDAO  to handle the commonly used DB operations
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
        columnsToFieldMap.put(TRIGGER_TABLE_OPERATION_ID, TRIGGER_BE_OPERATION_ID);
        columnsToFieldMap.put(TRIGGER_TABLE_STATUS_ID, TRIGGER_BE_STATUS_ID);
        columnsToFieldMap.put(TRIGGER_TABLE_NACHRICHT, TRIGGER_BE_NACHRICHT);
        columnsToFieldMap.put(TRIGGER_TABLE_RUNATUTC, TRIGGER_BE_RUNATUTC);

        columnsToFieldMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return columnsToFieldMap;
    }

    private static Map<String, String> getColumsToFieldsMapWithJoin() {
        final Map<String, String> columnsToFieldMap = getColumsToFieldsMap();

        columnsToFieldMap.put(TRIGGER_TABLE_OPERATION_NAME, TRIGGER_BE_OPERATION);
        columnsToFieldMap.put(TRIGGER_TABLE_STATUS_NAME, TRIGGER_BE_STATUS);

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

    public List<TriggerBE> findAllUnprocessed() {
        return basicDAO.selectEntityList(TRIGGER, FIND_ALL_UNPROCESSED);
    }
    public List<TriggerBE> findAllLimited() {
        return basicDAO.selectEntityList(TRIGGER, FIND_ALL_LIMITED);
    }
    //TODO Entsprechende Methoden schreiben und anpassen nachher bei Bedarf rausnehmen was nicht benötigt wird
    public TriggerBE findAllCount(){ return basicDAO.selectSingleEntity(TRIGGER, FIND_ALL_COUNT);}
    public TriggerBE findUnprocessedCount(){
        return basicDAO.selectSingleEntity(TRIGGER, FIND_UNPROCESSED_COUNT);
    }
    public TriggerBE findSucceededCount() {
        return basicDAO.selectSingleEntity(TRIGGER, FIND_SUCCEEDED_COUNT);
    }

    public TriggerBE findNewCount() {
        return basicDAO.selectSingleEntity(TRIGGER, FIND_NEW_COUNT);
    }

    public TriggerBE findInProgressCount() {
        return basicDAO.selectSingleEntity(TRIGGER, FIND_IN_PROGRESS_COUNT);
    }

    public TriggerBE findErrorCount() {
        return basicDAO.selectSingleEntity(TRIGGER, FIND_ERRORS_COUNT);
    }
    //TODO Bis hier hin

    public TriggerBE create(TriggerBE triggerBE, Long currentUserId) {
        basicDAO.setCreationAttributes(triggerBE, currentUserId);

        RawTriggerBE rawTrigger = resolveTrigger(triggerBE);
        rawTrigger = basicDAO.insertEntity(RAW_TRIGGER, rawTrigger);
        return resolveRawTrigger(rawTrigger);
    }

    public TriggerBE update(TriggerBE triggerBE, Long currentUserId) {
        basicDAO.setModificationAttributes(triggerBE, currentUserId);

        RawTriggerBE rawTrigger = resolveTrigger(triggerBE);
        rawTrigger = basicDAO.updateEntity(RAW_TRIGGER, rawTrigger, TRIGGER_TABLE_ID);
        return resolveRawTrigger(rawTrigger);
    }


    TriggerBE resolveRawTrigger(RawTriggerBE raw) {
        TriggerBE created = new TriggerBE();
        created.setId(raw.getId());
        created.setKategorie(raw.getKategorie());
        created.setAltsystemId(raw.getAltsystemId());
        created.setChangeOperationId(raw.getChangeOperationId());
        created.setChangeStatusId(raw.getChangeStatusId());
        created.setNachricht(raw.getNachricht());
        created.setRunAtUtc(raw.getRunAtUtc());

        TriggerChangeOperation operation = TriggerChangeOperation.parse(raw.getChangeOperationId());
        TriggerChangeStatus status = TriggerChangeStatus.parse(raw.getChangeStatusId());

        created.setChangeOperation(operation);
        created.setChangeStatus(status);

        return created;
    }

    private RawTriggerBE resolveTrigger(TriggerBE raw) {
        RawTriggerBE created = new RawTriggerBE();
        created.setId(raw.getId());
        created.setKategorie(raw.getKategorie());
        created.setAltsystemId(raw.getAltsystemId());
        created.setChangeOperationId(raw.getChangeOperationId());
        created.setChangeStatusId(raw.getChangeStatusId());
        created.setNachricht(raw.getNachricht());
        created.setRunAtUtc(raw.getRunAtUtc());

        Long operationId = raw.getChangeOperation().getId();
        Long statusId = raw.getChangeStatus().getId();

        created.setChangeOperationId(operationId);
        created.setChangeStatusId(statusId);

        return created;
    }
}
