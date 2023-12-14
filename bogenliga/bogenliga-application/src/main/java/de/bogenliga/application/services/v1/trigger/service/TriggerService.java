package de.bogenliga.application.services.v1.trigger.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.common.altsystem.AltsystemDO;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import de.bogenliga.application.services.v1.trigger.model.MigrationChange;
import de.bogenliga.application.services.v1.trigger.model.MigrationChangeState;
import de.bogenliga.application.services.v1.trigger.model.MigrationChangeType;
import static java.time.temporal.ChronoUnit.MINUTES;

@RestController
@CrossOrigin
@RequestMapping("v1/trigger")

public class TriggerService implements ServiceFacade {
    private static final Map<String, Class<?>> tableNameToClass = getTableNameToClassMap();

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(TriggerService.class);
    private final BasicDAO basicDao;

    public TriggerService(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    private static Map<String, Class<?>> getTableNameToClassMap() {
        Map<String, Class<?>> result = new HashMap<>();

        // TODO register other tables
        result.put("bl_liga", AltsystemLigaDO.class);

        return result;
    }

   /* private LocalDateTime syncTimestamp = null;
    private void setSyncTimestamp(LocalDateTime timestamp) {
        syncTimestamp = timestamp;
    }

    @Scheduled(fixedRate = 600000)
    public void triggerSchedule(){
        syncTimestamp = LocalDateTime.now();
        if(syncTimestamp==null){
            return;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        long amount = MINUTES.between(syncTimestamp, currentTime);
        if (amount <= 10) {
            syncData();
        }

    }*/
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    @GetMapping("/buttonSync")
    public void syncData() {
        LOGGER.debug("Computing changes");
        List<MigrationChange<?>> changes = computeAllChanges();

        for (MigrationChange<?> change : changes) {
            LOGGER.debug("Migrating {}", change.getAltsystemDataObject().getClass().getSimpleName());
            boolean migrationSuccessful = change.tryMigration();

            LOGGER.debug("Migration successful? {}", migrationSuccessful);
        }
    }


    /**
     * Checks the imported old database tables for changes and returns all
     * updated and newly created models.
     */
    private List<MigrationChange<?>> computeAllChanges() {
        List<MigrationChange<?>> changes = new ArrayList<>();

        for (String oldTableName : tableNameToClass.keySet()) {
            changes.addAll(computeChangesOfTable(oldTableName));
        }

        return changes;
    }

    private <T extends AltsystemDO> List<MigrationChange<T>> computeChangesOfTable(String oldTableName) {
        Class<T> oldClass = (Class<T>) tableNameToClass.get(oldTableName);
        String sqlQuery = "SELECT * FROM " + oldTableName;

        final List<T> allTableObjects = basicDao.selectEntityList(new BusinessEntityConfiguration<>(
                oldClass, oldTableName, new HashMap<>(), LOGGER
        ), sqlQuery);

        List<MigrationChange<T>> changes = new ArrayList<>();
        for (T retrievedObject : allTableObjects) {
            changes.add(new MigrationChange<>(retrievedObject, MigrationChangeType.CREATE, MigrationChangeState.NEW));
        }

        return changes;
    }
}
