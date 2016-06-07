/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * https://www.linkedin.com/groups/Script-Change-Author-Name-Comments-159987.S.5984062085800144899
 */
package com.itextpdf.samples.sandbox.interactive;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class ChangeAuthor extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/interactive/change_author.pdf";
    public static final String SRC = "./src/test/resources/pdfs/page229_annotations.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeAuthor().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfDictionary pageDict = pdfDoc.getFirstPage().getPdfObject();
        PdfArray annots = pageDict.getAsArray(PdfName.Annots);
        if (annots != null) {
            for (int i = 0; i < annots.size(); i++) {
                changeAuthor(annots.getAsDictionary(i));
            }
        }

        pdfDoc.close();
    }

    public void changeAuthor(PdfDictionary annotation) {
        if (annotation == null) {
            return;
        }
        PdfString t = annotation.getAsString(PdfName.T);
        if (t == null) {
            return;
        }
        if ("iText".equals(t.toString())) {
            annotation.put(PdfName.T, new PdfString("Bruno Lowagie"));
        }
    }
}
