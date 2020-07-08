package com.itextpdf.samples.sandbox.typography.latin;

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

public class LatinBreveDiacritic {

    public static final String DEST = "./target/sandbox/typography/LatinBreveDiacritic.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LatinBreveDiacritic().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {
        Document document = new Document(new PdfDocument(new PdfWriter(dest)));

        // ĂăĔĕĞğĬĭŎŏŬŭ˘ḜḝẶặᾰᾸῐῘῠῨ
        String line1 = "\u0102\u0103\u0114\u0115\u011E\u011F\u012C\u012D\u014E\u014F\u016C\u016D\u02D8\u1E1C\u1E1D\u1EB6" +
                "\u1EB7\u1FB0\u1FB8\u1FD0\u1FD8\u1FE0\u1FE8";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "FreeSans.ttf", PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document.setFont(font);
        document.setFontSize(10);

        document.add(new Paragraph(line1));

        document.close();
    }
}
