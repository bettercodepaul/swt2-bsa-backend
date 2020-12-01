package de.bogenliga.application.services.v1.einstellungen.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.einstellungen.api.EinstellungenComponent;
import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.services.v1.einstellungen.mapper.EinstellungenDTOMapper;
import de.bogenliga.application.services.v1.einstellungen.model.EinstellungenDTO;
import de.bogenliga.application.services.v1.feedback.service.FeedbackClassService;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * TODO [AL] class documentation
 *
 * @author Lars Bahnmüller, Lars_Herbert.Bahnmueller@Student.Reutlingen-University.DE
 */

@RestController
@CrossOrigin
@RequestMapping("v1/einstellungen")
public class EinstellungenService {

    private final EinstellungenComponent einstellungenComponent;
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackClassService.class);

    /**
     * Create a intance of UserRoleComponent
     */
    @Autowired
    public EinstellungenService(EinstellungenComponent einstellungenComponent) {
        this.einstellungenComponent = einstellungenComponent;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EinstellungenDTO> findAll() {
        final List<EinstellungenDO> einstellungenDOList = einstellungenComponent.findAll();
        return einstellungenDOList.stream().map(EinstellungenDTOMapper.toDTO).collect(Collectors.toList());
    }

}
