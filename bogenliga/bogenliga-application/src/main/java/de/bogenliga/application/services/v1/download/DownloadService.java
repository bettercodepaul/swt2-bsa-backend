package de.bogenliga.application.services.v1.download;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.Bogenkontrollliste.api.BogenkontrolllisteComponent;
import de.bogenliga.application.business.Meldezettel.api.MeldezettelComponent;
import de.bogenliga.application.business.Schusszettel.api.SchusszettelComponent;
import de.bogenliga.application.business.Setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.lizenz.api.LizenzComponent;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.setzliste.service.SetzlisteService;


/**
 * I´m a REST resource and handle download requests over the HTTP protocol.
 *
 * @author Dennis Eitle, Michael Hesse
 * @see <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">Wikipedia - CRUD</a>
 * @see <a href="https://en.wikipedia.org/wiki/Representational_state_transfer">Wikipedia - REST</a>
 * @see <a href="https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">Wikipedia - HTTP</a>
 * @see <a href="https://en.wikipedia.org/wiki/Design_by_contract">Wikipedia - Design by contract</a>
 * @see <a href="https://spring.io/guides/gs/actuator-service/">
 * Building a RESTful Web Service with Spring Boot Actuator</a>
 * @see <a href="https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-dsbMitglied">
 * Build a REST API with Spring 4 and Java Config</a>
 * @see <a href="https://www.baeldung.com/spring-autowire">Guide to Spring @Autowired</a>
 */
@RestController
@CrossOrigin(maxAge = 0)
@RequestMapping("v1/download/")
public class DownloadService implements ServiceFacade {

    private static final Logger LOG = LoggerFactory.getLogger(DownloadService.class);

    private final String PRECONDITION_WETTKAMPFID = "WettkampfID cannot be negative";

    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final SetzlisteComponent setzlisteComponent;
    private final SchusszettelComponent schusszettelComponent;
    private final LizenzComponent lizenzComponent;
    private final MeldezettelComponent meldezettelComponent;
    private final BogenkontrolllisteComponent bogenkontrolllisteComponent;


    /**
     * Constructor with dependency injection
     */
    @Autowired
    public DownloadService(final SetzlisteComponent setzlisteComponent, final LizenzComponent lizenzComponent,
                           final SchusszettelComponent schusszettelComponent,
                           final MeldezettelComponent meldezettelComponent,
                           final BogenkontrolllisteComponent bogenkontrolllisteComponent) {
        this.lizenzComponent = lizenzComponent;
        this.setzlisteComponent = setzlisteComponent;
        this.schusszettelComponent = schusszettelComponent;
        this.meldezettelComponent = meldezettelComponent;
        this.bogenkontrolllisteComponent = bogenkontrolllisteComponent;
    }
  
    /**
     * returns the Setzliste as pdf file for client download
     * <p>
     * @param wettkampfid  from GET-Request: ID for the competition
     * Usage:
     * <pre>{@code Request: GET /v1/download/pdf/setzliste?wettkampfid=x}</pre>
     *
     * @return PDF as InputStreamResource
     */
    @CrossOrigin(maxAge = 0)
    @RequestMapping(method = RequestMethod.GET,
            path = "pdf/setzliste",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadSetzlistePdf(@RequestParam("wettkampfid") final long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        SetzlisteService setzlisteService = new SetzlisteService(setzlisteComponent);
        setzlisteService.generateSetzliste(wettkampfid);

        final byte[] fileBloB = setzlisteComponent.getPDFasByteArray(wettkampfid);

        return generateInputStream(fileBloB);
    }

    /**
     * returns the Schusszettel as pdf file for client download
     * <p>
     * @param wettkampfid  from GET-Request: ID for the competition
     * Usage:
     * <pre>{@code Request: GET /v1/download/pdf/schusszettel?wettkampfid=x}</pre>
     *
     * @return PDF as InputStreamResource
     */
    @CrossOrigin(maxAge = 0)
    @RequestMapping(method = RequestMethod.GET,
            path = "pdf/schusszettel",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadSchusszettelPdf(@RequestParam("wettkampfid") final long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        final byte[] fileBloB = schusszettelComponent.getAllSchusszettelPDFasByteArray(wettkampfid);

        return generateInputStream(fileBloB);
    }

    /**
     * returns the Meldezettel as pdf file for client download
     * <p>
     * @param wettkampfid  from GET-Request: ID for the competition
     * Usage:
     * <pre>{@code Request: GET /v1/download/pdf/meldezettel?wettkampfid=x}</pre>
     *
     * @return PDF as InputStreamResource
     */
    @CrossOrigin(maxAge = 0)
    @RequestMapping(method = RequestMethod.GET,
            path = "pdf/meldezettel",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadMeldezettelPdf(@RequestParam("wettkampfid") final long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        final byte[] fileBloB = meldezettelComponent.getMeldezettelPDFasByteArray(wettkampfid);

        return generateInputStream(fileBloB);
    }

    /**
     * returns the bogenkontrollliste as pdf file for client download
     * <p>
     * @param wettkampfid  from GET-Request: ID for the competition
     * Usage:
     * <pre>{@code Request: GET /v1/download/pdf/bogenkontrollliste?wettkampfid=x}</pre>
     *
     * @return PDF as InputStreamResource
     */
    @CrossOrigin(maxAge = 0)
    @RequestMapping(method = RequestMethod.GET,
            path = "pdf/bogenkontrollliste",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadbogenkontrolllistePdf(@RequestParam("wettkampfid") final long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        final byte[] fileBloB = bogenkontrolllisteComponent.getBogenkontrolllistePDFasByteArray(wettkampfid);

        return generateInputStream(fileBloB);
    }

    /**
     * generates a pdf file as InputStreamResource from fileBloB
     * @param fileBloB bytearray from file that the pdf is based on
     *
     * @return PDF as InputStreamResource
     */
    private ResponseEntity<InputStreamResource> generateInputStream(byte[] fileBloB) {
        final Resource resource = new InputStreamResource(new ByteArrayInputStream(fileBloB));
        try {
            InputStream is = new ByteArrayInputStream(fileBloB);
            long r = resource.contentLength();
            return ResponseEntity.ok()
                    .contentLength(r)
                    .body(new InputStreamResource(is));
        } catch (final IOException e) {
            LOG.error("Error: ", e);
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "PDF download failed", e);
        }
    }
    @CrossOrigin(maxAge = 0)
    @RequestMapping(method = RequestMethod.GET,
            path = "pdf/schuetzenlizenz/{dsbMitgliedId}/{teamId}",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public ResponseEntity<InputStreamResource> downloadLizenz(@PathVariable("dsbMitgliedId") final long dsbMitgliedID,
    @PathVariable("teamId") final long teamID) {
        LOG.debug("dsbMitgliedID: " + dsbMitgliedID);
        LOG.debug("teamID: " + teamID);
        final byte[] fileBloB = lizenzComponent.getLizenzPDFasByteArray(dsbMitgliedID, teamID);

        return generateInputStream(fileBloB);
    }
}
