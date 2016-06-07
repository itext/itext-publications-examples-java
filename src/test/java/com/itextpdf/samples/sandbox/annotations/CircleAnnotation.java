/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/29275194/change-pdf-fillcolor-annotation-property-using-itextsharp-c-sharp
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfCircleAnnotation;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Category(SampleTest.class)
public class CircleAnnotation extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/annotations/circle_annotation.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CircleAnnotation().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(new FileInputStream(SRC)),
                new PdfWriter(new FileOutputStream(DEST)));
        Rectangle rect = new Rectangle(150, 770, 50, 50);

        PdfAnnotation annotation = new PdfCircleAnnotation(rect)
                .setContents("Circle")
                .setTitle(new PdfString("Circle"))
                .setColor(Color.BLUE)
                .setFlags(PdfAnnotation.PRINT)
                .setBorderStyle(PdfAnnotation.STYLE_DASHED)
                .setDashPattern(new PdfArray(new int[]{3, 2}))
                .setBorder(new PdfArray(new float[]{0, 0, 2}))
                .put(PdfName.IC, new PdfArray(new int[]{1, 0, 0}));
        pdfDoc.getFirstPage().addAnnotation(annotation);
        pdfDoc.close();
    }
}
