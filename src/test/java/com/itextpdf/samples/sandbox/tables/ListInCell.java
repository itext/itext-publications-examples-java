/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class ListInCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/list_in_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListInCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        // This is how not to do it (but it works anyway):
        // We create a list:
        List list = new List();
        list.add(new ListItem("Item 1"));
        list.add(new ListItem("Item 2"));
        list.add(new ListItem("Item 3"));

        Cell phraseCell = new Cell();
        phraseCell.add(list);

        // We add the cell to a table:
        Table phraseTable = new Table(2);
        phraseTable.setMarginTop(5);
        phraseTable.addCell("List wrapped in a phrase:");
        phraseTable.addCell(phraseCell);

        // We add these nested tables to the document:
        doc.add(new Paragraph("A list, wrapped in a phrase, wrapped in a cell, " +
                "wrapped in a table, wrapped in a phrase:"));
        phraseTable.setMarginTop(5);
        doc.add(phraseTable);


        // We add the list directly to a cell:
        Cell cell = new Cell();
        cell.add(list);

        // We add the cell to the table:
        Table table = new Table(2);
        table.setMarginTop(5);
        table.addCell("List placed directly into cell");
        table.addCell(cell);

        // We add the table to the document:
        doc.add(new Paragraph("A list, wrapped in a cell, wrapped in a table:"));
        doc.add(table);

        // Avoid adding tables to phrase (but it works anyway):

        doc.add(new Paragraph("A list, wrapped in a cell, wrapped in a table, wrapped in a phrase:"));
        table.setMarginTop(5);
        doc.add(table);

        doc.close();
    }
}
