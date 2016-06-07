/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/36253087
 */

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class DiagonalCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/diagonal_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DiagonalCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(6);

        Cell cell = new Cell();
        cell.setNextRenderer(new DiagonalCellRenderer(cell, "Gravity", "Occ"));
        table.addCell(cell.setHeight(30));

        table.addCell(new Cell().add("1").setHeight(30));
        table.addCell(new Cell().add("2").setHeight(30));
        table.addCell(new Cell().add("3").setHeight(30));
        table.addCell(new Cell().add("4").setHeight(30));
        table.addCell(new Cell().add("5").setHeight(30));
        for (int i = 0; i < 5; ) {
            table.addCell(new Cell().add(String.valueOf(++i)).setHeight(30));
            table.addCell(new Cell().setHeight(30));
            table.addCell(new Cell().setHeight(30));
            table.addCell(new Cell().setHeight(30));
            table.addCell(new Cell().setHeight(30));
            table.addCell(new Cell().setHeight(30));
        }

        doc.add(table);
        doc.close();
    }


    protected class DiagonalCellRenderer extends CellRenderer {
        protected String textTopRight;
        protected String textBottomLeft;


        public DiagonalCellRenderer(Cell modelElement, String textTopRight, String textBottomLeft) {
            super(modelElement);
            this.textTopRight = textTopRight;
            this.textBottomLeft = textBottomLeft;
        }

        @Override
        public void drawBorder(DrawContext drawContext) {
            PdfCanvas canvas = drawContext.getCanvas();
            Rectangle rect = getOccupiedAreaBBox();
            canvas
                    .saveState()
                    .moveTo(rect.getLeft(), rect.getTop())
                    .lineTo(rect.getRight(), rect.getBottom())
                    .stroke()
                    .restoreState();
            new Canvas(canvas, drawContext.getDocument(), getOccupiedAreaBBox())
                    .showTextAligned(textTopRight, rect.getRight() - 2, rect.getTop() - 2, TextAlignment.RIGHT, VerticalAlignment.TOP, 0)
                    .showTextAligned(textBottomLeft, rect.getLeft() + 2, rect.getBottom() + 2, TextAlignment.LEFT);
        }
    }
}
