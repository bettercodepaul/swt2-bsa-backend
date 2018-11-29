package de.bogenliga.application.business.Setzliste;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import de.bogenliga.application.business.Setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.Setzliste.api.types.SetzlisteDO;
import de.bogenliga.application.business.Setzliste.impl.business.SetzlisteComponentImpl;

import java.io.FileNotFoundException;

public class pdfTest {
    public static void main(String [ ] args) throws FileNotFoundException
    {
       // SetzlisteDO setzlisteDO = SetzlisteComponent.getTable();

        String dest = "pdfText.pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add(new Paragraph("Hello World!"));
        document.close();
    }
}
