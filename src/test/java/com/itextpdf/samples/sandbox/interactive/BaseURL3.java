/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24568386/set-baseurl-of-an-existing-pdf-document
 */
package com.itextpdf.samples.sandbox.interactive;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class BaseURL3 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/interactive/base_url3.pdf";
    public static final String SRC = "./src/test/resources/pdfs/base_url.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new BaseURL3().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfDictionary uri = new PdfDictionary();
        uri.put(PdfName.Type, PdfName.URI);
        uri.put(new PdfName("Base"), new PdfString("http://itextpdf.com/"));
        pdfDoc.getCatalog().put(PdfName.URI, uri);

        pdfDoc.close();
    }
}
