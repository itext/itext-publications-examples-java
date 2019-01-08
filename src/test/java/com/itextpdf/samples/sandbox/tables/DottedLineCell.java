/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class DottedLineCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/dotted_line_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DottedLineCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Table event"));
        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table.setNextRenderer(new DottedLineTableRenderer(table, new Table.RowRange(0, 2)));
        table.addCell(new Cell().add(new Paragraph("A1")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("A2")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("A3")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("B1")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("B2")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("B3")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("C1")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("C2")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("C3")).setBorder(Border.NO_BORDER));
        doc.add(table);
        doc.add(new Paragraph("Cell event"));
        table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        Cell cell = new Cell().add(new Paragraph("Test"));
        cell.setNextRenderer(new DottedLineCellRenderer(cell));
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell.setBorder(Border.NO_BORDER));
        doc.add(table);

        doc.close();
    }


    private class DottedLineTableRenderer extends TableRenderer {
        public DottedLineTableRenderer(Table modelElement, Table.RowRange rowRange) {
            super(modelElement, rowRange);
        }

        @Override
        public void drawChildren(DrawContext drawContext) {
            super.drawChildren(drawContext);
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.setLineDash(3f, 3f);
            // first horizontal line
            CellRenderer[] cellRenderers = rows.get(0);
            canvas.moveTo(cellRenderers[0].getOccupiedArea().getBBox().getLeft(),
                    cellRenderers[0].getOccupiedArea().getBBox().getTop());
            canvas.lineTo(cellRenderers[cellRenderers.length - 1].getOccupiedArea().getBBox().getRight(),
                    cellRenderers[cellRenderers.length - 1].getOccupiedArea().getBBox().getTop());

            for (int i = 0; i < rows.size(); i++) {
                cellRenderers = rows.get(i);
                // horizontal lines
                canvas.moveTo(cellRenderers[0].getOccupiedArea().getBBox().getX(),
                        cellRenderers[0].getOccupiedArea().getBBox().getY());
                canvas.lineTo(cellRenderers[cellRenderers.length - 1].getOccupiedArea().getBBox().getRight(),
                        cellRenderers[cellRenderers.length - 1].getOccupiedArea().getBBox().getBottom());
                // first vertical line
                Rectangle cellRect = cellRenderers[0].getOccupiedArea().getBBox();
                canvas.moveTo(cellRect.getLeft(), cellRect.getBottom());
                canvas.lineTo(cellRect.getLeft(), cellRect.getTop());
                // vertical lines
                for (int j = 0; j < cellRenderers.length; j++) {
                    cellRect = cellRenderers[j].getOccupiedArea().getBBox();
                    canvas.moveTo(cellRect.getRight(), cellRect.getBottom());
                    canvas.lineTo(cellRect.getRight(), cellRect.getTop());
                }
            }
            canvas.stroke();
        }
    }


    private class DottedLineCellRenderer extends CellRenderer {
        public DottedLineCellRenderer(Cell modelElement) {
            super(modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            drawContext.getCanvas().setLineDash(3f, 3f);
            drawContext.getCanvas().rectangle(this.getOccupiedArea().getBBox());
            drawContext.getCanvas().stroke();
        }
    }
}
