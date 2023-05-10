package com.itextpdf.samples.sandbox.typography.gujarati;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.ChoiceFormFieldBuilder;
import com.itextpdf.forms.fields.PdfFormCreator;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GujaratiDropDownBox {

    public static final String DEST = "./target/sandbox/typography/GujaratiDropDownBox.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new GujaratiDropDownBox().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        PdfAcroForm form = PdfFormCreator.getAcroForm(document.getPdfDocument(), true);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansGujarati-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // વાઈસરૉયને
        String line1 = "\u0AB5\u0ABE\u0A88\u0AB8\u0AB0\u0AC9\u0AAF\u0AA8\u0AC7";

        // રાજ્યમાં
        String line2 = "\u0AB0\u0ABE\u0A9C\u0ACD\u0AAF\u0AAE\u0ABE\u0A82";

        // વસતા
        String line3 = "\u0AB5\u0AB8\u0AA4\u0ABE";

        // Initialize the array with 3 lines of text. These lines will be used as combo box options
        String[] comboText = {line1, line2, line3};

        // Create a form field and apply the properties on it
        PdfFormField formField = new ChoiceFormFieldBuilder(document.getPdfDocument(), "test")
                .setWidgetRectangle(new Rectangle(50, 750, 50, 15)).setOptions(comboText)
                .createComboBox();
        formField.setValue(line1);
        formField.setJustification(TextAlignment.CENTER)
                .setFont(font)
                .setFontSizeAutoScale();
        formField.getFirstFormAnnotation().setBorderWidth(1);

        form.addField(formField);

        document.close();
    }
}


