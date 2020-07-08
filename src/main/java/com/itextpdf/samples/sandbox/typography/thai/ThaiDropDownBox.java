package com.itextpdf.samples.sandbox.typography.thai;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class ThaiDropDownBox {

    public static final String DEST = "./target/sandbox/typography/ThaiDropDownBox.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ThaiDropDownBox().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        PdfAcroForm form = PdfAcroForm.getAcroForm(document.getPdfDocument(), true);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansThai-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // คำปรารภ
        String line1 = "\u0E04\u0E33\u0E1B\u0E23\u0E32\u0E23\u0E20";

        // สมาชิก
        String line2 = "\u0E2A\u0E21\u0E32\u0E0A\u0E34\u0E01";

        // ความยุติธรรม
        String line3 = "\u0E04\u0E27\u0E32\u0E21\u0E22\u0E38\u0E15\u0E34\u0E18\u0E23\u0E23\u0E21";

        // Initialize the array with 3 lines of text. These lines will be used as combo box options
        String[] comboText = {line1, line2, line3};

        // Create a form field and apply the properties on it
        PdfFormField formField = PdfTextFormField.createComboBox(document.getPdfDocument(),
                new Rectangle(50, 750, 75, 15), "test", line1, comboText);
        formField
                .setBorderWidth(1)
                .setJustification(1)
                .setFont(font)
                .setFontSizeAutoScale();

        form.addField(formField);

        document.close();
    }
}






