package com.itextpdf.samples.sandbox.typography.gujarati;

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

public class GujaratiChoiceFormField {

    public static final String DEST = "./target/sandbox/typography/GujaratiChoiceFormField.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new GujaratiChoiceFormField().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);

        // Embedded parameter indicates whether the font is to be embedded into the target document.
        // We set it to make sure that the resultant document looks the same within different environments
        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansGujarati-Regular.ttf",
                PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // વાઈસરૉયને
        String line1 = "\u0AB5\u0ABE\u0A88\u0AB8\u0AB0\u0AC9\u0AAF\u0AA8\u0AC7";

        // રાજ્યમાં
        String line2 = "\u0AB0\u0ABE\u0A9C\u0ACD\u0AAF\u0AAE\u0ABE\u0A82";

        // વસતા
        String line3 = "\u0AB5\u0AB8\u0AA4\u0ABE";

        // Create an array with text lines
        String[] options = new String[]{line1, line2, line3};

        Rectangle rect = new Rectangle(50, 650, 120, 70);

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


