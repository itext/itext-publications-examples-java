/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23083220/how-to-set-pdf-version-using-itextsharp
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.samples.GenericTest;

import java.io.File;

public class ChangeVersion extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/change_version.pdf";
    public static final String SRC = "./src/test/resources/pdfs/OCR.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeVersion().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST, new WriterProperties().setPdfVersion(PdfVersion.PDF_1_5)));
        pdfDoc.close();
    }
}
