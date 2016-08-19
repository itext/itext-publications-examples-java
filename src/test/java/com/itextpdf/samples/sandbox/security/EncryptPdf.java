/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27867868/how-can-i-decrypt-a-pdf-document-with-the-owner-password
 */
package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Assert;
import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class EncryptPdf extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/security/encrypt_pdf.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello_encrypted.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new EncryptPdf().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfReader reader = new PdfReader(SRC, new ReaderProperties().setPassword("World".getBytes()));
        PdfWriter writer = new PdfWriter(DEST, new WriterProperties()
                .setStandardEncryption("Hello".getBytes(), "World".getBytes(), EncryptionConstants.ALLOW_PRINTING,
                        EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA));
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        pdfDoc.close();
    }

    // Override the comparison method in order to pass owner passwords
    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        if (cmp == null || cmp.length() == 0) return;
        CompareTool compareTool = new CompareTool();
        compareTool.enableEncryptionCompare();
        String outPath = new File(dest).getParent();
        new File(outPath).mkdirs();
        if (compareXml) {
            if (!compareTool.compareXmls(dest, cmp)) {
                addError("The XML structures are different.");
            }
        } else {
            if (compareRenders) {
                addError(compareTool.compareVisually(dest, cmp, outPath, differenceImagePrefix));
                addError(compareTool.compareLinkAnnotations(dest, cmp));
            } else {
                addError(compareTool.compareByContent(dest, cmp, outPath, differenceImagePrefix, "World".getBytes(), "World".getBytes()));
            }
            addError(compareTool.compareDocumentInfo(dest, cmp, "World".getBytes(), "World".getBytes()));
        }

        if (errorMessage != null) Assert.fail(errorMessage);
    }
}
