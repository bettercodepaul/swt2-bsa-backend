package de.bogenliga.application.services.v1.feedback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackClassService.class);


    @RequestMapping(value = "{feedback}", method = RequestMethod.GET)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public void sendFeedback(@PathVariable("feedback") final String feedback) {
        LOGGER.debug("Receive 'feedback' request with '{}'", feedback);
    }

}
