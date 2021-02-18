package com.itextpdf.samples.sandbox.typography.tamil;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class TamilPushButton {

    public static final String DEST = "./target/sandbox/typography/TamilPushButton.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TamilPushButton().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);

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
        PdfButtonFormField pushButton = PdfFormField.createPushButton(pdfDocument, rect, "Name", text);
        pushButton
                .setFont(font)
                .setFontSize(10);

        // Add the button to the form
        form.addField(pushButton);

        pdfDocument.close();
    }
}




