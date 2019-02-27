package de.bogenliga.application.services.v1.wettkampf.service;

import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfOverviewComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfOverviewDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.wettkampf.mapper.WettkampfDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.mapper.WettkampfOverviewDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfOverviewDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


/**
 * I'm a REST resource and handle Wettkampf CRUD requests over the HTTP protocol
 *
 * @author Marvin Holm, Daniel Schott
 */
@RestController
@CrossOrigin
@RequestMapping("v1/wettkampfOverview")
public class WettkampfOverviewService implements ServiceFacade {
    private static final Logger LOG = LoggerFactory.getLogger(WettkampfOverviewService.class);

    private final WettkampfOverviewComponent wettkampfOverviewComponent;


    /**
     * Constructor with dependency injection
     *
     * @param wettkampfOverviewComponent to handle only database read requests
     */

    @Autowired
    public WettkampfOverviewService(final WettkampfOverviewComponent wettkampfOverviewComponent){
        this.wettkampfOverviewComponent = wettkampfOverviewComponent;
    }

    /**
     * getOverview-Method gives back all WettkämpfeOverview Entries.
     *
     * @return wettkampfDoList - List filled with Data Objects of WettkämpfeOverview
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WettkampfOverviewDTO> getOverview() {
        final List<WettkampfOverviewDO> wettkampfOverviewDOList = wettkampfOverviewComponent.getOverview();
        return wettkampfOverviewDOList.stream().map(WettkampfOverviewDTOMapper.toDTO).collect(Collectors.toList());
    }
}
