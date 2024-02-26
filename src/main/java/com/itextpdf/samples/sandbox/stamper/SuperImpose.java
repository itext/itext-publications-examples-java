package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;

public class SuperImpose {
    public static final String DEST = "./target/sandbox/stamper/super_impose.pdf";
    public static final String[] EXTRA = {
            "./src/main/resources/pdfs/hello.pdf",
            "./src/main/resources/pdfs/base_url.pdf",
            "./src/main/resources/pdfs/state.pdf"
    };
    public static final String SRC = "./src/main/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SuperImpose().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(),
                pdfDoc.getFirstPage().getResources(), pdfDoc);

        for (String path : EXTRA) {
            PdfDocument srcDoc = new PdfDocument(new PdfReader(path));
            PdfFormXObject page = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);
            canvas.addXObjectAt(page, 0, 0);
            srcDoc.close();
        }

        pdfDoc.close();
    }
}
