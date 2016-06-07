/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/33609564/how-to-modify-size-for-pdfannotations-in-itext-and-disable-some-options
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class MovePopup extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/annotations/move_popup.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello_sticky_note.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MovePopup().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfDictionary page = pdfDoc.getFirstPage().getPdfObject();
        PdfArray annots = page.getAsArray(PdfName.Annots);

        PdfDictionary sticky = annots.getAsDictionary(0);
        PdfArray stickyRect = sticky.getAsArray(PdfName.Rect);
        PdfArray stickyRectangle = new PdfArray(new float[]{
                stickyRect.getAsNumber(0).floatValue() - 120, stickyRect.getAsNumber(1).floatValue() - 70,
                stickyRect.getAsNumber(2).floatValue(), stickyRect.getAsNumber(3).floatValue() - 30
        });
        sticky.put(PdfName.Rect, stickyRectangle);

        PdfDictionary popup = annots.getAsDictionary(1);
        PdfArray popupRect = popup.getAsArray(PdfName.Rect);
        PdfArray popupRectangle = new PdfArray(new float[]{
                popupRect.getAsNumber(0).floatValue() - 250, popupRect.getAsNumber(1).floatValue(),
                popupRect.getAsNumber(2).floatValue(), popupRect.getAsNumber(3).floatValue() - 250
        });
        popup.put(PdfName.Rect, popupRectangle);

        doc.close();
    }
}
