/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to a question on StackOverflow:
 * http://stackoverflow.com/questions/27011829/divide-one-page-pdf-file-in-two-pages-pdf-file
 */
package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class TileInTwo2 extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/merge/tile_in_two2.pdf";
    public static final String SRC
            = "./src/test/resources/pdfs/united_states.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TileInTwo2().manipulatePdf(DEST);
    }

    public static PageSize getHalfPageSize(Rectangle pageSize) {
        float width = pageSize.getWidth();
        float height = pageSize.getHeight();
        return new PageSize(width / 2, height);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PageSize mediaBox = getHalfPageSize(srcDoc.getFirstPage().getPageSizeWithRotation());

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        pdfDoc.setDefaultPageSize(mediaBox);

        PdfCanvas canvas;
        int n = srcDoc.getNumberOfPages();
        int i = 1;
        while (true) {
            PdfFormXObject page = srcDoc.getPage(i).copyAsFormXObject(pdfDoc);
            canvas = new PdfCanvas(pdfDoc.addNewPage());
            canvas.addXObject(page, 0, 0);
            canvas = new PdfCanvas(pdfDoc.addNewPage());
            canvas.addXObject(page, -mediaBox.getWidth(), 0);
            if (++i > n) {
                break;
            }
            mediaBox = getHalfPageSize(srcDoc.getPage(i).getPageSizeWithRotation());
            pdfDoc.setDefaultPageSize(mediaBox);
        }
        pdfDoc.close();
        srcDoc.close();
    }
}
