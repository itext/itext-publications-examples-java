package com.itextpdf.samples.sandbox.typography.telugu;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class TeluguLink {

    public static final String DEST = "./target/sandbox/typography/TeluguLink.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TeluguLink().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // తెలుగు గుణింతాలు
        String text = "\u0C24\u0C46\u0C32\u0C41\u0C17\u0C41\u0020\u0C17\u0C41\u0C23\u0C3F\u0C02\u0C24\u0C3E\u0C32\u0C41";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansTelugu-Regular.ttf",
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





