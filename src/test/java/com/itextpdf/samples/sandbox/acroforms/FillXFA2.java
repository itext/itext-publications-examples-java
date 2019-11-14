/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/28601068/get-names-field-from-interactive-form-pdf
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;

public class FillXFA2 {
    public static final String DEST = "./target/sandbox/acroforms/xfa_example_filled.pdf";

    public static final String SRC = "./src/test/resources/pdfs/xfa_invoice_example.pdf";
    public static final String XML = "./src/test/resources/xml/xfa_example.xml";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FillXFA2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfReader reader = new PdfReader(SRC);
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        XfaForm xfa = form.getXfaForm();

        // Method fills this object with XFA data under datasets/data.
        xfa.fillXfaForm(new FileInputStream(XML));
        xfa.write(pdfDoc);

        pdfDoc.close();
    }
}
