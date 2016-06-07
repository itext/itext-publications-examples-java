/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24220668/fontfactory-lowagie-java-getting-java-io-eofexception-when-trying-to-use-gre
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
public class LiberationSans extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/fonts/liberation_sans.pdf";
    public static final String FONT = "./src/test/resources/font/LiberationSans-Regular.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new LiberationSans().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfFontFactory.register(FONT, "Greek-Regular");
        PdfFont f = PdfFontFactory.createRegisteredFont("Greek-Regular", PdfEncodings.CP1253, true);

        Paragraph p = new Paragraph("\u039d\u03cd\u03c6\u03b5\u03c2").setFont(f);
        doc.add(p);

        doc.close();
    }
}
