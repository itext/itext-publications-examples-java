package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;

import java.io.File;
import java.util.Map;

public class RenameField {
    public static final String DEST = "./target/sandbox/acroforms/rename_field.pdf";

    public static final String SRC = "./src/main/resources/pdfs/subscribe.pdf";

    public static void main(String args[]) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RenameField().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        form.renameField("personal.loginname", "login");

        pdfDoc.close();

        pdfDoc = new PdfDocument(new PdfReader(dest));
        form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();

        // See the renamed field in the console
        for (String name : fields.keySet()) {
            System.out.println(name);
        }

        pdfDoc.close();
    }
}
