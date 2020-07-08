package com.itextpdf.samples.sandbox.typography.devanagari;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class DevanagariPushButton {

    public static final String DEST = "./target/sandbox/typography/DevanagariPushButton.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DevanagariPushButton().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansDevanagari-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // पुश मे
        String text = "\u092A\u0941\u0936\u0020\u092E\u0947";

        Rectangle rect = new Rectangle(50, 650, 150, 25);

        // Create a button for the form field, set its font and size
        PdfButtonFormField pushButton = PdfFormField.createPushButton(pdfDocument, rect, "Name", text);
        pushButton
                .setFont(font)
                .setFontSize(10);

        // Add the button to the form
        form.addField(pushButton);

        pdfDocument.close();
    }
}






