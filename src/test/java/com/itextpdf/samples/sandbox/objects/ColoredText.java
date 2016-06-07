/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27578497/applying-color-to-strings-in-paragraph-using-itext
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class ColoredText extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/colored_text.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColoredText().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Text redText = new Text("This text is red. ")
                .setFontColor(Color.RED)
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA));
        Text blueText = new Text("This text is blue and bold. ")
                .setFontColor(Color.BLUE)
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD));
        Text greenText = new Text("This text is green and italic. ")
                .setFontColor(Color.GREEN)
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE));

        Paragraph p1 = new Paragraph(redText).setMargin(0);
        doc.add(p1);
        Paragraph p2 = new Paragraph().setMargin(0);
        p2.add(blueText);
        p2.add(greenText);
        doc.add(p2);

        new Canvas(new PdfCanvas(pdfDoc.getLastPage()), pdfDoc, new Rectangle(36, 600, 108, 160))
                .add(p1)
                .add(p2);

        doc.close();
    }
}
