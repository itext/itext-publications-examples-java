/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24582887/rectangle-overlapping-in-itext-pdf-generating
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class Rectangles extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/rectangles.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Rectangles().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        float llx = 36;
        float lly = 700;
        float urx = 200;
        float ury = 806;
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        Rectangle rect1 = new Rectangle(llx, lly, urx - llx, ury - lly);
        canvas
                .setStrokeColor(Color.BLACK)
                .setLineWidth(1)
                .setFillColor(new DeviceGray(0.9f))
                .rectangle(rect1)
                .fillStroke();
        Rectangle rect2 = new Rectangle(llx + 60, lly, urx - llx - 60, ury - 40 - lly);
        canvas
                .setStrokeColor(Color.WHITE)
                .setLineWidth(0.5f)
                .setFillColor(new DeviceGray(0.1f))
                .rectangle(rect2)
                .fillStroke();
        pdfDoc.close();
    }
}
