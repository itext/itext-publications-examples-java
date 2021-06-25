package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class ClipCenterCellContent {
    public static final String DEST = "./target/sandbox/tables/clip_center_cell_content.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ClipCenterCellContent().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();

        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 5; c++) {
                Cell cell = new Cell();

                if (r == 'D' && c == 2) {

                    // Draw a content that will be clipped in the cell
                    cell.setNextRenderer(new ClipCenterCellContentCellRenderer(cell,
                            new Paragraph("D2 is a cell with more content than we can fit into the cell.")));
                } else {
                    cell.add(new Paragraph(String.valueOf((char) r) + c));
                }
                table.addCell(cell);
            }
        }

        doc.add(table);

        doc.close();
    }


    private static class ClipCenterCellContentCellRenderer extends CellRenderer {
        private Paragraph content;

        public ClipCenterCellContentCellRenderer(Cell modelElement, Paragraph content) {
            super(modelElement);
            this.content = content;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new ClipCenterCellContentCellRenderer((Cell) modelElement, content);
        }

        @Override
        public void draw(DrawContext drawContext) {

            // Fictitiously layout the renderer and find out, how much space does it require
            IRenderer pr = content.createRendererSubTree().setParent(this);

            LayoutResult textArea = pr.layout(new LayoutContext(
                    new LayoutArea(0, new Rectangle(getOccupiedAreaBBox().getWidth(), 1000))));

            float spaceNeeded = textArea.getOccupiedArea().getBBox().getHeight();
            System.out.println(String.format("The content requires %s pt whereas the height is %s pt.",
                    spaceNeeded, getOccupiedAreaBBox().getHeight()));

            float offset = (getOccupiedAreaBBox().getHeight() - textArea.getOccupiedArea().getBBox().getHeight()) / 2;
            System.out.println(String.format("The difference is %s pt; we'll need an offset of %s pt.",
                    -2f * offset, offset));

            PdfFormXObject xObject = new PdfFormXObject(new Rectangle(getOccupiedAreaBBox().getWidth(),
                    getOccupiedAreaBBox().getHeight()));
            Canvas layoutCanvas = new Canvas(new PdfCanvas(xObject, drawContext.getDocument()),
                    new Rectangle(0, offset, getOccupiedAreaBBox().getWidth(), spaceNeeded));
            layoutCanvas.add(content);

            drawContext.getCanvas().addXObjectAt(xObject, occupiedArea.getBBox().getLeft(), occupiedArea.getBBox().getBottom());
        }
    }
}
