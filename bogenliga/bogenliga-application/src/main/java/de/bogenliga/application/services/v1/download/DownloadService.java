package de.bogenliga.application.services.v1.download;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.Setzliste.api.SetzlisteComponent;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;


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

    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final SetzlisteComponent setzlisteComponent;


    /**
     * Constructor with dependency injection
     */
    @Autowired
    public DownloadService(final SetzlisteComponent setzlisteComponent) {
        this.setzlisteComponent = setzlisteComponent;
    }

    /**
     * returns the Setzliste as pdf file for client download
     * <p>
     * @param wettkampfid  from GET-Request: ID for the competition
     * Usage:
     * <pre>{@code Request: GET /v1/download/pdf/setzliste?wettkampfid=x&?wettkampftag=y}</pre>
     *
     * @return PDF as InputStreamResource
     */
    @CrossOrigin(maxAge = 0)
    @RequestMapping(method = RequestMethod.GET,
            path = "pdf/setzliste",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadSetzlistePdf(@RequestParam("wettkampfid") final int wettkampfid) {
        LOG.debug("wettkampfid: " + wettkampfid);

        final byte[] fileBloB = setzlisteComponent.getPDFasByteArray(wettkampfid);
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
}
