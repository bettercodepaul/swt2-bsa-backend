package de.bogenliga.application.services.v1.trigger.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.common.service.ServiceFacade;

@RestController
@CrossOrigin
@RequestMapping("v1/trigger")

public class TriggerService implements ServiceFacade {

    private LocalDateTime syncTimestamp = null;

    @GetMapping("/ping")
    public String ping() {
        setSyncTimestamp(LocalDateTime.now());
        return syncTimestamp.toString();
    }

    private void setSyncTimestamp(LocalDateTime timestamp) {
        syncTimestamp = timestamp;
    }
    @Scheduled(fixedRate = 600000)
    public void triggerSchedule(){
        if(syncTimestamp==null){
            return;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        if ((syncTimestamp.plusMinutes(10).isBefore(currentTime))) {
            syncTimestamp = currentTime;
            System.out.println(currentTime);
            //Sync-Funktion
            //wenn Timestamp älter als  10 minuten, dann sync else return
        }
        else return;
        {
            System.out.println(syncTimestamp);

            System.out.println(currentTime);
        }
    }
}
