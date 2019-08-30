/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This example was written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/34396579
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

public class FreeSansBold {
    public static final String DEST
            = "./target/sandbox/fonts/free_sans_bold.pdf";
    public static final String FONT
            = "./src/test/resources/font/FreeSans.ttf";
    public static final String FONTBOLD
            = "./src/test/resources/font/FreeSansBold.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FreeSansBold().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        Paragraph p = new Paragraph("FreeSans regular: \u0110").setFont(font);
        doc.add(p);

        PdfFont bold = PdfFontFactory.createFont(FONTBOLD, PdfEncodings.IDENTITY_H);
        p = new Paragraph("FreeSans bold: \u0110").setFont(bold);
        doc.add(p);

        doc.close();
    }
}
