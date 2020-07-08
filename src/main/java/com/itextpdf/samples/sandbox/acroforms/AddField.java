package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;

import java.io.File;

public class AddField {
    public static final String DEST = "./target/sandbox/acroforms/add_field.pdf";

    public static final String SRC = "./src/main/resources/pdfs/form.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddField().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        PdfButtonFormField button = PdfFormField.createPushButton(pdfDoc,
                new Rectangle(36, 700, 36, 30), "post", "POST");
        button.setBackgroundColor(ColorConstants.GRAY);
        button.setValue("POST");

        // The second parameter is optional, it declares which fields to include in the submission or which to exclude,
        // depending on the setting of the Include/Exclude flag.
        button.setAction(PdfAction.createSubmitForm("http://itextpdf.com:8180/book/request", null,
                PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_COORDINATES));
        button.setVisibility(PdfFormField.VISIBLE_BUT_DOES_NOT_PRINT);
        form.addField(button);

        pdfDoc.close();
    }
}
