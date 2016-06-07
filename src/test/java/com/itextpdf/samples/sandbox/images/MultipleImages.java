/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/29331838/add-multiple-images-into-a-single-pdf-file-with-itext-using-java
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
public class MultipleImages extends GenericTest {
    public static final String DEST =
            "./target/test/resources/sandbox/images/multiple_images.pdf";
    public static final String[] IMAGES = {
            "./src/test/resources/img/berlin2013.jpg",
            "./src/test/resources/img/javaone2013.jpg",
            "./src/test/resources/img/map_cic.png"
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MultipleImages().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        Image image = new Image(ImageDataFactory.create(IMAGES[0]));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(image.getImageWidth(), image.getImageHeight()));
        for (int i = 0; i < IMAGES.length; i++) {
            image = new Image(ImageDataFactory.create(IMAGES[i]));
            pdfDoc.addNewPage(new PageSize(image.getImageWidth(), image.getImageHeight()));
            // Notice that now it is not necessary to set image position,
            // because images are not overlapped while adding.
            image.setFixedPosition(i + 1, 0, 0);
            doc.add(image);
        }
        doc.close();
    }
}
