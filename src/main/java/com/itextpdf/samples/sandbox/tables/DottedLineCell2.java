package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class DottedLineCell2 {
    public static final String DEST = "./target/sandbox/tables/dotted_line_cell2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DottedLineCell2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc);

        Paragraph paragraph = new Paragraph("Setter approach");
        document.add(paragraph.setFontSize(25));

        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        table.setMarginBottom(30);

        table.addCell(createCell("left border", new Style().setBorderLeft(new DottedBorder(1))));
        table.addCell(createCell("right border", new Style().setBorderRight(new DottedBorder(1))));
        table.addCell(createCell("top border", new Style().setBorderTop(new DottedBorder(1))));
        table.addCell(createCell("bottom border", new Style().setBorderBottom(new DottedBorder(1))));

        document.add(table);

        table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        table.setMarginBottom(30);

        table.addCell(createCell("left and top border", new Style()
                .setBorderLeft(new DottedBorder(1))
                .setBorderTop(new DottedBorder(1))));
        table.addCell(createCell("right and bottom border", new Style()
                .setBorderRight(new DottedBorder(1))
                .setBorderBottom(new DottedBorder(1))));
        table.addCell(createCell("no border", new Style()));
        table.addCell(createCell("full border", new Style()
                .setBorderBottom(new DottedBorder(1))
                .setBorderTop(new DottedBorder(1))
                .setBorderRight(new DottedBorder(1))
                .setBorderLeft(new DottedBorder(1))));

        document.add(table);

        paragraph = new Paragraph("Custom render approach");
        document.add(paragraph.setFontSize(25));

        table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        table.setMarginBottom(30);

        Cell cell = new Cell().add(new Paragraph("left border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {false, true, false, false}));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("right border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {false, false, false, true}));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("top border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {true, false, false, false}));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("bottom border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {false, false, true, false}));
        table.addCell(cell);

        document.add(table);

        table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        table.setMarginBottom(30);

        cell = new Cell().add(new Paragraph("left and top border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {true, true, false, false}));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("right and bottom border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {false, false, true, true}));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("no border"));
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("full border"));
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell, new boolean[] {true, true, true, true}));
        table.addCell(cell);

        document.add(table);

        document.close();
    }

    private static class DottedLineCellRenderer extends CellRenderer {
        boolean[] borders;

        public DottedLineCellRenderer(Cell modelElement, boolean[] borders) {
            super(modelElement);
            this.borders = new boolean[borders.length];

            for (int i = 0; i < this.borders.length; i++) {
                this.borders[i] = borders[i];
            }
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new DottedLineCellRenderer((Cell) modelElement, borders);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            PdfCanvas canvas = drawContext.getCanvas();
            Rectangle position = getOccupiedAreaBBox();
            canvas.saveState();
            canvas.setLineDash(1f,3f);

            if (borders[0]) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getTop());
            }

            if (borders[2]) {
                canvas.moveTo(position.getRight(), position.getBottom());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }

            if (borders[3]) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getRight(), position.getBottom());
            }

            if (borders[1]) {
                canvas.moveTo(position.getLeft(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }

            canvas.stroke();
            canvas.restoreState();
        }
    }

    private static Cell createCell(String content, Style style) {
        Cell cell = new Cell()
                .add(new Paragraph(content))

                // By default there is a BORDER property set as SolidBorder. We want to override it
                // and that's why this property is set to null.
                // However, if there is a BORDER property in the passed Style instance,
                // it will be used because it's added afterwards.
                .addStyle(new Style().setBorder(Border.NO_BORDER));

        cell.addStyle(style);

        return cell;
    }
}
