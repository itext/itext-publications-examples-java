/**
 * Formerly OpenAt100pct sample in iText 5
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

/*
 * SetOpenZoom.java
 * 
 * This class demonstrates how to set the initial view of a PDF document to open
 * at a specific zoom level. The code creates a simple PDF with a "Hello World" text
 * and configures it to open at 100% zoom level using an explicit XYZ destination
 * in the document catalog's open action. This ensures that when the document is opened
 * in a PDF viewer, it will be displayed at exactly 100% magnification.
 */

public class SetOpenZoom {
    public static final String DEST = "./target/sandbox/annotations/open_at_100pct.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SetOpenZoom().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(612, 792));
        doc.add(new Paragraph("Hello World"));

        // Set the height of a page to 842 points and zoom value to 1 (which means 100% zoom)
        PdfExplicitDestination zoomPage = PdfExplicitDestination.createXYZ(pdfDoc.getPage(1),
                0, 842, 1);
        pdfDoc.getCatalog().setOpenAction(zoomPage);

        doc.close();
    }
}
