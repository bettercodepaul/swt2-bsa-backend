package de.bogenliga.application.services.v1.trigger.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.altsystem.liga.entity.AltsystemLiga;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.service.ServiceFacade;
import static java.time.temporal.ChronoUnit.MINUTES;

@RestController
@CrossOrigin
@RequestMapping("v1/trigger")

public class TriggerService implements ServiceFacade {
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM bl_liga";

    private final AltsystemLiga altsystemLiga = new AltsystemLiga();

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(TriggerService.class);
    private final BasicDAO basicDao;

    public TriggerService(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    private LocalDateTime syncTimestamp = null;
/*TODO RequiresPermission so, dass nur Admin Zugriff hat*/
    @GetMapping("/ping")
    public String ping() {
        setSyncTimestamp(LocalDateTime.now());
        return "pong";
    }

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

    }

    /*@TODO RequiresPermission so, dass nur Admin per buttonSync syncen kann*/
    @GetMapping("/buttonSync")
    public void syncData() {
         /*
         Wer zieht die Daten aus dem alten System/ der alten Datenbank?
         @TODO Abfragen, ob Datenbank aktualisiert wurde
          */

        computeAllChangedModels();
    }


    /**
     * Checks the imported old database tables for changes and returns all
     * updated and newly created models.
     */
    private void computeAllChangedModels() {
        final List<AltsystemLigaDO> result = basicDao.selectEntityList(new BusinessEntityConfiguration<>(
                AltsystemLigaDO.class, "bl_liga", new HashMap<>(), LOGGER
        ), FIND_ALL);

        for (AltsystemLigaDO liga : result) {
            LOGGER.debug("LIGA NAME: " + liga.getName());
            altsystemLiga.create(liga);
        }
    }
}
