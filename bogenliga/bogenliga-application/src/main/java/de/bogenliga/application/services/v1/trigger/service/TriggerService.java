package de.bogenliga.application.services.v1.trigger.service;

import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import static java.time.temporal.ChronoUnit.MINUTES;

@RestController
@CrossOrigin
@RequestMapping("v1/trigger")

public class TriggerService implements ServiceFacade {

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
        System.out.println("Hilfe ich bin hier gefangen");
         /*
         Wer zieht die Daten aus dem alten System/ der alten Datenbank?
         @TODO Abfragen, ob Datenbank aktualisiert wurde
         @TODO Testen, was verändert wurde
         @TODO Schnittstelle Backendfunktionen
          */
    }
}
