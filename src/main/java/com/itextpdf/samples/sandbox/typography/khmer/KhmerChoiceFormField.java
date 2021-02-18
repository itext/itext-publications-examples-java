package com.itextpdf.samples.sandbox.typography.khmer;

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

public class KhmerChoiceFormField {

    public static final String DEST = "./target/sandbox/typography/KhmerChoiceFormField.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new KhmerChoiceFormField().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));


        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);

        // Embedded parameter indicates whether the font is to be embedded into the target document.
        // We set it to make sure that the resultant document looks the same within different environments
        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "KhmerOS.ttf",
                PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // ភាសាខ្មែរ
        String line1 = "\u1797\u17B6\u179F\u17B6\u1781\u17D2\u1798\u17C2\u179A";

        // ឆ្នាំ១៩៤៨
        String line2 = "\u1786\u17D2\u1793\u17B6\u17C6\u17E1\u17E9\u17E4\u17E8";

        // បុព្វកថា
        String line3 = "\u1794\u17BB\u1796\u17D2\u179C\u1780\u1790\u17B6";

        // Create an array with text lines
        String[] options = new String[]{line1, line2, line3};

        Rectangle rect = new Rectangle(50, 650, 100, 80);

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
