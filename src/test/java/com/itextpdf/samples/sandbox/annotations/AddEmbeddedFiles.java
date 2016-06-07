/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27823189/add-multiple-attachments-in-a-pdf-using-itext-pdf-stamper
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class AddEmbeddedFiles extends GenericTest {
    public static final String[] ATTACHMENTS = {
            "hello", "world", "what", "is", "up"
    };
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_embedded_files.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddEmbeddedFiles().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        for (String s : ATTACHMENTS) {
            PdfFileSpec spec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc,
                    String.format("Some test: %s", s).getBytes(), null, String.format("%s.txt", s), null, null, null, true);
            pdfDoc.addFileAttachment(String.format("Some test: %s", s), spec);
        }
        pdfDoc.close();
    }
}