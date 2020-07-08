package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;
import java.io.IOException;

public class ColoredLetters {
    public static final String DEST = "./target/sandbox/objects/colored_letters.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColoredLetters().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont helveticaFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont helveticaBoldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        Paragraph p = new Paragraph();
        String s = "all text is written in red, except the letters b and g; they are written in blue and green.";
        for (int i = 0; i < s.length(); i++) {
            p.add(returnCorrectColor(s.charAt(i), helveticaFont, helveticaBoldFont));
        }
        doc.add(p);

        doc.close();
    }

    private static Text returnCorrectColor(char letter, PdfFont helveticaFont, PdfFont helveticaBoldFont) {
        if (letter == 'b') {
            return new Text("b")
                    .setFontColor(ColorConstants.BLUE)
                    .setFont(helveticaBoldFont);
        } else if (letter == 'g') {
            return new Text("g")
                    .setFontColor(ColorConstants.GREEN)
                    .setFont(helveticaFont)
                    .setItalic();
        } else {
            return new Text(String.valueOf(letter))
                    .setFontColor(ColorConstants.RED)
                    .setFont(helveticaFont);
        }
    }
}
