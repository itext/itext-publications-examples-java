/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/38900816
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;

import java.io.File;

public class HideButton {
    public static final String DEST = "./target/sandbox/acroforms/hide_button.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello_button.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new HideButton().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.getField("Test").setVisibility(PdfFormField.HIDDEN);
        pdfDoc.close();
    }
}
