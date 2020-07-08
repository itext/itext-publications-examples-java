package com.itextpdf.samples.sandbox.typography.devanagari;

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

public class DevanagariBoldText {

    public static final String DEST = "./target/sandbox/typography/DevanagariBoldText.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DevanagariBoldText().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansDevanagari-Regular.ttf",
                PdfEncodings.IDENTITY_H);
        PdfFont fontBold = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansDevanagari-Bold.ttf",
                PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document.setFont(font);
        document.setFontSize(10);

        // मैथिली का प्रथम प्रमाण रामायण में मिलता
        Text devanagariText = new Text("\u092E\u0948\u0925\u093F\u0932\u0940\u0020\u0915\u093E\u0020\u092A\u094D\u0930" +
                "\u0925\u092E\u0020\u092A\u094D\u0930\u092E\u093E\u0923\u0020\u0930\u093E\u092E\u093E\u092F\u0923\u0020" +
                "\u092E\u0947\u0902\u0020\u092E\u093F\u0932\u0924\u093E");

        // Add paragraphs with text to the document:
        // Text without thickness
        document.add(new Paragraph(devanagariText));

        // Add a paragraph with a set bold font to the paragraph
        document.add(new Paragraph(devanagariText).setFont(fontBold));

        // We don't suggest usage of setBold() method to reach text thickness since the result is written with the usual
        // rather than the bold font: we only emulate "thickness". It's recommended to use an actual bold font instead.
        // For example NotoSansDevanagari-Bold
        document.add(new Paragraph(devanagariText).setBold());

        document.close();
    }
}



