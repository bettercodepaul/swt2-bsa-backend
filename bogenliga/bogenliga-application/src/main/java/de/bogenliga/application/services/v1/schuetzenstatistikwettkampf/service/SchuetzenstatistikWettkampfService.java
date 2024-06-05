package de.bogenliga.application.services.v1.schuetzenstatistikwettkampf.service;

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
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.SchuetzenstatistikWettkampfComponent;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types.SchuetzenstatistikWettkampftageDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.schuetzenstatistikwettkampf.mapper.SchuetzenstatistikWettkampfDTOMapper;
import de.bogenliga.application.services.v1.schuetzenstatistikwettkampf.model.SchuetzenstatistikWettkampfDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 *
 * @author Anna Baur
 * & Alessa Hackh
 */
@RestController
@CrossOrigin(origins = {"http://bogenliga.de", "https://bogenliga.de",
        "http://app.bogenliga.de", "https://app.bogenliga.de"})
@RequestMapping("v1/schuetzenstatistikwettkampf")
public class SchuetzenstatistikWettkampfService implements ServiceFacade {

    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "Veranstaltung Id must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf Id must not be negative";
    private static final String PRECONDITION_MSG_SPORTJAHR = "sportjahr must not be negative";

    private final Logger logger = LoggerFactory.getLogger(SchuetzenstatistikWettkampfService.class);

    private final SchuetzenstatistikWettkampfComponent schuetzenstatistikWettkampfComponent;



    /**
     * Constructor with dependency injection
     *
     * @param schuetzenstatistikWettkampfComponent to handle the database CRUD requests
     */
    @Autowired
    public SchuetzenstatistikWettkampfService(final SchuetzenstatistikWettkampfComponent schuetzenstatistikWettkampfComponent) {
        this.schuetzenstatistikWettkampfComponent = schuetzenstatistikWettkampfComponent;
    }


    /**
     * I return the current "schuetzenstatistik" for a "veranstaltung" entries of the database.
     *
     * @return lost of {@link SchuetzenstatistikWettkampfDTO} as JSON
     */
    @GetMapping(
            value = "byVeranstaltungAndVerein/{veranstaltungId}/{vereinId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<SchuetzenstatistikWettkampfDTO> getSchuetzenstatistikWettkampfVeranstaltung(@PathVariable("veranstaltungId") final long veranstaltungId,
                                                                                            @PathVariable("vereinId") final long vereinId) {

        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);
        logger.debug("Receive 'SchuetzenstatistikWettkampf für Veranstaltung' request with ID '{}'  and Verein-ID '{}'", veranstaltungId, vereinId);

        final List<SchuetzenstatistikWettkampftageDO> schuetzenstatistikWettkampftageDOList = schuetzenstatistikWettkampfComponent.getSchuetzenstatistikWettkampfVeranstaltung(veranstaltungId, vereinId);

        return schuetzenstatistikWettkampftageDOList.stream().map(SchuetzenstatistikWettkampfDTOMapper.toDTO).toList();
    }

    /**
     * I return the current "schuetzenstatistik" for a "wettkampftid (tag)" entries of the database.
     *
     * @return lost of {@link SchuetzenstatistikWettkampfDTO} as JSON
     */
    @GetMapping(
            value = "byWettkampfAndVerein/{wettkampfId}/{vereinId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<SchuetzenstatistikWettkampfDTO> getSchuetzenstatistikWettkampf(@PathVariable("wettkampfId") final long wettkampfId,
                                                                               @PathVariable("vereinId") final long vereinId) {

        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        logger.debug("Receive 'Schuetzenstatistik für Wettkampf' request with Wettkampf-ID '{}' and Verein-ID '{}'", wettkampfId, vereinId);

        final List<SchuetzenstatistikWettkampftageDO> schuetzenstatistikWettkampftageDOList = schuetzenstatistikWettkampfComponent.getSchuetzenstatistikWettkampf(wettkampfId, vereinId);

        return schuetzenstatistikWettkampftageDOList.stream().map(SchuetzenstatistikWettkampfDTOMapper.toDTO).toList();
    }

    @GetMapping(
            value = "bySportjahrAndVerein/{sportjahr}/{vereinId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<SchuetzenstatistikWettkampfDTO> getSchuetzenstatistikAlleLigen(@PathVariable("sportjahr") final long sportjahr,
                                                                               @PathVariable("vereinId") final long vereinId) {
        Preconditions.checkArgument(sportjahr >= 0, PRECONDITION_MSG_SPORTJAHR);
        logger.debug("Receive 'Schuetzenstatistik für Sportjahr' request with Sportjahr", sportjahr);

        final List<SchuetzenstatistikWettkampftageDO> schuetzenstatistikWettkampftageDOList = schuetzenstatistikWettkampfComponent.getSchuetzenstatistikAlleLigen(sportjahr, vereinId);

        return schuetzenstatistikWettkampftageDOList.stream().map(SchuetzenstatistikWettkampfDTOMapper.toDTO).toList();
    }

}
