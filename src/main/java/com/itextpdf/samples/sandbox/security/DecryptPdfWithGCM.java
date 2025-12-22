package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;

import java.io.File;

/*
 * DecryptPdfWithGCM.java
 *
 * Example showing how to decrypt PDF encrypted with AES-GCM algorithm.
 * Demonstrates decryption using owner password for GCM-encrypted documents.
 */
public class DecryptPdfWithGCM {
    public static final String DEST = "./target/sandbox/security/decrypt_pdf_with_GCM.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hello_encrypted_with_GCM.pdf";

    public static final String OWNER_PASSWORD = "World";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DecryptPdfWithGCM().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument document = new PdfDocument(new PdfReader(SRC, new ReaderProperties()
                .setPassword(OWNER_PASSWORD.getBytes())), new PdfWriter(dest));
        document.close();
    }
}
