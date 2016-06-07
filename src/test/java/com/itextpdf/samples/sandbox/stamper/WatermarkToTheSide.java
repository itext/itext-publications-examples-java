/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to a question by a customer.
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class WatermarkToTheSide extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/watermark_to_the_side.pdf";
    public static final String SRC = "./src/test/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new WatermarkToTheSide().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        int n = pdfDoc.getNumberOfPages();
        PdfCanvas canvas;
        Rectangle pageSize;
        float x, y;
        for (int p = 1; p <= n; p++) {
            pageSize = pdfDoc.getPage(p).getPageSize();
            // left of the page
            x = pageSize.getLeft();
            // middle of the height
            y = (pageSize.getTop() + pageSize.getBottom()) / 2;
            // getting the canvas covering the existing content
            canvas = new PdfCanvas(pdfDoc.getPage(p));
            // adding some lines to the left
            new Canvas(canvas, pdfDoc, pageSize)
                    .showTextAligned("This is some extra text added to the left of the page", x + 18, y, TextAlignment.CENTER, (float) Math.toRadians(90));
            new Canvas(canvas, pdfDoc, pageSize)
                    .showTextAligned("This is some more text added to the left of the page", x + 34, y, TextAlignment.CENTER, (float) Math.toRadians(90));
            // !!!IMPORTANT
            // Notice, that in itext7 we do not consider the rotation while adding via Document or Canvas
            // So the third page differs from itext5 result
        }
        pdfDoc.close();
    }
}
