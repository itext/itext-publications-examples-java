/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/29702986/how-to-check-a-checkbox-in-pdf-file-with-the-same-variable-name-with-itext-and-j
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Category(SampleTest.class)
public class FillHybridForm extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/fill_hybrid_form.pdf";
    public static final String SRC = "./src/test/resources/pdfs/f8966.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillHybridForm().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.removeXfaForm();

        form.getField("topmostSubform[0].Page2[0].p2_cb01[0]").setValue("1");
        form.getField("topmostSubform[0].Page2[0].p2_cb01[1]").setValue("2");
        form.getField("topmostSubform[0].Page2[0].p2_cb01[2]").setValue("3");
        form.getField("topmostSubform[0].Page2[0].p2_cb01[3]").setValue("4");
        form.getField("topmostSubform[0].Page2[0].p2_cb01[4]").setValue("5");
        form.getField("topmostSubform[0].Page2[0].p2_cb01[5]").setValue("6");

        pdfDoc.close();
    }
}
