package de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.service;

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
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.SchuetzenstatistikLetzteJahreComponent;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.mapper.SchuetzenstatistikLetzteJahreDTOMapper;
import de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.model.SchuetzenstatistikLetzteJahreDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * @author Alessa Hackh
 */

@RestController
@CrossOrigin(origins = {"http://bogenliga.de", "https://bogenliga.de",
        "http://app.bogenliga.de", "https://app.bogenliga.de"})
@RequestMapping("v1/schuetzenstatistikletztejahre")
public class SchuetzenstatistikLetzteJahreService implements ServiceFacade {
    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "Veranstaltung Id must not be negative";
    private static final String PRECONDITION_MSG_VEREIN_ID = "Verein Id must not be negative";
    private static final String PRECONDITION_MSG_SPORTJAHR = "sportjahr must not be negative";

    private final Logger logger = LoggerFactory.getLogger(SchuetzenstatistikLetzteJahreService.class);

    private final SchuetzenstatistikLetzteJahreComponent schuetzenstatistikLetzteJahreComponent;

    /**
     * Constructor with dependency injection
     *
     * @param schuetzenstatistikLetzteJahreComponent to handle the database CRUD requests
     */
    @Autowired
    public SchuetzenstatistikLetzteJahreService(final SchuetzenstatistikLetzteJahreComponent schuetzenstatistikLetzteJahreComponent) {
        this.schuetzenstatistikLetzteJahreComponent = schuetzenstatistikLetzteJahreComponent;
    }

    @GetMapping(
            value = "bySportjahrAndVeranstaltungAndVerein/{sportjahr}/{veranstaltungId}/{vereinId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<SchuetzenstatistikLetzteJahreDTO> getSchuetzenstatistikLetzteJahre(@PathVariable("sportjahr") final long sportjahr,
                                                                                   @PathVariable("veranstaltungId") final long veranstaltungId,
                                                                                   @PathVariable("vereinId") final long vereinId)
    {
        Preconditions.checkArgument(sportjahr >= 0, PRECONDITION_MSG_SPORTJAHR);
        logger.debug("Receive 'Schuetzenstatistik für Sportjahr' request with Sportjahr", sportjahr);
        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);
        logger.debug("Receive 'Schuetzenstatistik für Veranstaltung' request with VeranstaltungId", veranstaltungId);
        Preconditions.checkArgument(vereinId >= 0, PRECONDITION_MSG_VEREIN_ID);
        logger.debug("Receive 'Schuetzenstatistik für Verein' request with VereinId", vereinId);

        final List<SchuetzenstatistikLetzteJahreDO> schuetzenstatistikLetzteJahreDOList = schuetzenstatistikLetzteJahreComponent.getSchuetzenstatistikLetzteJahre(sportjahr, veranstaltungId, vereinId);

        return schuetzenstatistikLetzteJahreDOList.stream().map(SchuetzenstatistikLetzteJahreDTOMapper.toDTO).toList();
    }
}
