package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class ReduceSize {
    public static final String DEST = "./target/sandbox/images/reduce_size.pdf";

    public static final String SRC = "./src/main/resources/pdfs/single_image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ReduceSize().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfWriter writer = new PdfWriter(dest, new WriterProperties().setFullCompressionMode(true));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), writer);
        float factor = 0.5f;

        for (PdfIndirectReference indRef : pdfDoc.listIndirectReferences()) {

            // Get a direct object and try to resolve indirects chain.
            // Note: If chain of references has length of more than 32,
            // this method return 31st reference in chain.
            PdfObject pdfObject = indRef.getRefersTo();
            if (pdfObject == null || !pdfObject.isStream()) {
                continue;
            }

            PdfStream stream = (PdfStream) pdfObject;
            if (!PdfName.Image.equals(stream.getAsName(PdfName.Subtype))) {
                continue;
            }

            if (!PdfName.DCTDecode.equals(stream.getAsName(PdfName.Filter))) {
                continue;
            }

            PdfImageXObject image = new PdfImageXObject(stream);
            BufferedImage origImage = image.getBufferedImage();
            if (origImage == null) {
                continue;
            }

            int width = (int) (origImage.getWidth() * factor);
            int height = (int) (origImage.getHeight() * factor);
            if (width <= 0 || height <= 0) {
                continue;
            }

            // Scale the image
            BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            AffineTransform at = AffineTransform.getScaleInstance(factor, factor);
            Graphics2D graphics = resultImage.createGraphics();
            graphics.drawRenderedImage(origImage, at);
            ByteArrayOutputStream scaledBitmapStream = new ByteArrayOutputStream();
            ImageIO.write(resultImage, "JPG", scaledBitmapStream);

            resetImageStream(stream, scaledBitmapStream.toByteArray(), width, height);
            scaledBitmapStream.close();
        }

        pdfDoc.close();
    }

    private static void resetImageStream(PdfStream stream, byte[] imgBytes, int imgWidth, int imgHeight) {
        stream.clear();
        stream.setData(imgBytes);
        stream.put(PdfName.Type, PdfName.XObject);
        stream.put(PdfName.Subtype, PdfName.Image);
        stream.put(PdfName.Filter, PdfName.DCTDecode);
        stream.put(PdfName.Width, new PdfNumber(imgWidth));
        stream.put(PdfName.Height, new PdfNumber(imgHeight));
        stream.put(PdfName.BitsPerComponent, new PdfNumber(8));
        stream.put(PdfName.ColorSpace, PdfName.DeviceRGB);
    }
}
