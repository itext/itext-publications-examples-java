/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie and Nishanthi Grashia in answer to the following question:
 * http://stackoverflow.com/questions/24359321/adding-row-to-a-table-in-pdf-using-itext
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
public class SimpleTable2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/simple_table2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleTable2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(8);
        Cell cell = new Cell(2, 1).add("hi");
        table.addCell(cell);
        for (int i = 0; i < 14; i++) {
            table.addCell("hi");
        }
        doc.add(table);

        doc.close();
    }
}
