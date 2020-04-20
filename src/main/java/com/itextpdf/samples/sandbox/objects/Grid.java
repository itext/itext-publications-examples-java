package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;
import java.io.IOException;

public class Grid {
    public static final String DEST = "./target/sandbox/objects/grid.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Grid().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        PageSize pageSize = new PageSize(612, 792);
        pdfDoc.setDefaultPageSize(pageSize);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        for (float x = 0; x < pageSize.getWidth(); x += 72f) {
            for (float y = 0; y < pageSize.getHeight(); y += 72f) {
                canvas.circle(x, y, 1f);
            }
        }
        canvas.fill();

        pdfDoc.close();
    }
}
