package de.bogenliga.application.services.v1.trigger.service;

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
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.trigger.api.TriggerComponent;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.dao.TriggerDAO;
import de.bogenliga.application.common.altsystem.AltsystemDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.services.v1.regionen.mapper.RegionenDTOMapper;
import de.bogenliga.application.services.v1.regionen.model.RegionenDTO;
import de.bogenliga.application.services.v1.trigger.mapper.TriggerDTOMapper;
import de.bogenliga.application.services.v1.trigger.model.TriggerDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import de.bogenliga.application.services.v1.trigger.model.TriggerChange;


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

    @Autowired
    public TriggerService(final BasicDAO basicDao, final TriggerDAO triggerDAO, final LigaComponent ligaComponent, final
                          TriggerComponent triggerComponent) {
        this.basicDao = basicDao;
        this.triggerDAO = triggerDAO;
        this.triggerComponent = triggerComponent;

        dataObjectToEntity = new HashMap<>();
        dataObjectToEntity.put(AltsystemLigaDO.class, new AltsystemLiga(ligaComponent));

        debugSelect();
    }

    private static Map<String, Class<?>> getTableNameToClassMap() {
        Map<String, Class<?>> result = new HashMap<>();

        // TODO register other tables
        result.put("bl_liga", AltsystemLigaDO.class);

        return result;
    }

    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    @GetMapping("/buttonSync")
    public int startTheSync() {
        try {
            syncData();
        } catch(Exception e) {
            LOGGER.debug("Could not sync data.");
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

    public void generateTimestamp(){
        Timestamp syncDataTimestamp = new Timestamp(System.currentTimeMillis());
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void scheduler(){
        startTheSync();
    }

    public void syncData() {
        LOGGER.debug("Computing changes");
        List<TriggerChange<?>> changes = computeAllChanges();

        for (TriggerChange<?> change : changes) {
            LOGGER.debug("Migrating {}", change.getClass().getSimpleName());
            boolean migrationSuccessful = change.tryMigration();

            LOGGER.debug("Migration successful? {}", migrationSuccessful);
        }
        //Updated den Timestamp nach dem sync


       /** String sqlQuery = "INSERT INTO Migrationtimestamp";
        basicDao.insertEntity(new BusinessEntityConfiguration<Object>(sqlQuery, ))
        */
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
    private List<TriggerChange<?>> computeAllChanges() {
        List<TriggerChange<?>> changes = new ArrayList<>();

        for (String oldTableName : tableNameToClass.keySet()) {
            changes.addAll(computeChangesOfTable(oldTableName));
        }

        return changes;
    }

    private <T extends AltsystemDO> List<TriggerChange<T>> computeChangesOfTable(String oldTableName) {
        Class<T> oldClass = (Class<T>) tableNameToClass.get(oldTableName);
        String sqlQuery = "SELECT * FROM " + oldTableName;

        final List<T> allTableObjects = basicDao.selectEntityList(new BusinessEntityConfiguration<>(
                oldClass, oldTableName, new HashMap<>(), LOGGER
        ), sqlQuery);

        List<TriggerChange<T>> changes = new ArrayList<>();
        for (T retrievedObject : allTableObjects) {
            AltsystemEntity<T> entity = (AltsystemEntity<T>) dataObjectToEntity.get(retrievedObject.getClass());

            // TODO create new row in altsystem_aenderung
            TriggerDO dataObject = new TriggerDO(1L, oldTableName, retrievedObject.getId(), TriggerChangeOperation.CREATE, TriggerChangeStatus.NEW, "keine nachricht", null);

            changes.add(new TriggerChange<>(dataObject, retrievedObject, entity));
        }

        return changes;
    }
}
