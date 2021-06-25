package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class CellTitle {
    public static final String DEST = "./target/sandbox/tables/cell_title.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CellTitle().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // By default column width is calculated automatically for the best fit.
        // useAllAvailableWidth() method makes table use the whole page's width while placing the content.
        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        Cell cell = getCell("The title of this cell is title 1", "title 1");
        table.addCell(cell);
        cell = getCell("The title of this cell is title 2", "title 2");
        table.addCell(cell);
        cell = getCell("The title of this cell is title 3", "title 3");
        table.addCell(cell);
        doc.add(table);

        doc.close();
    }


    private static class CellTitleRenderer extends CellRenderer {
        protected String title;

        public CellTitleRenderer(Cell modelElement, String title) {
            super(modelElement);
            this.title = title;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new CellTitleRenderer((Cell) modelElement, title);
        }

        @Override
        public void drawBorder(DrawContext drawContext) {
            PdfPage currentPage = drawContext.getDocument().getPage(getOccupiedArea().getPageNumber());

            // Create an above canvas in order to draw above borders.
            // Notice: bear in mind that iText draws cell borders on its TableRenderer level.
            PdfCanvas aboveCanvas = new PdfCanvas(currentPage.newContentStreamAfter(), currentPage.getResources(),
                    drawContext.getDocument());
            new Canvas(aboveCanvas, getOccupiedAreaBBox())
                    .add(new Paragraph(title)
                            .setMultipliedLeading(1)
                            .setMargin(0)
                            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                            .setFixedPosition(getOccupiedAreaBBox().getLeft() + 5,
                                    getOccupiedAreaBBox().getTop() - 8, 30));
        }
    }

    private static Cell getCell(String content, String title) {
        Cell cell = new Cell().add(new Paragraph(content));
        cell.setNextRenderer(new CellTitleRenderer(cell, title));
        cell.setPaddingTop(8).setPaddingBottom(8);
        return cell;
    }
}
