package com.itextpdf.samples.sandbox.typography.tamil;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PushButtonFormFieldBuilder;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.samples.util.LicenseUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/*
 * TamilPushButton.java
 *
 * Creates PDF push button form field with Tamil caption text to
 * illustrate typography in interactive button elements.
 */

public class TamilPushButton {

    public static final String DEST = "./target/sandbox/typography/TamilPushButton.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        String licensePath = LicenseUtil.getPathToLicenseFileWithITextCoreAndPdfCalligraphProducts();
        try (FileInputStream license = new FileInputStream(licensePath)) {
            LicenseKey.loadLicenseFile(license);
        }
        

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TamilPushButton().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        PdfAcroForm form = PdfFormCreator.getAcroForm(pdfDocument, true);

        // Embedded parameter indicates whether the font is to be embedded into the target document.
        // We set it to make sure that the resultant document looks the same within different environments
        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansTamil-Regular.ttf",
                PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED);

        // என்னை தள்ளி விடு
        String text = "\u0B8E\u0BA9\u0BCD\u0BA9\u0BC8\u0020\u0BA4\u0BB3\u0BCD\u0BB3\u0BBF\u0020\u0BB5\u0BBF\u0B9F\u0BC1";

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        Rectangle rect = new Rectangle(50, 650, 140, 30);

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




