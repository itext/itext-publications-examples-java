package com.itextpdf.samples.sandbox.events;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VariableHeader {
    public static final String DEST = "./target/sandbox/events/variable_header.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new VariableHeader().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        VariableHeaderEventHandler handler = new VariableHeaderEventHandler();
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

        for (int i = 2; i < 301; i++) {
            List<Integer> factors = getFactors(i);
            if (factors.size() == 1) {
                doc.add(new Paragraph("This is a prime number!"));
            }

            for (int factor : factors) {
                doc.add(new Paragraph("Factor: " + factor));
            }

            handler.setHeader(String.format("THE FACTORS OF %s", i));

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

    private static class VariableHeaderEventHandler implements IEventHandler {
        protected String header;

        public void setHeader(String header) {
            this.header = header;
        }

        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent documentEvent = (PdfDocumentEvent) currentEvent;
            PdfPage page = documentEvent.getPage();
            new Canvas(page, page.getPageSize())
                    .showTextAligned(header, 490, 806, TextAlignment.CENTER)
                    .close();
        }
    }
}
