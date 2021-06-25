package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class DiagonalCell {
    public static final String DEST = "./target/sandbox/tables/diagonal_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DiagonalCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(UnitValue.createPercentArray(6)).useAllAvailableWidth();
        Cell cell = new Cell();

        // Draws cell content with top right text 'Gravity' and bottom left text 'Occ'
        cell.setNextRenderer(new DiagonalCellRenderer(cell, "Gravity", "Occ"));

        table.addCell(cell.setMinHeight(30));
        table.addCell(new Cell().add(new Paragraph("1")).setMinHeight(30));
        table.addCell(new Cell().add(new Paragraph("2")).setMinHeight(30));
        table.addCell(new Cell().add(new Paragraph("3")).setMinHeight(30));
        table.addCell(new Cell().add(new Paragraph("4")).setMinHeight(30));
        table.addCell(new Cell().add(new Paragraph("5")).setMinHeight(30));

        for (int i = 0; i < 5; ) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(++i))).setMinHeight(30));
            table.addCell(new Cell().setMinHeight(30));
            table.addCell(new Cell().setMinHeight(30));
            table.addCell(new Cell().setMinHeight(30));
            table.addCell(new Cell().setMinHeight(30));
            table.addCell(new Cell().setMinHeight(30));
        }

        doc.add(table);

        doc.close();
    }


    private static class DiagonalCellRenderer extends CellRenderer {
        private String textTopRight;
        private String textBottomLeft;


        public DiagonalCellRenderer(Cell modelElement, String textTopRight, String textBottomLeft) {
            super(modelElement);
            this.textTopRight = textTopRight;
            this.textBottomLeft = textBottomLeft;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new DiagonalCellRenderer((Cell) modelElement, textTopRight, textBottomLeft);
        }

        @Override
        public void drawBorder(DrawContext drawContext) {
            PdfCanvas canvas = drawContext.getCanvas();
            Rectangle rect = getOccupiedAreaBBox();

            canvas
                    .saveState()
                    .moveTo(rect.getLeft(), rect.getTop())
                    .lineTo(rect.getRight(), rect.getBottom())
                    .stroke()
                    .restoreState();

            new Canvas(canvas, getOccupiedAreaBBox())
                    .showTextAligned(textTopRight, rect.getRight() - 2, rect.getTop() - 2,
                            TextAlignment.RIGHT, VerticalAlignment.TOP, 0)
                    .showTextAligned(textBottomLeft, rect.getLeft() + 2, rect.getBottom() + 2, TextAlignment.LEFT);
        }
    }
}
