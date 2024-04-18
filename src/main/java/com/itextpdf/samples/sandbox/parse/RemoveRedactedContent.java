package com.itextpdf.samples.sandbox.parse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfcleanup.PdfCleaner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RemoveRedactedContent {
    public static final String DEST = "./target/sandbox/parse/remove_redacted_content.pdf";

    public static final String SRC = "./src/main/resources/pdfs/page229_redacted.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RemoveRedactedContent().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        // If you use cleanUpRedactAnnotations method, then regions to be erased are extracted from the redact annotations
        // contained inside the given document.
        PdfCleaner.cleanUpRedactAnnotations(pdfDoc);

        pdfDoc.close();
    }
}
