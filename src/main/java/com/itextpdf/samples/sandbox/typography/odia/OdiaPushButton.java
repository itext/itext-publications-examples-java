package com.itextpdf.samples.sandbox.typography.odia;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PushButtonFormFieldBuilder;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class OdiaPushButton {

    public static final String DEST = "./target/sandbox/typography/OdiaPushButton.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new OdiaPushButton().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansOriya-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // ପୁଷ ମେ
        String text = "\u0B2A\u0B41\u0B37\u0020\u0B2E\u0B47";

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        Rectangle rect = new Rectangle(50, 650, 100, 25);

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

