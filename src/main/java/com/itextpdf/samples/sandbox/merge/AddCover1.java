package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;

import java.io.File;
import java.io.IOException;

public class AddCover1 {
    public static final String DEST = "./target/sandbox/merge/add_cover.pdf";

    public static final String COVER = "./src/main/resources/pdfs/hero.pdf";
    public static final String RESOURCE = "./src/main/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddCover1().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfDocument cover = new PdfDocument(new PdfReader(COVER));
        PdfDocument resource = new PdfDocument(new PdfReader(RESOURCE));

        PdfMerger merger = new PdfMerger(pdfDoc);
        merger.merge(cover, 1, 1);
        merger.merge(resource, 1, resource.getNumberOfPages());

        // Source documents can be closed implicitly after merging,
        // by passing true to the setCloseSourceDocuments(boolean) method
        cover.close();
        resource.close();

        // The resultant pdf doc will be closed implicitly.
        merger.close();
    }
}
