package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.IOException;

public class AddCover2 {
    public static final String DEST = "./target/sandbox/merge/add_cover2.pdf";

    public static final String COVER = "./src/main/resources/pdfs/hero.pdf";
    public static final String RESOURCE = "./src/main/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddCover2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(RESOURCE), new PdfWriter(dest));
        PdfDocument cover = new PdfDocument(new PdfReader(COVER));

        // Copier contains the additional logic to copy acroform fields to a new page.
        // PdfPageFormCopier uses some caching logic which can potentially improve performance
        // in case of the reusing of the same instance.
        PdfPageFormCopier formCopier = new PdfPageFormCopier();
        cover.copyPagesTo(1, 1, pdfDoc, 1, formCopier);

        cover.close();
        pdfDoc.close();
    }
}
