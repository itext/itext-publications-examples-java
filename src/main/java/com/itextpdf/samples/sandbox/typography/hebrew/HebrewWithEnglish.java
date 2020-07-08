package com.itextpdf.samples.sandbox.typography.hebrew;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class HebrewWithEnglish {

    public static final String DEST = "./target/sandbox/typography/HebrewWithEnglish.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HebrewWithEnglish().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSerifHebrew-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Be aware that from now on this font size will be used for all the child elements unless it's overwritten in them
        document.setFontSize(10);

        // Create another font
        PdfFont freeSansFont = PdfFontFactory.createFont(FONTS_FOLDER + "FreeSans.ttf",
                PdfEncodings.IDENTITY_H);

        // ראשון
        Text text1 = new Text("\u05E8\u05D0\u05E9\u05D5\u05DF\u0020").setFont(font);

        Text text2 = new Text("B.A").setFont(freeSansFont);

        //  מהאוניברסיטה העברית בירושלים
        Text text3 = new Text("\u0020\u05DE\u05D4\u05D0\u05D5\u05E0\u05D9\u05D1\u05E8\u05E1\u05D9\u05D8\u05D4\u0020" +
                "\u05D4\u05E2\u05D1\u05E8\u05D9\u05EA\u0020\u05D1\u05D9\u05E8\u05D5\u05E9\u05DC\u05D9\u05DD\u0020").setFont(font);

        Text text4 = new Text("23").setFont(freeSansFont);

        // Wrap text with a paragraph, then set its direction and alignment
        Paragraph paragraph = new Paragraph();
        paragraph.add(text1)
                .add(text2)
                .add(text3)
                .add(text4);
        paragraph
                // In Hebrew text goes from right to left, that's why we need to overwrite the default iText's alignment
                // and direction
                .setBaseDirection(BaseDirection.RIGHT_TO_LEFT)
                .setTextAlignment(TextAlignment.RIGHT);

        document.add(paragraph);

        document.close();
    }
}
