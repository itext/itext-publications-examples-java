/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/26360814/rupee-symbol-is-not-showing-in-android
 */
package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileOutputStream;

@Category(SampleTest.class)
public class RupeeSymbol extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/fonts/rupee_symbol.pdf";

    public static final String FONT1 = "./src/test/resources/font/PlayfairDisplay-Regular.ttf";
    public static final String FONT2 = "./src/test/resources/font/PT_Sans-Web-Regular.ttf";
    public static final String FONT3 = "./src/test/resources/font/FreeSans.ttf";
    public static final String RUPEE = "The Rupee character \u20B9 and the Rupee symbol \u20A8";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RupeeSymbol().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(new FileOutputStream(DEST)));
        Document doc = new Document(pdfDoc);
        PdfFont font1 = PdfFontFactory.createFont(FONT1, PdfEncodings.IDENTITY_H);
        PdfFont font2 = PdfFontFactory.createFont(FONT2, PdfEncodings.IDENTITY_H);
        PdfFont font3 = PdfFontFactory.createFont(FONT3, PdfEncodings.IDENTITY_H);
        PdfFont font4 = PdfFontFactory.createFont(FONT3, PdfEncodings.WINANSI, true);

        doc.add(new Paragraph(RUPEE).setFont(font1));
        doc.add(new Paragraph(RUPEE).setFont(font2));
        doc.add(new Paragraph(RUPEE).setFont(font3));
        doc.add(new Paragraph(RUPEE).setFont(font4));
        doc.close();
    }
}
