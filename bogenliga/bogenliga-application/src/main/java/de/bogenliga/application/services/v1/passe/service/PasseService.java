package de.bogenliga.application.services.v1.passe.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.common.service.ServiceFacade;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@RestController
@CrossOrigin
@RequestMapping("v1/passe")
public class PasseService implements ServiceFacade {

    private static final String PRECONDITION_MSG_MANSCHAFTSID = "Manschaft Id must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPFID = "Wettkampf Id must not be null";
    private static final String PRECONDITION_MSG_MATCHNR = "Match Nr must not be null";
    private static final String PRECONDITION_MSG_LFDNR = "LFDNr must nt be null";
    private static final String PRECONDITION_MSG_DSBMITGLIEDNR = "DSB Mitglied Nr must not be null";
    private static final String PRECONDITION_MSG_RINGZAHl = "Ringzahl must not be null an complete";
    private static final Logger LOG = LoggerFactory.getLogger(PasseService.class);

}
