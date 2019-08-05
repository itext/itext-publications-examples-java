/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie.
 */
package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;
import java.io.IOException;

public class TilingHero {
    public static final String DEST
            = "./target/sandbox/merge/tiling_hero.pdf";
    public static final String RESOURCE
            = "./src/test/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TilingHero().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(RESOURCE));

        Rectangle pageSize = srcDoc.getFirstPage().getPageSizeWithRotation();
        float width = pageSize.getWidth();
        float height = pageSize.getHeight();
        Rectangle mediaBox = new Rectangle(0, 3 * height, width, height);

        PdfDocument resultDoc = new PdfDocument(new PdfWriter(DEST));
        resultDoc.setDefaultPageSize(new PageSize(mediaBox));
        PdfFormXObject page = srcDoc.getFirstPage().copyAsFormXObject(resultDoc);
        PdfCanvas canvas;
        for (int i = 0; i < 16; ) {
            canvas = new PdfCanvas(resultDoc.addNewPage());
            canvas.addXObject(page, 4, 0, 0, 4, 0, 0);
            i++;
            mediaBox = new Rectangle(
                    (i % 4) * width, (4 - (i / 4)) * height,
                    width, -height);
            resultDoc.setDefaultPageSize(new PageSize(mediaBox));
        }

        srcDoc.close();
        resultDoc.close();
    }
}
