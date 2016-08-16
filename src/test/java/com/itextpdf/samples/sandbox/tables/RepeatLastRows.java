/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22153449/print-last-5-rows-to-next-page-itext-java
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
public class RepeatLastRows extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/repeat_last_rows.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RepeatLastRows().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(1);
        table.setWidth(523);
        table.setNextRenderer(new RepeatTableRenderer(table, new Table.RowRange(0, 113)));
        // the number is changed in order to provide the same as in itext5 example
        for (int i = 1; i < 115; i++)
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
            if (rows.size() - row > 5) {
                overflowRenderer = (RepeatTableRenderer) createOverflowRenderer(
                        new Table.RowRange(rowRange.getStartRow() + row, rowRange.getFinishRow()));
                overflowRenderer.rows = rows.subList(row, rows.size());
            } else {
                overflowRenderer = (RepeatTableRenderer) createOverflowRenderer(
                        new Table.RowRange(rowRange.getFinishRow() - 5, rowRange.getFinishRow()));
                overflowRenderer.rows = rows.subList(rows.size() - 5, rows.size());
            }
            splitRenderer.occupiedArea = occupiedArea;
            return new TableRenderer[]{splitRenderer, overflowRenderer};
        }
    }
}
