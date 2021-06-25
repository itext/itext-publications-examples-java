package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class RepeatLastRows {
    public static final String DEST = "./target/sandbox/tables/repeat_last_rows.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RepeatLastRows().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        table.setNextRenderer(new RepeatTableRenderer(table, new Table.RowRange(0, 113)));

        for (int i = 1; i < 115; i++) {
            table.addCell(new Cell().add(new Paragraph("row " + i)));
        }

        doc.add(table);

        doc.close();
    }


    private static class RepeatTableRenderer extends TableRenderer {
        public RepeatTableRenderer(Table modelElement, Table.RowRange rowRange) {
            super(modelElement, rowRange);
        }

        protected RepeatTableRenderer(Table modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
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

            return new TableRenderer[] {splitRenderer, overflowRenderer};
        }
    }
}
