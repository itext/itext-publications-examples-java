/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie, showing that not all fonts contain
 * all glyphs for all languages.
 */
package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FontTest {
    public static final String DEST = "./target/sandbox/fonts/font_test.pdf";
    public static final String TEXT = "Quick brown fox jumps over the lazy dog; 0123456789";
    public static final String CP1250 = "Nikogar\u0161nja zemlja";
    public static final String CP1251 = "\u042f \u043b\u044e\u0431\u043b\u044e \u0442\u0435\u0431\u044f";
    public static final String CP1252 = "Un long dimanche de fian\u00e7ailles";
    public static final String CP1253 = "\u039d\u03cd\u03c6\u03b5\u03c2";
    public static final String CHINESE = "\u5341\u950a\u57cb\u4f0f";
    public static final String JAPANESE = "\u8ab0\u3082\u77e5\u3089\u306a\u3044";
    public static final String KOREAN = "\ube48\uc9d1";

    public static final String[] FONTS = {
            "./src/test/resources/font/cmr10.afm",
            "./src/test/resources/font/cmr10.pfb",
            "./src/test/resources/font/cmr10.pfm",
            "./src/test/resources/font/EBGaramond12-Italic.ttf",
            "./src/test/resources/font/EBGaramond12-Regular.ttf",
            "./src/test/resources/font/FreeSans.ttf",
            "./src/test/resources/font/FreeSansBold.ttf",
            "./src/test/resources/font/NotoSans-Bold.ttf",
            "./src/test/resources/font/NotoSans-BoldItalic.ttf",
            "./src/test/resources/font/NotoSansCJKjp-Regular.otf",
            "./src/test/resources/font/NotoSansCJKkr-Regular.otf",
            "./src/test/resources/font/NotoSansCJKsc-Regular.otf"
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FontTest().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        for (String font : FONTS) {
            PdfFontFactory.register(font);
        }
        Set<String> fonts = new HashSet<String>(FontProgramFactory.getRegisteredFonts());
        for (String fontname : fonts) {
            showFontInfo(doc, fontname);
        }
        doc.close();
    }

    protected void showFontInfo(Document doc, String fontname) {
        System.out.println(fontname);
        PdfFont font;
        try {
            font = PdfFontFactory.createRegisteredFont(fontname, PdfEncodings.IDENTITY_H);
        } catch (Exception e) {
            doc.add(new Paragraph(
                    String.format("The font %s doesn't have unicode support: %s", fontname, e.getMessage())));
            return;
        }
        doc.add(new Paragraph(
                String.format("Postscript name for %s: %s", fontname, font.getFontProgram().getFontNames().getFontName())));
        doc.add(new Paragraph(TEXT).setFont(font));
        doc.add(new Paragraph(String.format("CP1250: %s", CP1250)).setFont(font));
        doc.add(new Paragraph(String.format("CP1251: %s", CP1251)).setFont(font));
        doc.add(new Paragraph(String.format("CP1252: %s", CP1252)).setFont(font));
        doc.add(new Paragraph(String.format("CP1253: %s", CP1253)).setFont(font));
        doc.add(new Paragraph(String.format("CHINESE: %s", CHINESE)).setFont(font));
        doc.add(new Paragraph(String.format("JAPANESE: %s", JAPANESE)).setFont(font));
        doc.add(new Paragraph(String.format("KOREAN: %s", KOREAN)).setFont(font));
    }
}
