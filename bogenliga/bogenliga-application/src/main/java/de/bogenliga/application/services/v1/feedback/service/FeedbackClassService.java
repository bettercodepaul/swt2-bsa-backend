package de.bogenliga.application.services.v1.feedback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.user.api.UserRoleComponent;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */


@RestController
@CrossOrigin
@RequestMapping("v1/feedback")
public class FeedbackClassService implements ServiceFacade {


    private final UserRoleComponent userRoleComponent;
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackClassService.class);

    @Autowired
    public FeedbackClassService(UserRoleComponent userRoleComponent) {
        this.userRoleComponent = userRoleComponent;
    }


    @RequestMapping(value = "{feedback}", method = RequestMethod.GET)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public void sendFeedback(@PathVariable("feedback") final String feedback) {
        LOGGER.debug("Receive 'feedback' request with '{}'", feedback);
        //this.userRoleComponent.sendFeedback(feedback);
    }

}
