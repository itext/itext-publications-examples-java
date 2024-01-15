/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.typography.bengali;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.ChoiceFormFieldBuilder;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BengaliDropDownBox {

    public static final String DEST = "./target/sandbox/typography/BengaliDropDownBox.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new BengaliDropDownBox().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        PdfAcroForm form = PdfFormCreator.getAcroForm(document.getPdfDocument(), true);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansBengali-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // সুরে মিললে সুর হবে
        String line1 = "\u09B8\u09C1\u09B0\u09C7\u0020\u09AE\u09BF\u09B2\u09B2\u09C7\u0020\u09B8\u09C1\u09B0\u0020\u09B9" +
                "\u09AC\u09C7";

        // সুরে
        String line2 = "\u09B8\u09C1\u09B0\u09C7";

        // িললে
        String line3 = "\u09AE\u09BF\u09B2\u09B2\u09C7";

        // Initialize the array with 3 lines of text. These lines will be used as combo box options
        String[] comboText = {line1, line2, line3};

        // Create a form field and apply the properties on it
        PdfFormField formField = new ChoiceFormFieldBuilder(document.getPdfDocument(), "test")
                .setWidgetRectangle(new Rectangle(50, 750, 150, 15)).setOptions(comboText)
                .createComboBox();
        formField.setValue(line1);
        formField.setJustification(TextAlignment.CENTER)
                .setFont(font)
                .setFontSizeAutoScale();
        formField.getFirstFormAnnotation().setBorderWidth(1);

        form.addField(formField);

        document.close();
    }
}
