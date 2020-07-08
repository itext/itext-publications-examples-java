package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class ListInCell {
    public static final String DEST = "./target/sandbox/tables/list_in_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ListInCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        // This is how not to do it (but it works anyway):

        // Create a list:
        List list = new List();
        list.add(new ListItem("Item 1"));
        list.add(new ListItem("Item 2"));
        list.add(new ListItem("Item 3"));

        // Wrap this list in a paragraph
        Paragraph paragraph = new Paragraph();
        paragraph.add(list);

        // Add this paragraph to a cell
        Cell paragraphCell = new Cell();
        paragraphCell.add(paragraph);

        // Add the cell to a table
        Table paragraphTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        paragraphTable.setMarginTop(5);
        paragraphTable.addCell("List wrapped in a paragraph:");
        paragraphTable.addCell(paragraphCell);

        /// Add this nested table to the document
        doc.add(new Paragraph("A list, wrapped in a paragraph, wrapped in a cell, " +
                "wrapped in a table, wrapped in a phrase:"));
        paragraphTable.setMarginTop(5);
        doc.add(paragraphTable);

        // This is how to do it:

        // Add the list directly to a cell
        Cell cell = new Cell();
        cell.add(list);

        // Add the cell to the table
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.setMarginTop(5);
        table.addCell("List placed directly into cell");
        table.addCell(cell);

        // Add the table to the document
        doc.add(new Paragraph("A list, wrapped in a cell, wrapped in a table:"));
        doc.add(table);

        // Avoid adding tables to paragraph (but it works anyway):
        Paragraph tableWrapper = new Paragraph();
        tableWrapper.setMarginTop(0);
        tableWrapper.add(table);
        doc.add(new Paragraph("A list, wrapped in a cell, wrapped in a table, wrapped in a paragraph:"));

        doc.add(tableWrapper);

        doc.close();
    }
}
