package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.NonTerminalFormFieldBuilder;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.forms.fields.TextFormFieldBuilder;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

public class AddFieldAndKids {
    public static final String DEST = "./target/sandbox/stamper/add_field_and_kids.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddFieldAndKids().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfFormField personal =
                new NonTerminalFormFieldBuilder(pdfDoc, "personal").createNonTerminalFormField();
        PdfTextFormField name = new TextFormFieldBuilder(pdfDoc, "name")
                .setWidgetRectangle(new Rectangle(36, 760, 108, 30)).createText();
        name.setValue("");
        personal.addKid(name);
        PdfTextFormField password = new TextFormFieldBuilder(pdfDoc, "password")
                .setWidgetRectangle(new Rectangle(150, 760, 300, 30)).createText();
        password.setValue("");
        personal.addKid(password);

        PdfAcroForm.getAcroForm(pdfDoc, true).addField(personal, pdfDoc.getFirstPage());
        pdfDoc.close();
    }
}
