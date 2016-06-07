/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/34555756/one-cell-with-different-border-types
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class DottedLineCell2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/dotted_line_cell2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DottedLineCell2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc);
        Table table;
        Cell cell;

        table = new Table(4);
        table.setMarginBottom(30);
        cell = new Cell().add("left border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell,
                new boolean[]{false, true, false, false}));
        table.addCell(cell);
        cell = new Cell().add("right border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell,
                new boolean[]{false, false, false, true}));
        table.addCell(cell);
        cell = new Cell().add("top border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell,
                new boolean[]{true, false, false, false}));
        table.addCell(cell);
        cell = new Cell().add("bottom border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell,
                new boolean[]{false, false, true, false}));
        table.addCell(cell);
        document.add(table);

        table = new Table(4);
        table.setMarginBottom(30);
        cell = new Cell().add("left and top border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell,
                new boolean[]{true, true, false, false}));
        table.addCell(cell);
        cell = new Cell().add("right and bottom border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell,
                new boolean[]{false, false, true, true}));
        table.addCell(cell);
        cell = new Cell().add("no border");
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
        cell = new Cell().add("full border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new DottedLineCellRenderer(cell,
                new boolean[]{true, true, true, true}));
        table.addCell(cell);
        document.add(table);
        document.close();
    }
}


class DottedLineCellRenderer extends CellRenderer {
    boolean[] borders;

    public DottedLineCellRenderer(Cell modelElement, boolean[] borders) {
        super(modelElement);
        this.borders = new boolean[borders.length];
        for (int i = 0; i < this.borders.length; i++) {
            this.borders[i] = borders[i];
        }
    }

    @Override
    public void draw(DrawContext drawContext) {
        super.draw(drawContext);
        PdfCanvas canvas = drawContext.getCanvas();
        Rectangle position = getOccupiedAreaBBox();
        canvas.saveState();
        canvas.setLineDash(0, 4, 2);
        if (borders[0]) {
            canvas.moveTo(position.getRight(), position.getTop());
            canvas.lineTo(position.getLeft(), position.getTop());
        }
        if (borders[2]) {
            canvas.moveTo(position.getRight(), position.getBottom());
            canvas.lineTo(position.getLeft(), position.getBottom());
        }
        if (borders[3]) {
            canvas.moveTo(position.getRight(), position.getTop());
            canvas.lineTo(position.getRight(), position.getBottom());
        }
        if (borders[1]) {
            canvas.moveTo(position.getLeft(), position.getTop());
            canvas.lineTo(position.getLeft(), position.getBottom());
        }
        canvas.stroke();
        canvas.restoreState();
    }
}
