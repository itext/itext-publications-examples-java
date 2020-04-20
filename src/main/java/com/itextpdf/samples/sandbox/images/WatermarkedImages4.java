package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;

public class WatermarkedImages4 {
    public static final String DEST = "./target/sandbox/images/watermarked_images4.pdf";

    public static final String IMAGE1 = "./src/main/resources/img/bruno.jpg";
    public static final String IMAGE2 = "./src/main/resources/img/dog.bmp";
    public static final String IMAGE3 = "./src/main/resources/img/fox.bmp";
    public static final String IMAGE4 = "./src/main/resources/img/bruno_ingeborg.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new WatermarkedImages4().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image img = getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE1)));
        doc.add(img);

        img = getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE2)));
        doc.add(img);

        img = getWatermarkedImage(pdfDoc, new Image(ImageDataFactory.create(IMAGE3)));
        doc.add(img);

        Image srcImage = new Image(ImageDataFactory.create(IMAGE4));
        srcImage.scaleToFit(400, 700);
        img = getWatermarkedImage(pdfDoc, srcImage);
        doc.add(img);

        doc.close();
    }

    private static Image getWatermarkedImage(PdfDocument pdfDocument, Image img) {
        float width = img.getImageScaledWidth();
        float height = img.getImageScaledHeight();
        PdfFormXObject template = new PdfFormXObject(new Rectangle(width, height));

        // Use a highlevel Canvas to add the image because the scaling properties were set to the
        // highlevel Image object.
        new Canvas(template, pdfDocument)
                .add(img)
                .close();

        new PdfCanvas(template, pdfDocument)
                .saveState()
                .setStrokeColor(ColorConstants.GREEN)
                .setLineWidth(3)
                .moveTo(width * 0.25f, height * 0.25f)
                .lineTo(width * 0.75f, height * 0.75f)
                .moveTo(width * 0.25f, height * 0.75f)
                .lineTo(width * 0.25f, height * 0.25f)
                .stroke()
                .setStrokeColor(ColorConstants.WHITE)
                .ellipse(0, 0, width, height)
                .stroke()
                .restoreState();

        return new Image(template);
    }
}
