/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
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

import java.io.File;

public class LiberationSans {
    public static final String DEST = "./target/sandbox/fonts/liberation_sans.pdf";

    public static final String FONT = "./src/test/resources/font/LiberationSans-Regular.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LiberationSans().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        String fontName = "Greek-Regular";
        PdfFontFactory.register(FONT, fontName);
        PdfFont f = PdfFontFactory.createRegisteredFont(fontName, PdfEncodings.CP1253, true);

        // "Νύφες"
        Paragraph p = new Paragraph("\u039d\u03cd\u03c6\u03b5\u03c2").setFont(f);
        doc.add(p);

        doc.close();
    }
}
