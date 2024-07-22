package de.bogenliga.application.business.trigger.impl.dao;


import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.business.trigger.impl.entity.TriggerCountBE;
@Repository
public class TriggerCountDAO implements DataAccessObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            de.bogenliga.application.business.trigger.impl.dao.TriggerCountDAO.class);
    private final BasicDAO basicDAO;
    private static final String TABLE = "altsystem_aenderung";
    private static final String TRIGGER_BE_ID = "Id";

    private static final String TRIGGER_COUNT_BE = "COUNT";
    private static final String TRIGGER_COUNT = "COUNT";
    private static final BusinessEntityConfiguration<TriggerCountBE> TRIGGER = new BusinessEntityConfiguration<>(
            TriggerCountBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);
    private static final String selectCount = "SELECT COUNT(*) AS COUNT";
    private static final String FIND_ALL_COUNT =
            selectCount
                    + " FROM altsystem_aenderung";
    private static final String FIND_UNPROCESSED_COUNT =
            selectCount
                    + " FROM altsystem_aenderung"
                    + " WHERE altsystem_aenderung.status = 4"
                    + " OR altsystem_aenderung.status = 3";

    private static final String FIND_IN_PROGRESS_COUNT =
            selectCount
                    + " FROM altsystem_aenderung"
                    + " WHERE altsystem_aenderung.status = 2";

    private static final String COUNT_ENTRIES_BY_STATUS_AND_DATEINTERVAL =
            "SELECT COUNT(*)"
                    + " FROM altsystem_aenderung"
                    + "     LEFT JOIN altsystem_aenderung_operation op"
                    + "         ON altsystem_aenderung.operation = op.operation_id"
                    + "     LEFT JOIN altsystem_aenderung_status st"
                    + "         ON altsystem_aenderung.status = st.status_id"
                    + "         where status = $status$"
                    + "         AND $dateInterval$";

    private static final String COUNT_ENTRIES_BY_ALLSTATUS_AND_DATEINTERVAL =
            "SELECT COUNT(*)"
                    + " FROM altsystem_aenderung"
                    + "     LEFT JOIN altsystem_aenderung_operation op"
                    + "         ON altsystem_aenderung.operation = op.operation_id"
                    + "     LEFT JOIN altsystem_aenderung_status st"
                    + "         ON altsystem_aenderung.status = st.status_id"
                    + "         where $dateInterval$";

    @Autowired
    public TriggerCountDAO(final BasicDAO basicDAO){
        this.basicDAO = basicDAO;

    }

    private static Map<String,String> getColumnsToFieldsMap(){
        final Map<String,String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(TRIGGER_COUNT,TRIGGER_COUNT_BE);

        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return columnsToFieldsMap;
    }

    public TriggerCountBE findAllCount(){ return basicDAO.selectSingleEntity(TRIGGER, FIND_ALL_COUNT);}
    public TriggerCountBE findUnprocessedCount(){return basicDAO.selectSingleEntity(TRIGGER, FIND_UNPROCESSED_COUNT);}
    public TriggerCountBE findInProgressCount(){return basicDAO.selectSingleEntity(TRIGGER, FIND_IN_PROGRESS_COUNT);}

    public TriggerCountBE countEntriesByStatusAndDateInterval(String dateInterval, String status) {
        switch (status) {
            case ("Neu"):
                status  = "1";
                break;
            case ("Laufend"):
                status = "2";
                break;
            case ("Fehlgeschlagen"):
                status = "3";
                break;
            case ("Erfolgreich"):
                status = "4";
                break;
            default:
                status = "5";
        }
        String actualTimestamp = changeTimestampToInterval(dateInterval);
        if(status.equals("1") || status.equals("2")|| status.equals("3")|| status.equals("4")) {
            String changedSQL = COUNT_ENTRIES_BY_STATUS_AND_DATEINTERVAL
                    .replace("$dateInterval$", actualTimestamp)
                    .replace("$status$", status);
            return basicDAO.selectSingleEntity(TRIGGER, changedSQL);
        }
        String changedSQL = COUNT_ENTRIES_BY_ALLSTATUS_AND_DATEINTERVAL
                .replace("$dateInterval$", actualTimestamp);
        return basicDAO.selectSingleEntity(TRIGGER, changedSQL);
    }




    public String changeTimestampToInterval(String timestamp) {
        Map<String, String> intervalMap = new HashMap<>();
        intervalMap.put("alle", "(altsystem_aenderung.created_at_utc <= CURRENT_DATE + INTERVAL '1 day' \n" +
                "       OR altsystem_aenderung.last_modified_at_utc <= CURRENT_DATE + INTERVAL '1 day')");
        intervalMap.put("letzter Monat", "(altsystem_aenderung.created_at_utc >= CURRENT_DATE - INTERVAL '1 MONTH' \n" +
                "       OR altsystem_aenderung.last_modified_at_utc >= CURRENT_DATE - INTERVAL '1 MONTH')");
        intervalMap.put("letzten drei Monate", "(altsystem_aenderung.created_at_utc >= CURRENT_DATE - INTERVAL '3 MONTH' \n" +
                "       OR altsystem_aenderung.last_modified_at_utc >= CURRENT_DATE - INTERVAL '3 MONTH')");
        intervalMap.put("letzten sechs Monate", "(altsystem_aenderung.created_at_utc >= CURRENT_DATE - INTERVAL '6 MONTH' \n" +
                "       OR altsystem_aenderung.last_modified_at_utc >= CURRENT_DATE - INTERVAL '6 MONTH')");
        intervalMap.put("im letzten Jahr", "(altsystem_aenderung.created_at_utc >= CURRENT_DATE - INTERVAL '12 MONTH' \n" +
                "       OR altsystem_aenderung.last_modified_at_utc >= CURRENT_DATE - INTERVAL '12 MONTH')");
        intervalMap.put("älter als ein Monat", "(altsystem_aenderung.created_at_utc <= CURRENT_DATE - INTERVAL '1 MONTH' \n" +
                "       OR altsystem_aenderung.last_modified_at_utc <= CURRENT_DATE - INTERVAL '1 MONTH')");
        intervalMap.put("älter als drei Monate", "(altsystem_aenderung.created_at_utc <= CURRENT_DATE - INTERVAL '3 MONTH' \n" +
                "       OR altsystem_aenderung.last_modified_at_utc <= CURRENT_DATE - INTERVAL '3 MONTH')");
        intervalMap.put("älter als sechs Monate", "(altsystem_aenderung.created_at_utc <= CURRENT_DATE - INTERVAL '6 MONTH' \n" +
                "       OR altsystem_aenderung.last_modified_at_utc <= CURRENT_DATE - INTERVAL '6 MONTH')");


        return intervalMap.getOrDefault(timestamp.replace("%20", " "), "");
    }

}
