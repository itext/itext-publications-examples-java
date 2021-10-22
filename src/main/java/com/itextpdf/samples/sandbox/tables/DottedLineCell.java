package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class DottedLineCell {
    public static final String DEST = "./target/sandbox/tables/dotted_line_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DottedLineCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Table event setter approach"));

        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table.addCell(createCellWithBorders("A1"));
        table.addCell(createCellWithBorders("A2"));
        table.addCell(createCellWithBorders("A3"));
        table.addCell(createCellWithBorders("B1"));
        table.addCell(createCellWithBorders("B2"));
        table.addCell(createCellWithBorders("B3"));
        table.addCell(createCellWithBorders("C1"));
        table.addCell(createCellWithBorders("C2"));
        table.addCell(createCellWithBorders("C3"));
        doc.add(table);

        doc.add(new Paragraph("Cell event setter approach"));

        table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        table.addCell(createCellWithBorders("Test"));

        doc.add(table);

        doc.add(new Paragraph("Table event custom render approach"));

        table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table.addCell(new Cell().add(new Paragraph("A1")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("A2")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("A3")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("B1")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("B2")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("B3")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("C1")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("C2")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("C3")).setBorder(Border.NO_BORDER));

        // Draws dotted line borders.
        table.setNextRenderer(new DottedLineTableRenderer(table));

        doc.add(table);

        doc.add(new Paragraph("Cell event custom render approach"));

        table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        Cell cell = new Cell().add(new Paragraph("Test"));
        cell.setNextRenderer(new DottedLineCellRenderer(cell));

        // Since we override the border drawing, we do not need the default logic to be triggered, that's why we set
        // the border value as null
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }


    private static class DottedLineTableRenderer extends TableRenderer {
        public DottedLineTableRenderer(Table modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new DottedLineTableRenderer((Table) modelElement);
        }

        @Override
        public void drawChildren(DrawContext drawContext) {
            super.drawChildren(drawContext);
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.setLineDash(1f, 3f);

            // first horizontal line
            CellRenderer[] cellRenderers = rows.get(0);
            canvas.moveTo(cellRenderers[0].getOccupiedArea().getBBox().getLeft(),
                    cellRenderers[0].getOccupiedArea().getBBox().getTop());
            canvas.lineTo(cellRenderers[cellRenderers.length - 1].getOccupiedArea().getBBox().getRight(),
                    cellRenderers[cellRenderers.length - 1].getOccupiedArea().getBBox().getTop());

            for (CellRenderer[] renderers : rows) {

                // horizontal lines
                canvas.moveTo(renderers[0].getOccupiedArea().getBBox().getX(),
                        renderers[0].getOccupiedArea().getBBox().getY());
                canvas.lineTo(renderers[renderers.length - 1].getOccupiedArea().getBBox().getRight(),
                        renderers[renderers.length - 1].getOccupiedArea().getBBox().getBottom());

                // first vertical line
                Rectangle cellRect = renderers[0].getOccupiedArea().getBBox();
                canvas.moveTo(cellRect.getLeft(), cellRect.getBottom());
                canvas.lineTo(cellRect.getLeft(), cellRect.getTop());

                // vertical lines
                for (CellRenderer renderer : renderers) {
                    cellRect = renderer.getOccupiedArea().getBBox();
                    canvas.moveTo(cellRect.getRight(), cellRect.getBottom());
                    canvas.lineTo(cellRect.getRight(), cellRect.getTop());
                }
            }

            canvas.stroke();
        }
    }

    private static class DottedLineCellRenderer extends CellRenderer {
        public DottedLineCellRenderer(Cell modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new DottedLineCellRenderer((Cell) modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            drawContext.getCanvas().setLineDash(1f, 3f);
            drawContext.getCanvas().rectangle(this.getOccupiedArea().getBBox());
            drawContext.getCanvas().stroke();
        }
    }

    private Cell createCellWithBorders(String content) {
        Cell cell = new Cell().add(new Paragraph(content));
        cell.setBorder(new DottedBorder(1));

        return cell;
    }
}
