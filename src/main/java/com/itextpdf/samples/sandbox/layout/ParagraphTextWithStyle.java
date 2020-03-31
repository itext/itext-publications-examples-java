/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written for JavaOne 2016.
 * Differences between iText 5 and iText 7 are discussed in the JavaOne talk
 * "Oops, I broke my API" by Raf Hens and Bruno Lowagie.
 * This is the iText 7 version of one of the examples.
 */
package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.File;
import java.io.IOException;

public class ParagraphTextWithStyle {
    public static final String DEST = "./target/sandbox/layout/paragraphTextWithStyle.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ParagraphTextWithStyle().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        PdfFont code = PdfFontFactory.createFont(StandardFonts.COURIER);

        Style style = new Style()
                .setFont(code)
                .setFontSize(14)
                .setFontColor(ColorConstants.RED)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY);

        Paragraph paragraph = new Paragraph()
                .add("In this example, named ")
                .add(new Text("HelloWorldStyles").addStyle(style))
                .add(", we experiment with some text in ")
                .add(new Text("code style").addStyle(style))
                .add(".");

        try (Document document = new Document(pdf)) {
            document.add(paragraph);
        }
    }
}
