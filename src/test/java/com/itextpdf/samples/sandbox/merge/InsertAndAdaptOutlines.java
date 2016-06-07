/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written in answer to:
 * http://stackoverflow.com/questions/34381849
 */
package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class InsertAndAdaptOutlines extends GenericTest {
    public static final String DEST =
            "./target/test/resources/sandbox/merge/insert_and_adapt_outlines.pdf";
    public static final String INSERT =
            "./src/test/resources/pdfs/hello.pdf";
    public static final String SRC =
            "./src/test/resources/pdfs/bookmarks.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new InsertAndAdaptOutlines().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfDocument insertDoc = new PdfDocument(new PdfReader(INSERT));
        insertDoc.copyPagesTo(1, 1, pdfDoc, 4);
        insertDoc.close();

        PdfOutline outlines = pdfDoc.getOutlines(false);
        PdfOutline outline = outlines.getAllChildren().get(0).addOutline("Hello", 3);
        outline.addDestination(PdfExplicitDestination.createFit(4));
        pdfDoc.close();
    }
}
