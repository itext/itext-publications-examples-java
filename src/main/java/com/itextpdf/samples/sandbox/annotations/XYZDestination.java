package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

/*
 * XYZDestination.java
 * 
 * This class demonstrates how to create internal navigation links in a PDF document 
 * using XYZ destinations. The code creates a multi-page document with test content,
 * then adds a set of links on the last page that navigate to specific coordinates
 * on each of the preceding pages. This example shows how to implement precise
 * internal navigation with custom zoom levels using explicit destinations.
 */

public class XYZDestination {
    public static final String DEST = "./target/sandbox/annotations/xyz_destination.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new XYZDestination().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        for (int i = 0; i < 10; i++) {
            doc.add(new Paragraph("Test"));
            doc.add(new AreaBreak());
        }

        for (int i = 1; i < 11; i++) {

            // Create a link destination to the page, specified in the 1st argument.
            PdfDestination d = PdfExplicitDestination.createXYZ(pdfDoc.getPage(i), 36, 806, 0);
            Paragraph c = new Paragraph(new Link("Goto page " + i, d));
            doc.add(c);
        }

        doc.close();
    }
}
