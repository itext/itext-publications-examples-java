package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;

import java.io.File;

public class HideButton {
    public static final String DEST = "./target/sandbox/acroforms/hide_button.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello_button.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HideButton().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // Set the visibility flag of the form field annotation.
        // Options are: HIDDEN, HIDDEN_BUT_PRINTABLE, VISIBLE, VISIBLE_BUT_DOES_NOT_PRINT
        form.getField("Test").setVisibility(PdfFormField.HIDDEN);

        pdfDoc.close();
    }
}
