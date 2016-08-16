/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23935566/table-borders-not-expanding-properly-in-pdf-using-itext
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class CustomBorder extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/custom_border.pdf";
    public static final String TEXT = "This is some long paragraph that will be added over and over " +
            "again to prove a point. It should result in rows that are split and rows that aren't.";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CustomBorder().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(2);
        table.setWidth(500);
        table.setBorder(new SolidBorder(1));
        Cell cell = new Cell().add(new Paragraph(TEXT));
        cell.setBorder(null);
        for (int i = 0; i < 60; ) {
            table.addCell(new Cell().add(new Paragraph("Cell " + (++i))).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph(TEXT)).setBorder(Border.NO_BORDER));
        }
        table.setNextRenderer(new CustomBorderTableRenderer(table, new Table.RowRange(0, 59)));
        doc.add(table);

        doc.close();
    }
}


class CustomBorderTableRenderer extends TableRenderer {
    protected static boolean isNextPageTopToBeDrawn = false;
    protected static boolean isTopToBeDrawn = true;

    public CustomBorderTableRenderer(Table modelElement, Table.RowRange rowRange) {
        super(modelElement, rowRange);
    }

    protected CustomBorderTableRenderer(Table modelElement) {
        super(modelElement);
    }

    @Override
    public void drawBorder(DrawContext drawContext) {
        CellRenderer[] firstRowRenderers = rows.get(0);
        // yLines
        PdfCanvas canvas = drawContext.getCanvas();
        for (CellRenderer cellRenderer : firstRowRenderers) {
            canvas.moveTo(cellRenderer.getBorderAreaBBox().getRight(),
                    getBorderAreaBBox().getBottom());
            canvas.lineTo(cellRenderer.getBorderAreaBBox().getRight(),
                    getBorderAreaBBox().getTop());
        }
        // the first yLine
        canvas.moveTo(firstRowRenderers[0].getBorderAreaBBox().getLeft(),
                getBorderAreaBBox().getBottom());
        canvas.lineTo(firstRowRenderers[0].getBorderAreaBBox().getLeft(),
                getBorderAreaBBox().getTop());
        canvas.stroke();

        for (CellRenderer[] cellRenderers : rows) {
            canvas.moveTo(cellRenderers[0].getBorderAreaBBox().getLeft(),
                    cellRenderers[0].getBorderAreaBBox().getBottom());
            canvas.lineTo(cellRenderers[cellRenderers.length - 1].getBorderAreaBBox().getRight(),
                    cellRenderers[cellRenderers.length - 1].getBorderAreaBBox().getBottom());
        }
        if (isTopToBeDrawn) {
            canvas.moveTo(firstRowRenderers[0].getBorderAreaBBox().getLeft(),
                    firstRowRenderers[0].getBorderAreaBBox().getTop());
            canvas.lineTo(firstRowRenderers[firstRowRenderers.length - 1].getBorderAreaBBox().getRight(),
                    firstRowRenderers[0].getBorderAreaBBox().getTop());
            isTopToBeDrawn = false;
        }
        if (isNextPageTopToBeDrawn) {
            isTopToBeDrawn = true;
            isNextPageTopToBeDrawn = false;
        }
        canvas.stroke();
    }

    @Override
    public CustomBorderTableRenderer getNextRenderer() {
        return new CustomBorderTableRenderer((Table) modelElement);
    }

    @Override
    protected TableRenderer[] split(int row) {
        if (null != rows.get(row)) {
            isNextPageTopToBeDrawn = true;
            CellRenderer[] curRow = rows.get(row);
            for (int i = 0; i < curRow.length; i++) {
                if (0 != rows.get(row)[0].getChildRenderers().size()) {
                    isNextPageTopToBeDrawn = false;
                    break;
                }
            }
        }
        CustomBorderTableRenderer splitRenderer = (CustomBorderTableRenderer) createSplitRenderer(
                new Table.RowRange(rowRange.getStartRow(), rowRange.getStartRow() + row));
        splitRenderer.rows = rows.subList(0, row);
        CustomBorderTableRenderer overflowRenderer = (CustomBorderTableRenderer) createOverflowRenderer(
                new Table.RowRange(rowRange.getStartRow() + row, rowRange.getFinishRow()));
        overflowRenderer.rows = rows.subList(row, rows.size());
        splitRenderer.occupiedArea = occupiedArea;
        return new TableRenderer[]{splitRenderer, overflowRenderer};
    }
}
