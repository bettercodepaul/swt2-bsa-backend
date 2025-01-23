package de.bogenliga.application.business.schusszettel.impl.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;

@Component
public class SchusszettelComponentAsync {

    private final SchusszettelComponentImpl schusszettelComponentImpl;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Autowired
    public SchusszettelComponentAsync(SchusszettelComponentImpl schusszettelComponentImpl) {
        this.schusszettelComponentImpl = schusszettelComponentImpl;
    }

    @Async
    public CompletableFuture<ByteArrayOutputStream> generateSchusszettelPageAsync(MatchDO[] matchesBegegnung, long i, long k, int numberOfMatches, int veranstaltungGroesse) {
        return CompletableFuture.supplyAsync(() -> {
            try (final ByteArrayOutputStream pageStream = new ByteArrayOutputStream();
                 final PdfWriter pageWriter = new PdfWriter(pageStream);
                 final PdfDocument pagePdfDocument = new PdfDocument(pageWriter);
                 final Document pageDoc = new Document(pagePdfDocument, PageSize.A4)) {

                schusszettelComponentImpl.generateSchusszettelPage(pageDoc, matchesBegegnung);

                pageDoc.close();
                return pageStream;

            } catch (IOException e) {
                throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                        "PDF Seite konnte nicht erstellt werden: " + e);
            }
        }, executorService);
    }
}
