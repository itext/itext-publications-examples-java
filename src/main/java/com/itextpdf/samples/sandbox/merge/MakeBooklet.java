package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;
import java.io.IOException;

public class MakeBooklet {
    public static final String DEST = "./target/sandbox/merge/make_booklet.pdf";

    public static final String SRC = "./src/main/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MakeBooklet().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        float a4Width = PageSize.A4.getWidth();
        float a4Height = PageSize.A4.getHeight();
        PageSize pagesize = new PageSize(a4Width * 4, a4Height * 2);
        pdfDoc.setDefaultPageSize(pagesize);

        int numberOfPages = srcDoc.getNumberOfPages();
        int p = 1;
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        while ((p - 1) <= numberOfPages) {
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 3, 0);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 12, a4Width);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 15, a4Width * 2);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p, a4Width * 3);
            canvas.saveState();

            // Rotate on 180 degrees and copy pages to the top row.
            AffineTransform at = AffineTransform.getRotateInstance((float) -Math.PI);
            at.concatenate(AffineTransform.getTranslateInstance(0, -a4Height * 2));
            canvas.concatMatrix(at);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 4, -a4Width);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 11, -a4Width * 2);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 8, -a4Width * 3);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 7, -a4Width * 4);
            canvas.restoreState();

            canvas = new PdfCanvas(pdfDoc.addNewPage());
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 1, 0);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 14, a4Width);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 13, a4Width * 2);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 2, a4Width * 3);
            canvas.saveState();

            // Rotate on 180 degrees and copy pages to the top row.
            canvas.concatMatrix(at);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 6, -a4Width);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 9, -a4Width * 2);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 10, -a4Width * 3);
            copyPageToDoc(canvas, srcDoc, pdfDoc, p + 5, -a4Width * 4);
            canvas.restoreState();

            if ((p - 1) / 16 < numberOfPages / 16) {
                canvas = new PdfCanvas(pdfDoc.addNewPage());
            }

            p += 16;
        }

        pdfDoc.close();
        srcDoc.close();
    }

    private static void copyPageToDoc(PdfCanvas canvas, PdfDocument srcDoc, PdfDocument pdfDoc,
            int pageNumber, float offsetX) throws IOException {
        if (pageNumber > srcDoc.getNumberOfPages()) {
            return;
        }

        PdfFormXObject page = srcDoc.getPage(pageNumber).copyAsFormXObject(pdfDoc);
        canvas.addXObjectAt(page, offsetX, 0);
    }
}
