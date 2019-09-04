package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.xfdf.XfdfObject;
import com.itextpdf.forms.xfdf.XfdfObjectFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class FillFormXfdf {
    public static final String sourceFolder = "./src/test/resources/pdfs/";
    public static final String outFolder = "./target/sandbox/acroforms/";

    public static void main(String[] args) throws Exception {
        File file = new File(outFolder);
        file.getParentFile().mkdirs();
        new FillFormXfdf().setFields(outFolder);
        new FillFormXfdf().listInSetField(outFolder);
    }

    public void setFields(String outFolder) throws Exception {
        String pdfForm = sourceFolder + "simpleRegistrationForm.pdf";
        String xfdf = sourceFolder + "register.xfdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(new FileInputStream(pdfForm)),
                new PdfWriter(new FileOutputStream(outFolder + "setFields.pdf")));
        XfdfObjectFactory factory = new XfdfObjectFactory();
        XfdfObject xfdfObject = factory.createXfdfObject(new FileInputStream(xfdf));
        xfdfObject.mergeToPdf(pdfDocument, pdfForm);
        pdfDocument.close();
    }

    //Currently iText xfdf implementation works with the fields in the following way: when the <value> tag with text
    // contents is found, it is considered to be the value of the field. All other <value> tags are ignored.
    public void listInSetField(String outFolder) throws Exception {
        String pdfForm = sourceFolder + "simpleRegistrationForm.pdf";
        String xfdf = sourceFolder + "list_register.xfdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(new FileInputStream(pdfForm)),
                new PdfWriter(new FileOutputStream(outFolder + "listInSetField.pdf")));
        XfdfObjectFactory factory = new XfdfObjectFactory();
        XfdfObject xfdfObject = factory.createXfdfObject(new FileInputStream(xfdf));
        xfdfObject.mergeToPdf(pdfDocument, pdfForm);
        pdfDocument.close();
    }
}