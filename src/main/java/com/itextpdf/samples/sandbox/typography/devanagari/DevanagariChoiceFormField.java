package com.itextpdf.samples.sandbox.typography.devanagari;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfChoiceFormField;
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

public class DevanagariChoiceFormField {

    public static final String DEST = "./target/sandbox/typography/DevanagariChoiceFormField.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DevanagariChoiceFormField().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);

        // Embedded parameter indicates whether the font is to be embedded into the target document.
        // We set it to make sure that the resultant document looks the same within different environments
        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansDevanagari-Regular.ttf",
                PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // पकवान
        String line1 = "\u092A\u0915\u0935\u093E\u0928";

        // मरीया।
        String line2 = "\u092E\u0930\u0940\u092F\u093E\u0964";

        // जलदेव
        String line3 = "\u091C\u0932\u0926\u0947\u0935";

        // Create an array with text lines
        String[] options = new String[]{line1, line2, line3};

        Rectangle rect = new Rectangle(50, 650, 100, 70);

        // Create choice form field with parameters and set values
        PdfChoiceFormField choice = PdfFormField.createList(pdfDocument, rect, "List", "Test", options);
        choice
                .setMultiSelect(true)
                .setFont(font)
                .setFontSize(10);

        form.addField(choice);

        pdfDocument.close();
    }
}






