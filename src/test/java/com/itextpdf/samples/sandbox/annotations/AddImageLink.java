/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/26983703/itext-how-to-stamp-image-on-existing-pdf-and-create-an-anchor
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class AddImageLink extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_image_link.pdf";
    public static final String IMG = "./src/test/resources/img/info.png";
    public static final String SRC = "./src/test/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddImageLink().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        ImageData img = ImageDataFactory.create(IMG);
        float x = 10;
        float y = 650;
        float w = img.getWidth();
        float h = img.getHeight();

        new PdfCanvas(pdfDoc.getFirstPage().newContentStreamAfter(), pdfDoc.getFirstPage().getResources(), pdfDoc)
                .addImage(img, x, y, false);
        Rectangle linkLocation = new Rectangle(x, y, w, h);

        PdfAnnotation annotation = new PdfLinkAnnotation(linkLocation)
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setAction(PdfAction.createGoTo(PdfExplicitDestination.createFit(pdfDoc.getLastPage())))
                .setBorder(new PdfArray(new float[]{0, 0, 0}));
        pdfDoc.getFirstPage().addAnnotation(annotation);

        pdfDoc.close();
    }
}
