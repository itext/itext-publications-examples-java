/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

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
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class RenameField {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/rename_field.pdf";
    public static final String SRC = "./src/test/resources/pdfs/subscribe.pdf";
    public static List<String> CMP_RESULT;

    static {
        CMP_RESULT = new ArrayList<String>();
        CMP_RESULT.add("personal");
        CMP_RESULT.add("personal.name");
        CMP_RESULT.add("personal.login");
        CMP_RESULT.add("personal.password");
        CMP_RESULT.add("personal.reason");
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
    }

    @Test
    public void manipulatePdf() throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
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

        Assert.assertArrayEquals(CMP_RESULT.toArray(), result.toArray());
    }
}
