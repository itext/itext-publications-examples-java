/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This sample is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/21958449/can-itextsharp-generate-pdf-with-jpeg-images-that-are-multi-stage-filtered-both
 * <p>
 * The question was about adding compression to an image that already used /DCTDecode
 * <p>
 * IMPORTANT:
 * This sample uses kernel iText functionality that was written in answer to the question.
 * This example will only work starting with iText 5.5.1
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.CompressionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class FlateCompressJPEG1Pass extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/flate_compress_jpeg_1pass.pdf";
    public static final String IMAGE = "./src/test/resources/img/berlin2013.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FlateCompressJPEG1Pass().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        PageSize pageSize = new PageSize(PageSize.A4).rotate();

        Document doc = new Document(pdfDoc, pageSize);

        Image image = new Image(ImageDataFactory.create(IMAGE));
        image.getXObject().getPdfObject().setCompressionLevel(CompressionConstants.BEST_COMPRESSION);
        image.scaleToFit(PageSize.A4.rotate().getWidth(), pageSize.getHeight());
        image.setFixedPosition(0, 0);

        doc.add(image);

        doc.close();
    }
}
