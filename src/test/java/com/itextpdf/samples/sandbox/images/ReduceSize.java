/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/30483622/compressing-images-in-existing-pdfs-makes-the-resulting-pdf-file-bigger-lowagie
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

@Category(SampleTest.class)
public class ReduceSize extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/reduce_size.pdf";
    public static final String SRC = "./src/test/resources/pdfs/single_image.pdf";
    public static final float FACTOR = 0.5f;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ReduceSize().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfWriter writer = new PdfWriter(DEST, new WriterProperties().setFullCompressionMode(true));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), writer);
        PdfObject object;
        PdfStream stream;
        for (PdfIndirectReference indRef : pdfDoc.listIndirectReferences()) {
            object = indRef.getRefersTo();
            if (object == null || !object.isStream()) {
                continue;
            }
            stream = (PdfStream) object;
            if (!PdfName.Image.equals(stream.getAsName(PdfName.Subtype))) {
                continue;
            }
            if (!PdfName.DCTDecode.equals(stream.getAsName(PdfName.Filter))) {
                continue;
            }
            PdfImageXObject image = new PdfImageXObject(stream);
            BufferedImage bi = image.getBufferedImage();
            if (bi == null) {
                continue;
            }
            int width = (int) (bi.getWidth() * FACTOR);
            int height = (int) (bi.getHeight() * FACTOR);
            if (width <= 0 || height <= 0) {
                continue;
            }
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            AffineTransform at = AffineTransform.getScaleInstance(FACTOR, FACTOR);
            Graphics2D g = img.createGraphics();
            g.drawRenderedImage(bi, at);
            ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
            ImageIO.write(img, "JPG", imgBytes);
            stream.clear();
            stream.setData(imgBytes.toByteArray());
            stream.put(PdfName.Type, PdfName.XObject);
            stream.put(PdfName.Subtype, PdfName.Image);
            stream.put(PdfName.Filter, PdfName.DCTDecode);
            stream.put(PdfName.Width, new PdfNumber(width));
            stream.put(PdfName.Height, new PdfNumber(height));
            stream.put(PdfName.BitsPerComponent, new PdfNumber(8));
            stream.put(PdfName.ColorSpace, PdfName.DeviceRGB);
        }
        pdfDoc.close();
    }
}
