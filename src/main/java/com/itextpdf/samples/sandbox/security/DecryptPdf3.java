package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

/*
 * DecryptPdf3.java
 *
 * Example showing how to open PDF encrypted without user password.
 * Demonstrates unethical reading to access encrypted documents.
 * Note, that it is required to use UnethicalReading in this case.
 */
public class DecryptPdf3 {
    public static final String DEST = "./target/sandbox/security/decrypt_pdf3.pdf";
    public static final String SRC = "./src/main/resources/pdfs/encrypt_pdf_without_user_password.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DecryptPdf3().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC).setUnethicalReading(true), new PdfWriter(dest));
        pdfDoc.close();
    }
}
