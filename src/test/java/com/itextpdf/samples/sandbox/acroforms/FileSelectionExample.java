/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24830060/send-file-to-server-through-itext-pdf
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.samples.GenericTest;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class FileSelectionExample extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/file_selection_example.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FileSelectionExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        PdfTextFormField field = PdfFormField.createText(pdfDoc,
                new Rectangle(36, 788, 523, 18), "myfile", "");
        field.setFileSelect(true);
        field.setAdditionalAction(PdfName.U, PdfAction.createJavaScript(
                "this.getField('myfile').browseForFileToSubmit();"
                        + "this.getField('mytitle').setFocus();"));
        form.addField(field);

        PdfTextFormField title = PdfFormField.createText(pdfDoc,
                new Rectangle(36, 752, 523, 18), "mytitle", "");
        form.addField(title);

        pdfDoc.close();
    }
}
