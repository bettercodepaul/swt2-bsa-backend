package de.bogenliga.application.services.v1.download;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.bogenkontrollliste.api.BogenkontrolllisteComponent;
import de.bogenliga.application.business.meldezettel.api.MeldezettelComponent;
import de.bogenliga.application.business.schusszettel.api.SchusszettelComponent;
import de.bogenliga.application.business.setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.lizenz.api.LizenzComponent;
import de.bogenliga.application.business.rueckennummern.api.RueckennummernComponent;
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

    private static final String PRECONDITION_WETTKAMPFID = "WettkampfID cannot be negative";

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
    private final RueckennummernComponent rueckennummernComponent;
    private final WettkampfComponent wettkampfComponent;


    /**
     * Constructor with dependency injection
     */
    @Autowired
    public DownloadService(final SetzlisteComponent setzlisteComponent, final LizenzComponent lizenzComponent,
                           final SchusszettelComponent schusszettelComponent,
                           final MeldezettelComponent meldezettelComponent,
                           final BogenkontrolllisteComponent bogenkontrolllisteComponent,
                           final RueckennummernComponent rueckennummernComponent,
                           final WettkampfComponent wettkampfComponent) {
        this.lizenzComponent = lizenzComponent;
        this.setzlisteComponent = setzlisteComponent;
        this.schusszettelComponent = schusszettelComponent;
        this.meldezettelComponent = meldezettelComponent;
        this.bogenkontrolllisteComponent = bogenkontrolllisteComponent;
        this.rueckennummernComponent = rueckennummernComponent;
        this.wettkampfComponent = wettkampfComponent;
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
    @GetMapping(
            path = "pdf/setzliste",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadSetzlistePdf(@RequestParam("wettkampfid") final long wettkampfid, final Principal principal) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        SetzlisteService setzlisteService = new SetzlisteService(setzlisteComponent);
        setzlisteService.generateSetzliste(wettkampfid, principal);

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
    @GetMapping(
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
     * returns the filled in Schusszettel for two matches as pdf file for client download
     * Usage:
     * <pre>{@code Request: GET pdf/schusszettel_matches/{matchId1}/{matchId2}}</pre>
     *
     * @return PDF as InputStreamResource
     */
    @CrossOrigin(maxAge = 0)
    @GetMapping(
            path = "pdf/schusszettel_matches/{matchId1}/{matchId2}",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadSchusszettelFilledPdf(@PathVariable("matchId1") Long matchId1,
                                                                      @PathVariable("matchId2") Long matchId2) {

        final byte[] fileBloB = schusszettelComponent.getFilledSchusszettelPDFasByteArray(matchId1, matchId2);

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
    @GetMapping(
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
    @GetMapping(
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
     * return the Rueckennummern of a mannschaft as pdf file for client download
     *
     * @param mannschaftid from GET-request: ID of the mannschaft
     * Usage:
     * <pre>{@code Request: GET /v1/download/pdf/rueckennummern?mannschaftid=x}</pre>
     *
     * @return pdf as InputStreamRessource
     */
    @CrossOrigin(maxAge = 0)
    @GetMapping(
                    path = "pdf/rueckennummern",
                    produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadRueckennummernPdf(@RequestParam("mannschaftid") final long mannschaftid) {


        final byte[] fileBloB = rueckennummernComponent.getMannschaftsRueckennummernPDFasByteArray(mannschaftid);

        return generateInputStream(fileBloB);
    }


    /**
     * return the Rueckennummer of one dsbMitglied as pdf file for client download
     *
     * @param mannschaftid from GET-request: ID of the mannschaft
     * @param dsbmitgliedid from GET-request: ID of the dsbmitglied
     * Usage:
     * <pre>{@code Request: GET /v1/download/pdf/rueckennummer/?mannschaftid=x&dsbmitgliedid=y}</pre>
     *
     * @return pdf as InputStreamRessource
     */
    @CrossOrigin(maxAge = 0)
    @GetMapping(
            path = "pdf/rueckennummer",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadRueckennummerPdf(@RequestParam("mannschaftid") final long mannschaftid,
                                                                 @RequestParam("dsbmitgliedid") final long dsbmitgliedid) {

        final byte[] fileBloB = rueckennummernComponent.getRueckennummerPDFasByteArray(mannschaftid,dsbmitgliedid);

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
    @GetMapping(
            path = "pdf/schuetzenlizenz/{dsbMitgliedId}/{teamId}",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public ResponseEntity<InputStreamResource> downloadLizenz(@PathVariable("dsbMitgliedId") final long dsbMitgliedID,
    @PathVariable("teamId") final long teamID) {
        LOG.debug("dsbMitgliedID: {}", dsbMitgliedID);
        LOG.debug("teamID: {}", teamID);
        final byte[] fileBloB = lizenzComponent.getLizenzPDFasByteArray(dsbMitgliedID, teamID);

        return generateInputStream(fileBloB);
    }

    /**
     * return the Lizenzen of a mannschaft as pdf file for client download
     *
     * @param mannschaftid from GET-request: ID of the mannschaft
     * Usage:
     * <pre>{@code Request: GET /v1/download/pdf/lizenzen/?mannschaftid=x}</pre>
     *
     * @return pdf as InputStreamRessource
     */
    @CrossOrigin(maxAge = 0)
    @GetMapping(
            path = "pdf/lizenzen",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadLizenzenPdf(@RequestParam("mannschaftid") final long mannschaftid) {


        final byte[] fileBloB = lizenzComponent.getMannschaftsLizenzenPDFasByteArray(mannschaftid);

        return generateInputStream(fileBloB);
    }

    /**
     * return Einzelstatistik einer manschaft an einer veranstaltung in einem jahr
     *
     * @param veranstaltungsid from Get-request:
     * @param manschaftsid from Get-request:
     * @param jahr from Get-request:
     * Usage:
     * <pre>{@code Request: GET /v1/download/pdf/Einzelstatistik/?werte=x}</pre>
     *
     * @return pdf as InputStreamRessource
    */
    @CrossOrigin(maxAge = 0)
    @GetMapping(
            path = "pdf/Einzelstatistik",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadEinzelstatistikPdf(@RequestParam("veranstaltungsid") final long veranstaltungsid,
    @RequestParam("manschaftsid") final long manschaftsid,
    @RequestParam("jahr") final int jahr)
    {

        final byte[] fileBloB = wettkampfComponent.getPDFasByteArray("Einzelstatistik",veranstaltungsid,manschaftsid,jahr);

        return generateInputStream(fileBloB);
    }


    /**
     * return Gesamtstatistik einer manschaft an einer veranstaltung in einem jahr
     *
     * @param veranstaltungsid from Get-request:
     * @param manschaftsid from Get-request:
     * @param jahr from Get-request:
     * Usage:
     * <pre>{@code Request: GET /v1/download/pdf/Gesamtstatistik/?werte=x}</pre>
     *
     * @return pdf as InputStreamRessource
     */
    @CrossOrigin(maxAge = 0)
    @GetMapping(
            path = "pdf/Gesamtstatistik",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadGesamtstatistikPdf(@RequestParam("veranstaltungsid") final long veranstaltungsid,
                                                                   @RequestParam("manschaftsid") final long manschaftsid,
                                                                   @RequestParam("jahr") final int jahr)
    {

        final byte[] fileBloB = wettkampfComponent.getPDFasByteArray("Gesamtstatistik" ,veranstaltungsid,manschaftsid,jahr);

        return generateInputStream(fileBloB);
    }

    @CrossOrigin(maxAge = 0)
    @GetMapping(
            path = "pdf/Uebersicht",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadUebersichtPdf(@RequestParam("veranstaltungsid") final long veranstaltungsid,
                                                              @RequestParam("wettkampftag") final long wettkampftag)
    {

        final byte[] fileBloB = wettkampfComponent.getUebersichtPDFasByteArray(veranstaltungsid,wettkampftag);

        return generateInputStream(fileBloB);
    }

}
