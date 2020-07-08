package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;

import java.io.File;
import java.io.IOException;

public class InsertAndAdaptOutlines {
    public static final String DEST = "./target/sandbox/merge/insert_and_adapt_outlines.pdf";

    public static final String INSERT = "./src/main/resources/pdfs/hello.pdf";
    public static final String SRC = "./src/main/resources/pdfs/bookmarks.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new InsertAndAdaptOutlines().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        int insertPageNumber = 4;

        // Copier contains the additional logic to copy acroform fields to a new page.
        // PdfPageFormCopier uses some caching logic which can potentially improve performance
        // in case of the reusing of the same instance.
        PdfPageFormCopier formCopier = new PdfPageFormCopier();
        PdfDocument insertDoc = new PdfDocument(new PdfReader(INSERT));
        insertDoc.copyPagesTo(1, 1, pdfDoc, insertPageNumber, formCopier);
        insertDoc.close();

        PdfOutline outlines = pdfDoc.getOutlines(false);
        PdfOutline outline = outlines.getAllChildren().get(0).addOutline("Hello", insertPageNumber - 1);
        outline.addDestination(PdfExplicitDestination.createFit(pdfDoc.getPage(insertPageNumber)));

        pdfDoc.close();
    }
}
