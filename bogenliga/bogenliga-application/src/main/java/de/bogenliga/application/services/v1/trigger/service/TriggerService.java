package de.bogenliga.application.services.v1.trigger.service;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.altsystem.liga.entity.AltsystemLiga;
import de.bogenliga.application.business.trigger.api.TriggerComponent;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.dao.MigrationTimestampDAO;
import de.bogenliga.application.business.trigger.impl.dao.TriggerDAO;
import de.bogenliga.application.business.trigger.impl.entity.MigrationTimestampBE;
import de.bogenliga.application.common.altsystem.AltsystemDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.services.v1.olddbimport.OldDbImport;
import de.bogenliga.application.services.v1.trigger.mapper.TriggerDTOMapper;
import de.bogenliga.application.services.v1.trigger.model.TriggerChange;
import de.bogenliga.application.services.v1.trigger.model.TriggerDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;



@RestController
@CrossOrigin
@RequestMapping("v1/trigger")

public class TriggerService implements ServiceFacade {
    private static final Map<String, Class<?>> tableNameToClass = getTableNameToClassMap();

    private final Map<Class<?>, AltsystemEntity<?>> dataObjectToEntity;

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(TriggerService.class);
    private final BasicDAO basicDao;
    private final TriggerDAO triggerDAO;
    private final TriggerComponent triggerComponent;

    private final MigrationTimestampDAO migrationTimestampDAO;

    @Autowired
    public TriggerService(final BasicDAO basicDao, final TriggerDAO triggerDAO, final AltsystemLiga altsystemLiga, final
    TriggerComponent triggerComponent, final MigrationTimestampDAO migrationTimestampDAO) {
        this.basicDao = basicDao;
        this.triggerDAO = triggerDAO;
        this.triggerComponent = triggerComponent;
        this.migrationTimestampDAO = migrationTimestampDAO;

        dataObjectToEntity = new HashMap<>();
        dataObjectToEntity.put(AltsystemLigaDO.class, altsystemLiga);

        debugSelect();
    }

    private static Map<String, Class<?>> getTableNameToClassMap() {
        Map<String, Class<?>> result = new HashMap<>();

        // TODO register other tables
        result.put("altsystem_liga", AltsystemLigaDO.class);

        return result;
    }

    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    @GetMapping("/buttonSync")
    public int startTheSync(final Principal principal) {
        final long triggeringUserId = UserProvider.getCurrentUserId(principal);

        try {
            syncData(triggeringUserId);
        } catch(Exception e) {
            LOGGER.debug("Could not sync data.", e);
            return 0;
        }
        return 1;
    }
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_STAMMDATEN)
    public List<TriggerDTO> findAll() {
        final List<TriggerDO> triggerDOList = triggerComponent.findAll();

        return triggerDOList.stream().map(TriggerDTOMapper.toDTO).collect(Collectors.toList());
    }

    public void setMigrationTimestamp(Timestamp timestamp){
        List<MigrationTimestampBE> timestamplist = migrationTimestampDAO.findAll();
        if (timestamplist.isEmpty()){
            MigrationTimestampBE newTimestamp = new MigrationTimestampBE();
            newTimestamp.setSyncTimestamp(timestamp);
            migrationTimestampDAO.create(newTimestamp);
        }
        else{
            MigrationTimestampBE timestampBE = timestamplist.get(0);
            timestampBE.setSyncTimestamp(timestamp);
            migrationTimestampDAO.update(timestampBE);
        }
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void scheduler(){
        final long userId = 0;
        syncData(userId);
    }

    public void syncData(long triggeringUserId) {
        OldDbImport.sync();
        LOGGER.debug("Computing changes");

        List<TriggerChange<?>> changes = computeAllChanges(triggeringUserId);

        for (TriggerChange<?> change : changes) {
            LOGGER.debug("Migrating {}", change.getAltsystemDataObject());
            change.tryMigration();
        }

        //Updated den Timestamp nach dem sync
        setMigrationTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    // TODO remove when not needed anymore
    private void debugSelect() {
        final var results = triggerDAO.findAll();

        LOGGER.debug("FOUND " + results.size() + " RESULTS"); // set a breakpoint here to inspect results
        for (var result : results) {
            LOGGER.debug(result.toString());
        }
    }


    /**
     * Checks the imported old database tables for changes and returns all
     * updated and newly created models.
     */
    private List<TriggerChange<?>> computeAllChanges(final long triggeringUserId) {
        List<TriggerChange<?>> changes = new ArrayList<>();

        for (String oldTableName : tableNameToClass.keySet()) {
            try {
                changes.addAll(computeChangesOfTable(oldTableName, triggeringUserId));
            } catch (TechnicalException e) {
                LOGGER.error("Failed to compute changes of table " + oldTableName, e);
            }
        }

        return changes;
    }

    private <T extends AltsystemDO> List<TriggerChange<T>> computeChangesOfTable(String oldTableName, long triggeringUserId) {
        Class<T> oldClass = (Class<T>) tableNameToClass.get(oldTableName);
        String sqlQuery = "SELECT * FROM " + oldTableName;

        final List<T> allTableObjects = basicDao.selectEntityList(new BusinessEntityConfiguration<>(
                oldClass, oldTableName, new HashMap<>(), LOGGER
        ), sqlQuery);

        List<TriggerChange<T>> changes = new ArrayList<>();
        for (T retrievedObject : allTableObjects) {
            AltsystemEntity<T> entity = (AltsystemEntity<T>) dataObjectToEntity.get(retrievedObject.getClass());

            // TODO create new row in altsystem_aenderung
            TriggerDO dataObject = new TriggerDO(1L, oldTableName, retrievedObject.getId(), TriggerChangeOperation.CREATE, TriggerChangeStatus.NEW, "keine nachricht", null, null);

            changes.add(new TriggerChange<>(dataObject, retrievedObject, entity, triggeringUserId));
        }

        return changes;
    }
}