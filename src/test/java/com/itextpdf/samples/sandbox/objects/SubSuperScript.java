/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24772212/itext-pdf-writer-is-there-any-way-to-allow-unicode-subscript-symbol-in-the-pdf
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class SubSuperScript {
    public static final String DEST = "./target/sandbox/objects/sub_super_script.pdf";
    public static final String FONT = "./src/test/resources/font/Cardo-Regular.ttf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SubSuperScript().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        // Subscript two and superscript four respectively
        Paragraph p = new Paragraph("H\u2082SO\u2074").setFont(font).setFontSize(10);
        doc.add(p);

        doc.close();
    }
}
