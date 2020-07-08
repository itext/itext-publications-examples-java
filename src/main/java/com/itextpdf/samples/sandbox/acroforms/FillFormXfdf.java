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
    public static final String sourceFolder = "./src/main/resources/pdfs/";
    public static final String DEST = "./target/sandbox/acroforms/setFields.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillFormXfdf().setFields(DEST);
    }

    // Currently iText xfdf implementation works in the following way:
    // the XFDF file is used to insert data from it directly into the PDF.
    public void setFields(String dest) throws Exception {
        String pdfForm = sourceFolder + "simpleRegistrationForm.pdf";
        String xfdf = sourceFolder + "register.xfdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(new FileInputStream(pdfForm)),
                new PdfWriter(new FileOutputStream(dest)));
        XfdfObjectFactory factory = new XfdfObjectFactory();
        XfdfObject xfdfObject = factory.createXfdfObject(new FileInputStream(xfdf));
        xfdfObject.mergeToPdf(pdfDocument, pdfForm);
        pdfDocument.close();
    }
}