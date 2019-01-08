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
import com.itextpdf.layout.Style;
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
public class DottedLineHeader extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/dotted_line_header.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DottedLineHeader().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table.setNextRenderer(new DottedHeaderTableRenderer(table, new Table.RowRange(0, 1)));
        Style noBorder = new Style().setBorder(Border.NO_BORDER);
        table.addHeaderCell(new Cell().add(new Paragraph("A1")).addStyle(noBorder));
        table.addHeaderCell(new Cell().add(new Paragraph("A2")).addStyle(noBorder));
        table.addHeaderCell(new Cell().add(new Paragraph("A3")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("B1")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("B2")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("B3")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("C1")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("C2")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("C3")).addStyle(noBorder));
        doc.add(table);
        doc.add(new Paragraph("Cell event"));
        table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        Cell cell = new Cell().add(new Paragraph("A1")).addStyle(noBorder);
        cell.setNextRenderer(new DottedHeaderCellRenderer(cell));
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("A2")).addStyle(noBorder);
        cell.setNextRenderer(new DottedHeaderCellRenderer(cell));
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("A3")).addStyle(noBorder);
        cell.setNextRenderer(new DottedHeaderCellRenderer(cell));
        table.addCell(cell);
        table.addCell(new Cell().add(new Paragraph("B1")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("B2")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("B3")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("C1")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("C2")).addStyle(noBorder));
        table.addCell(new Cell().add(new Paragraph("C3")).addStyle(noBorder));
        doc.add(table);

        doc.close();
    }


    private class DottedHeaderTableRenderer extends TableRenderer {
        public DottedHeaderTableRenderer(Table modelElement, Table.RowRange rowRange) {
            super(modelElement, rowRange);
        }

        @Override
        public void drawChildren(DrawContext drawContext) {
            super.drawChildren(drawContext);
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.setLineDash(3f, 3f);
            Rectangle headersArea = headerRenderer.getOccupiedArea().getBBox();
            canvas.moveTo(headersArea.getLeft(), headersArea.getTop());
            canvas.lineTo(headersArea.getRight(), headersArea.getTop());

            canvas.moveTo(headersArea.getLeft(), headersArea.getBottom());
            canvas.lineTo(headersArea.getRight(), headersArea.getBottom());
            canvas.stroke();
        }
    }


    private class DottedHeaderCellRenderer extends CellRenderer {
        public DottedHeaderCellRenderer(Cell modelElement) {
            super(modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.setLineDash(3f, 3f);
            canvas.moveTo(this.getOccupiedArea().getBBox().getLeft(), this.getOccupiedArea().getBBox().getBottom());
            canvas.lineTo(this.getOccupiedArea().getBBox().getRight(),
                    this.getOccupiedArea().getBBox().getBottom());
            canvas.moveTo(this.getOccupiedArea().getBBox().getLeft(),
                    this.getOccupiedArea().getBBox().getTop());
            canvas.lineTo(this.getOccupiedArea().getBBox().getRight(),
                    this.getOccupiedArea().getBBox().getTop());
            canvas.stroke();
        }
    }
}
