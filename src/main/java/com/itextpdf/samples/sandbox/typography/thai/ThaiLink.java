package com.itextpdf.samples.sandbox.typography.thai;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ThaiLink {

    public static final String DEST = "./target/sandbox/typography/ThaiLink.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ThaiLink().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        //ผม เรา ฉัน
        String text = "\u0E1C\u0E21\u0020\u0E40\u0E23\u0E32\u0020\u0E09\u0E31\u0E19";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansThai-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Create an action with an URI. Use the action together with text to create a Link element
        Link link = new Link(text, PdfAction.createURI("http://itextpdf.com"));

        // Overwrite some default document properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document
                .setFont(font)
                .setFontSize(10);

        document
                .add(new Paragraph(link))
                .add(new Paragraph(text));

        document.close();
    }
}






