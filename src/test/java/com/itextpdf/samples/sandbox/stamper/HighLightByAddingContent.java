/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This example was written in answer to the question:
 * http://stackoverflow.com/questions/33952183
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class HighLightByAddingContent extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/stamper/high_light_by_adding_content.pdf";
    public static final String SRC
            = "./src/test/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new HighLightByAddingContent().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(),
                pdfDoc.getFirstPage().getResources(), pdfDoc);
        canvas.saveState();
        canvas.setFillColor(ColorConstants.YELLOW);
        canvas.rectangle(36, 786, 66, 16);
        canvas.fill();
        canvas.restoreState();
        pdfDoc.close();
    }
}
