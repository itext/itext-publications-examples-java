package com.itextpdf.samples.sandbox.typography.devanagari;

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

public class DevanagariTextFormField {

    public static final String DEST = "./target/sandbox/typography/DevanagariTextFormField.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DevanagariTextFormField().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // पकवान
        String fieldValue = "\u092A\u0915\u0935\u093E\u0928";

        String fieldName = "Field name";

        PdfAcroForm form = PdfAcroForm.getAcroForm(document.getPdfDocument(), true);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansDevanagari-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // Create a form field and set some of the properties
        PdfFormField formField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(50, 750, 160, 25));
        formField
                .setValue(fieldValue)
                .setBorderWidth(2)
                .setFont(font)
                .setFontSize(10)
                .setJustification(1)
                .setFieldName(fieldName);

        form.addField(formField);

        document.close();
    }
}






