/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/30211043/change-the-color-of-pdf-pages-alternatively-using-itext-pdf-in-java
 */
package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Category(SampleTest.class)
public class PageBackgrounds extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/events/page_backgrounds.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PageBackgrounds().manipulatePdf(DEST);
    }

    public static List<Integer> getFactors(int n) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        return factors;
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new PageBackgroundsEventHandler());
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Prime Numbers"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("An overview"));
        doc.add(new AreaBreak());
        List<Integer> factors;
        for (int i = 2; i < 301; i++) {
            factors = getFactors(i);
            if (factors.size() == 1) {
                doc.add(new Paragraph("This is a prime number!"));
            }
            for (int factor : factors) {
                doc.add(new Paragraph("Factor: " + factor));
            }
            if (300 != i) {
                doc.add(new AreaBreak());
            }
        }
        doc.close();
    }


    protected class PageBackgroundsEventHandler implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();

            int pagenumber = docEvent.getDocument().getNumberOfPages();
            if (pagenumber % 2 == 1 && pagenumber != 1) {
                return;
            }
            PdfCanvas canvas = new PdfCanvas(page);
            Rectangle rect = page.getPageSize();
            canvas
                    .saveState()
                    .setFillColor(pagenumber + 1 < 3 ? Color.BLUE : Color.LIGHT_GRAY)
                    .rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight())
                    .fillStroke()
                    .restoreState();
        }
    }
}
