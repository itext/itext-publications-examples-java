package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;
import java.io.IOException;

public class Rectangles {
    public static final String DEST = "./target/sandbox/objects/rectangles.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Rectangles().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        float llx = 36;
        float lly = 700;
        float urx = 200;
        float ury = 806;

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        Rectangle rect1 = new Rectangle(llx, lly, urx - llx, ury - lly);
        canvas
                .setStrokeColor(ColorConstants.BLACK)
                .setLineWidth(1)
                .setFillColor(new DeviceGray(0.9f))
                .rectangle(rect1)
                .fillStroke();

        Rectangle rect2 = new Rectangle(llx + 60, lly, urx - llx - 60, ury - 40 - lly);
        canvas
                .setStrokeColor(ColorConstants.WHITE)
                .setLineWidth(0.5f)
                .setFillColor(new DeviceGray(0.1f))
                .rectangle(rect2)
                .fillStroke();

        pdfDoc.close();
    }
}
