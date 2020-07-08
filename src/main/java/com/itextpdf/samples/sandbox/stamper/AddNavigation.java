package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;

import java.io.File;

public class AddNavigation {
    public static final String DEST = "./target/sandbox/stamper/add_navigation.pdf";
    public static final String SRC = "./src/main/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddNavigation().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        int[] borders = {0, 0, 1};
        PdfDestination pdfDestination = PdfExplicitDestination.createFit(pdfDoc.getPage(10));
        Rectangle rect = new Rectangle(0, 806, 595, 36);
        PdfAnnotation a10 = new PdfLinkAnnotation(rect)
                .setAction(PdfAction.createGoTo(pdfDestination))
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setPage(pdfDoc.getPage(10))
                .setBorder(new PdfArray(borders))
                .setColor(new PdfArray(new float[] {0, 1, 0}));
        pdfDoc.getPage(1).addAnnotation(a10);
        PdfDestination d2 = PdfExplicitDestination.createFit(pdfDoc.getPage(1));
        PdfAnnotation a1 = new PdfLinkAnnotation(rect)
                .setAction(PdfAction.createGoTo(d2))
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_PUSH)
                .setPage(pdfDoc.getPage(1))
                .setBorder(new PdfArray(borders))
                .setColor(new PdfArray(new float[] {0, 1, 0}));
        pdfDoc.getPage(10).addAnnotation(a1);
        pdfDoc.close();
    }
}
