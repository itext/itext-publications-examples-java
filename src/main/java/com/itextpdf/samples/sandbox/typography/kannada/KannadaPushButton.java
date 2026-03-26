package com.itextpdf.samples.sandbox.typography.kannada;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PushButtonFormFieldBuilder;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.samples.util.LicenseUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/*
 * KannadaPushButton.java
 *
 * Creates PDF push button form field with Kannada caption text to
 * illustrate typography in interactive button elements.
 */

public class KannadaPushButton {

    public static final String DEST = "./target/sandbox/typography/KannadaPushButton.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        String licensePath = LicenseUtil.getPathToLicenseFileWithITextCoreAndPdfCalligraphProducts();
        try (FileInputStream license = new FileInputStream(licensePath)) {
            LicenseKey.loadLicenseFile(license);
        }
        

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new KannadaPushButton().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        PdfAcroForm form = PdfFormCreator.getAcroForm(pdfDocument, true);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansKannada-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // ನನ್ನನ್ನು ತಳ್ಳು
        String text = "\u0CA8\u0CA8\u0CCD\u0CA8\u0CA8\u0CCD\u0CA8\u0CC1\u0020\u0CA4\u0CB3\u0CCD\u0CB3\u0CC1";

        Rectangle rect = new Rectangle(50, 650, 120, 25);

        // Create a button for the form field, set its font and size
        PdfButtonFormField pushButton = new PushButtonFormFieldBuilder(pdfDocument, "Name")
                .setWidgetRectangle(rect).setCaption(text).createPushButton();
        pushButton
                .setFont(font)
                .setFontSize(10);

        // Add the button to the form
        form.addField(pushButton);

        pdfDocument.close();
    }
}
