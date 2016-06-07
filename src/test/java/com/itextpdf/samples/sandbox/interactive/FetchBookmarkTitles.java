/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/28634172/java-reading-pdf-bookmark-names-with-itext
 */
package com.itextpdf.samples.sandbox.interactive;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.List;

@Category(SampleTest.class)
public class FetchBookmarkTitles {
    public static final String SRC = "./src/test/resources/pdfs/bookmarks.pdf";
    public static final String RESULT = "2011-10-12\n" +
            "2011-10-13\n" +
            "2011-10-14\n" +
            "2011-10-15\n" +
            "2011-10-16\n" +
            "2011-10-17\n" +
            "2011-10-18\n" +
            "2011-10-19\n";

    @Test
    public void manipulatePdf() throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

        PdfOutline outlines = pdfDoc.getOutlines(false);
        List<PdfOutline> bookmarks = outlines.getAllChildren().get(0).getAllChildren();
        StringBuffer stringBuffer = new StringBuffer();
        for (PdfOutline bookmark : bookmarks) {
            showTitle(bookmark, stringBuffer);
        }
        pdfDoc.close();

        System.out.println(stringBuffer.toString());

        Assert.assertArrayEquals(RESULT.getBytes(), stringBuffer.toString().getBytes());
    }

    public void showTitle(PdfOutline outline, StringBuffer stringBuffer) {
        System.out.println(outline.getTitle());
        stringBuffer.append(outline.getTitle() + "\n");
        List<PdfOutline> kids = outline.getAllChildren();
        if (kids != null) {
            for (PdfOutline kid : kids) {
                showTitle(kid, stringBuffer);
            }
        }
    }
}
