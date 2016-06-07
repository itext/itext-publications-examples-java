/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/35746651
 */

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class CellTitle extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/cell_title.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CellTitle().manipulatePdf(DEST);
    }

    public Cell getCell(String content, String title) {
        Cell cell = new Cell().add(content);
        cell.setNextRenderer(new CellTitleRenderer(cell, title));
        cell.setPaddingTop(8).setPaddingBottom(8);
        return cell;
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(1);
        Cell cell = getCell("The title of this cell is title 1", "title 1");
        table.addCell(cell);
        cell = getCell("The title of this cell is title 2", "title 2");
        table.addCell(cell);
        cell = getCell("The title of this cell is title 3", "title 3");
        table.addCell(cell);
        doc.add(table);
        doc.close();
    }


    protected class CellTitleRenderer extends CellRenderer {
        protected String title;

        public CellTitleRenderer(Cell modelElement, String title) {
            super(modelElement);
            this.title = title;
        }

        @Override
        public void drawBorder(DrawContext drawContext) {
            // create above canvas in order to draw above borders (notice that we draw borders using TableRenderer)
            PdfCanvas aboveCanvas = new PdfCanvas(drawContext.getDocument().getLastPage().newContentStreamAfter(),
                    drawContext.getDocument().getLastPage().getResources(), drawContext.getDocument());
            new Canvas(aboveCanvas, drawContext.getDocument(), getOccupiedAreaBBox())
                    .add(new Paragraph(title)
                            .setBackgroundColor(Color.LIGHT_GRAY)
                            .setFixedPosition(getOccupiedAreaBBox().getLeft() + 5, getOccupiedAreaBBox().getTop() - 8, 30));
        }
    }
}
