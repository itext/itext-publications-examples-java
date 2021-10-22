package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class NestedTableRoundedBorder {
    public static final String DEST = "./target/sandbox/tables/nested_table_rounded_border.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new NestedTableRoundedBorder().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // outer table
        Table outertable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        // inner table 1
        Table innertable = new Table(UnitValue.createPercentArray(new float[]{8, 12, 1, 4, 12})).useAllAvailableWidth();

        // first row
        // column 1
        Cell cell = new Cell().add(new Paragraph("Record Ref:"));
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);

        // column 2
        cell = new Cell().add(new Paragraph("GN Staff"));
        cell.setPaddingLeft(2);
        innertable.addCell(cell);

        // spacing
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);

        // column 4
        cell = new Cell().add(new Paragraph("Date: "));
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);

        // column 5
        cell = new Cell().add(new Paragraph("30/4/2015"));
        cell.setPaddingLeft(2);
        innertable.addCell(cell);

        // spacing
        cell = new Cell(1, 5);
        cell.setHeight(3);
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);

        // second row
        // column 1
        cell = new Cell().add(new Paragraph("Hospital:"));
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);

        // column 2
        cell = new Cell().add(new Paragraph("Derby Royal"));
        cell.setPaddingLeft(2);
        innertable.addCell(cell);

        // spacing
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);

        // column 4
        cell = new Cell().add(new Paragraph("Ward: "));
        cell.setBorder(Border.NO_BORDER);
        cell.setPaddingLeft(5);
        innertable.addCell(cell);

        // column 5
        cell = new Cell().add(new Paragraph("21"));
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
        innertable = new Table(UnitValue.createPercentArray(new float[]{3, 17, 1, 16}));
        innertable.setWidth(UnitValue.createPercentValue(100));

        // first row
        // column 1
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);

        // column 2
        cell = new Cell().add(new Paragraph("Name"));
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);

        // column 3
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        innertable.addCell(cell);

        // column 4
        cell = new Cell().add(new Paragraph("Signature: "));
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
            cell = new Cell().add(new Paragraph(String.format("%s:", i)));
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


    private static class RoundedBorderCellRenderer extends CellRenderer {
        public RoundedBorderCellRenderer(Cell modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new RoundedBorderCellRenderer((Cell) modelElement);
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
