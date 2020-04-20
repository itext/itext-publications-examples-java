package com.itextpdf.samples.sandbox.typography.hebrew;

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

public class HebrewHiddenTextForm {

    public static final String DEST = "./target/sandbox/typography/HebrewHiddenTextForm.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";
    public static final String RESOURCE_FOLDER = "./src/main/resources/pdfs/";
    public static final String INPUT_FILE = "hebrewAppearance.pdf";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HebrewHiddenTextForm().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a new pdf based on the resource one
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(RESOURCE_FOLDER + INPUT_FILE),
                new PdfWriter(dest));

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSerifHebrew-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Embed entire font without any subsetting. Please note that without subset it's impossible to edit a form field
        // with the predefined font
        font.setSubset(false);

        //רע ומר היה להם ליהודים
        String text = "\u05E8\u05E2\u0020\u05D5\u05DE\u05E8\u0020\u05D4\u05D9\u05D4\u0020\u05DC\u05D4\u05DD" +
                "\u0020\u05DC\u05D9\u05D4\u05D5\u05D3\u05D9\u05DD";

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

