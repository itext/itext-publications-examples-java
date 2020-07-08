package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.util.Map;

public class RemoveXFA {
    public static final String DEST = "./target/sandbox/acroforms/remove_xfa.pdf";

    public static final String SRC = "./src/main/resources/pdfs/reportcardinitial.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RemoveXFA().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // Method removes the XFA stream from the document.
        form.removeXfaForm();

        Map<String, PdfFormField> fields = form.getFormFields();
        for (Map.Entry<String, PdfFormField> name : fields.entrySet()) {
            if (name.getKey().indexOf("Total") > 0) {
                name.getValue().setColor(ColorConstants.RED);
            }

            name.getValue().setValue("X");
        }

        pdfDoc.close();
    }
}
