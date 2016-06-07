/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/26773942/itext-crop-out-a-part-of-pdf-file
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.Locale;

@Category(SampleTest.class)
public class ClipPdf extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/clip_pdf.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ClipPdf().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        int n = pdfDoc.getNumberOfPages();
        PdfDictionary page;
        PdfArray media;
        for (int p = 1; p <= n; p++) {
            page = pdfDoc.getPage(p).getPdfObject();
            media = page.getAsArray(PdfName.CropBox);
            if (media == null) {
                media = page.getAsArray(PdfName.MediaBox);
            }
            float llx = media.getAsNumber(0).floatValue() + 200;
            float lly = media.getAsNumber(1).floatValue() + 200;
            float w = media.getAsNumber(2).floatValue() - media.getAsNumber(0).floatValue() - 400;
            float h = media.getAsNumber(3).floatValue() - media.getAsNumber(1).floatValue() - 400;
            // !IMPORTANT to write Locale
            String command = String.format(
                    Locale.ENGLISH,
                    "\nq %.2f %.2f %.2f %.2f re W n\nq\n",
                    llx, lly, w, h);
            new PdfCanvas(pdfDoc.getPage(p).newContentStreamBefore(), new PdfResources(), pdfDoc).writeLiteral(command);
            new PdfCanvas(pdfDoc.getPage(p).newContentStreamAfter(), new PdfResources(), pdfDoc).writeLiteral("\nQ\nQ\n");
        }
        pdfDoc.close();
    }
}
