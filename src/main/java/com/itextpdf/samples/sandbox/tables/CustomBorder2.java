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
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class CustomBorder2 {
    public static final String DEST = "./target/sandbox/tables/custom_border2.pdf";

    public static final String TEXT = "This is some long paragraph that will be added over and over " +
            "again to prove a point. It should result in rows that are split and rows that aren't.";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CustomBorder2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // By default column width is calculated automatically for the best fit.
        // useAllAvailableWidth() method makes table use the whole page's width while placing the content.
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        for (int i = 1; i < 60; i++) {
            table.addCell(new Cell().add(new Paragraph("Cell " + i)));
            table.addCell(new Cell().add(new Paragraph(TEXT)));
        }

        // Use a custom renderer in which borders drawing is overridden
        table.setNextRenderer(new CustomBorder2TableRenderer(table));

        doc.add(table);

        doc.close();
    }


    private static class CustomBorder2TableRenderer extends TableRenderer {
        private boolean top = true;
        private boolean bottom = true;

        public CustomBorder2TableRenderer(Table modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new CustomBorder2TableRenderer((Table) modelElement);
        }

        @Override
        protected TableRenderer[] split(int row, boolean hasContent, boolean cellWithBigRowspanAdded) {
            // Being inside this method implies that split has occurred

            TableRenderer[] results = super.split(row, hasContent, cellWithBigRowspanAdded);

            CustomBorder2TableRenderer splitRenderer = (CustomBorder2TableRenderer) results[0];

            // iText shouldn't draw the bottom split renderer's border,
            // because there is an overflow renderer
            splitRenderer.bottom = false;

            // If top is true, the split renderer is the first renderer of the document.
            // If false, iText has already processed the first renderer
            splitRenderer.top = this.top;

            CustomBorder2TableRenderer overflowRenderer = (CustomBorder2TableRenderer) results[1];

            // iText shouldn't draw the top overflow renderer's border
            overflowRenderer.top = false;

            return results;
        }

        @Override
        protected void drawBorders(DrawContext drawContext) {
            Rectangle area = occupiedArea.getBBox();
            PdfCanvas canvas = drawContext.getCanvas();

            canvas
                    .saveState()
                    .moveTo(area.getLeft(), area.getBottom())
                    .lineTo(area.getLeft(), area.getTop())
                    .moveTo(area.getRight(), area.getTop())
                    .lineTo(area.getRight(), area.getBottom());

            if (top) {
                canvas
                        .moveTo(area.getLeft(), area.getTop())
                        .lineTo(area.getRight(), area.getTop());
            }

            if (bottom) {
                canvas
                        .moveTo(area.getLeft(), area.getBottom())
                        .lineTo(area.getRight(), area.getBottom());
            }

            canvas
                    .stroke()
                    .restoreState();
        }
    }
}
