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
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class RepeatLastRows2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/repeat_last_rows2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RepeatLastRows2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(1);
        table.setWidth(523);
        table.setNextRenderer(new RepeatTableRenderer(table, new Table.RowRange(0, 100)));
        for (int i = 1; i < 100; i++)
            table.addCell(new Cell().add(new Paragraph("row " + i)));
        doc.add(table);

        doc.close();
    }


    private class RepeatTableRenderer extends TableRenderer {
        public RepeatTableRenderer(Table modelElement, Table.RowRange rowRange) {
            super(modelElement, rowRange);
        }

        protected RepeatTableRenderer(Table modelElement) {
            super(modelElement);
        }

        @Override
        public RepeatTableRenderer getNextRenderer() {
            return new RepeatTableRenderer((Table) modelElement);
        }

        @Override
        protected TableRenderer[] split(int row) {
            RepeatTableRenderer splitRenderer = (RepeatTableRenderer) createSplitRenderer(
                    new Table.RowRange(rowRange.getStartRow(), rowRange.getStartRow() + row));
            splitRenderer.rows = rows.subList(0, row);
            RepeatTableRenderer overflowRenderer;
            if (row > 5) {
                overflowRenderer = (RepeatTableRenderer) createOverflowRenderer(
                        new Table.RowRange(rowRange.getStartRow() - 5 + row, rowRange.getFinishRow()));
                overflowRenderer.rows = rows.subList(row - 5, rows.size());
            } else {
                overflowRenderer = (RepeatTableRenderer) createOverflowRenderer(
                        new Table.RowRange(rowRange.getStartRow() + row, rowRange.getFinishRow()));
                overflowRenderer.rows = rows.subList(row, rows.size());
            }
            splitRenderer.occupiedArea = occupiedArea;
            return new TableRenderer[]{splitRenderer, overflowRenderer};
        }
    }
}
