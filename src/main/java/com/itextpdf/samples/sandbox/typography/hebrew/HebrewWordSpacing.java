package com.itextpdf.samples.sandbox.typography.hebrew;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HebrewWordSpacing {

    public static final String DEST = "./target/sandbox/typography/HebrewWordSpacing.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HebrewWordSpacing().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // ראשון  מהאוניברסיטה העברית בירושלים
        String text = "\u05E8\u05D0\u05E9\u05D5\u05DF\u0020\u0020\u05DE\u05D4\u05D0\u05D5\u05E0\u05D9\u05D1\u05E8\u05E1" +
                "\u05D9\u05D8\u05D4\u0020\u05D4\u05E2\u05D1\u05E8\u05D9\u05EA\u0020\u05D1\u05D9\u05E8\u05D5\u05E9\u05DC" +
                "\u05D9\u05DD\u0020";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSerifHebrew-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document
                .setFont(font)
                .setFontSize(10);

        // Create paragraph, add string to it with the default word spacing and add all to the document
        document.add(createParagraph(text));

        // Add text with a word spacing of 10 points
        document.add(createParagraph(text).setWordSpacing(10));

        // Set word spacing on the document. It will be applied to the next paragraph
        document.setWordSpacing(20);
        document.add(createParagraph(text));

        document.close();
    }

    // This method creates a paragraph with right text alignment
    private static Paragraph createParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);

        // In Hebrew text goes from right to left, that's why we need to overwrite the default iText's alignment
        paragraph.setTextAlignment(TextAlignment.RIGHT);
        return paragraph;
    }
}
