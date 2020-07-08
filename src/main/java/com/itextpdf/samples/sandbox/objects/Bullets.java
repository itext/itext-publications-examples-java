package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;
import java.io.IOException;

public class Bullets {
    public static final String DEST = "./target/sandbox/objects/bullets.pdf";
    public static final String[] ITEMS = {
            "Insurance system", "Agent", "Agency", "Agent Enrollment", "Agent Settings",
            "Appointment", "Continuing Education", "Hierarchy", "Recruiting", "Contract",
            "Message", "Correspondence", "Licensing", "Party"
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Bullets().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont zapfdingbatsFont = PdfFontFactory.createFont(StandardFonts.ZAPFDINGBATS);
        Text bullet = new Text(String.valueOf((char) 108)).setFont(zapfdingbatsFont);
        PdfFont helveticaFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // No-Break Space prevents breaking a text in this place
        char space = '\u00a0';

        Paragraph p = new Paragraph("Items can be split if they don't fit at the end: ").setFont(helveticaFont);
        for (String item : ITEMS) {
            p.add(bullet);
            p.add(new Text(" " + item + " ")).setFont(helveticaFont);
        }
        doc.add(p);
        doc.add(new Paragraph("\n"));

        p = new Paragraph("Items can't be split if they don't fit at the end: ").setFont(helveticaFont);
        for (String item : ITEMS) {
            p.add(bullet);
            p.add(new Text(space + item.replace(' ', space) + " ")).setFont(helveticaFont);
        }
        doc.add(p);
        doc.add(new Paragraph("\n"));

        PdfFont freeSansFont = PdfFontFactory.createFont("./src/main/resources/font/FreeSans.ttf", PdfEncodings.IDENTITY_H);
        p = new Paragraph("Items can't be split if they don't fit at the end: ").setFont(freeSansFont);
        for (String item : ITEMS) {

            // Paste unicode Bullet symbol
            p.add(new Text("\u2022" + space + item.replace(' ', space) + " "));
        }
        doc.add(p);

        doc.close();
    }
}
