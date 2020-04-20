package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.colors.ColorConstants;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PageBackgrounds {
    public static final String DEST = "./target/sandbox/events/page_backgrounds.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PageBackgrounds().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new PageBackgroundsEventHandler());
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Prime Numbers"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("An overview"));
        doc.add(new AreaBreak());
        for (int i = 2; i < 301; i++) {
            List<Integer> factors = getFactors(i);
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

    private static List<Integer> getFactors(int n) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }

        return factors;
    }

    private static class PageBackgroundsEventHandler implements IEventHandler {
        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            PdfPage page = docEvent.getPage();

            int pageNumber = docEvent.getDocument().getNumberOfPages();

            // Background color will be applied to the first page and all even pages
            if (pageNumber % 2 == 1 && pageNumber != 1) {
                return;
            }

            PdfCanvas canvas = new PdfCanvas(page);
            Rectangle rect = page.getPageSize();
            canvas
                    .saveState()
                    .setFillColor(pageNumber < 3 ? ColorConstants.BLUE : ColorConstants.LIGHT_GRAY)
                    .rectangle(rect)
                    .fillStroke()
                    .restoreState();
        }
    }
}
