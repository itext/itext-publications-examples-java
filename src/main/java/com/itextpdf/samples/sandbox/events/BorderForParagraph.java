package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class BorderForParagraph {
    public static final String DEST = "./target/sandbox/events/border_for_paragraph.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new BorderForParagraph().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Hello,"));
        doc.add(new Paragraph("In this doc, we'll add several paragraphs that will trigger page events. " +
                "As long as the event isn't activated, nothing special happens, " +
                "but let's make the event active and see what happens:"));

        Paragraph paragraphWithBorder = new Paragraph("This paragraph now has a border. Isn't that fantastic? " +
                "By changing the event, we can even provide a background color, " +
                "change the line width of the border and many other things. Now let's deactivate the event.");

        // There were no method that allows you to create a border for a Paragraph, since iText5 is EOL.
        // In iText 7 a border for a Paragraph can be created by calling setBorder() method.
        paragraphWithBorder.setBorder(new SolidBorder(1));
        doc.add(paragraphWithBorder);

        doc.add(new Paragraph("This paragraph no longer has a border."));

        doc.close();
    }
}
