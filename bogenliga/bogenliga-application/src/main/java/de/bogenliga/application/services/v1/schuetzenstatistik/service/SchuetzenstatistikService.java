package de.bogenliga.application.services.v1.schuetzenstatistik.service;

import de.bogenliga.application.business.schuetzenstatistik.api.SchuetzenstatistikComponent;
import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.services.v1.schuetzenstatistik.mapper.SchuetzenstatistikDTOMapper;
import de.bogenliga.application.services.v1.schuetzenstatistik.model.SchuetzenstatistikDTO;

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
import java.util.stream.Collectors;

/**
 * I'm a REST resource and handle schuetzenstatistik CRUD requests over the HTTP protocol
 */
@RestController
@CrossOrigin
@RequestMapping("v1/schuetzenstatistik")
public class SchuetzenstatistikService implements ServiceFacade {
    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "Veranstaltung Id must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf Id must not be negative";

    private final Logger logger = LoggerFactory.getLogger(SchuetzenstatistikService.class);

    private final SchuetzenstatistikComponent schuetzenstatistikComponent;



    /**
     * Constructor with dependency injection
     *
     * @param schuetzenstatistikComponent to handle the database CRUD requests
     */
    @Autowired
    public SchuetzenstatistikService(final SchuetzenstatistikComponent schuetzenstatistikComponent) {
        this.schuetzenstatistikComponent = schuetzenstatistikComponent;
    }


    /**
     * I return the current "schuetzenstatistik" for a "veranstaltung" entries of the database.
     *
     * @return lost of {@link SchuetzenstatistikDTO} as JSON
     */
    @GetMapping(
            value = "byVeranstaltungAndVerein/{veranstaltungId}/{vereinId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<SchuetzenstatistikDTO> getSchuetzenstatistikVeranstaltung(@PathVariable("veranstaltungId") final long veranstaltungId, @PathVariable("vereinId") final long vereinId) {

        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);
        logger.debug("Receive 'Schuetzenstatistik für Veranstaltung' request with ID '{}'  and Verein-ID '{}'", veranstaltungId, vereinId);

        final List<SchuetzenstatistikDO> schuetzenstatistikDOList = schuetzenstatistikComponent.getSchuetzenstatistikVeranstaltung(veranstaltungId, vereinId);

        return schuetzenstatistikDOList.stream().map(SchuetzenstatistikDTOMapper.toDTO).toList();
    }

    /**
     * I return the current "schuetzenstatistik" for a "wettkampftid (tag)" entries of the database.
     *
     * @return lost of {@link SchuetzenstatistikDTO} as JSON
     */
    @GetMapping(
            value = "byWettkampfAndVerein/{wettkampfId}/{vereinId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<SchuetzenstatistikDTO> getSchuetzenstatistikWettkampf(@PathVariable("wettkampfId") final long wettkampfId, @PathVariable("vereinId") final long vereinId) {

        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        logger.debug("Receive 'Schuetzenstatistik für Wettkampf' request with Wettkampf-ID '{}' and Verein-ID '{}'", wettkampfId, vereinId);

        final List<SchuetzenstatistikDO> schuetzenstatistikDOList = schuetzenstatistikComponent.getSchuetzenstatistikWettkampf(wettkampfId, vereinId);

        return schuetzenstatistikDOList.stream().map(SchuetzenstatistikDTOMapper.toDTO).toList();
    }


}
