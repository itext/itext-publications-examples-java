package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEventHandler;
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEvent;
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class Seascape {
    public static final String DEST = "./target/sandbox/events/seascape.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Seascape().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new SeascapeEventHandler());

        for (int i = 0; i < 50; i++) {
            doc.add(new Paragraph("Hello World!"));
        }

        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hello World!"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hello World!"));

        doc.close();
    }

    private static class SeascapeEventHandler extends AbstractPdfDocumentEventHandler {

        @Override
        public void onAcceptedEvent(AbstractPdfDocumentEvent currentEvent) {
            PdfDocumentEvent documentEvent = (PdfDocumentEvent) currentEvent;
            documentEvent.getPage().setRotation(270);
        }
    }
}
