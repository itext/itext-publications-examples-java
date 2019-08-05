/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27867868/how-can-i-decrypt-a-pdf-document-with-the-owner-password
 */
package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.*;

import java.io.File;

public class EncryptPdfWithoutUserPassword {
    public static final String DEST = "./target/sandbox/security/encrypt_pdf_without_user_password.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello_encrypted.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new EncryptPdfWithoutUserPassword().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfReader reader = new PdfReader(SRC, new ReaderProperties().setPassword("World".getBytes()));
        PdfWriter writer = new PdfWriter(DEST,
                new WriterProperties().setStandardEncryption(null, "World".getBytes(), EncryptionConstants.ALLOW_PRINTING,
                        EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA));
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        pdfDoc.close();
    }
}
