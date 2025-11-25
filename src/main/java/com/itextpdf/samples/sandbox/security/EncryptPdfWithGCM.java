package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.*;

import java.io.File;

/*
 * EncryptPdfWithGCM.java
 *
 * Example showing how to encrypt PDF using AES-GCM mode with PDF 2.0.
 * Demonstrates modern encryption requiring PDF 2.0 specification.
 */
public class EncryptPdfWithGCM {
    public static final String DEST = "./target/sandbox/security/encrypt_pdf_with_GCM.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hello_pdf2.pdf";

    public static final String OWNER_PASSWORD = "World";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new EncryptPdfWithGCM().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(
                new PdfReader(SRC),
                new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0).setStandardEncryption(
                        null,
                        OWNER_PASSWORD.getBytes(),
                        EncryptionConstants.ALLOW_PRINTING,
                        EncryptionConstants.ENCRYPTION_AES_GCM))
        );
        pdfDoc.close();
    }
}
