/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/29096314/how-to-make-an-image-a-qualified-mask-candidate-in-itext
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class MakeJpgMask extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/make_jpg_mask.pdf";
    public static final String IMAGE = "./src/test/resources/img/javaone2013.jpg";
    public static final String MASK = "./src/test/resources/img/berlin2013.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MakeJpgMask().manipulatePdf(DEST);
    }

    public static ImageData makeBlackAndWhitePng(String image) throws IOException {
        BufferedImage bi = ImageIO.read(new File(image));
        BufferedImage newBi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
        newBi.getGraphics().drawImage(bi, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(newBi, "png", baos);
        return ImageDataFactory.create(baos.toByteArray());
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        PageSize pageSize = new PageSize(PageSize.A4).rotate();
        Document doc = new Document(pdfDoc, pageSize);

        ImageData image = ImageDataFactory.create(IMAGE);
        ImageData mask = makeBlackAndWhitePng(MASK);
        mask.makeMask();
        image.setImageMask(mask);

        Image img = new Image(image);
        img.scaleAbsolute(pageSize.getWidth(), pageSize.getHeight());
        img.setFixedPosition(0, 0);
        doc.add(img);

        doc.close();
    }
}
