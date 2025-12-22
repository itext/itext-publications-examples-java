package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.CompressionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;

/*
 * FlateCompressJPEG1Pass.java
 *
 * Example showing how to add additional compression to JPEG images.
 * Demonstrates applying best compression level to DCT-encoded images.
 */
public class FlateCompressJPEG1Pass {
    public static final String DEST = "./target/sandbox/images/flate_compress_jpeg_1pass.pdf";

    public static final String IMAGE = "./src/main/resources/img/berlin2013.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FlateCompressJPEG1Pass().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PageSize pageSize = PageSize.A4.rotate();
        Document doc = new Document(pdfDoc, pageSize);

        Image image = new Image(ImageDataFactory.create(IMAGE));
        image.getXObject().getPdfObject().setCompressionLevel(CompressionConstants.BEST_COMPRESSION);
        image.scaleAbsolute(pageSize.getWidth(), pageSize.getHeight());
        image.setFixedPosition(0, 0);
        doc.add(image);

        doc.close();
    }
}
