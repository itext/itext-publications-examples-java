package com.itextpdf.samples.sandbox.typography.gurmukhi;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class GurmukhiCharacterSpacing {

    public static final String DEST = "./target/sandbox/typography/GurmukhiCharacterSpacing.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new GurmukhiCharacterSpacing().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // ਸਾਰੇ ਇਨਸਾਨ
        String text = "\u0A38\u0A3E\u0A30\u0A47\u0020\u0A07\u0A28\u0A38\u0A3E\u0A28";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansGurmukhi-Regular.ttf", PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document.setFont(font);
        document.setFontSize(10);

        // Create paragraph, add string to it with the default character spacing and add all to the document
        document.add(new Paragraph(text));

        // Add text with a character spacing of 5 points
        document.add(new Paragraph(text).setCharacterSpacing(5));

        // Set character spacing on the document. It will be applied to the next paragraph
        document.setCharacterSpacing(10);
        document.add(new Paragraph(text));

        document.close();
    }
}

