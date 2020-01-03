/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19698771/checking-off-pdf-checkbox-with-itextsharp
 * <p>
 * Given a check box in a form, how do we know
 * which values to use in {@link com.itextpdf.forms.fields.PdfFormField#setValue(String)}?
 * <p>
 * This sample differs from the corresponding iText 5 sample.
 * Here it is described not only how to find out, which values can be set in a checkbox,
 * but it is also shown that values are settable and it can be verified in the resultant pdf.
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.util.Map;

public class CheckBoxValues {
    public static final String DEST = "./target/sandbox/acroforms/check_box_values.pdf";

    public static final String SRC = "./src/test/resources/pdfs/checkboxExample.pdf";

    public static final String CHECKED_FIELD_NAME = "checked";
    public static final String UNCHECKED_FIELD_NAME = "unchecked";

    public static final String CHECKED_STATE_VALUE = "Yes";
    public static final String UNCHECKED_STATE_VALUE = "Off";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CheckBoxValues().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();
        PdfFormField checkedField = fields.get(CHECKED_FIELD_NAME);
        PdfFormField uncheckedField = fields.get(UNCHECKED_FIELD_NAME);

        // Get array of possible values of the checkbox
        String[] states = checkedField.getAppearanceStates();

        // See all possible values in the console
        for (String state : states) {
            System.out.print(state + "; ");
        }

        // Search and set checked state to the previously unchecked checkbox and vice versa
        for (String state : states) {
            if (state.equals(CHECKED_STATE_VALUE)) {
                uncheckedField.setValue(state);
            } else if (state.equals(UNCHECKED_STATE_VALUE)) {
                checkedField.setValue(state);
            }
        }

        pdfDoc.close();
    }
}
