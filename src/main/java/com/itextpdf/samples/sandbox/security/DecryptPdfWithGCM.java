package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;

import java.io.File;

/**
 * This example shows how to decrypt a pdf document encrypted with AES_GCM using owner password.
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
