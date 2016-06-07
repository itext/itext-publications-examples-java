/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

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
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileInputStream;

@Category(SampleTest.class)
public class FillXFA2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/xfa_form_poland_filled.pdf";
    public static final String SRC = "./src/test/resources/pdfs/xfa_form_poland.pdf";
    public static final String XML = "./src/test/resources/xml/xfa_form_poland.xml";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillXFA2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfReader reader = new PdfReader(SRC);
        reader.setUnethicalReading(true);
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(DEST));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        XfaForm xfa = form.getXfaForm();
        xfa.fillXfaForm(new FileInputStream(XML));
        xfa.write(pdfDoc);

        pdfDoc.close();
    }
}
