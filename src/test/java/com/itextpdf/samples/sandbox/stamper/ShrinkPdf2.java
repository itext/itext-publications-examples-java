/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/25356302/shrink-pdf-pages-with-rotation-using-rectangle-in-existing-pdf
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.Locale;

@Category(SampleTest.class)
public class ShrinkPdf2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/shrink_pdf2.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ShrinkPdf2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        int n = pdfDoc.getNumberOfPages();
        float percentage = 0.8f;
        for (int p = 1; p <= n; p++) {
            float offsetX = (pdfDoc.getPage(p).getPageSize().getWidth() * (1 - percentage)) / 2;
            float offsetY = (pdfDoc.getPage(p).getPageSize().getHeight() * (1 - percentage)) / 2;
            new PdfCanvas(pdfDoc.getPage(p).newContentStreamBefore(), new PdfResources(), pdfDoc).writeLiteral(
                    String.format(Locale.ENGLISH, "\nq %s 0 0 %s %s %s cm\nq\n",
                            percentage, percentage, offsetX, offsetY));
            new PdfCanvas(pdfDoc.getPage(p).newContentStreamAfter(),
                    new PdfResources(), pdfDoc).writeLiteral("\nQ\nQ\n");
        }
        pdfDoc.close();
    }
}
