package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;

import java.io.File;

/*
 * DecryptPdf2.java
 *
 * Example showing how to modify encrypted PDF preserving old encryption.
 * Demonstrates unethical reading mode to bypass user password requirement.
 */
public class DecryptPdf2 {
    public static final String DEST = "./target/sandbox/security/decrypt_pdf2.pdf";
    public static final String SRC = "./src/main/resources/pdfs/encrypt_pdf_without_user_password.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DecryptPdf2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        try (PdfDocument document = new PdfDocument(
                new PdfReader(SRC).setUnethicalReading(true),
                new PdfWriter(dest),
                new StampingProperties().preserveEncryption()
        )) {
            // here we can modify the document
        }
    }
}
