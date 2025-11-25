package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/*
 * TickboxCharacter.java
 *
 * This example demonstrates displaying a checkbox character in a PDF document using the ZapfDingbats standard font.
 * The sample shows how to render special symbol characters by utilizing one of PDF's built-in fonts without requiring font embedding.
 */

public class TickboxCharacter {
    public static final String DEST = "./target/sandbox/fonts/tickbox_character.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TickboxCharacter().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph("This is a tick box character: ");

        Text text = new Text("o");
        PdfFont zapfdingbats = PdfFontFactory.createFont(StandardFonts.ZAPFDINGBATS);
        text.setFont(zapfdingbats);
        text.setFontSize(14);
        p.add(text);

        doc.add(p);

        doc.close();
    }
}
