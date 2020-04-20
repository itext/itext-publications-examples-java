package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;

import java.io.File;

public class FileSelectionExample {
    public static final String DEST = "./target/sandbox/acroforms/file_selection_example.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FileSelectionExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        PdfTextFormField field = PdfFormField.createText(pdfDoc,
                new Rectangle(36, 788, 523, 18), "myfile", "");

        // If true is passed, then the text entered in the field will represent the pathname of a file
        // whose contents are to be submitted as the value of the field.
        field.setFileSelect(true);

        // When the mouse is released inside the annotation's area (that's what PdfName.U stands for),
        // then the focus will be set on the "mytitle" field.
        field.setAdditionalAction(PdfName.U,
                PdfAction.createJavaScript("this.getField('myfile').browseForFileToSubmit();"
                        + "this.getField('mytitle').setFocus();"));
        form.addField(field);

        PdfTextFormField title = PdfFormField.createText(pdfDoc,
                new Rectangle(36, 752, 523, 18), "mytitle", "");
        form.addField(title);

        pdfDoc.close();
    }
}
