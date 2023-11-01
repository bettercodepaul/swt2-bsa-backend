package de.bogenliga.application.services.v1.trigger.service;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.common.service.ServiceFacade;

@RestController
@CrossOrigin
@RequestMapping("v1/trigger")

public class TriggerService implements ServiceFacade {

    private LocalDateTime lastTrigger = null;

    @GetMapping("/ping")
    public String ping() {
        lastTrigger = LocalDateTime.now();
        return lastTrigger.toString();
    }
}
