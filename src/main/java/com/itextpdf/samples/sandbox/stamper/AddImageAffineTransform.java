package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;

public class AddImageAffineTransform {
    public static final String DEST = "./target/sandbox/stamper/add_image_affine_transform.pdf";
    public static final String IMG = "./src/main/resources/img/bruno.jpg";
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddImageAffineTransform().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        ImageData image = ImageDataFactory.create(IMG);

        // Translation defines the position of the image on the page and scale transformation sets image dimensions
        // Please also note that image without scaling is drawn in 1x1 rectangle. And here we draw image on page using
        // its original size in pixels.
        AffineTransform affineTransform = AffineTransform.getTranslateInstance(36, 300);

        // Make sure that the image is visible by concatenating a scale transformation
        affineTransform.concatenate(AffineTransform.getScaleInstance(image.getWidth(), image.getHeight()));

        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        float[] matrix = new float[6];
        affineTransform.getMatrix(matrix);
        canvas.addImageWithTransformationMatrix(image, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
        pdfDoc.close();
    }
}
