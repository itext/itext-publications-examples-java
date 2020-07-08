package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;

import java.io.File;

public class DecryptPdf {
    public static final String DEST = "./target/sandbox/security/decrypt_pdf.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hello_encrypted.pdf";

    public static final String OWNER_PASSWORD = "World";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DecryptPdf().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        try (PdfDocument document = new PdfDocument(
                new PdfReader(SRC, new ReaderProperties().setPassword(OWNER_PASSWORD.getBytes())),
                new PdfWriter(dest)
        )) {
            byte[] userPasswordBytes = document.getReader().computeUserPassword();

            // The result of user password computation logic can be null in case of
            // AES256 password encryption or non password encryption algorithm
            String userPassword = userPasswordBytes == null ? null : new String(userPasswordBytes);
            System.out.println(userPassword);
        }
    }
}
