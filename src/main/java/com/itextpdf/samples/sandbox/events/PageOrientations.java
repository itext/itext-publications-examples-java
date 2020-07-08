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

public class PageOrientations {
    public static final String DEST = "./target/sandbox/events/page_orientations.pdf";

    public static final PdfNumber PORTRAIT = new PdfNumber(0);
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    public static final PdfNumber SEASCAPE = new PdfNumber(270);

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PageOrientations().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        // The default page orientation is set to portrait in the custom event handler.
        PageOrientationsEventHandler eventHandler = new PageOrientationsEventHandler();
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, eventHandler);
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("A simple page in portrait orientation"));

        eventHandler.setOrientation(LANDSCAPE);
        doc.add(new AreaBreak());
        doc.add(new Paragraph("A simple page in landscape orientation"));

        eventHandler.setOrientation(INVERTEDPORTRAIT);
        doc.add(new AreaBreak());
        doc.add(new Paragraph("A simple page in inverted portrait orientation"));

        eventHandler.setOrientation(SEASCAPE);
        doc.add(new AreaBreak());
        doc.add(new Paragraph("A simple page in seascape orientation"));

        doc.close();
    }


    private static class PageOrientationsEventHandler implements IEventHandler {
        private PdfNumber orientation = PORTRAIT;

        public void setOrientation(PdfNumber orientation) {
            this.orientation = orientation;
        }

        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            docEvent.getPage().put(PdfName.Rotate, orientation);
        }
    }
}
