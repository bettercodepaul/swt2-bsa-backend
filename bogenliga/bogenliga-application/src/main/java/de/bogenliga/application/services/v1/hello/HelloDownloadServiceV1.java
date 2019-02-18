package de.bogenliga.application.services.v1.hello;

import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * Dummy file download service
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @deprecated Remove REST service version dummy
 */
@Deprecated
@RestController
@CrossOrigin
@RequestMapping("/v1/hello-world/download")
public class HelloDownloadServiceV1 implements ServiceFacade {

    private static final Logger LOG = LoggerFactory.getLogger(HelloDownloadServiceV1.class);


    /**
     * Provide PDF
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "pdf",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public ResponseEntity<InputStreamResource> downloadPdf() {
        LOG.debug("Download static PDF file...");
        final Resource resource = new ClassPathResource("SWT2_Beispiel.pdf");
        long r = 0;
        InputStream is = null;

        try {
            is = resource.getInputStream();
            r = resource.contentLength();

            return ResponseEntity.ok()
                    .contentLength(r)
                    .body(new InputStreamResource(is));
        } catch (final IOException e) {
            LOG.error("PDF download failed", e);
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "PDF download failed", e);
        }
    }


    /**
     * Provide DOC
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "doc",
            produces = "application/msword")
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public ResponseEntity<InputStreamResource> downloadWord() {
        LOG.debug("Download static WORD file...");
        final Resource resource = new ClassPathResource("SWT2_Beispiel.doc");
        long r = 0;
        InputStream is = null;

        try {
            is = resource.getInputStream();
            r = resource.contentLength();

            return ResponseEntity.ok()
                    .contentLength(r)
                    .body(new InputStreamResource(is));
        } catch (final IOException e) {
            LOG.error("WORD download failed", e);
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "WORD download failed", e);
        }
    }


    /**
     * Provide XLS
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "xls",
            produces = "application/vnd.ms-excel")
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public ResponseEntity<InputStreamResource> downloadExcel() {
        LOG.debug("Download static EXCEL file...");
        final Resource resource = new ClassPathResource("SWT2_Beispiel.xls");
        long r = 0;
        InputStream is = null;

        try {
            is = resource.getInputStream();
            r = resource.contentLength();

            return ResponseEntity.ok()
                    .contentLength(r)
                    .body(new InputStreamResource(is));
        } catch (final IOException e) {
            LOG.error("EXCEL download failed", e);
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "EXCEL download failed", e);
        }
    }


    /**
     * Provide CSV
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "csv",
            produces = MediaType.TEXT_PLAIN_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public ResponseEntity<InputStreamResource> downloadCSV() {
        LOG.debug("Download static CSV file...");
        final Resource resource = new ClassPathResource("SWT2_Beispiel.csv");
        long r = 0;
        InputStream is = null;

        try {
            is = resource.getInputStream();
            r = resource.contentLength();

            return ResponseEntity.ok()
                    .contentLength(r)
                    .body(new InputStreamResource(is));
        } catch (final IOException e) {
            LOG.error("CSV download failed", e);
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "CSV download failed", e);
        }
    }


    /**
     * Provide MDB (MS Access)
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "mdb",
            produces = "application/vnd.ms-access")
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public ResponseEntity<InputStreamResource> downloadMDB() {
        LOG.debug("Download static MDB file...");
        final Resource resource = new ClassPathResource("SWT2_Beispiel.mdb");
        long r = 0;
        InputStream is = null;

        try {
            is = resource.getInputStream();
            r = resource.contentLength();

            return ResponseEntity.ok()
                    .contentLength(r)
                    .body(new InputStreamResource(is));
        } catch (final IOException e) {
            LOG.error("MDB download failed", e);
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "MDB download failed", e);
        }
    }
}
