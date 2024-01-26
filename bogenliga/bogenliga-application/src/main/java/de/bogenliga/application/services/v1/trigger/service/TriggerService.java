package de.bogenliga.application.services.v1.trigger.service;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
//import de.bogenliga.application.business.altsystem.ergebnisse.dataobject.AltsystemErgebnisseDO;
//import de.bogenliga.application.business.altsystem.ergebnisse.entity.AltsystemErgebnisse;
//import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
//import de.bogenliga.application.business.altsystem.liga.entity.AltsystemLiga;
//import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
//import de.bogenliga.application.business.altsystem.mannschaft.entity.AltsystemMannschaft;
//import de.bogenliga.application.business.altsystem.saison.dataobject.AltsystemSaisonDO;
//import de.bogenliga.application.business.altsystem.saison.entity.AltsystemSaison;
//import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
//import de.bogenliga.application.business.altsystem.schuetze.entity.AltsystemSchuetze;
//import de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject.AltsystemWettkampfdatenDO;
//import de.bogenliga.application.business.altsystem.wettkampfdaten.entity.AltsystemWettkampfdaten;
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

    //private final Map<Class<?>, AltsystemEntity<?>> dataObjectToEntity;

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(TriggerService.class);
    private final BasicDAO basicDao;
    private final TriggerDAO triggerDAO;
    private final TriggerComponent triggerComponent;

    private final MigrationTimestampDAO migrationTimestampDAO;

    @Autowired
    public TriggerService(final BasicDAO basicDao, final TriggerDAO triggerDAO, final TriggerComponent triggerComponent, final MigrationTimestampDAO migrationTimestampDAO
                          //final AltsystemLiga altsystemLiga,
                          //final AltsystemSaison altsystemSaison,
                          //final AltsystemMannschaft altsystemMannschaft,
                          //final AltsystemSchuetze altsystemSchuetze,
                          //final AltsystemWettkampfdaten altsystemWettkampfdaten,
                          //final AltsystemErgebnisse altsystemErgebnisse
    ) {
        this.basicDao = basicDao;
        this.triggerDAO = triggerDAO;
        this.triggerComponent = triggerComponent;
        this.migrationTimestampDAO = migrationTimestampDAO;
/*
        dataObjectToEntity = new HashMap<>();
        dataObjectToEntity.put(AltsystemLigaDO.class, altsystemLiga);
        dataObjectToEntity.put(AltsystemSaisonDO.class, altsystemSaison);
        dataObjectToEntity.put(AltsystemMannschaftDO.class, altsystemMannschaft);
        dataObjectToEntity.put(AltsystemSchuetzeDO.class, altsystemSchuetze);
        dataObjectToEntity.put(AltsystemWettkampfdatenDO.class, altsystemWettkampfdaten);
        dataObjectToEntity.put(AltsystemErgebnisseDO.class, altsystemErgebnisse);*/
    }

    private static Map<String, Class<?>> getTableNameToClassMap() {
        Map<String, Class<?>> result = new LinkedHashMap<>();

      /*  result.put("altsystem_liga", AltsystemLigaDO.class);
        result.put("altsystem_saison", AltsystemSaisonDO.class);
        result.put("altsystem_mannschaft", AltsystemMannschaftDO.class);
        result.put("altsystem_schuetze", AltsystemSchuetzeDO.class);
        result.put("altsystem_wettkampfdaten", AltsystemWettkampfdatenDO.class);
        result.put("altsystem_ergebniss", AltsystemErgebnisseDO.class);
*/
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

    public Timestamp getMigrationTimestamp() {
        List<MigrationTimestampBE> timestamplist = migrationTimestampDAO.findAll();
        if (timestamplist.isEmpty()){
            return null;
        }
        else{
            return timestamplist.get(0).getSyncTimestamp();
        }
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void scheduler(){
        final long userId = 0;
        syncData(userId);
    }

    public void syncData(long triggeringUserId) {
        LOGGER.debug("Importing tables from old database");
        OldDbImport.sync();

        Timestamp lastSync = getMigrationTimestamp();

        LOGGER.debug("Computing changes");
        List<TriggerChange<?>> changes = computeAllChanges(triggeringUserId, lastSync);
        LOGGER.debug("Computed {} changes", changes.size());

        for (TriggerChange<?> change : changes) {
            boolean success = change.tryMigration();
            LOGGER.debug("Migrated {} (Success: {})", change.getAltsystemDataObject(), success);
        }

        //Updated den Timestamp nach dem sync
        setMigrationTimestamp(new Timestamp(System.currentTimeMillis()));
    }


    /**
     * Reads all unprocessed changes from altsystem_aenderung.
     */
    private List<TriggerChange<?>> loadUnprocessedChanges() {
        List<TriggerChange<?>> changes = new ArrayList<>();

        List<TriggerDO> changeObjects = triggerComponent.findAllUnprocessed();

        for (TriggerDO triggerDO : changeObjects) {
            String oldTableName = triggerDO.getKategorie();
            Class<?> oldClass = tableNameToClass.get(oldTableName);
            String sqlQuery = "SELECT * FROM " + oldTableName + " WHERE id = ?";

            try {
                final AltsystemDO retrievedObject = (AltsystemDO) basicDao.selectSingleEntity(new BusinessEntityConfiguration<>(
                        oldClass, oldTableName, new HashMap<>(), LOGGER
                ), sqlQuery, triggerDO.getAltsystemId());

                //AltsystemEntity entity = dataObjectToEntity.get(retrievedObject.getClass());
                //changes.add(new TriggerChange<>(triggerComponent, triggerDO, retrievedObject, entity, triggerDO.getCreatedByUserId()));
            } catch (TechnicalException e) {
                LOGGER.error("Failed to load old model for " + oldTableName, e);
            }
        }

        return changes;
    }


    /**
     * Checks the imported old database tables for changes and returns all
     * updated and newly created models.
     */
    private List<TriggerChange<?>> computeAllChanges(final long triggeringUserId, Timestamp lastSync) {
        List<TriggerChange<?>> changes = loadUnprocessedChanges();

        for (String oldTableName : tableNameToClass.keySet()) {
            try {
                changes.addAll(computeChangesOfTable(oldTableName, triggeringUserId, lastSync));
            } catch (TechnicalException e) {
                LOGGER.error("Failed to compute changes of table " + oldTableName, e);
            }
        }

        return changes;
    }

    private <T extends AltsystemDO> List<TriggerChange<T>> computeChangesOfTable(String oldTableName, long triggeringUserId, Timestamp lastSync) {
        Class<T> oldClass = (Class<T>) tableNameToClass.get(oldTableName);
        String sqlQuery = "SELECT * FROM " + oldTableName;

        final List<T> allTableObjects = basicDao.selectEntityList(new BusinessEntityConfiguration<>(
                oldClass, oldTableName, AltsystemDO.technicalColumnsToFieldsMap, LOGGER
        ), sqlQuery);

        List<TriggerChange<T>> changes = new ArrayList<>();
        for (T retrievedObject : allTableObjects) {
            TriggerChangeOperation operation = determineOperationFromTimestamp(retrievedObject, lastSync);

            if (operation == null) {
                // No changes for this object (already up-to-date)
                continue;
            }

            //AltsystemEntity<T> entity = (AltsystemEntity<T>) dataObjectToEntity.get(retrievedObject.getClass());

            TriggerDO triggerDO = new TriggerDO(
                    null,
                    oldTableName,
                    retrievedObject.getId(),
                    operation,
                    TriggerChangeStatus.NEW,
                    "",
                    null,
                    null
            );
            TriggerDO createdTriggerChange = triggerComponent.create(triggerDO, triggeringUserId);

            //changes.add(new TriggerChange<>(triggerComponent, createdTriggerChange, retrievedObject, entity, triggeringUserId));
        }

        return changes;
    }

    private static TriggerChangeOperation determineOperationFromTimestamp(AltsystemDO altsystemDO, Timestamp lastSync) {
        if (lastSync == null) {
            // No data was imported, ever
            return TriggerChangeOperation.CREATE;
        }

        Timestamp createdAt = altsystemDO.getCreatedAt();
        if (createdAt.after(lastSync)) {
            // This object was newly added
            return TriggerChangeOperation.CREATE;
        }

        Timestamp updatedAt = altsystemDO.getUpdatedAt();
        if (updatedAt.after(lastSync)) {
            // This object is already imported, but has to be updated
            return TriggerChangeOperation.UPDATE;
        }

        // This object is already up-to-date
        return null;
    }
}
