package com.itextpdf.samples.sandbox.events;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.TextAlignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Text2PdfWithFooter2 {
    public static final String TEXT = "./src/main/resources/txt/tree.txt";
    public static final String DEST = "./target/sandbox/events/text2pdf_with_footer2.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Text2PdfWithFooter2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));

        // Add a custom event handler, that draws a page number at the bottom of the page
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new Footer());
        Document document = new Document(pdf).setTextAlignment(TextAlignment.JUSTIFIED);

        parseTextAndFillDocument(document, TEXT);

        document.close();
    }

    private void parseTextAndFillDocument(Document doc, String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            PdfFont normal = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            boolean title = true;
            String line;
            while ((line = br.readLine()) != null) {
                Paragraph paragraph;
                if (title) {

                    // If the text line is a title,
                    // then create a paragraph with bold font and rounded borders
                    paragraph = new Paragraph(line)
                            .setFont(bold)
                            .setBorder(new SolidBorder(1))
                            .setBorderRadius(new BorderRadius(5));
                } else {

                    // If the text line is not a title, then set a normal font
                    paragraph = new Paragraph(line)
                            .setFont(normal);
                }

                doc.add(paragraph);
                title = line.isEmpty();
            }
        }
    }

    private static class Footer implements IEventHandler {

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();

            float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
            float y = pageSize.getBottom() + 15;
            new Canvas(page, pageSize)
                    .showTextAligned(String.valueOf(pdf.getPageNumber(page)), x, y, TextAlignment.CENTER)
                    .close();
        }
    }
}
