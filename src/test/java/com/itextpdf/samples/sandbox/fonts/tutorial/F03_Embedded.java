/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * These examples are written by Bruno Lowagie in the context of an article about fonts.
 */
package com.itextpdf.samples.sandbox.fonts.tutorial;

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
public class F03_Embedded extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/fonts/tutorial/f03_embedded.pdf";
    public static final String FONT = "./src/test/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new F03_Embedded().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.CP1250, true);
        doc.add(new Paragraph("Odkud jste?").setFont(font));
        doc.add(new Paragraph("Uvid\u00edme se za chvilku. M\u011bj se.").setFont(font));
        doc.add(new Paragraph("Dovolte, abych se p\u0159edstavil.").setFont(font));
        doc.add(new Paragraph("To je studentka.").setFont(font));
        doc.add(new Paragraph("V\u0161echno v po\u0159\u00e1dku?").setFont(font));
        doc.add(new Paragraph("On je in\u017een\u00fdr. Ona je l\u00e9ka\u0159.").setFont(font));
        doc.add(new Paragraph("Toto je okno.").setFont(font));
        doc.add(new Paragraph("Zopakujte to pros\u00edm.").setFont(font));
        doc.close();
    }
}
