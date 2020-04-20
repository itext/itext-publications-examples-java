package com.itextpdf.samples.sandbox.typography.kannada;

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

public class KannadaTable {

    public static final String DEST = "./target/sandbox/typography/KannadaTable.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new KannadaTable().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // ಅಶೋಕನ ಬ್ರಾಹ್ಮೀ ಲಿಪಿ ಉತ್ತರಕ್ಕೆ
        String text = "\u0C85\u0CB6\u0CCB\u0C95\u0CA8\u0020\u0CAC\u0CCD\u0CB0\u0CBE\u0CB9\u0CCD\u0CAE\u0CC0\u0020\u0CB2" +
                "\u0CBF\u0CAA\u0CBF\u0020\u0C89\u0CA4\u0CCD\u0CA4\u0CB0\u0C95\u0CCD\u0C95\u0CC6";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansKannada-Regular.ttf",
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
