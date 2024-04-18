/**
 * <p>
 * Example that shows how to scale an existing PDF using the UserUnit and how to remove the rotation of a page.
 */

package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

public class ScaleRotate {
    public static final String DEST = "./target/sandbox/stamper/scale_rotate.pdf";
    public static final String SRC = "./src/main/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ScaleRotate().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        for (int p = 1; p <= pdfDoc.getNumberOfPages(); p++) {
            PdfDictionary page = pdfDoc.getPage(p).getPdfObject();
            if (page.getAsNumber(PdfName.UserUnit) == null) {
                page.put(PdfName.UserUnit, new PdfNumber(2.5f));
            }
            page.remove(PdfName.Rotate);
        }

        pdfDoc.close();
    }
}
