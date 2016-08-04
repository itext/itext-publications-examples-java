/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/37991675
 */

package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
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
public class TickboxCharacter extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/fonts/tickbox_character.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TickboxCharacter().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph("This is a tick box character: ");

        Text text = new Text("o");
        PdfFont zapfdingbats = PdfFontFactory.createFont(FontConstants.ZAPFDINGBATS);
        text.setFont(zapfdingbats);
        text.setFontSize(14);
        p.add(text);

        doc.add(p);

        doc.close();
    }
}
