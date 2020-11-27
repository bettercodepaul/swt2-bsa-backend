package de.bogenliga.application.services.v1.einstellungen.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.user.api.UserRoleComponent;
import de.bogenliga.application.services.v1.feedback.service.FeedbackClassService;

/**
 * TODO [AL] class documentation
 *
 * @author Lars Bahnmüller, Lars_Herbert.Bahnmueller@Student.Reutlingen-University.DE
 */

@RestController
@CrossOrigin
@RequestMapping("v1/einstellungen")
public class EinstellungenService {

    private final UserRoleComponent userRoleComponent;
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackClassService.class);

    /**
     * Create a intance of UserRoleComponent
     */
    @Autowired
    public EinstellungenService(UserRoleComponent userRoleComponent) {
        this.userRoleComponent = userRoleComponent;
    }
}
