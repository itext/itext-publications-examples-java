package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;

import java.io.File;

public class DecryptPdf2 {
    public static final String DEST = "./target/sandbox/security/decrypt_pdf2.pdf";
    public static final String SRC = "./src/main/resources/pdfs/encrypt_pdf_without_user_password.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DecryptPdf2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        // This is not actually a decrypt example.
        // The old iText5 test shows how to open an encrypted pdf document
        // without user password for modifying with preserving an old owner password
        try (PdfDocument document = new PdfDocument(
                new PdfReader(SRC).setUnethicalReading(true),
                new PdfWriter(dest),
                new StampingProperties().preserveEncryption()
        )) {
            // here we can modify the document
        }
    }
}
