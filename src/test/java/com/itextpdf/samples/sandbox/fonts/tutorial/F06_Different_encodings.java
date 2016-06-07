/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * These examples are written by Bruno Lowagie in the context of an article about fonts.
 */
package com.itextpdf.samples.sandbox.fonts.tutorial;

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
public class F06_Different_encodings extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/fonts/tutorial/f06_different_encodings.pdf";
    public static final String FONT = "./src/test/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new F06_Different_encodings().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfFont french = PdfFontFactory.createFont(FONT, "WINANSI", true);
        PdfFont czech = PdfFontFactory.createFont(FONT, "Cp1250", true);
        PdfFont russian = PdfFontFactory.createFont(FONT, "Cp1251", true);

        doc.add(new Paragraph("Vous \u00eates d'o\u00f9?").setFont(french));
        doc.add(new Paragraph("\u00c0 tout \u00e0 l'heure. \u00c0 bient\u00f4t.").setFont(french));
        doc.add(new Paragraph("Je me pr\u00e9sente.").setFont(french));
        doc.add(new Paragraph("C'est un \u00e9tudiant.").setFont(french));
        doc.add(new Paragraph("\u00c7a va?").setFont(french));
        doc.add(new Paragraph("Il est ing\u00e9nieur. Elle est m\u00e9decin.").setFont(french));
        doc.add(new Paragraph("C'est une fen\u00eatre.").setFont(french));
        doc.add(new Paragraph("R\u00e9p\u00e9tez, s'il vous pla\u00eet.").setFont(french));
        doc.add(new Paragraph("Odkud jste?").setFont(czech));
        doc.add(new Paragraph("Uvid\u00edme se za chvilku. M\u011bj se.").setFont(czech));
        doc.add(new Paragraph("Dovolte, abych se p\u0159edstavil.").setFont(czech));
        doc.add(new Paragraph("To je studentka.").setFont(czech));
        doc.add(new Paragraph("V\u0161echno v po\u0159\u00e1dku?").setFont(czech));
        doc.add(new Paragraph("On je in\u017een\u00fdr. Ona je l\u00e9ka\u0159.").setFont(czech));
        doc.add(new Paragraph("Toto je okno.").setFont(czech));
        doc.add(new Paragraph("Zopakujte to pros\u00edm.").setFont(czech));
        doc.add(new Paragraph("\u041e\u0442\u043a\u0443\u0434\u0430 \u0442\u044b?")
                .setFont(russian));
        doc.add(new Paragraph("\u0423\u0432\u0438\u0434\u0438\u043c\u0441\u044f \u0432 " +
                "\u043d\u0435\u043c\u043d\u043e\u0433\u043e. \u0423\u0432\u0438\u0434\u0438\u043c\u0441\u044f.")
                .setFont(russian));
        doc.add(new Paragraph("\u041f\u043e\u0437\u0432\u043e\u043b\u044c\u0442\u0435 \u043c\u043d\u0435 " +
                "\u043f\u0440\u0435\u0434\u0441\u0442\u0430\u0432\u0438\u0442\u044c\u0441\u044f.")
                .setFont(russian));
        doc.add(new Paragraph("\u042d\u0442\u043e \u0441\u0442\u0443\u0434\u0435\u043d\u0442.")
                .setFont(russian));
        doc.add(new Paragraph("\u0425\u043e\u0440\u043e\u0448\u043e?")
                .setFont(russian));
        doc.add(new Paragraph("\u041e\u043d \u0438\u043d\u0436\u0435\u043d\u0435\u0440. " +
                "\u041e\u043d\u0430 \u0434\u043e\u043a\u0442\u043e\u0440.")
                .setFont(russian));
        doc.add(new Paragraph("\u042d\u0442\u043e \u043e\u043a\u043d\u043e.")
                .setFont(russian));
        doc.add(new Paragraph("\u041f\u043e\u0432\u0442\u043e\u0440\u0438\u0442\u0435, " +
                "\u043f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430.")
                .setFont(russian));

        doc.close();
    }
}
