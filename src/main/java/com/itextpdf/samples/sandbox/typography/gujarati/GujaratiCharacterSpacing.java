package com.itextpdf.samples.sandbox.typography.gujarati;

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

public class GujaratiCharacterSpacing {

    public static final String DEST = "./target/sandbox/typography/GujaratiCharacterSpacing.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new GujaratiCharacterSpacing().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // વાઈસરૉયને
        String text = "\u0AB5\u0ABE\u0A88\u0AB8\u0AB0\u0AC9\u0AAF\u0AA8\u0AC7";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansGujarati-Regular.ttf",
                PdfEncodings.IDENTITY_H);

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

