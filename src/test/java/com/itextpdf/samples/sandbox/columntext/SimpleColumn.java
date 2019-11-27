/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/34445641
 */
package com.itextpdf.samples.sandbox.columntext;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;

public class SimpleColumn {
    public static final String DEST = "./target/sandbox/columntext/simple_column.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleColumn().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(100, 120));

        Paragraph paragraph = new Paragraph("REALLLLLLLLLLY LONGGGGGGGGGG text").setFontSize(4.5f);

        paragraph.setWidth(61);
        doc.showTextAligned(paragraph, 9,85, TextAlignment.LEFT);

        doc.close();
    }
}
