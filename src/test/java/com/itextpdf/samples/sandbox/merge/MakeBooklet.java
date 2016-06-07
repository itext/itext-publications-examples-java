/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to a question on StackOverflow:
 * http://stackoverflow.com/questions/34432502
 */
package com.itextpdf.samples.sandbox.merge;


import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class MakeBooklet extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/merge/make_booklet.pdf";
    public static final String SRC = "./src/test/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MakeBooklet().manipulatePdf(DEST);
    }

    public void addPage(PdfCanvas canvas, PdfDocument srcDoc, PdfDocument pdfDoc, int p, float x) throws IOException {
        if (p > srcDoc.getNumberOfPages()) {
            return;
        }
        PdfFormXObject page = srcDoc.getPage(p).copyAsFormXObject(pdfDoc);
        canvas.addXObject(page, x, 0);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        float a4_width = PageSize.A4.getWidth();
        float a4_height = PageSize.A4.getHeight();
        PageSize pagesize = new PageSize(a4_width * 4, a4_height * 2);
        pdfDoc.setDefaultPageSize(pagesize);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        int n = srcDoc.getNumberOfPages();
        int p = 1;
        while ((p - 1) / 16 <= n / 16) {
            addPage(canvas, srcDoc, pdfDoc, p + 3, 0);
            addPage(canvas, srcDoc, pdfDoc, p + 12, a4_width);
            addPage(canvas, srcDoc, pdfDoc, p + 15, a4_width * 2);
            addPage(canvas, srcDoc, pdfDoc, p, a4_width * 3);
            AffineTransform at = AffineTransform.getRotateInstance((float) -Math.PI);
            at.concatenate(AffineTransform.getTranslateInstance(0, -a4_height * 2));
            canvas.saveState();
            canvas.concatMatrix(at);
            addPage(canvas, srcDoc, pdfDoc, p + 4, -a4_width);
            addPage(canvas, srcDoc, pdfDoc, p + 11, -a4_width * 2);
            addPage(canvas, srcDoc, pdfDoc, p + 8, -a4_width * 3);
            addPage(canvas, srcDoc, pdfDoc, p + 7, -a4_width * 4);
            canvas.restoreState();

            canvas = new PdfCanvas(pdfDoc.addNewPage());
            addPage(canvas, srcDoc, pdfDoc, p + 1, 0);
            addPage(canvas, srcDoc, pdfDoc, p + 14, a4_width);
            addPage(canvas, srcDoc, pdfDoc, p + 13, a4_width * 2);
            addPage(canvas, srcDoc, pdfDoc, p + 2, a4_width * 3);
            canvas.saveState();
            canvas.concatMatrix(at);
            addPage(canvas, srcDoc, pdfDoc, p + 6, -a4_width);
            addPage(canvas, srcDoc, pdfDoc, p + 9, -a4_width * 2);
            addPage(canvas, srcDoc, pdfDoc, p + 10, -a4_width * 3);
            addPage(canvas, srcDoc, pdfDoc, p + 5, -a4_width * 4);
            canvas.restoreState();

            if ((p - 1) / 16 < n / 16) {
                canvas = new PdfCanvas(pdfDoc.addNewPage());
            }
            p += 16;
        }
        pdfDoc.close();
        srcDoc.close();
    }
}
