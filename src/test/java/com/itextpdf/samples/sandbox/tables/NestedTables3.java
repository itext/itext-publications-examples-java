/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following questions:
 * http://stackoverflow.com/questions/31108488/pdfptable-header-repeat-when-data-in-a-different-table-increases-in-itext
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.renderer.AbstractRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class NestedTables3 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/nested_tables3.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new NestedTables3().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        Table table = new Table(2);
        table.setNextRenderer(new InnerTableRenderer(table, new Table.RowRange(0, 0)));
        Cell cell = new Cell(1, 2).add("This outer header is repeated on every page");
        table.addHeaderCell(cell);
        Table inner1 = new Table(1);
        cell = new Cell();
        cell.setHeight(20);
        inner1.addHeaderCell(cell);
        cell = new Cell().add("This inner header won't be repeated on every page");
        inner1.addHeaderCell(cell);
        for (int i = 0; i < 10; i++) {
            inner1.addCell(new Cell().add(new Paragraph("test")));
        }
        cell = new Cell().add(inner1);
        table.addCell(cell.setPadding(0));
        Table inner2 = new Table(1);
        cell = new Cell();
        cell.setHeight(20);
        inner2.addHeaderCell(cell);
        cell = new Cell().add("This inner may be repeated on every page");
        inner2.addHeaderCell(cell);
        for (int i = 0; i < 35; i++) {
            inner2.addCell("test");
        }
        cell = new Cell().add(inner2);
        table.addCell(cell.setPadding(0));
        doc.add(table);

        doc.close();
    }

    private class InnerTableRenderer extends TableRenderer {
        public InnerTableRenderer(Table modelElement, Table.RowRange rowRange) {
            super(modelElement, rowRange);
        }

        protected InnerTableRenderer(Table modelElement) {
            super(modelElement);
        }

        @Override
        protected TableRenderer[] split(int row) {
            InnerTableRenderer splitRenderer = (InnerTableRenderer) createSplitRenderer(
                    new Table.RowRange(rowRange.getStartRow(), rowRange.getStartRow() + row));
            splitRenderer.rows = rows.subList(0, row);
            InnerTableRenderer overflowRenderer = (InnerTableRenderer) createOverflowRenderer(
                    new Table.RowRange(rowRange.getStartRow() + row, rowRange.getFinishRow()));
            overflowRenderer.rows = rows.subList(row, rows.size());
            splitRenderer.occupiedArea = occupiedArea;

            return new TableRenderer[]{splitRenderer, overflowRenderer};
        }

        @Override
        public void drawChildren(DrawContext drawContext) {
            super.drawChildren(drawContext);
            for (IRenderer renderer : childRenderers) {
                PdfCanvas canvas = drawContext.getCanvas();
                canvas.beginText();
                Rectangle box = ((AbstractRenderer) renderer).getInnerAreaBBox();
                canvas.moveText(box.getLeft(), box.getTop() - this.getPropertyAsFloat(Property.FONT_SIZE));
                canvas.setFontAndSize(this.getPropertyAsFont(Property.FONT),
                        this.getPropertyAsFloat(Property.FONT_SIZE));
                canvas.showText("This inner table header will always be repeated");
                canvas.endText();
                canvas.stroke();
            }
        }

        @Override
        public InnerTableRenderer getNextRenderer() {
            return new InnerTableRenderer((Table) modelElement);
        }
    }
}
