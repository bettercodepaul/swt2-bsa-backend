package de.bogenliga.application.services.v1.ligatabelle.service;

import de.bogenliga.application.business.ligatabelle.api.LigatabelleComponent;
import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.services.v1.ligatabelle.mapper.LigatabelleDTOMapper;
import de.bogenliga.application.services.v1.ligatabelle.model.LigatabelleDTO;

import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * I'm a REST resource and handle liga CRUD requests over the HTTP protocol
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
@RestController
@CrossOrigin
@RequestMapping("v1/mannschaft")
public class LigatabelleService implements ServiceFacade {
    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "Veranstaltung Id must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf Id must not be negative";

    private final Logger logger = LoggerFactory.getLogger(LigatabelleService.class);

    private final LigatabelleComponent ligatabelleComponent;


    /**
     * Constructor with dependency injection
     *
     * @param ligatabelleComponent to handle the database CRUD requests
     */
    @Autowired
    public LigatabelleService(final LigatabelleComponent ligatabelleComponent) {
        this.ligatabelleComponent = ligatabelleComponent;
    }


    /**
     * I return the current "ligatabelle" for a "veranstaltung" entries of the database.
     *
     * @return lost of {@link LigatabelleDTO} as JSON
     */
    @GetMapping(
            value = "veranstaltung={id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LigatabelleDTO> getLigatabelleVeranstaltung(@PathVariable("id") final long id) {

        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);
        logger.debug("Receive 'Ligatabelle für Veranstaltung' request with ID '{}'", id);

        final List<LigatabelleDO> ligatabelleDOList = ligatabelleComponent.getLigatabelleVeranstaltung(id);

        return ligatabelleDOList.stream().map(LigatabelleDTOMapper.toDTO).toList();
    }

    /**
     * I return the current "ligatabelle" for a "wettkampftid (tag)" entries of the database.
     *
     * @return lost of {@link LigatabelleDTO} as JSON
     */
    @GetMapping(
            value = "wettkampf={id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LigatabelleDTO> getLigatabelleWettkampf(@PathVariable("id") final long id) {

        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        logger.debug("Receive 'Ligatabelle für Wettkampf' request with ID '{}'", id);

        final List<LigatabelleDO> ligatabelleDOList = ligatabelleComponent.getLigatabelleWettkampf(id);

        return ligatabelleDOList.stream().map(LigatabelleDTOMapper.toDTO).toList();
    }


}
