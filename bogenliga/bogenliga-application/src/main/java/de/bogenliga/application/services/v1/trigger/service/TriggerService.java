package de.bogenliga.application.services.v1.trigger.service;

import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.common.service.ServiceFacade;
import static java.time.temporal.ChronoUnit.MINUTES;

@RestController
@CrossOrigin
@RequestMapping("v1/trigger")

public class TriggerService implements ServiceFacade {

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
         @TODO Testen, was verändert wurde
         @TODO Schnittstelle Backendfunktionen
          */
    }
}
