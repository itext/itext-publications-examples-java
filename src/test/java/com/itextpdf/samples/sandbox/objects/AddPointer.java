/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/26752663/adding-maps-at-itext-java
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class AddPointer extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/add_pointer.pdf";
    public static final String IMG = "./src/test/resources/img/map_cic.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddPointer().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Image img = new Image(ImageDataFactory.create(IMG));
        Document doc = new Document(pdfDoc, new PageSize(img.getImageWidth(), img.getImageHeight()));

        img.setFixedPosition(0, 0);
        doc.add(img);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas.setStrokeColor(Color.RED)
                .setLineWidth(3)
                .moveTo(220, 330)
                .lineTo(240, 370)
                .arc(200, 350, 240, 390, 0, (float) 180)
                .lineTo(220, 330)
                .closePathStroke()
                .setFillColor(Color.RED)
                .circle(220, 370, 10)
                .fill();

        doc.close();
    }
}
