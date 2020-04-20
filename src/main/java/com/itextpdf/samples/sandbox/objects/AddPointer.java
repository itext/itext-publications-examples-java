package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;
import java.io.IOException;

public class AddPointer {
    public static final String DEST = "./target/sandbox/objects/add_pointer.pdf";
    public static final String IMG = "./src/main/resources/img/map_cic.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddPointer().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Image img = new Image(ImageDataFactory.create(IMG));
        Document doc = new Document(pdfDoc, new PageSize(img.getImageWidth(), img.getImageHeight()));

        img.setFixedPosition(0, 0);
        doc.add(img);

        // Added a custom shape on top of a image
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas.setStrokeColor(ColorConstants.RED)
                .setLineWidth(3)
                .moveTo(220, 330)
                .lineTo(240, 370)
                .arc(200, 350, 240, 390, 0, 180)
                .lineTo(220, 330)
                .closePathStroke()
                .setFillColor(ColorConstants.RED)
                .circle(220, 370, 10)
                .fill();

        doc.close();
    }
}
