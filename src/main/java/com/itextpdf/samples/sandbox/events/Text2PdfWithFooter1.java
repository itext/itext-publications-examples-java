package com.itextpdf.samples.sandbox.events;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEventHandler;
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEvent;
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Text2PdfWithFooter1 {
    public static final String TEXT = "./src/main/resources/txt/tree.txt";
    public static final String DEST = "./target/sandbox/events/text2pdf_with_footer1.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Text2PdfWithFooter1().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));

        // Add a custom event handler, that draws a page number at the bottom of the page
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new Footer());
        Document document = new Document(pdf).setTextAlignment(TextAlignment.JUSTIFIED);

        parseTextAndFillDocument(document, TEXT);

        document.close();
    }

    private static void parseTextAndFillDocument(Document doc, String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            PdfFont normal = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            Border border = new SolidBorder(ColorConstants.BLUE, 1);
            boolean title = true;
            String line;
            while ((line = br.readLine()) != null) {
                Paragraph paragraph;
                if (title) {

                    // If the text line is a title, then set a bold font and the created border
                    paragraph = new Paragraph(line)
                            .setFont(bold)
                            .setBorder(border);
                } else {

                    // If the text line is not a title, then set a normal font
                    paragraph = new Paragraph(line).setFont(normal);
                }

                doc.add(paragraph);
                title = line.isEmpty();
            }
        }
    }

    private static class Footer extends AbstractPdfDocumentEventHandler {

        @Override
        public void onAcceptedEvent(AbstractPdfDocumentEvent event) {
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
