package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;

public class WatermarkedImages1 {
    public static final String DEST = "./target/sandbox/images/watermarked_images1.pdf";

    public static final String IMAGE1 = "./src/main/resources/img/bruno.jpg";
    public static final String IMAGE2 = "./src/main/resources/img/dog.bmp";
    public static final String IMAGE3 = "./src/main/resources/img/fox.bmp";
    public static final String IMAGE4 = "./src/main/resources/img/bruno_ingeborg.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new WatermarkedImages1().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image image = getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE1)), "Bruno");
        doc.add(image);

        image = getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE2)), "Dog");
        doc.add(image);

        image = getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE3)), "Fox");
        doc.add(image);

        Image srcImage = new Image(ImageDataFactory.create(IMAGE4));
        srcImage.scaleToFit(400, 700);
        image = getWatermarkedImage(pdfDoc, srcImage, "Bruno and Ingeborg");
        doc.add(image);

        doc.close();
    }

    private static Image getWatermarkedImage(PdfDocument pdfDoc, Image img, String watermark) {
        float width = img.getImageScaledWidth();
        float height = img.getImageScaledHeight();
        PdfFormXObject template = new PdfFormXObject(new Rectangle(width, height));
        new Canvas(template, pdfDoc)
                .add(img)
                .setFontColor(DeviceGray.WHITE)
                .showTextAligned(watermark, width / 2, height / 2, TextAlignment.CENTER, (float) Math.PI / 6)
                .close();
        return new Image(template);
    }
}
