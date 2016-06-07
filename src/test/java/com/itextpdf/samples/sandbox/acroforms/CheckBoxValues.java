/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19698771/checking-off-pdf-checkbox-with-itextsharp
 * <p>
 * Given a check box in a form, how do we know which values to use in setField?
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.FileInputStream;
import java.util.Map;

@Category(SampleTest.class)

public class CheckBoxValues {
    public static final String CMP = "Yes\nOff\n";
    public static final String DEST = "./target/test/resources/sandbox/acroforms/check_box_values.pdf";
    public static final String FIELD = "CP_1";
    public static final String SRC = "./src/test/resources/pdfs/datasheet.pdf";

    @BeforeClass
    public static void beforeClass() throws Exception {
        new CheckBoxValues().manipulatePdf();
    }

    @Test
    public void manipulatePdf() throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Map<String, PdfFormField> fields = form.getFormFields();
        PdfFormField field = fields.get(FIELD);

        StringBuilder sb = new StringBuilder();
        String[] states = field.getAppearanceStates();
        for (String state : states) {
            sb.append(state);
            sb.append('\n');
        }
        System.out.println(sb);

        pdfDoc.close();

        Assert.assertEquals(CMP, sb.toString());
    }
}
