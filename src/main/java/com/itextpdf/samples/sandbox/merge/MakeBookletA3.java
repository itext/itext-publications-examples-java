package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;
import java.io.IOException;

public class MakeBookletA3 {
    public static final String DEST = "./target/sandbox/merge/make_booklet_a3.pdf";

    public static final String SRC = "./src/main/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MakeBookletA3().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        float a4_width = PageSize.A4.getWidth();
        PageSize pageSize = new PageSize(a4_width * 2, PageSize.A4.getHeight());
        pdfDoc.setDefaultPageSize(pageSize);

        int numberOfPages = srcDoc.getNumberOfPages();
        int p = 1;
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        while ((p - 1) <= numberOfPages) {
            copyPage(canvas, srcDoc, pdfDoc, p + 3, 0);
            copyPage(canvas, srcDoc, pdfDoc, p, a4_width);

            canvas = new PdfCanvas(pdfDoc.addNewPage());
            copyPage(canvas, srcDoc, pdfDoc, p + 1, 0);
            copyPage(canvas, srcDoc, pdfDoc, p + 2, a4_width);

            if ((p - 1) / 4 < numberOfPages / 4) {
                canvas = new PdfCanvas(pdfDoc.addNewPage());
            }

            p += 4;
        }

        pdfDoc.close();
        srcDoc.close();
    }

    private static void copyPage(PdfCanvas canvas, PdfDocument srcDoc, PdfDocument pdfDoc,
            int pageNumber, float offsetX) throws IOException {
        if (pageNumber > srcDoc.getNumberOfPages()) {
            return;
        }

        PdfFormXObject page = srcDoc.getPage(pageNumber).copyAsFormXObject(pdfDoc);
        canvas.addXObjectAt(page, offsetX, 0);
    }
}
