/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This example was written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/33726908/writing-arabic-in-pdf-using-itext
 */
package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;

public class ArabicExample {
    public static final String DEST = "./target/sandbox/fonts/arabic_example.pdf";
    public static final String FONT = "./src/test/resources/font/NotoNaskhArabic-Regular.ttf";

    // "السعر الاجمالي"
    public static final String ARABIC = "\u0627\u0644\u0633\u0639\u0631 \u0627\u0644\u0627\u062c\u0645\u0627\u0644\u064a";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ArabicExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont f = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        // It is required to add iText typography dependency to handle correctly arabic text
        Paragraph p = new Paragraph("This is auto detection: ");
        p.add(new Text(ARABIC).setFont(f));
        p.add(new Text(": 50.00 USD"));
        doc.add(p);

        doc.close();
    }
}
