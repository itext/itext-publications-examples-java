/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following StackOverflow question:
 * http://stackoverflow.com/questions/29775893/watermark-in-a-pdf-with-itext
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class AddExtraMargin extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/add_extra_margin.pdf";
    public static final String SRC = "./src/test/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddExtraMargin().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        int n = pdfDoc.getNumberOfPages();
        PdfCanvas over;
        PdfDictionary pageDict;
        PdfArray mediaBox;
        float llx, lly, ury;
        // loop over every page
        for (int i = 1; i <= n; i++) {
            pageDict = pdfDoc.getPage(i).getPdfObject();
            mediaBox = pageDict.getAsArray(PdfName.MediaBox);
            llx = mediaBox.getAsNumber(0).floatValue();
            lly = mediaBox.getAsNumber(1).floatValue();
            ury = mediaBox.getAsNumber(3).floatValue();
            mediaBox.set(0, new PdfNumber(llx - 36));
            over = new PdfCanvas(pdfDoc.getPage(i));
            over.saveState();
            over.setFillColor(new DeviceGray(0.5f));
            over.rectangle(llx - 36, lly, 36, ury - llx);
            over.fill();
            over.restoreState();
        }
        pdfDoc.close();
    }
}
