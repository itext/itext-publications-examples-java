package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;
import java.util.Locale;

public class ClipPdf {
    public static final String DEST = "./target/sandbox/stamper/clip_pdf.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ClipPdf().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        for (int p = 1; p <= pdfDoc.getNumberOfPages(); p++) {
            PdfPage page = pdfDoc.getPage(p);
            Rectangle media = page.getCropBox();

            if (media == null) {
                media = page.getMediaBox();
            }
            float llx = media.getX() + 200;
            float lly = media.getY() + 200;
            float w = media.getWidth() - 400;
            float h = media.getHeight() - 400;

            // It's important to write explicit Locale settings, because decimal separator differs in
            // different regions and in PDF only dot is respected
            String command = String.format(Locale.ENGLISH,

                    // re operator constructs a rectangle
                    // W operator - sets the clipping path
                    // n operator - starts a new path
                    // q, Q - operators save and restore the graphics state stack
                    "\nq %.2f %.2f %.2f %.2f re W n\nq\n", llx, lly, w, h);

            // The content, placed on a content stream before, will be rendered before the other content
            // and, therefore, could be understood as a background (bottom "layer")
            PdfPage pdfPage = pdfDoc.getPage(p);
            new PdfCanvas(pdfPage.newContentStreamBefore(), pdfPage.getResources(), pdfDoc)
                    .writeLiteral(command);

            // The content, placed on a content stream after, will be rendered after the other content
            // and, therefore, could be understood as a foreground (top "layer")
            new PdfCanvas(pdfPage.newContentStreamAfter(), pdfPage.getResources(), pdfDoc)
                    .writeLiteral("\nQ\nQ\n");
        }

        pdfDoc.close();
    }
}
