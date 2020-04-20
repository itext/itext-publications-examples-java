package com.itextpdf.samples.sandbox.interactive;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChangeAuthor {
    public static final String DEST = "./target/sandbox/interactive/change_author.pdf";

    public static final String SRC = "./src/main/resources/pdfs/page229_annotations.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeAuthor().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        List<PdfAnnotation> pageAnnots = pdfDoc.getFirstPage().getAnnotations();
        for (PdfAnnotation annot : pageAnnots) {
            if (annot.getTitle() != null) {
                annot.setTitle(new PdfString("Bruno Lowagie"));
            }
        }

        pdfDoc.close();
    }
}
