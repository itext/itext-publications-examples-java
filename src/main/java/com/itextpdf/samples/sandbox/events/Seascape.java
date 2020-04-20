package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
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


    private static class SeascapeEventHandler implements IEventHandler {
        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent documentEvent = (PdfDocumentEvent) currentEvent;
            documentEvent.getPage().setRotation(270);
        }
    }
}
