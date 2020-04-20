package com.itextpdf.samples.sandbox.typography.arabic;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ArabicHiddenTextForm {

    public static final String DEST = "./target/sandbox/typography/ArabicHiddenTextForm.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";
    public static final String RESOURCE_FOLDER = "./src/main/resources/pdfs/";
    public static final String INPUT_FILE = "arabicAppearance.pdf";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ArabicHiddenTextForm().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a new pdf based on the resource one
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(RESOURCE_FOLDER + INPUT_FILE),
                new PdfWriter(dest));

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoNaskhArabic-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        // في القيام بنشاط
        String text = "\u0641\u064A\u0020\u0627\u0644\u0642\u064A\u0627\u0645\u0020\u0628\u0646\u0634\u0627\u0637";

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);

        // Set needAppearance value to false in order to hide the text of the form fields
        form.setNeedAppearances(false);

        // Update the value and some other properties of all the pdf document's form fields
        for (Map.Entry<String, PdfFormField> entry : form.getFormFields().entrySet()) {
            PdfFormField field = entry.getValue();
            field.setValue(text);
            field.setFont(font).setJustification(2);
        }

        pdfDocument.close();
    }
}
