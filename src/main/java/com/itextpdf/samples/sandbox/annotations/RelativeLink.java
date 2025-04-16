package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

/*
 * RelativeLink.java
 * 
 * This class demonstrates how to create a PDF document with a hyperlink that
 * references a relative file path. The code creates a simple PDF with a "Click me"
 * text link that, when clicked, attempts to open an XML file using a relative path.
 * This example shows how to create external URI actions that point to local resources
 * using relative paths rather than absolute URLs.
 */

public class RelativeLink {
    public static final String DEST = "./target/sandbox/annotations/relative_link.pdf";

    public static final String XML = "../../../src/main/resources/xml/data.xml";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RelativeLink().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph chunk = new Paragraph(new Link("Click me", PdfAction.createURI(XML)));
        doc.add(chunk);

        doc.close();
    }
}
