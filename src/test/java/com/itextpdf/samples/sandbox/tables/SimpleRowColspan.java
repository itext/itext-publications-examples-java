/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/20016630/how-to-create-a-table-in-a-generated-pdf-using-itextsharp
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class SimpleRowColspan extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/simple_row_colspan.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleRowColspan().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(new float[]{1, 2, 2, 2, 1});
        table.setWidthPercent(100);

        Cell cell;
        cell = new Cell(2, 1).add("S/N");
        table.addCell(cell);
        cell = new Cell(1, 3).add("Name");
        table.addCell(cell);
        cell = new Cell(2, 1).add("Age");
        table.addCell(cell);
        table.addCell("SURNAME");
        table.addCell("FIRST NAME");
        table.addCell("MIDDLE NAME");
        table.addCell("1");
        table.addCell("James");
        table.addCell("Fish");
        table.addCell("Stone");
        table.addCell("17");
        doc.add(table);

        doc.close();
    }
}
