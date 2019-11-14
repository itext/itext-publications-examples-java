/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24404413/increase-width-of-acrofieldsitextsharp
 */
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

public class ChangeFieldSize {
    public static final String DEST = "./target/sandbox/acroforms/change_field_size.pdf";

    public static final String SRC = "./src/test/resources/pdfs/form.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeFieldSize().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        PdfFormField field = form.getField("Name");
        PdfWidgetAnnotation widgetAnnotation = field.getWidgets().get(0);
        PdfArray annotationRect = widgetAnnotation.getRectangle();

        // Change value of the right coordinate (index 2 corresponds with right coordinate)
        annotationRect.set(2, new PdfNumber(annotationRect.getAsNumber(2).floatValue() + 20f));

        String value = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        field.setValue(value);
        form.getField("Company").setValue(value);

        pdfDoc.close();
    }
}
