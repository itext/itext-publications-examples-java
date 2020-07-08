package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;

public class ShrinkPdf {
    public static final String DEST = "./target/sandbox/stamper/shrink_pdf.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ShrinkPdf().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        for (int p = 1; p <= pdfDoc.getNumberOfPages(); p++) {
            PdfPage page = pdfDoc.getPage(p);
            Rectangle media = page.getCropBox();
            if (media == null) {
                media = page.getMediaBox();
            }

            // Shrink the page to 50%
            Rectangle crop = new Rectangle(0, 0, media.getWidth() / 2, media.getHeight() / 2);
            page.setMediaBox(crop);
            page.setCropBox(crop);

            // The content, placed on a content stream before, will be rendered before the other content
            // and, therefore, could be understood as a background (bottom "layer")
            new PdfCanvas(page.newContentStreamBefore(),
                    page.getResources(), pdfDoc).writeLiteral("\nq 0.5 0 0 0.5 0 0 cm\nq\n");

            // The content, placed on a content stream after, will be rendered after the other content
            // and, therefore, could be understood as a foreground (top "layer")
            new PdfCanvas(page.newContentStreamAfter(),
                    page.getResources(), pdfDoc).writeLiteral("\nQ\nQ\n");
        }

        pdfDoc.close();
    }
}
