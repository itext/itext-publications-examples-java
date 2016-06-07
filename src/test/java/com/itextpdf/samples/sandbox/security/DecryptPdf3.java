/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27867868/how-can-i-decrypt-a-pdf-document-with-the-owner-password
 */
package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class DecryptPdf3 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/security/decrypt_pdf3.pdf";
    public static final String SRC = "./src/test/resources/pdfs/encrypt_pdf_without_user_password.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DecryptPdf3().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        MyReader reader = new MyReader(SRC);
        reader.setUnethicalReading(true);
        reader.decryptOnPurpose();
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(DEST));
        pdfDoc.close();
        reader.close();
    }


    class MyReader extends PdfReader {
        public MyReader(String filename) throws IOException {
            super(filename);
        }

        public void decryptOnPurpose() {
            encrypted = false;
        }
    }
}
