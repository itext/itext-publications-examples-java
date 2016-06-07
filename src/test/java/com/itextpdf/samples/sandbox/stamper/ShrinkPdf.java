/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/25356302/shrink-pdf-pages-with-rotation-using-rectangle-in-existing-pdf
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ShrinkPdf extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/shrink_pdf.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ShrinkPdf().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        int n = pdfDoc.getNumberOfPages();
        PdfPage page;
        Rectangle crop;
        Rectangle media;
        for (int p = 1; p <= n; p++) {
            page = pdfDoc.getPage(p);
            media = page.getCropBox();
            if (media == null) {
                media = page.getMediaBox();
            }
            crop = new Rectangle(0, 0, media.getWidth() / 2, media.getHeight() / 2);
            page.setMediaBox(crop);
            page.setCropBox(crop);
            new PdfCanvas(pdfDoc.getPage(p).newContentStreamBefore(),
                    new PdfResources(), pdfDoc).writeLiteral("\nq 0.5 0 0 0.5 0 0 cm\nq\n");
            new PdfCanvas(pdfDoc.getPage(p).newContentStreamAfter(),
                    new PdfResources(), pdfDoc).writeLiteral("\nQ\nQ\n");
        }
        pdfDoc.close();
    }
}
