/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/26859473/how-to-show-an-image-with-large-dimensions-across-multiple-pages-in-itextpdf
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class TiledImage extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/tiled_image.pdf";
    public static final String IMAGE = "./src/test/resources/img/bruno_ingeborg.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TiledImage().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        ImageData image = ImageDataFactory.create(IMAGE);
        float width = image.getWidth();
        float height = image.getHeight();
        PageSize pageSize = new PageSize(width / 2, height / 2);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(pageSize);

        PdfCanvas canvas;
        canvas = new PdfCanvas(pdfDoc.addNewPage().newContentStreamBefore(),
                pdfDoc.getLastPage().getResources(), pdfDoc);
        canvas.addImage(image, width, 0, 0, height, 0, -height / 2, false);
        canvas = new PdfCanvas(pdfDoc.addNewPage().newContentStreamBefore(),
                pdfDoc.getLastPage().getResources(), pdfDoc);
        canvas.addImage(image, width, 0, 0, height, 0, 0, false);
        canvas = new PdfCanvas(pdfDoc.addNewPage().newContentStreamBefore(),
                pdfDoc.getLastPage().getResources(), pdfDoc);
        canvas.addImage(image, width, 0, 0, height, -width / 2, -height / 2, false);
        canvas = new PdfCanvas(pdfDoc.addNewPage().newContentStreamBefore(),
                pdfDoc.getLastPage().getResources(), pdfDoc);
        canvas.addImage(image, width, 0, 0, height, -width / 2, 0, false);

        pdfDoc.close();
    }
}
