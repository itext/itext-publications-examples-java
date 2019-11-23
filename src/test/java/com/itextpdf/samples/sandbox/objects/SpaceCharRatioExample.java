/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/30869268/java-itext-alignment
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.IOException;

public class SpaceCharRatioExample {
    public static final String DEST = "./target/sandbox/objects/space_char_ratio.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SpaceCharRatioExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph()
                .setSpacingRatio(1f)
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setMarginLeft(20f)
                .setMarginRight(20f)
                .add("HelloWorld HelloWorld HelloWorld HelloWorld HelloWorldHelloWorld HelloWorldHelloWorldHelloWorld" +
                        "HelloWorldHelloWorld HelloWorld HelloWorld HelloWorldHelloWorldHelloWorld");
        doc.add(p);

        doc.close();
    }

}
