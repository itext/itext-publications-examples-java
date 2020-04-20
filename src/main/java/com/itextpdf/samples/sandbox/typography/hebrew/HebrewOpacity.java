package com.itextpdf.samples.sandbox.typography.hebrew;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class HebrewOpacity {

    public static final String DEST = "./target/sandbox/typography/HebrewOpacity.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HebrewOpacity().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // רע ומר היה להם ליהודים
        String text = "\u05E8\u05E2\u0020\u05D5\u05DE\u05E8\u0020\u05D4\u05D9\u05D4\u0020\u05DC\u05D4\u05DD" +
                "\u0020\u05DC\u05D9\u05D4\u05D5\u05D3\u05D9\u05DD";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSerifHebrew-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document
                .setFont(font)
                .setFontSize(10);

        // Wrap the text with paragraphs of different opacity and add them to the document: at first with 0.1, then with
        // 0.5 and then with the default opacity
        document.add(createParagraph(text).setOpacity(0.1f));
        document.add(createParagraph(text).setOpacity(0.5f));
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

