/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/29561417/draw-lines-on-image-in-pdf-using-itextsharp
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class WatermarkedImages4 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/watermarked_images4.pdf";
    public static final String IMAGE1 = "./src/test/resources/img/bruno.jpg";
    public static final String IMAGE2 = "./src/test/resources/img/dog.bmp";
    public static final String IMAGE3 = "./src/test/resources/img/fox.bmp";
    public static final String IMAGE4 = "./src/test/resources/img/bruno_ingeborg.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new WatermarkedImages4().manipulatePdf(DEST);
    }

    public Image getWatermarkedImage(PdfDocument pdfDocument, Image img) {
        float width = img.getImageScaledWidth();
        float height = img.getImageScaledHeight();
        PdfFormXObject template = new PdfFormXObject(new Rectangle(width, height));
        new Canvas(template, pdfDocument).add(img);
        new PdfCanvas(template, pdfDocument).
                saveState().
                setStrokeColor(Color.GREEN).
                setLineWidth(3).
                moveTo(width * .25f, height * .25f).
                lineTo(width * .75f, height * .75f).
                moveTo(width * .25f, height * .75f).
                lineTo(width * .25f, height * .25f).
                stroke().
                setStrokeColor(Color.WHITE).
                ellipse(0, 0, width, height).
                stroke().
                restoreState();
        return new Image(template);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        doc.add(getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE1))));
        doc.add(getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE2))));
        doc.add(getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE3))));
        Image image = new Image(ImageDataFactory.create(IMAGE4));
        image.scaleToFit(400, 700);
        doc.add(getWatermarkedImage(pdfDoc, image));
        doc.close();
    }
}
