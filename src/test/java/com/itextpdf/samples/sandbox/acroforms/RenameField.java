/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21344750/itextsharp-renamefield-bug
 * <p>
 * When renaming a field, you need to respect the existing hierarchy.
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RenameField {
    public static final String DEST = "./target/sandbox/acroforms/rename_field.pdf";
    public static final String SRC = "./src/test/resources/pdfs/subscribe.pdf";

    public static void main(String args[]) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RenameField().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        PdfFormField login = form.getField("personal.loginname");
        login.setFieldName("login");
        form.getFormFields().remove("personal.loginname");
        form.getFormFields().put("login", login);

        pdfDoc.close();

        pdfDoc = new PdfDocument(new PdfReader(DEST));

        Map<String, PdfFormField> fields = PdfAcroForm.getAcroForm(pdfDoc, true).getFormFields();
        List<String> result = new ArrayList<>();
        for (String name : fields.keySet()) {
            System.out.println(name);
            result.add(name);
        }

        pdfDoc.close();
    }
}
