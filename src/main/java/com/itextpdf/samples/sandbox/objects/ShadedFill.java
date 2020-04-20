package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.kernel.pdf.colorspace.PdfShading;

import java.io.File;
import java.io.IOException;

public class ShadedFill {
    public static final String DEST = "./target/sandbox/objects/shaded_fill.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ShadedFill().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        float x = 36;
        float y = 740;

        // Side of an equilateral triangle
        float side = 70;

        PdfShading.Axial axialShading = new PdfShading.Axial(new PdfDeviceCs.Rgb(), x, y, ColorConstants.PINK.getColorValue(),
                x + side, y, ColorConstants.BLUE.getColorValue());
        PdfPattern.Shading shading = new PdfPattern.Shading(axialShading);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.setFillColorShading(shading);
        canvas.moveTo(x, y);
        canvas.lineTo(x + side, y);
        canvas.lineTo(x + (side / 2), (float) (y + (side * Math.sin(Math.PI / 3))));
        canvas.closePathFillStroke();

        pdfDoc.close();
    }
}
