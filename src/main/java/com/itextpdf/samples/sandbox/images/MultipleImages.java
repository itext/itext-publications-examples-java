package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;

public class MultipleImages {
    public static final String DEST = "./target/sandbox/images/multiple_images.pdf";

    public static final String[] IMAGES = {
            "./src/main/resources/img/berlin2013.jpg",
            "./src/main/resources/img/javaone2013.jpg",
            "./src/main/resources/img/map_cic.png"
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MultipleImages().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        Image image = new Image(ImageDataFactory.create(IMAGES[0]));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(image.getImageWidth(), image.getImageHeight()));

        for (int i = 0; i < IMAGES.length; i++) {
            image = new Image(ImageDataFactory.create(IMAGES[i]));
            pdfDoc.addNewPage(new PageSize(image.getImageWidth(), image.getImageHeight()));
            image.setFixedPosition(i + 1, 0, 0);
            doc.add(image);
        }

        doc.close();
    }
}
