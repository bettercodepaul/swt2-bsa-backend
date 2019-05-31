package de.bogenliga.application.business.Schusszettel.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import de.bogenliga.application.business.Schusszettel.api.SchusszettelComponent;
import de.bogenliga.application.business.Setzliste.impl.business.SetzlisteComponentImpl;
import de.bogenliga.application.business.Setzliste.impl.entity.SetzlisteBE;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * * Implementation of {@link SchusszettelComponent}
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 */
@Component
public class SchusszettelComponentImpl implements SchusszettelComponent {

    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";
    private static final Logger LOGGER = LoggerFactory.getLogger(SetzlisteComponentImpl.class);

    private final MatchComponent matchComponent;
    private long WettkampfID;


    @Autowired
    public SchusszettelComponentImpl(final MatchComponent matchComponent) {
        this.matchComponent = matchComponent;
    }


    @Override
    public byte[] getAllSchusszettelPDFasByteArray(long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);

        this.WettkampfID = wettkampfid;

        byte[] bResult;
        try (final ByteArrayOutputStream result = new ByteArrayOutputStream();
                 final PdfWriter writer = new PdfWriter(result);
                 final PdfDocument pdfDocument = new PdfDocument(writer);
                 final Document doc = new Document(pdfDocument, PageSize.A4.rotate())) {

                //generateDoc(doc, setzlisteBEList);

                bResult = result.toByteArray();

            } catch (final IOException e) {
                LOGGER.error("document could not be generated");
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                    "PDF Schusszettel konnte nicht erstellt werden: " + e);
            }
        return bResult;
    }

    /**
     * <p>writes a Schusszettel document for the Wettkamnpf
     * </p>
     * @param doc document to write
     */
    private void generateSchusszettelPage(Document doc) {

    }
}
