package de.bogenliga.application.services.v1.wettkampf.service;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.wettkampf.mapper.WettkampfDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;


/**
 * I'm a REST resource and handle Wettkampf CRUD requests over the HTTP protocol
 *
 * @author Marvin Holm, Daniel Schott
 */
@RestController
@CrossOrigin
@RequestMapping("v1/wettkampf")
public class WettkampfService implements ServiceFacade {
    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf ID must not be negative and must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID = "Wettkampfveranstaltungsid must not be negative and must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_DATUM = "Format: YYYY-MM-DD Format must be correct,  Wettkampfdatum must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_ORT = "WettkampfOrt must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_BEGINN = "Format: HH:MM, Format must be correct, Wettkampfbeginn must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_TAG = "Must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_DISZIPLIN_ID = "Must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_TYP_ID = "Must not be null and must not be negative";

    private static final Logger LOG = LoggerFactory.getLogger(WettkampfService.class);

    private final WettkampfComponent wettkampfComponent;


    /**
     * Constructor with dependency injection
     *
     * @param wettkampfComponent to handle the database CRUD requests
     */

    @Autowired
    public WettkampfService (final WettkampfComponent wettkampfComponent){
        this.wettkampfComponent = wettkampfComponent;
    }

}
