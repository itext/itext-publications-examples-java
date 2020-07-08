package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfNumber;

import java.io.File;

public class ChangeButton {
    public static final String DEST = "./target/sandbox/acroforms/change_button.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello_button.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeButton().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        PdfFormField button = form.copyField("Test");
        PdfArray rect = button.getWidgets().get(0).getRectangle();

        // Increase value of the right coordinate (index 2 corresponds with right coordinate)
        rect.set(2, new PdfNumber(rect.getAsNumber(2).floatValue() + 172));

        button.setValue("Print Amended");
        form.replaceField("Test", button);

        pdfDoc.close();
    }
}
