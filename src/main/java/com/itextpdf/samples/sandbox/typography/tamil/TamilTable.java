package com.itextpdf.samples.sandbox.typography.tamil;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class TamilTable {

    public static final String DEST = "./target/sandbox/typography/TamilTable.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TamilTable().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // மனித உரிமைகள் பற்றிய உலகப் பிரகடனம்
        String text = "\u0BAE\u0BA9\u0BBF\u0BA4\u0020\u0B89\u0BB0\u0BBF\u0BAE\u0BC8\u0B95\u0BB3\u0BCD\u0020\u0BAA" +
                "\u0BB1\u0BCD\u0BB1\u0BBF\u0BAF\u0020\u0B89\u0BB2\u0B95\u0BAA\u0BCD\u0020\u0BAA\u0BBF\u0BB0\u0B95" +
                "\u0B9F\u0BA9\u0BAE\u0BCD";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansTamil-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document
                .setFont(font)
                .setFontSize(10);

        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table
                .addCell(text)
                .addCell(text)
                .addCell(text);

        document.add(table);

        document.close();
    }
}


