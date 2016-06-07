/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27020542/rotating-pdf-90-degrees-using-itextsharp-in-c-sharp
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class Rotate90Degrees extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/rotate90degrees.pdf";
    public static final String SRC = "./src/test/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Rotate90Degrees().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        int n = pdfDoc.getNumberOfPages();
        PdfPage page;
        PdfNumber rotate;
        for (int p = 1; p <= n; p++) {
            page = pdfDoc.getPage(p);
            rotate = page.getPdfObject().getAsNumber(PdfName.Rotate);
            if (rotate == null) {
                page.setRotation(90);
            } else {
                page.setRotation((rotate.intValue() + 90) % 360);
            }
        }
        pdfDoc.close();
    }
}
