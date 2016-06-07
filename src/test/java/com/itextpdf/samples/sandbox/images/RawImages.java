/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This sample is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27070222/how-to-create-unique-images-with-image-getinstance
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class RawImages extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/raw_images.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RawImages().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        Image gray = new Image(ImageDataFactory.create(1, 1, 1, 8,
                new byte[]{(byte) 0x80}, null));
        gray.scaleToFit(30, 30);

        Image red = new Image(ImageDataFactory.create(1, 1, 3, 8,
                new byte[]{(byte) 255, (byte) 0, (byte) 0}, null));
        red.scaleToFit(30, 30);

        Image green = new Image(ImageDataFactory.create(1, 1, 3, 8,
                new byte[]{(byte) 0, (byte) 255, (byte) 0}, null));
        green.scaleToFit(30, 30);

        Image blue = new Image(ImageDataFactory.create(1, 1, 3, 8,
                new byte[]{(byte) 0, (byte) 0, (byte) 255}, null));
        blue.scaleToFit(30, 30);

        Image cyan = new Image(ImageDataFactory.create(1, 1, 4, 8,
                new byte[]{(byte) 255, (byte) 0, (byte) 0, (byte) 0}, null));
        cyan.scaleToFit(30, 30);

        Image magenta = new Image(ImageDataFactory.create(1, 1, 4, 8,
                new byte[]{(byte) 0, (byte) 255, (byte) 0, (byte) 0}, null));
        magenta.scaleToFit(30, 30);

        Image yellow = new Image(ImageDataFactory.create(1, 1, 4, 8,
                new byte[]{(byte) 0, (byte) 0, (byte) 255, (byte) 0}, null));
        yellow.scaleToFit(30, 30);

        Image black = new Image(ImageDataFactory.create(1, 1, 4, 8,
                new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 255}, null));
        black.scaleToFit(30, 30);

        doc.add(gray);
        doc.add(red);
        doc.add(green);
        doc.add(blue);
        doc.add(cyan);
        doc.add(magenta);
        doc.add(yellow);
        doc.add(black);

        doc.close();
    }
}
