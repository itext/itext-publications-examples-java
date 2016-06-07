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
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
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
public class CustomBorder3 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/custom_border_3.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CustomBorder3().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc);

        ILineDash solid = new Solid();
        ILineDash dotted = new Dotted();
        ILineDash dashed = new Dashed();

        Table table;
        Cell cell;

        table = new Table(4);
        table.setMarginBottom(30);
        cell = new Cell().add("dotted left border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new CustomBorder3Renderer(cell,
                new ILineDash[]{null, dotted, null, null}));
        table.addCell(cell);
        cell = new Cell().add("solid right border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new CustomBorder3Renderer(cell,
                new ILineDash[]{null, null, null, solid}));
        table.addCell(cell);
        cell = new Cell().add("dashed top border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new CustomBorder3Renderer(cell,
                new ILineDash[]{dashed, null, null, null}));
        table.addCell(cell);
        cell = new Cell().add("bottom border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new CustomBorder3Renderer(cell,
                new ILineDash[]{null, null, solid, null}));
        table.addCell(cell);
        document.add(table);

        table = new Table(4);
        table.setMarginBottom(30);
        cell = new Cell().add("dotted left and solid top border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new CustomBorder3Renderer(cell,
                new ILineDash[]{solid, dotted, null, null}));
        table.addCell(cell);
        cell = new Cell().add("dashed right and dashed bottom border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new CustomBorder3Renderer(cell,
                new ILineDash[]{null, null, dashed, dashed}));
        table.addCell(cell);
        cell = new Cell().add("no border");
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
        cell = new Cell().add("full solid border");
        cell.setBorder(Border.NO_BORDER);
        cell.setNextRenderer(new CustomBorder3Renderer(cell,
                new ILineDash[]{solid, solid, solid, solid}));
        table.addCell(cell);
        document.add(table);
        document.close();
    }


    interface ILineDash {
        void applyLineDash(PdfCanvas canvas);
    }


    class Solid implements ILineDash {
        public void applyLineDash(PdfCanvas canvas) {
        }
    }


    class Dotted implements ILineDash {
        public void applyLineDash(PdfCanvas canvas) {
            canvas.setLineCapStyle(PdfCanvasConstants.LineCapStyle.ROUND);
            canvas.setLineDash(0, 4, 2);
        }
    }


    class Dashed implements ILineDash {
        public void applyLineDash(PdfCanvas canvas) {
            canvas.setLineDash(3, 3);
        }
    }


    class CustomBorder3Renderer extends CellRenderer {
        ILineDash[] borders;

        public CustomBorder3Renderer(Cell modelElement, ILineDash[] borders) {
            super(modelElement);
            this.borders = new ILineDash[borders.length];
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
            if (null != borders[0]) {
                canvas.saveState();
                borders[0].applyLineDash(canvas);
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getTop());
                canvas.stroke();
                canvas.restoreState();
            }
            if (null != borders[2]) {
                canvas.saveState();
                borders[2].applyLineDash(canvas);
                canvas.moveTo(position.getRight(), position.getBottom());
                canvas.lineTo(position.getLeft(), position.getBottom());
                canvas.stroke();
                canvas.restoreState();
            }
            if (null != borders[3]) {
                canvas.saveState();
                borders[3].applyLineDash(canvas);
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getRight(), position.getBottom());
                canvas.stroke();
                canvas.restoreState();
            }
            if (null != borders[1]) {
                canvas.saveState();
                borders[1].applyLineDash(canvas);
                canvas.moveTo(position.getLeft(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getBottom());
                canvas.stroke();
                canvas.restoreState();
            }
            canvas.stroke();
            canvas.restoreState();
        }
    }
}


