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

}
