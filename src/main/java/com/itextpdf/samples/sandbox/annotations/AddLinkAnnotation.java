package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;

import java.io.File;

public class AddLinkAnnotation {
    public static final String DEST = "./target/sandbox/annotations/add_link_annotation.pdf";

    public static final String SRC = "./src/main/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddLinkAnnotation().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Rectangle linkLocation = new Rectangle(523, 770, 36, 36);
        int[] borders = {0, 0, 1};

        // Make the link destination page fit to the display
        PdfExplicitDestination destination = PdfExplicitDestination.createFit(pdfDoc.getPage(3));
        PdfAnnotation annotation = new PdfLinkAnnotation(linkLocation)

                // Set highlighting type which is enabled after a click on the annotation
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)

                // Add link to the 3rd page.
                .setAction(PdfAction.createGoTo(destination))
                .setBorder(new PdfArray(borders));
        pdfDoc.getFirstPage().addAnnotation(annotation);

        pdfDoc.close();
    }
}
