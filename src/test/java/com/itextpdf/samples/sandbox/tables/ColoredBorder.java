/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/35073619
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class ColoredBorder extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/colored_border.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColoredBorder().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table;
        table = new Table(2);
        Cell cell;
        cell = new Cell().add("Cell 1");
        cell.setBorderTop(new SolidBorder(Color.RED, 1));
        cell.setBorderBottom(new SolidBorder(Color.BLUE, 1));
        table.addCell(cell);
        cell = new Cell().add("Cell 2");
        cell.setBorderLeft(new SolidBorder(Color.GREEN, 5));
        cell.setBorderTop(new SolidBorder(Color.YELLOW, 8));
        table.addCell(cell);
        cell = new Cell().add("Cell 3");
        cell.setBorderLeft(new SolidBorder(Color.RED, 1));
        cell.setBorderBottom(new SolidBorder(Color.BLUE, 1));
        table.addCell(cell);
        cell = new Cell().add("Cell 4");
        cell.setBorderLeft(new SolidBorder(Color.GREEN, 5));
        cell.setBorderTop(new SolidBorder(Color.YELLOW, 8));
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
