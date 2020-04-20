package com.itextpdf.samples.sandbox.typography.bengali;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class BengaliBoldText {

    public static final String DEST = "./target/sandbox/typography/BengaliBoldText.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new BengaliBoldText().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansBengali-Regular.ttf",
                PdfEncodings.IDENTITY_H);
        PdfFont fontBold = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansBengali-Bold.ttf",
                PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document.setFont(font);
        document.setFontSize(10);

        // আমার কানে লাগে
        Text bengaliText = new Text("\u0986\u09AE\u09BE\u09B0\u0020\u0995\u09BE\u09A8\u09C7\u0020\u09B2\u09BE\u0997\u09C7");

        // Add paragraphs with text to the document:
        // Text without thickness
        document.add(new Paragraph(bengaliText));

        // Add a paragraph with a set bold font to the paragraph
        document.add(new Paragraph(bengaliText).setFont(fontBold));

        // We don't suggest usage of setBold() method to reach text thickness since the result is written with the usual
        // rather than the bold font: we only emulate "thickness". It's recommended to use an actual bold font instead.
        // For example NotoSansBengali-Bold
        document.add(new Paragraph(bengaliText).setBold());

        document.close();
    }
}
