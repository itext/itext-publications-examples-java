/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie.
 */
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
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class PageOrientations extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/events/page_orientations.pdf";

    /* Constants form itext5 */
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    public static final PdfNumber PORTRAIT = new PdfNumber(0);
    public static final PdfNumber SEASCAPE = new PdfNumber(270);

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PageOrientations().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
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


    public static class PageOrientationsEventHandler implements IEventHandler {
        protected PdfNumber orientation = PORTRAIT;

        public void setOrientation(PdfNumber orientation) {
            this.orientation = orientation;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            docEvent.getPage().put(PdfName.Rotate, orientation);
        }
    }
}
