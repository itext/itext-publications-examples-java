package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

public class Rotate90Degrees {
    public static final String DEST = "./target/sandbox/stamper/rotate90degrees.pdf";
    public static final String SRC = "./src/main/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Rotate90Degrees().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        for (int p = 1; p <= pdfDoc.getNumberOfPages(); p++) {
            PdfPage page = pdfDoc.getPage(p);
            int rotate = page.getRotation();
            if (rotate == 0) {
                page.setRotation(90);
            } else {
                page.setRotation((rotate + 90) % 360);
            }
        }

        pdfDoc.close();
    }
}
