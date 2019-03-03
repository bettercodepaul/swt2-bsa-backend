package de.bogenliga.application.services.v1.download;

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
 * @author Dennis Eitle
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
     * Provide Setzliste PDF
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "pdf/setzliste",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public ResponseEntity<InputStreamResource> downloadSetzlistePdf() {
        LOG.debug("Download static PDF file...");
        //TODO: Aktuell werden Parameter noch nicht übergeben, da die Wettkampftabelle im Frontend noch statisch ist. Sobald das erledigt ist, können Parameter aus dem Frontend wie bei "getTableByVarsV2()" übergeben werden
        final String fileName = setzlisteComponent.getTable(30, 1);
        final Resource resource = new ClassPathResource(fileName);
        long r = 0;
        InputStream is = null;
        try {
            is = resource.getInputStream();
            r = resource.contentLength();
            return ResponseEntity.ok()
                    .contentLength(r)
                    .body(new InputStreamResource(is));
        } catch (final IOException e) {
            LOG.error("Error: ", e);
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "PDF download failed", e);
        }
    }


    /**
     * I return the setzliste pdf.
     * <p>
     * Usage:
     * <pre>{@code Request: GET /v1/setzliste/app.bogenliga.frontend.autorefresh.active}</pre>
     * <pre>{@code Response:
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  }
     * }
     * </pre>
     *
     * @return application/pdf
     */
    @CrossOrigin(maxAge = 0)
    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public @ResponseBody
    ResponseEntity<InputStreamResource> getTableByVarsV2(@RequestParam("wettkampfid") final int wettkampfid,
                                                         @RequestParam("wettkampftag") final int wettkampftag) {
        LOG.debug("wettkampfid: " + wettkampfid);
        LOG.debug("wettkampftag: " + wettkampftag);
        final String fileName = setzlisteComponent.getTable(wettkampfid, wettkampftag);
        final Resource resource = new ClassPathResource(fileName);
        long r = 0;
        InputStream is = null;

        try {
            is = resource.getInputStream();
            r = resource.contentLength();
        } catch (final IOException e) {
            LOG.error("Error: ", e);

        }

        return ResponseEntity.ok().contentLength(r).cacheControl(CacheControl.noCache())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new InputStreamResource(is));
    }
}
