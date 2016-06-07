/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/30508966/saving-xfdf-as-pdf
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

import java.io.FileOutputStream;

@Ignore
@Category(SampleTest.class)
public class ImportXFDF extends GenericTest {
    public static String DEST = "./target/test/resources/sandbox/acroforms/import_xfdf.pdf";
    public static final String SRC = "./src/test/resources/pdfs/Requisition_Fillable.pdf";
    public static final String XFDF = "./src/test/resources/sandbox/acroforms/data.xfdf";

    public static void main(String[] args) throws Exception {
        new ImportXFDF().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        // TODO DEVSIX-526
        // XfdfReader xfdf = new XfdfReader(new FileInputStream(XFDF));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        //fields.setFields(xfdf);
        form.flattenFields();
        pdfDoc.close();
    }
}
