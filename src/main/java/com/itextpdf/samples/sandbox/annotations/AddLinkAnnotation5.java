package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

public class AddLinkAnnotation5 {
    public static final String SRC = "./src/main/resources/pdfs/primes.pdf";

    public static final String DEST = "./target/sandbox/annotations/add_link_annotation5.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddLinkAnnotation5().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // Make the link destination page fit to the display
        PdfExplicitDestination destination = PdfExplicitDestination.createFit(pdfDoc.getPage(3));
        Link link = new Link(
                "This is a link. Click it and you'll be forwarded to another page in this document.",

                // Add link to the 3rd page.
                PdfAction.createGoTo(destination));

        // Set highlighting type which is enabled after a click on the annotation
        link.getLinkAnnotation().setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT);
        Paragraph p = new Paragraph(link).setWidth(240);
        doc.showTextAligned(p, 320, 695, 1, TextAlignment.LEFT,
                VerticalAlignment.TOP, 0);

        doc.close();
    }
}
