/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to a question on StackOverflow:
 * http://stackoverflow.com/questions/34432502
 */
package com.itextpdf.samples.sandbox.merge;


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
public class MakeBookletA3 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/merge/make_booklet_a3.pdf";
    public static final String SRC = "./src/test/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MakeBookletA3().manipulatePdf(DEST);
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
        PageSize pageSize = new PageSize(
                PageSize.A4.getWidth() * 2,
                PageSize.A4.getHeight());
        pdfDoc.setDefaultPageSize(pageSize);
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        float a4_width = PageSize.A4.getWidth();
        int n = srcDoc.getNumberOfPages();
        int p = 1;
        while ((p - 1) / 4 <= n / 4) {
            addPage(canvas, srcDoc, pdfDoc, p + 3, 0);
            addPage(canvas, srcDoc, pdfDoc, p, a4_width);
            canvas = new PdfCanvas(pdfDoc.addNewPage());
            addPage(canvas, srcDoc, pdfDoc, p + 1, 0);
            addPage(canvas, srcDoc, pdfDoc, p + 2, a4_width);
            if ((p - 1) / 4 < n / 4) {
                canvas = new PdfCanvas(pdfDoc.addNewPage());
            }
            p += 4;
        }
        pdfDoc.close();
        srcDoc.close();
    }
}
