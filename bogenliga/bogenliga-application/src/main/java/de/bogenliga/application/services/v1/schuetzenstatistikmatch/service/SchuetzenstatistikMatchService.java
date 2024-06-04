package de.bogenliga.application.services.v1.schuetzenstatistikmatch.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.SchuetzenstatistikMatchComponent;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.types.SchuetzenstatistikMatchDO;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.schuetzenstatistikmatch.mapper.SchuetzenstatistikMatchDTOMapper;
import de.bogenliga.application.services.v1.schuetzenstatistik.model.SchuetzenstatistikDTO;
import de.bogenliga.application.services.v1.schuetzenstatistikmatch.model.SchuetzenstatistikMatchDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * REST resource that handles schuetzenstatistik CRUD requests over the HTTP protocol
 *
 * @author Lennart Raach
 */
@RestController
@CrossOrigin(origins = {"http://bogenliga.de", "https://bogenliga.de",
                        "http://app.bogenliga.de", "https://app.bogenliga.de"})
@RequestMapping("v1/schuetzenstatistikmatch")
public class SchuetzenstatistikMatchService {
    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "Veranstaltung Id must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf Id must not be negative";

    private final Logger logger = LoggerFactory.getLogger(SchuetzenstatistikMatchService.class);

    private final SchuetzenstatistikMatchComponent schuetzenstatistikMatchComponent;
    /**
     * Constructor with dependency injection
     *
     * @param schuetzenstatistikMatchComponent to handle the database CRUD requests
     */
    @Autowired
    public SchuetzenstatistikMatchService(final SchuetzenstatistikMatchComponent schuetzenstatistikMatchComponent) {
        this.schuetzenstatistikMatchComponent = schuetzenstatistikMatchComponent;
    }


    /**
     * I return the current "schuetzenstatistikMatch" for a "veranstaltung" entries of the database.
     *
     * @return lost of {@link SchuetzenstatistikMatchDTO} as JSON
     */
    @GetMapping(
            value = "byVeranstaltungAndVerein/{veranstaltungId}/{vereinId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<SchuetzenstatistikMatchDTO> getSchuetzenstatistikMatchVeranstaltung(@PathVariable("veranstaltungId") final long veranstaltungId, @PathVariable("vereinId") final long vereinId) {

        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);
        logger.debug("Receive 'Schuetzenstatistik für Veranstaltung' request with ID '{}'  and Verein-ID '{}'", veranstaltungId, vereinId);

        final List<SchuetzenstatistikMatchDO> schuetzenstatistikMatchDOList = schuetzenstatistikMatchComponent.getSchuetzenstatistikMatchVeranstaltung(veranstaltungId, vereinId);

        return schuetzenstatistikMatchDOList.stream().map(SchuetzenstatistikMatchDTOMapper.toDTO).toList();
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
    public List<SchuetzenstatistikMatchDTO> getSchuetzenstatistikMatchWettkampf(@PathVariable("wettkampfId") final long wettkampfId, @PathVariable("vereinId") final long vereinId) {

        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        logger.debug("Receive 'Schuetzenstatistik für Wettkampf' request with Wettkampf-ID '{}' and Verein-ID '{}'", wettkampfId, vereinId);

        final List<SchuetzenstatistikMatchDO> schuetzenstatistikMatchDOList = schuetzenstatistikMatchComponent.getSchuetzenstatistikMatchWettkampf(wettkampfId, vereinId);

        return schuetzenstatistikMatchDOList.stream().map(SchuetzenstatistikMatchDTOMapper.toDTO).toList();
    }


}
