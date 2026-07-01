package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

/*
 * SunCharacter.java
 *
 * This example demonstrates rendering a special Unicode character (sun symbol ☉) in a PDF document using the Cardo font.
 * The sample shows how to embed a TrueType font that supports specific Unicode glyphs not available in standard fonts.
 */

public class SunCharacter {
    public static final String DEST = "./target/sandbox/fonts/sun_character.pdf";

    public static final String FONT = "./src/main/resources/font/Cardo-Regular.ttf";

    // "The Cardo family of fonts supports this character: ☉"
    public static final String TEXT = "The Cardo family of fonts supports this character: \u2609";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SunCharacter().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        Paragraph p = new Paragraph(TEXT).setFont(font);
        doc.add(p);

        doc.close();
    }
}
