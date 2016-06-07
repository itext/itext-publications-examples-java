/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to a question on StackOverflow:
 * http://stackoverflow.com/questions/34019298
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
public class MakeA3Booklet extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/merge/make_a3_booklet.pdf";
    public static final String SRC = "./src/test/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MakeA3Booklet().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        pdfDoc.setDefaultPageSize(new PageSize(PageSize.A3).rotate());

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        float a4_width = PageSize.A4.getWidth();
        int n = srcDoc.getNumberOfPages();
        int p = 0;
        PdfFormXObject page;
        while (p++ < n) {
            page = srcDoc.getPage(p).copyAsFormXObject(pdfDoc);
            if (p % 2 == 1) {
                canvas.addXObject(page, 0, 0);
            } else {
                canvas.addXObject(page, a4_width, 0);
                canvas = new PdfCanvas(pdfDoc.addNewPage());
            }
        }
        pdfDoc.close();
        srcDoc.close();
    }
}
