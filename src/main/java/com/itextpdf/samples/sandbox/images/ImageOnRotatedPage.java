package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;

public class ImageOnRotatedPage {
    public static final String DEST = "./target/sandbox/images/image_on_rotated_page.pdf";

    public static final String IMAGE = "./src/main/resources/img/cardiogram.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ImageOnRotatedPage().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        Image img = new Image(ImageDataFactory.create(IMAGE));
        img.scaleToFit(770, 523);
        float offsetX = (770 - img.getImageScaledWidth()) / 2;
        float offsetY = (523 - img.getImageScaledHeight()) / 2;
        img.setFixedPosition(36 + offsetX, 36 + offsetY);
        doc.add(img);

        doc.close();
    }
}
