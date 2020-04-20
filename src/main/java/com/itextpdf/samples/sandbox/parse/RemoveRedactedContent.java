package com.itextpdf.samples.sandbox.parse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfcleanup.PdfCleanUpTool;

import java.io.File;
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

        // If the second argument is true, then regions to be erased are extracted from the redact annotations
        // contained inside the given document. If the second argument is false (that's default behavior),
        // then use PdfCleanUpTool.addCleanupLocation(PdfCleanUpLocation)
        // method to set regions to be erased from the document.
        PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, true);
        cleaner.cleanUp();

        pdfDoc.close();
    }
}
