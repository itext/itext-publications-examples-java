/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27083206/itextshape-clickable-polygon-or-path
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class AddPolygonLink extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_polygon_link.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddPolygonLink().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas.moveTo(36, 700)
                .lineTo(72, 760)
                .lineTo(144, 720)
                .lineTo(72, 730)
                .closePathStroke();
        Rectangle linkLocation = new Rectangle(36, 700, 144, 760);
        PdfLinkAnnotation linkAnnotation = new PdfLinkAnnotation(linkLocation)
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setAction(PdfAction.createGoTo(PdfExplicitDestination.createFit(1)));
        PdfArray arrayOfQuadPoints = new PdfArray(new int[]{72, 730, 144, 720, 72, 760, 36, 700});
        linkAnnotation.put(PdfName.QuadPoints, arrayOfQuadPoints);

        pdfDoc.getFirstPage().addAnnotation(linkAnnotation);

        doc.close();
    }
}
