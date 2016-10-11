/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23935566/table-borders-not-expanding-properly-in-pdf-using-itext
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class CustomBorder extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/custom_border.pdf";
    public static final String TEXT = "This is some long paragraph\n" +
            "that will be added over and over\n" +
            "again to prove a point.\n" +
            "It should result in rows\n" +
            "that are split\n" +
            " and rows that aren't.";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CustomBorder().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(2);
        table.setWidth(500);

        for (int i = 1; i < 60; i++) {
            table.addCell(new Cell().add(new Paragraph("Cell " + i)).setBorderBottom(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph(TEXT)).setBorderBottom(Border.NO_BORDER));
        }
        // the last row
        table.addCell(new Cell().add(new Paragraph("Cell " + 60)));
        table.addCell(new Cell().add(new Paragraph(TEXT)));

        doc.add(table);

        doc.close();
    }
}
