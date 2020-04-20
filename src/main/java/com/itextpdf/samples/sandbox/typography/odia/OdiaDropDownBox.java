package com.itextpdf.samples.sandbox.typography.odia;

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

public class OdiaDropDownBox {

    public static final String DEST = "./target/sandbox/typography/OdiaDropDownBox.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new OdiaDropDownBox().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        PdfAcroForm form = PdfAcroForm.getAcroForm(document.getPdfDocument(), true);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansOriya-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // ସବୁ
        String line1 = "\u0B38\u0B2C\u0B41";

        // ମନୁଷ୍ଯ
        String line2 = "\u0B2E\u0B28\u0B41\u0B37\u0B4D\u0B2F";

        // ଜନ୍ମକାଳରୁ
        String line3 = "\u0B1C\u0B28\u0B4D\u0B2E\u0B15\u0B3E\u0B33\u0B30\u0B41";

        // Initialize the array with 3 lines of text. These lines will be used as combo box options
        String[] comboText = {line1, line2, line3};

        // Create a form field and apply the properties on it
        PdfFormField formField = PdfTextFormField.createComboBox(document.getPdfDocument(),
                new Rectangle(50, 750, 150, 15), "test", line1, comboText);
        formField
                .setBorderWidth(1)
                .setJustification(1)
                .setFont(font)
                .setFontSizeAutoScale();

        form.addField(formField);

        document.close();
    }
}

