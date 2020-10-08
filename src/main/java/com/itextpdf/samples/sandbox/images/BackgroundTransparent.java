package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class BackgroundTransparent {
    public static final String DEST = "./target/sandbox/images/background_transparent.pdf";

    public static final String IMAGE = "./src/main/resources/img/berlin2013.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new BackgroundTransparent().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PageSize pageSize = PageSize.A4.rotate();
        Document doc = new Document(pdfDoc, pageSize);

        ImageData image = ImageDataFactory.create(IMAGE);
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.saveState();
        PdfExtGState state = new PdfExtGState().setFillOpacity(0.6f);
        canvas.setExtGState(state);
        Rectangle rect = new Rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
        canvas.addImageFittedIntoRectangle(image, rect, false);
        canvas.restoreState();

        doc.add(new Paragraph("Berlin!"));

        doc.close();
    }
}
