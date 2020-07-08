package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;
import java.util.Locale;

public class ShrinkPdf2 {
    public static final String DEST = "./target/sandbox/stamper/shrink_pdf2.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ShrinkPdf2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        // Please note that we don't change the page size in this example, but only shrink the content (in this case to 80%)
        // and the content is shrunk to center of the page, leaving bigger margins to the top, bottom, left and right
        float percentage = 0.8f;
        for (int p = 1; p <= pdfDoc.getNumberOfPages(); p++) {
            PdfPage pdfPage = pdfDoc.getPage(p);
            Rectangle pageSize = pdfPage.getPageSize();

            // Applying the scaling in both X, Y direction to preserve the aspect ratio.
            float offsetX = (pageSize.getWidth() * (1 - percentage)) / 2;
            float offsetY = (pageSize.getHeight() * (1 - percentage)) / 2;

            // The content, placed on a content stream before, will be rendered before the other content
            // and, therefore, could be understood as a background (bottom "layer")
            new PdfCanvas(pdfPage.newContentStreamBefore(), pdfPage.getResources(), pdfDoc)
                    .writeLiteral(String.format(Locale.ENGLISH, "\nq %s 0 0 %s %s %s cm\nq\n",
                            percentage, percentage, offsetX, offsetY));

            // The content, placed on a content stream after, will be rendered after the other content
            // and, therefore, could be understood as a foreground (top "layer")
            new PdfCanvas(pdfPage.newContentStreamAfter(), pdfPage.getResources(), pdfDoc)
                    .writeLiteral("\nQ\nQ\n");
        }

        pdfDoc.close();
    }
}
