/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/34396579
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

@Category(SampleTest.class)
public class FreeSansBold extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/fonts/free_sans_bold.pdf";
    public static final String FONT
            = "./src/test/resources/font/FreeSans.ttf";
    public static final String FONTBOLD
            = "./src/test/resources/font/FreeSansBold.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FreeSansBold().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        Paragraph p = new Paragraph("FreeSans regular: \u0110").setFont(font);
        doc.add(p);

        PdfFont bold = PdfFontFactory.createFont(FONTBOLD, PdfEncodings.IDENTITY_H);
        p = new Paragraph("FreeSans bold: \u0110").setFont(bold);
        doc.add(p);

        doc.close();
    }
}
