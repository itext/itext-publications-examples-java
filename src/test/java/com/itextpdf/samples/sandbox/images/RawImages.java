/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
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

import java.io.File;

public class RawImages {
    public static final String DEST = "./target/sandbox/images/raw_images.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RawImages().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());
        int fitWidth = 30;
        int fitHeight = 30;

        // Add the gray square
        Image img = new Image(ImageDataFactory.create(1, 1, 1, 8,
                new byte[] {(byte) 0x80}, null));
        img.scaleToFit(fitWidth, fitHeight);
        doc.add(img);

        // Add the red square
        img = new Image(ImageDataFactory.create(1, 1, 3, 8,
                new byte[] {(byte) 255, (byte) 0, (byte) 0}, null));
        img.scaleToFit(fitWidth, fitHeight);
        doc.add(img);

        // Add the green square
        img = new Image(ImageDataFactory.create(1, 1, 3, 8,
                new byte[] {(byte) 0, (byte) 255, (byte) 0}, null));
        img.scaleToFit(fitWidth, fitHeight);
        doc.add(img);

        // Add the blue square
        img = new Image(ImageDataFactory.create(1, 1, 3, 8,
                new byte[] {(byte) 0, (byte) 0, (byte) 255}, null));
        img.scaleToFit(fitWidth, fitHeight);
        doc.add(img);

        // Add the cyan square
        img = new Image(ImageDataFactory.create(1, 1, 4, 8,
                new byte[] {(byte) 255, (byte) 0, (byte) 0, (byte) 0}, null));
        img.scaleToFit(fitWidth, fitHeight);
        doc.add(img);

        // Add the magenta square
        img = new Image(ImageDataFactory.create(1, 1, 4, 8,
                new byte[] {(byte) 0, (byte) 255, (byte) 0, (byte) 0}, null));
        img.scaleToFit(fitWidth, fitHeight);
        doc.add(img);

        // Add the yellow square
        img = new Image(ImageDataFactory.create(1, 1, 4, 8,
                new byte[] {(byte) 0, (byte) 0, (byte) 255, (byte) 0}, null));
        img.scaleToFit(fitWidth, fitHeight);
        doc.add(img);

        // Add the black square
        img = new Image(ImageDataFactory.create(1, 1, 4, 8,
                new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 255}, null));
        img.scaleToFit(fitWidth, fitHeight);
        doc.add(img);

        doc.close();
    }
}
