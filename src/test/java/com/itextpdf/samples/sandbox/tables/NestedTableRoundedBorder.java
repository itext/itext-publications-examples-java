/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written in answer to the following question:
 * http://stackoverflow.com/questions/31330062/need-to-make-pdf-sample-with-boxes-as-table-columns-by-android-app
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class NestedTableRoundedBorder extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/nested_table_rounded_border.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new NestedTableRoundedBorder().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Cell cell;
        // outer table
        Table outertable = new Table(1);
        // inner table 1
        Table innertable = new Table(new float[]{8, 12, 1, 4, 12});
        innertable.setWidthPercent(100);
        // first row
        // column 1
        cell = new Cell().add("Record Ref:");
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // column 2
        cell = new Cell().add("GN Staff");
        cell.setPaddingLeft(2);
        innertable.addCell(cell);
        // column 3
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // column 4
        cell = new Cell().add("Date: ");
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // column 5
        cell = new Cell().add("30/4/2015");
        cell.setPaddingLeft(2);
        innertable.addCell(cell);
        // spacing
        cell = new Cell(1, 5);
        cell.setHeight(3);
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // second row
        // column 1
        cell = new Cell().add("Hospital:");
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // column 2
        cell = new Cell().add("Derby Royal");
        cell.setPaddingLeft(2);
        innertable.addCell(cell);
        // column 3
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // column 4
        cell = new Cell().add("Ward: ");
        cell.setBorder(Border.NO_BORDER);
        cell.setPaddingLeft(5);
        innertable.addCell(cell);
        // column 5
        cell = new Cell().add("21");
        cell.setPaddingLeft(2);
        innertable.addCell(cell);
        // spacing
        cell = new Cell(1, 5);
        cell.setHeight(3);
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // first nested table
        cell = new Cell().add(innertable);
        cell.setNextRenderer(new RoundedBorderCellRenderer(cell));
        cell.setBorder(Border.NO_BORDER);
        cell.setPadding(8);
        outertable.addCell(cell);
        // inner table 2
        innertable = new Table(new float[]{3, 17, 1, 16});
        innertable.setWidthPercent(100);
        // first row
        // column 1
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // column 2
        cell = new Cell().add("Name");
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // column 3
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // column 4
        cell = new Cell().add("Signature: ");
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // spacing
        cell = new Cell(1, 4);
        cell.setHeight(3);
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);
        // subsequent rows
        for (int i = 1; i < 4; i++) {
            // column 1
            cell = new Cell().add(String.format("%s:", i));
            cell.setBorder(Border.NO_BORDER);
            innertable.addCell(cell);
            // column 2
            cell = new Cell();
            innertable.addCell(cell);
            // column 3
            cell = new Cell();
            cell.setBorder(Border.NO_BORDER);
            innertable.addCell(cell);
            // column 4
            cell = new Cell();
            innertable.addCell(cell);
            // spacing
            cell = new Cell(1, 4);
            cell.setHeight(3);
            cell.setBorder(Border.NO_BORDER);
            innertable.addCell(cell);
        }
        // second nested table
        cell = new Cell().add(innertable);
        cell.setNextRenderer(new RoundedBorderCellRenderer(cell));
        cell.setBorder(Border.NO_BORDER);
        cell.setPaddingLeft(8);
        cell.setPaddingTop(8);
        cell.setPaddingRight(8);
        cell.setPaddingBottom(8);
        outertable.addCell(cell);
        // add the table
        doc.add(outertable);

        doc.close();
    }


    private class RoundedBorderCellRenderer extends CellRenderer {
        public RoundedBorderCellRenderer(Cell modelElement) {
            super(modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            drawContext.getCanvas().roundRectangle(getOccupiedAreaBBox().getX() + 1.5f, getOccupiedAreaBBox().getY() + 1.5f,
                    getOccupiedAreaBBox().getWidth() - 3, getOccupiedAreaBBox().getHeight() - 3, 4);
            drawContext.getCanvas().stroke();
            super.draw(drawContext);
        }
    }
}
