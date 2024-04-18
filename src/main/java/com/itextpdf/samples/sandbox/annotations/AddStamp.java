package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfStampAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;

public class AddStamp {
    public static final String DEST = "./target/sandbox/annotations/add_stamp.pdf";

    public static final String IMG = "./src/main/resources/img/itext.png";
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddStamp().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        ImageData img = ImageDataFactory.create(IMG);
        float width = img.getWidth();
        float height = img.getHeight();
        PdfFormXObject xObj = new PdfFormXObject(new Rectangle(width, height));

        PdfCanvas canvas = new PdfCanvas(xObj, pdfDoc);
        canvas.addImageAt(img, 0, 0, false);

        Rectangle location = new Rectangle(36, 770 - height, width, height);
        PdfStampAnnotation stamp = new PdfStampAnnotation(location);
        stamp.setStampName(new PdfName("ITEXT"));
        stamp.setNormalAppearance(xObj.getPdfObject());

        // Set to print the annotation when the page is printed
        stamp.setFlags(PdfAnnotation.PRINT);
        pdfDoc.getFirstPage().addAnnotation(stamp);

        pdfDoc.close();
    }
}
