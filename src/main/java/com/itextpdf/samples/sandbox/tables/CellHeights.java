package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class CellHeights {
    public static final String DEST = "./target/sandbox/tables/cell_heights.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CellHeights().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A5.rotate());

        // By default column width is calculated automatically for the best fit.
        // useAllAvailableWidth() method makes table use the whole page's width while placing the content.
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        // a long phrase with newlines
        Paragraph p = new Paragraph("Dr. iText or:\nHow I Learned to Stop Worrying\nand Love PDF.");
        Cell cell = new Cell().add(p);

        // the phrase fits the fixed height
        table.addCell("set height (more than sufficient)");
        cell.setHeight(172);

        // In iText7 a cell is meant to be used only once in the table.
        // If you want to reuse it, please clone it (either including the content or not)
        table.addCell(cell.clone(true));

        // the phrase doesn't fit the fixed height
        table.addCell("set height (not sufficient)");
        cell.setHeight(36);
        table.addCell(cell.clone(true));

        // The minimum height is exceeded
        table.addCell("minimum height");
        cell = new Cell().add(new Paragraph("Dr. iText"));
        cell.setMinHeight(70);
        table.addCell(cell.clone(true));

        // the last cell that should be extended
        table.addCell("extend last row");
        cell.deleteOwnProperty(Property.MIN_HEIGHT);
        table.addCell(cell.clone(true));

        table.setExtendBottomRow(true);

        doc.add(table);

        doc.close();
    }
}
