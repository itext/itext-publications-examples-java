package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.element.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ReplaceImage {
    public static final String DEST = "./target/sandbox/images/replace_image.pdf";

    public static final String SRC = "./src/main/resources/pdfs/image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ReplaceImage().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        // Assume that there is a single XObject in the source document
        // and this single object is an image.
        PdfDictionary pageDict = pdfDoc.getFirstPage().getPdfObject();
        PdfDictionary resources = pageDict.getAsDictionary(PdfName.Resources);
        PdfDictionary xObjects = resources.getAsDictionary(PdfName.XObject);
        PdfName imgRef = xObjects.keySet().iterator().next();
        PdfStream stream = xObjects.getAsStream(imgRef);
        Image img = convertToBlackAndWhitePng(new PdfImageXObject(stream));

        // Replace the original image with the grayscale image
        xObjects.put(imgRef, img.getXObject().getPdfObject());

        pdfDoc.close();
    }

    private static Image convertToBlackAndWhitePng(PdfImageXObject image) throws IOException {
        BufferedImage bi = image.getBufferedImage();
        BufferedImage newBi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
        newBi.getGraphics().drawImage(bi, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(newBi, "png", baos);
        return new Image(ImageDataFactory.create(baos.toByteArray()));
    }
}
