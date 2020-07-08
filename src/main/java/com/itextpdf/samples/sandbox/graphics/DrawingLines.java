package com.itextpdf.samples.sandbox.graphics;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;

public class DrawingLines {
    public static final String DEST = "./target/sandbox/graphics/drawing_lines.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DrawingLines().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());

        // Create a 100% Magenta color
        Color magentaColor = new DeviceCmyk(0.f, 1.f, 0.f, 0.f);
        canvas
                .setStrokeColor(magentaColor)
                .moveTo(36, 36)
                .lineTo(36, 806)
                .lineTo(559, 36)
                .lineTo(559, 806)
                .closePathStroke();

        pdfDoc.close();
    }
}
