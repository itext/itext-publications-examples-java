package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;

public class WatermarkToTheSide {
    public static final String DEST = "./target/sandbox/stamper/watermark_to_the_side.pdf";
    public static final String SRC = "./src/main/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new WatermarkToTheSide().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        for (int p = 1; p <= pdfDoc.getNumberOfPages(); p++) {
            Rectangle pageSize = pdfDoc.getPage(p).getPageSize();

            PdfCanvas canvas = new PdfCanvas(pdfDoc.getPage(p));

            // In case the page has a rotation, then new content will be automatically rotated.
            // Such an automatic rotation means, that we should consider page as if it's not rotated.
            // This is the particular case for the page 3 below
            if (p == 3) {

                // The width of the page rotated by 90 degrees corresponds to the height of the unrotated one.
                // The left side of the page rotated by 90 degrees corresponds to the bottom of the unrotated page.
                drawText(canvas, pdfDoc, pageSize, pageSize.getWidth() / 2, 18, 180);
                drawText(canvas, pdfDoc, pageSize, pageSize.getWidth() / 2, 34, 180);
            } else {
                drawText(canvas, pdfDoc, pageSize, pageSize.getLeft() + 18,
                        (pageSize.getTop() + pageSize.getBottom()) / 2, 90);
                drawText(canvas, pdfDoc, pageSize, pageSize.getLeft() + 34,
                        (pageSize.getTop() + pageSize.getBottom()) / 2, 90);
            }
        }

        pdfDoc.close();
    }

    private static void drawText(PdfCanvas canvas, PdfDocument pdfDoc, Rectangle pageSize, float x, float y, double rotation) {
        Canvas canvasDrawText = new Canvas(canvas, pageSize)
                .showTextAligned("This is some extra text added to the left of the page",
                        x, y, TextAlignment.CENTER, (float) Math.toRadians(rotation));
        canvasDrawText.close();
    }
}
