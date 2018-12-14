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
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@RestController
@CrossOrigin
public class HelloDownloadServiceV1 implements ServiceFacade {

    private static final Logger LOG = LoggerFactory.getLogger(HelloDownloadServiceV1.class);


    @RequestMapping(method = RequestMethod.GET,
            path = "/v1/hello-world/download/pdf",
            produces = MediaType.APPLICATION_PDF_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public ResponseEntity<InputStreamResource> downloadPdf() {
        LOG.debug("Download static PDF file...");
        final Resource resource = new ClassPathResource("SWT2_PDF_Beispiel.pdf");
        long r = 0;
        InputStream is = null;

        try {
            is = resource.getInputStream();
            r = resource.contentLength();

            return ResponseEntity.ok()
                    .contentLength(r)
                    .body(new InputStreamResource(is));
        } catch (final IOException e) {
            e.printStackTrace();
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "PDF download failed", e);
        }
    }
}
