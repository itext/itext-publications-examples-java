/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/26814958/pdf-vertical-postion-method-gives-the-next-page-position-instead-of-current-page
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class WatermarkedImages1 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/watermarked_images1.pdf";
    public static final String IMAGE1 = "./src/test/resources/img/bruno.jpg";
    public static final String IMAGE2 = "./src/test/resources/img/dog.bmp";
    public static final String IMAGE3 = "./src/test/resources/img/fox.bmp";
    public static final String IMAGE4 = "./src/test/resources/img/bruno_ingeborg.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new WatermarkedImages1().manipulatePdf(DEST);
    }

    public Image getWatermarkedImage(PdfDocument pdfDoc, Image img, String watermark) {
        float width = img.getImageScaledWidth();
        float height = img.getImageScaledHeight();
        PdfFormXObject template = new PdfFormXObject(new Rectangle(width, height));
        new Canvas(template, pdfDoc).
                add(img).
                setFontColor(DeviceGray.WHITE).
                showTextAligned(watermark, width / 2, height / 2, TextAlignment.CENTER, (float) Math.PI / 6);
        return new Image(template);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        doc.add(getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE1)), "Bruno"));
        doc.add(getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE2)), "Dog"));
        doc.add(getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE3)), "Fox"));
        Image image = new Image(ImageDataFactory.create(IMAGE4));
        image.scaleToFit(400, 700);
        doc.add(getWatermarkedImage(pdfDoc, image, "Bruno and Ingeborg"));
        doc.close();
    }
}
