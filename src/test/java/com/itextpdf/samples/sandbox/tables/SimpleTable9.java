/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class SimpleTable9 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/simple_table9.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleTable9().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("With 3 columns:"));
        Table table = new Table(new float[]{1, 1, 8});
        table.setWidthPercent(100);
        table.setMarginTop(5);
        table.addCell("Col a");
        table.addCell("Col b");
        table.addCell("Col c");
        table.addCell("Value a");
        table.addCell("Value b");
        table.addCell("This is a long description for column c. " +
                "It needs much more space hence we made sure that the third column is wider.");
        doc.add(table);
        doc.add(new Paragraph("With 2 columns:"));
        table = new Table(2);
        table.setMarginTop(5);
        table.addCell("Col a");
        table.addCell("Col b");
        table.addCell("Value a");
        table.addCell("Value b");
        table.addCell(new Cell(1, 2).add("Value b"));
        table.addCell(new Cell(1, 2).add("This is a long description for column c. " +
                "It needs much more space hence we made sure that the third column is wider."));
        table.addCell("Col a");
        table.addCell("Col b");
        table.addCell("Value a");
        table.addCell("Value b");
        table.addCell(new Cell(1, 2).add("Value b"));
        table.addCell(new Cell(1, 2).add("This is a long description for column c. " +
                "It needs much more space hence we made sure that the third column is wider."));
        doc.add(table);

        doc.close();
    }
}
