/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22122340/creating-table-with-2-rows-in-pdf-footer-using-itext
 */
package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.color.Color;
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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class TableFooter extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/events/table_footer.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableFooter().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc, PageSize.A4);
        doc.setMargins(36, 36, 72, 36);

        Table table = new Table(1);
        table.setWidth(523);

        Cell cell = new Cell().add(new Paragraph("This is a test doc"));
        cell.setBackgroundColor(Color.ORANGE);
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("This is a copyright notice"));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(cell);

        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new TableFooterEventHandler(table));

        for (int i = 0; i < 150; i++) {
            doc.add(new Paragraph("Hello World!"));
        }
        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hello World!"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hello World!"));

        doc.close();
    }


    protected class TableFooterEventHandler implements IEventHandler {
        private Table table;

        public TableFooterEventHandler(Table table) {
            this.table = table;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
            new Canvas(canvas, pdfDoc, new Rectangle(36, 20, page.getPageSize().getWidth() - 72, 50))
                    .add(table);
        }
    }
}
