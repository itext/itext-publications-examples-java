/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
 * This example was written in answer to the following question:
 * http://stackoverflow.com/questions/34036200
 */
package com.itextpdf.samples.sandbox.pdfa;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class AddAltTags extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/pdfa/add_alt_tags.pdf";
    public static final String SRC = "./src/test/resources/pdfs/no_alt_attribute.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddAltTags().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfDictionary catalog = pdfDoc.getCatalog().getPdfObject();
        PdfDictionary structTreeRoot = catalog.getAsDictionary(PdfName.StructTreeRoot);
        manipulate(structTreeRoot);
        pdfDoc.close();
    }

    public void manipulate(PdfDictionary element) {
        if (element == null) {
            return;
        }
        if (PdfName.Figure.equals(element.get(PdfName.S))) {
            element.put(PdfName.Alt, new PdfString("Figure without an Alt description"));
        }
        PdfArray kids = element.getAsArray(PdfName.K);
        if (kids == null) {
            return;
        }
        for (int i = 0; i < kids.size(); i++) {
            manipulate(kids.getAsDictionary(i));
        }
    }
}
