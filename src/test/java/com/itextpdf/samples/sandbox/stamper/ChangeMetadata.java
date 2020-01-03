/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/37110820/itext-setting-creation-date-modified-date-in-sandbox-stamper-superimpose-java
 */

package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;

import java.io.File;

public class ChangeMetadata {
    public static final String DEST = "./target/sandbox/stamper/change_meta_data.pdf";
    public static final String SRC = "./src/test/resources/pdfs/state.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeMetadata().manipulatePdf();
    }

    protected void manipulatePdf() throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC),
                new PdfWriter(DEST, new WriterProperties().addXmpMetadata()));
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info.setTitle("New title");
        info.addCreationDate();

        pdfDoc.close();
    }
}
