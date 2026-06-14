package de.bogenliga.application.services.v1.wettkampf.service;

import de.bogenliga.application.business.wettkampf.api.AnzeigenComponent;
import de.bogenliga.application.common.service.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnzeigenScheduler {

        private final AnzeigenComponent anzeigenComponent;

        @Autowired
        public AnzeigenScheduler(AnzeigenComponent anzeigenComponent) {
            this.anzeigenComponent = anzeigenComponent;
        }

        @Scheduled(cron = "0 0 2 * * *") // Jede Nacht um 2 Uhr
        public void cleanUpAnzeigen() {
            // 0L = Systemuser
            anzeigenComponent.deleteAll(0L);
        }
    }
