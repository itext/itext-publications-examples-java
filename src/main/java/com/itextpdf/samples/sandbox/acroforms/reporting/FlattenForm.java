package com.itextpdf.samples.sandbox.acroforms.reporting;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

public class FlattenForm {
    public static final String DEST = "./target/sandbox/acroforms/reporting/flatten_form.pdf";

    public static final String SRC = "./src/main/resources/pdfs/state.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FlattenForm().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        form.getField("name").setValue("CALIFORNIA");
        form.getField("abbr").setValue("CA");
        form.getField("capital").setValue("Sacramento");
        form.getField("city").setValue("Los Angeles");
        form.getField("population").setValue("36,961,664");
        form.getField("surface").setValue("163,707");
        form.getField("timezone1").setValue("PT (UTC-8)");
        form.getField("timezone2").setValue("-");
        form.getField("dst").setValue("YES");

        // If no fields have been explicitly included via partialFormFlattening(),
        // then all fields are flattened. Otherwise only the included fields are flattened.
        form.flattenFields();

        pdfDoc.close();
    }
}
