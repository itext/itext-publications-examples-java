/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormAnnotation;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PushButtonFormFieldBuilder;
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
        PdfAcroForm form = PdfFormCreator.getAcroForm(pdfDoc, true);

        PdfButtonFormField button = new PushButtonFormFieldBuilder(pdfDoc, "post")
                .setWidgetRectangle(new Rectangle(36, 700, 36, 30)).setCaption("POST").createPushButton();
        button.getFirstFormAnnotation().setBackgroundColor(ColorConstants.GRAY);
        button.setValue("POST");

        // The second parameter is optional, it declares which fields to include in the submission or which to exclude,
        // depending on the setting of the Include/Exclude flag.
        button.getFirstFormAnnotation().setAction(PdfAction.createSubmitForm("http://itextpdf.com:8180/book/request", null,
                PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_COORDINATES));
        button.getFirstFormAnnotation().setVisibility(PdfFormAnnotation.VISIBLE_BUT_DOES_NOT_PRINT);
        form.addField(button);

        pdfDoc.close();
    }
}
