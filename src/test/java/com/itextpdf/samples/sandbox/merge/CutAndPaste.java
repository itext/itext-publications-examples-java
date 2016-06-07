/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie.
 * http://stackoverflow.com/questions/28368317/itext-or-itextsharp-move-text-in-an-existing-pdf
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
public class CutAndPaste extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/merge/page229_cut_paste.pdf";
    public static final String SRC = "./src/test/resources/pdfs/page229.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CutAndPaste().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        Rectangle toMove = new Rectangle(100, 500, 100, 100);

        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        Rectangle pageSize = srcDoc.getFirstPage().getPageSize();

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        pdfDoc.setDefaultPageSize(new PageSize(pageSize));
        pdfDoc.addNewPage();

        PdfFormXObject t_canvas1 = new PdfFormXObject(pageSize);
        PdfCanvas canvas1 = new PdfCanvas(t_canvas1, pdfDoc);

        PdfFormXObject pageXObject = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);

        canvas1.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
        canvas1.rectangle(toMove.getLeft(), toMove.getBottom(),
                toMove.getWidth(), toMove.getHeight());
        canvas1.eoClip();
        canvas1.newPath();
        canvas1.addXObject(pageXObject, 0, 0);

        PdfFormXObject t_canvas2 = new PdfFormXObject(pageSize);
        PdfCanvas canvas2 = new PdfCanvas(t_canvas2, pdfDoc);
        canvas2.rectangle(toMove.getLeft(), toMove.getBottom(),
                toMove.getWidth(), toMove.getHeight());
        canvas2.clip();
        canvas2.newPath();
        canvas2.addXObject(pageXObject, 0, 0);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas.addXObject(t_canvas1, 0, 0);
        canvas.addXObject(t_canvas2, -20, -2);

        srcDoc.close();
        pdfDoc.close();
    }
}
