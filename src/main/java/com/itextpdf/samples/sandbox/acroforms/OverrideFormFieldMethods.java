/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormAnnotation;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PdfFormFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;

import java.io.File;

public class OverrideFormFieldMethods {
    public static final String DEST = "./target/sandbox/acroforms/overrideFormFieldMethods.pdf";

    public static final String SRC = "./src/main/resources/pdfs/checkboxes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new OverrideFormFieldMethods().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest))) {
            PdfFormFactory customFactory = new PdfFormFactory() {
                @Override
                public PdfFormAnnotation createFormAnnotation(PdfWidgetAnnotation widget, PdfDocument document) {
                    return new CustomPdfFormAnnotation(widget, document);
                }
            };
            PdfFormCreator.setFactory(customFactory);
            PdfAcroForm acroForm = PdfFormCreator.getAcroForm(pdfDoc, true);
            acroForm.getField("cb0").regenerateField();
            acroForm.getField("cb1").regenerateField();
            acroForm.getField("cb2").regenerateField();
            acroForm.getField("cb3").regenerateField();
            acroForm.getField("cb4").regenerateField();
            acroForm.getField("cb5").regenerateField();
        } finally {
            PdfFormCreator.setFactory(new PdfFormFactory());
        }
    }
    
    private static class CustomPdfFormAnnotation extends PdfFormAnnotation {
        protected CustomPdfFormAnnotation(PdfWidgetAnnotation widget, PdfDocument document) {
            super(widget, document);
            // All widgets will have 2 points red border by default.
            setBorderWidth(2);
            setBorderColor(ColorConstants.RED);
        }
    }
}
