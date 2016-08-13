/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

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
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.samples.GenericTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class HideButton extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/hide_button.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello_button.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new HideButton().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.getField("Test").setVisibility(PdfFormField.HIDDEN);
        pdfDoc.close();
    }
}
