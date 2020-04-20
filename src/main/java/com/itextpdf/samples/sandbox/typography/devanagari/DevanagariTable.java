package com.itextpdf.samples.sandbox.typography.devanagari;

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

public class DevanagariTable {

    public static final String DEST = "./target/sandbox/typography/DevanagariTable.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DevanagariTable().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // मैथिली का प्रथम प्रमाण रामायण में मिलता
        String text = "\u092E\u0948\u0925\u093F\u0932\u0940\u0020\u0915\u093E\u0020\u092A\u094D\u0930\u0925\u092E\u0020" +
                "\u092A\u094D\u0930\u092E\u093E\u0923\u0020\u0930\u093E\u092E\u093E\u092F\u0923\u0020\u092E\u0947\u0902" +
                "\u0020\u092E\u093F\u0932\u0924\u093E";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansDevanagari-Regular.ttf",
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





