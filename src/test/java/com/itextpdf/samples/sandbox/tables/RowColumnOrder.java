/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/37526223
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class RowColumnOrder extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/row_column_order.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RowColumnOrder().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("By design tables are filled row by row:"));
        Table table = new Table(5);
        table.setMarginTop(10);
        table.setMarginBottom(10);
        for (int i = 1; i <= 15; i++) {
            table.addCell("cell " + i);
        }
        doc.add(table);

        doc.add(new Paragraph("If you want to change this behavior, you need to create a two-dimensional array first:"));
        String[][] array = new String[3][];
        int column = 0;
        int row = 0;
        for (int i = 1; i <= 15; i++) {
            if (column == 0) {
                array[row] = new String[5];
            }
            array[row++][column] = "cell " + i;
            if (row == 3) {
                column++;
                row = 0;
            }
        }
        table = new Table(5);
        table.setMarginTop(10);
        for (String[] r : array) {
            for (String c : r) {
                table.addCell(c);
            }
        }
        doc.add(table);
        doc.close();
    }
}
