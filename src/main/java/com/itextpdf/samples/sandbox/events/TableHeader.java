package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class TableHeader {
    public static final String DEST = "./target/sandbox/events/table_header.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TableHeader().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        TableHeaderEventHandler handler = new TableHeaderEventHandler(doc);
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

        // Calculate top margin to be sure that the table will fit the margin.
        float topMargin = 20 + handler.getTableHeight();
        doc.setMargins(topMargin, 36, 36, 36);

        for (int i = 0; i < 50; i++) {
            doc.add(new Paragraph("Hello World!"));
        }

        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hello World!"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hello World!"));

        doc.close();
    }


    private static class TableHeaderEventHandler implements IEventHandler {
        private Table table;
        private float tableHeight;
        private Document doc;

        public TableHeaderEventHandler(Document doc) {
            this.doc = doc;
            initTable();

            TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
            renderer.setParent(new DocumentRenderer(doc));

            // Simulate the positioning of the renderer to find out how much space the header table will occupy.
            LayoutResult result = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
            tableHeight = result.getOccupiedArea().getBBox().getHeight();
        }

        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
            PageSize pageSize = pdfDoc.getDefaultPageSize();
            float coordX = pageSize.getX() + doc.getLeftMargin();
            float coordY = pageSize.getTop() - doc.getTopMargin();
            float width = pageSize.getWidth() - doc.getRightMargin() - doc.getLeftMargin();
            float height = getTableHeight();
            Rectangle rect = new Rectangle(coordX, coordY, width, height);

            new Canvas(canvas, rect)
                    .add(table)
                    .close();
        }

        public float getTableHeight() {
            return tableHeight;
        }

        private void initTable()
        {
            table = new Table(1);
            table.useAllAvailableWidth();
            table.addCell("Header row 1");
            table.addCell("Header row 2");
            table.addCell("Header row 3");
        }
    }
}
