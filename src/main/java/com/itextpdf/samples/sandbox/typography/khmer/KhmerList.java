package com.itextpdf.samples.sandbox.typography.khmer;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class KhmerList {

    public static final String DEST = "./target/sandbox/typography/KhmerList.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new KhmerList().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // Embedded parameter indicates whether the font is to be embedded into the target document.
        // We set it to make sure that the resultant document looks the same within different environments
        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "KhmerOS.ttf", PdfEncodings.IDENTITY_H,
                EmbeddingStrategy.PREFER_EMBEDDED);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document
                .setFont(font)
                .setFontSize(10);

        // ភាសាខ្មែរ
        String text = "\u1797\u17B6\u179F\u17B6\u1781\u17D2\u1798\u17C2\u179A";

        List list = new List();

        list
                .add(text)
                .add(text)
                .add(text);

        document.add(list);

        document.close();
    }
}
