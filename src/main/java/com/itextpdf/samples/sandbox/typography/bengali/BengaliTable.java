/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.typography.bengali;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BengaliTable {

    public static final String DEST = "./target/sandbox/typography/BengaliTable.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new BengaliTable().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // আমার কানে লাগে
        String text = "\u0986\u09AE\u09BE\u09B0\u0020\u0995\u09BE\u09A8\u09C7\u0020\u09B2\u09BE\u0997\u09C7";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansBengali-Regular.ttf",
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
