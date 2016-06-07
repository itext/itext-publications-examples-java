/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/35449606
 */
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
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.ByteArrayOutputStream;
import java.io.File;

@Category(SampleTest.class)
public class TableHeader extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/events/table_header.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableHeader().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4);

        TableHeaderEventHandler handler = new TableHeaderEventHandler(doc);
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
        doc.setMargins(20 + handler.getTableHeight(), 36, 36, 36);
        for (int i = 0; i < 50; i++) {
            doc.add(new Paragraph("Hello World!"));
        }
        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hello World!"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hello World!"));

        doc.close();
    }


    public class TableHeaderEventHandler implements IEventHandler {
        protected Table table;
        protected float tableHeight;
        protected Document doc;

        public TableHeaderEventHandler(Document doc) {
            this.doc = doc;
            table = new Table(1);
            table.setWidth(523);
            table.addCell("Header row 1");
            table.addCell("Header row 2");
            table.addCell("Header row 3");
            TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
            renderer.setParent(new Document(new PdfDocument(new PdfWriter(new ByteArrayOutputStream()))).getRenderer());
            tableHeight = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4))).getOccupiedArea().getBBox().getHeight();
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
            Rectangle rect = new Rectangle(pdfDoc.getDefaultPageSize().getX() + doc.getLeftMargin(),
                    pdfDoc.getDefaultPageSize().getTop() - doc.getTopMargin(), 100, getTableHeight());
            new Canvas(canvas, pdfDoc, rect)
                    .add(table);
        }

        public float getTableHeight() {
            return tableHeight;
        }
    }
}
