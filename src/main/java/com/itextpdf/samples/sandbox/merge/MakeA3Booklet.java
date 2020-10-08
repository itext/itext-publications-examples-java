package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;
import java.io.IOException;

public class MakeA3Booklet {
    public static final String DEST = "./target/sandbox/merge/make_a3_booklet.pdf";

    public static final String SRC = "./src/main/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MakeA3Booklet().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(PageSize.A3.rotate());

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        float a4Width = PageSize.A4.getWidth();
        int numberOfPages = srcDoc.getNumberOfPages();
        int i = 0;
        while (i++ < numberOfPages) {
            PdfFormXObject page = srcDoc.getPage(i).copyAsFormXObject(pdfDoc);
            if (i % 2 == 1) {
                canvas.addXObjectAt(page, 0, 0);
            } else {
                canvas.addXObjectAt(page, a4Width, 0);
                canvas = new PdfCanvas(pdfDoc.addNewPage());
            }
        }

        pdfDoc.close();
        srcDoc.close();
    }
}
