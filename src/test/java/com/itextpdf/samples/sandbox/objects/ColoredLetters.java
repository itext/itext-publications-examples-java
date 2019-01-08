/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27080565/itext-make-a-single-letter-bold-within-a-word
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class ColoredLetters extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/colored_letters.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColoredLetters().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph();
        String s = "all text is written in red, except the letters b and g; they are written in blue and green.";
        for (int i = 0; i < s.length(); i++) {
            p.add(returnCorrectColor(s.charAt(i)));
        }
        doc.add(p);

        doc.close();
    }

    private Text returnCorrectColor(char letter) throws IOException {
        if (letter == 'b') {
            return new Text("b")
                    .setFontColor(ColorConstants.BLUE)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD));
        } else if (letter == 'g') {
            return new Text("g")
                    .setFontColor(ColorConstants.GREEN)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                    .setItalic();
        } else {
            return new Text(String.valueOf(letter))
                    .setFontColor(ColorConstants.RED)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));
        }
    }
}
