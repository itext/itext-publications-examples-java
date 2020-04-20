package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;

import java.io.File;

public class FormatFields {
    public static final String DEST = "./target/sandbox/acroforms/format_fields.pdf";

    public static final String SRC = "./src/main/resources/pdfs/form.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FormatFields().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // The second parameter sets how the field's value will be displayed in the resultant pdf.
        // If the second parameter is null, then actual value will be shown.
        form.getField("Name").setValue("1.0", "100%");
        form.getField("Company").setValue("1217000.000000", "$1,217,000");

        pdfDoc.close();
    }
}
