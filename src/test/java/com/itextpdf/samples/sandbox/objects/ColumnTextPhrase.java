/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27989754/itext-failure-with-adding-elements-with-5-5-4
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class ColumnTextPhrase {
    public static final String DEST = "./target/sandbox/objects/column_text_phrase.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColumnTextPhrase().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD, PdfEncodings.CP1252, true);
        Paragraph pz = new Paragraph("Hello World!").setFont(font).setFontSize(13);
        new Canvas(new PdfCanvas(pdfDoc.getFirstPage()), pdfDoc, new Rectangle(120, 48, 80, 580))
                .add(pz);

        // The Leading is used in this paragraph, the Leading is a spacing between lines of text
        font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        pz = new Paragraph("Hello World!").setFont(font).setFixedLeading(20);
        new Canvas(new PdfCanvas(pdfDoc.getFirstPage()), pdfDoc, new Rectangle(120, 48, 80, 480))
                .add(pz);

        pdfDoc.close();
    }
}
