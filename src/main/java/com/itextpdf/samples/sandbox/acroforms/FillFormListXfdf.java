package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.xfdf.XfdfObject;
import com.itextpdf.forms.xfdf.XfdfObjectFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class FillFormListXfdf {
    public static final String sourceFolder = "./src/main/resources/pdfs/";
    public static final String DEST = "./target/sandbox/acroforms/listInSetField.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillFormListXfdf().listInSetField(DEST);
    }

    // Currently iText xfdf implementation works with the fields in the following way: when the <value> tag with text
    // contents is found, it is considered to be the value of the field. All other <value> tags are ignored.
    public void listInSetField(String dest) throws Exception {
        String pdfForm = sourceFolder + "simpleRegistrationForm.pdf";
        String xfdf = sourceFolder + "list_register.xfdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(new FileInputStream(pdfForm)),
                new PdfWriter(new FileOutputStream(dest)));
        XfdfObjectFactory factory = new XfdfObjectFactory();
        XfdfObject xfdfObject = factory.createXfdfObject(new FileInputStream(xfdf));
        xfdfObject.mergeToPdf(pdfDocument, pdfForm);
        pdfDocument.close();
    }
}