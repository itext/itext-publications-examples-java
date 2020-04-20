package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.util.Map;

public class AlignField {
    public static final String DEST = "./target/sandbox/acroforms/align_field.pdf";

    public static final String SRC = "./src/main/resources/pdfs/subscribe.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AlignField().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();

        PdfFormField field = fields.get("personal.name");
        field.setJustification(PdfFormField.ALIGN_LEFT);
        field.setValue("Test");

        field = fields.get("personal.loginname");
        field.setJustification(PdfFormField.ALIGN_CENTER);
        field.setValue("Test");

        field = fields.get("personal.password");
        field.setJustification(PdfFormField.ALIGN_RIGHT);
        field.setValue("Test");

        field = fields.get("personal.reason");
        field.setValue("Test");

        pdfDoc.close();
    }
}
