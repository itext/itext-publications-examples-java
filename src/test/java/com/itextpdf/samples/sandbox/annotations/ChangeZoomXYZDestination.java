/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22828782/all-links-of-existing-pdf-change-the-action-property-to-inherit-zoom-itext-lib
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ChangeZoomXYZDestination extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/annotations/change_zoom_xyz_destination.pdf";
    public static final String SRC = "./src/test/resources/pdfs/xyz_destination.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeZoomXYZDestination().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        PdfDictionary pageDict = pdfDoc.getPage(11).getPdfObject();
        PdfArray annots = pageDict.getAsArray(PdfName.Annots);
        for (int i = 0; i < annots.size(); i++) {
            PdfDictionary annotation = annots.getAsDictionary(i);
            if (PdfName.Link.equals(annotation.getAsName(PdfName.Subtype))) {
                PdfArray d = annotation.getAsArray(PdfName.Dest);
                if (d != null && d.size() == 5 && PdfName.XYZ.equals(d.getAsName(1)))
                    d.set(4, new PdfNumber(0));
            }
        }

        pdfDoc.close();
    }
}
