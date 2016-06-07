/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21628429/itextsharp-how-to-generate-a-report-with-dynamic-header-in-pdf-using-itextsharp
 */
package com.itextpdf.samples.sandbox.events;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Category(SampleTest.class)
public class VariableHeader extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/events/variable_header.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new VariableHeader().manipulatePdf(DEST);
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
        Document doc = new Document(pdfDoc);
        VariableHeaderEventHandler handler = new VariableHeaderEventHandler();
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
        List<Integer> factors;
        for (int i = 2; i < 301; i++) {
            factors = getFactors(i);
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


    protected class VariableHeaderEventHandler implements IEventHandler {
        protected String header;

        public void setHeader(String header) {
            this.header = header;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
            try {
                new PdfCanvas(documentEvent.getPage())
                        .beginText()
                        .setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 12)
                        .moveText(450, 806)
                        .showText(header)
                        .endText()
                        .stroke();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
