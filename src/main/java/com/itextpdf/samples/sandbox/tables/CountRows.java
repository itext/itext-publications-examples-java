package com.itextpdf.samples.sandbox.tables;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;

public class CountRows {
    public static final String DEST = "./target/sandbox/tables/row_count.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CountRows().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc);

        FooterEventHandler footer = new FooterEventHandler(document);
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, footer);

        Table table = new Table(new float[] {50, 100}).setTextAlignment(TextAlignment.CENTER);
        for (int i = 0; i < 40; i++) {
            Cell cell = new Cell();
            cell.setNextRenderer(new RowNumberCellRenderer(cell, footer));
            table.addCell(cell);
            table.addCell("Some text");
        }

        document.add(table);

        document.close();
    }

    private static class FooterEventHandler implements IEventHandler {
        private Map<Integer, Integer> pageRowsCounts = new HashMap<>();
        private Document document;

        public FooterEventHandler(Document document) {
            this.document = document;
        }

        public int addRow(int currentPageNumber) {
            int rows;
            if (!pageRowsCounts.containsKey(currentPageNumber)) {
                rows = 0;
            } else {
                rows = pageRowsCounts.get(currentPageNumber);
            }

            rows++;
            pageRowsCounts.put(currentPageNumber, rows);
            return rows;
        }

        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdfDoc.getPageNumber(page);

            Paragraph paragraph = new Paragraph(String.format("There are %s rows on this page",
                    pageRowsCounts.get(pageNumber)));
            Canvas canvas = new Canvas(page, page.getPageSize());
            canvas
                    .showTextAligned(paragraph, document.getRightMargin(),
                            document.getBottomMargin() / 2, TextAlignment.LEFT)
                    .close();
        }
    }

    private static class RowNumberCellRenderer extends CellRenderer {
        private FooterEventHandler footer;

        public RowNumberCellRenderer(Cell modelElement, FooterEventHandler footer) {
            super(modelElement);
            this.footer = footer;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public CellRenderer getNextRenderer() {
            return new RowNumberCellRenderer((Cell) modelElement, footer);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            int pageNumber = getOccupiedArea().getPageNumber();
            Rectangle rect = getOccupiedAreaBBox();

            Paragraph p = new Paragraph(String.valueOf(footer.addRow(pageNumber)));
            float coordX = (rect.getLeft() + rect.getRight()) / 2;
            float coordY = (rect.getBottom() + rect.getTop()) / 2;
            Canvas canvas = new Canvas(drawContext.getCanvas(), rect);
            canvas
                    .showTextAligned(p, coordX, coordY, TextAlignment.CENTER, VerticalAlignment.MIDDLE)
                    .close();
        }
    }
}
