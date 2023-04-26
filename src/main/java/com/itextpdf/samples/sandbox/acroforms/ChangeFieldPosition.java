package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;

import java.io.File;
import java.util.Map;

public class ChangeFieldPosition {
    public static final String DEST = "./target/sandbox/acroforms/change_field_position.pdf";

    public static final String SRC = "./src/main/resources/pdfs/state.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeFieldPosition().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Map<String, PdfFormField> fields = form.getAllFormFields();
        PdfFormField field = fields.get("timezone2");
        PdfWidgetAnnotation widgetAnnotation = field.getWidgets().get(0);
        PdfArray annotationRect = widgetAnnotation.getRectangle();

        // Change value of the right coordinate (index 2 corresponds with right coordinate)
        annotationRect.set(2, new PdfNumber(annotationRect.getAsNumber(2).floatValue() - 10f));

        pdfDoc.close();
    }
}
