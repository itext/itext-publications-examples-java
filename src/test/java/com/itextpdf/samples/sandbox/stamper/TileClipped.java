/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/32769493/tiling-with-itext-and-adding-margins
 */
package com.itextpdf.samples.sandbox.stamper;

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

@Category(SampleTest.class)
public class TileClipped extends GenericTest {
    public static final String DEST =
            "./target/test/resources/sandbox/stamper/tile_clipped.pdf";
    public static final String SRC =
            "./src/test/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TileClipped().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        float margin = 30;
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        Rectangle rect = srcDoc.getFirstPage().getPageSizeWithRotation();
        Rectangle pageSize = new Rectangle(rect.getWidth() + margin * 2, rect.getHeight() + margin * 2);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(new PageSize(pageSize));

        PdfCanvas content = new PdfCanvas(pdfDoc.addNewPage());
        PdfFormXObject page = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);
        // adding the same page 16 times with a different offset
        float x, y;
        for (int i = 0; i < 16; i++) {
            x = -rect.getWidth() * (i % 4) + margin;
            y = rect.getHeight() * (i / 4 - 3) + margin;
            content.rectangle(margin, margin, rect.getWidth(), rect.getHeight());
            content.clip();
            content.newPath();
            content.addXObject(page, 4, 0, 0, 4, x, y);
            if (15 != i) {
                content = new PdfCanvas(pdfDoc.addNewPage());
            }
        }
        srcDoc.close();
        pdfDoc.close();
    }
}
