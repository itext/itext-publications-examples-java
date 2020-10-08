package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfHtmlHeaderAndFooter {
    public static final String SRC = "./src/main/resources/pdfhtml/";
    public static final String DEST = "./target/sandbox/pdfhtml/ipsum.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        String htmlSource = SRC + "ipsum.html";

        new PdfHtmlHeaderAndFooter().manipulatePdf(htmlSource, DEST);
    }

    public void manipulatePdf(String htmlSource, String pdfDest) throws IOException {
        PdfWriter writer = new PdfWriter(pdfDest);
        PdfDocument pdfDocument = new PdfDocument(writer);
        String header = "pdfHtml Header and footer example using page-events";
        Header headerHandler = new Header(header);
        Footer footerHandler = new Footer();

        pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);

        // Base URI is required to resolve the path to source files
        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(SRC);
        HtmlConverter.convertToDocument(new FileInputStream(htmlSource), pdfDocument, converterProperties);

        // Write the total number of pages to the placeholder
        footerHandler.writeTotal(pdfDocument);
        pdfDocument.close();
    }

    // Header event handler
    protected class Header implements IEventHandler {
        private String header;

        public Header(String header) {
            this.header = header;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();

            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();

            Canvas canvas = new Canvas(new PdfCanvas(page), pageSize);
            canvas.setFontSize(18);

            // Write text at position
            canvas.showTextAligned(header,
                    pageSize.getWidth() / 2,
                    pageSize.getTop() - 30, TextAlignment.CENTER);
            canvas.close();
        }
    }

    // Footer event handler
    protected class Footer implements IEventHandler {
        protected PdfFormXObject placeholder;
        protected float side = 20;
        protected float x = 300;
        protected float y = 25;
        protected float space = 4.5f;
        protected float descent = 3;

        public Footer() {
            placeholder = new PdfFormXObject(new Rectangle(0, 0, side, side));
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdf.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();

            // Creates drawing canvas
            PdfCanvas pdfCanvas = new PdfCanvas(page);
            Canvas canvas = new Canvas(pdfCanvas, pageSize);

            Paragraph p = new Paragraph()
                    .add("Page ")
                    .add(String.valueOf(pageNumber))
                    .add(" of");

            canvas.showTextAligned(p, x, y, TextAlignment.RIGHT);
            canvas.close();

            // Create placeholder object to write number of pages
            pdfCanvas.addXObjectAt(placeholder, x + space, y - descent);
            pdfCanvas.release();
        }

        public void writeTotal(PdfDocument pdf) {
            Canvas canvas = new Canvas(placeholder, pdf);
            canvas.showTextAligned(String.valueOf(pdf.getNumberOfPages()),
                    0, descent, TextAlignment.LEFT);
            canvas.close();
        }
    }
}
