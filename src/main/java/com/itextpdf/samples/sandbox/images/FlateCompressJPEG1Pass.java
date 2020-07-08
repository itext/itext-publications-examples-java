/**
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

import java.io.File;

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
