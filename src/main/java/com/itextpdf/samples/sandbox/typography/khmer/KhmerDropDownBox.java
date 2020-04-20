package com.itextpdf.samples.sandbox.typography.khmer;

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

public class KhmerDropDownBox {

    public static final String DEST = "./target/sandbox/typography/KhmerDropDownBox.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new KhmerDropDownBox().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        PdfAcroForm form = PdfAcroForm.getAcroForm(document.getPdfDocument(), true);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "KhmerOS.ttf",
                PdfEncodings.IDENTITY_H);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // ភាសាខ្មែរ
        String line1 = "\u1797\u17B6\u179F\u17B6\u1781\u17D2\u1798\u17C2\u179A";

        // ឆ្នាំ១៩៤៨
        String line2 = "\u1786\u17D2\u1793\u17B6\u17C6\u17E1\u17E9\u17E4\u17E8";

        // បុព្វកថា
        String line3 = "\u1794\u17BB\u1796\u17D2\u179C\u1780\u1790\u17B6";

        // Initialize the array with 3 lines of text. These lines will be used as combo box options
        String[] comboText = {line1, line2, line3};

        // Create a form field and apply the properties on it
        PdfFormField formField = PdfTextFormField.createComboBox(document.getPdfDocument(),
                new Rectangle(50, 750, 50, 15), "test", line1, comboText);
        formField
                .setBorderWidth(1)
                .setJustification(1)
                .setFont(font)
                .setFontSizeAutoScale();

        form.addField(formField);

        document.close();
    }
}
