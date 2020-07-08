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

public class PageRotation {
    public static final String DEST = "./target/sandbox/events/page_rotation.pdf";

    public static final PdfNumber PORTRAIT = new PdfNumber(0);
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    public static final PdfNumber SEASCAPE = new PdfNumber(270);

    private static final Paragraph HELLO_WORLD = new Paragraph("Hello World!");

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PageRotation().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        // The default page rotation is set to portrait in the custom event handler.
        PageRotationEventHandler eventHandler = new PageRotationEventHandler();
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, eventHandler);
        Document doc = new Document(pdfDoc);

        doc.add(HELLO_WORLD);

        eventHandler.setRotation(LANDSCAPE);
        doc.add(new AreaBreak());
        doc.add(HELLO_WORLD);

        eventHandler.setRotation(INVERTEDPORTRAIT);
        doc.add(new AreaBreak());
        doc.add(HELLO_WORLD);

        eventHandler.setRotation(SEASCAPE);
        doc.add(new AreaBreak());
        doc.add(HELLO_WORLD);

        eventHandler.setRotation(PORTRAIT);
        doc.add(new AreaBreak());
        doc.add(HELLO_WORLD);

        doc.close();
    }


    private static class PageRotationEventHandler implements IEventHandler {
        private PdfNumber rotation = PORTRAIT;

        public void setRotation(PdfNumber orientation) {
            this.rotation = orientation;
        }

        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            docEvent.getPage().put(PdfName.Rotate, rotation);
        }
    }
}
