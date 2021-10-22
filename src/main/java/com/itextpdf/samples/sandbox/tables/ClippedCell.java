package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class ClippedCell {
    public static final String DEST = "./target/sandbox/tables/clipped_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ClippedCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        // a long phrase with newlines
        Paragraph p = new Paragraph("Dr. iText or:\nHow I Learned to Stop Worrying\nand Love PDF.");
        Cell cell = new Cell().add(p);

        // the phrase doesn't fits the height
        cell.setHeight(50f);
        cell.setNextRenderer(new ClipContentRenderer(cell));

        table.addCell(cell);

        doc.add(table);

        doc.close();
    }


    private static class ClipContentRenderer extends CellRenderer {
        public ClipContentRenderer(Cell modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new ClipContentRenderer((Cell) modelElement);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            Rectangle area = layoutContext.getArea().getBBox();

            LayoutContext context = new LayoutContext(new LayoutArea(layoutContext.getArea().getPageNumber(),
                    new Rectangle(area.getLeft(), area.getTop() - retrieveHeight(), area.getWidth(), retrieveHeight())));

            LayoutResult result = super.layout(context);
            // If content doesn't fit the size of a cell,
            // iTest will still return layout result full as if everything is OK.
            // As a result, the cell's content will be clipped.
            if (LayoutResult.FULL != result.getStatus()) {
                return new LayoutResult(LayoutResult.FULL, result.getOccupiedArea(), null, null);
            }

            return result;
        }
    }
}
