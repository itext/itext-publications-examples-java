/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23935566/table-borders-not-expanding-properly-in-pdf-using-itext
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
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class CustomBorder2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/custom_border2.pdf";
    public static final String TEXT = "This is some long paragraph that will be added over and over " +
            "again to prove a point. It should result in rows that are split and rows that aren't.";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CustomBorder2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(2);
        table.setWidth(500);
        table.setNextRenderer(new CustomBorder2TableRenderer(table, new Table.RowRange(0, 60)));
        for (int i = 1; i < 60; i++) {
            table.addCell(new Cell().add("Cell " + i).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(TEXT).setBorder(Border.NO_BORDER));
        }
        doc.add(table);

        doc.close();
    }

    class CustomBorder2TableRenderer extends TableRenderer {
        boolean wasSplitted = false;

        public CustomBorder2TableRenderer(Table modelElement) {
            super(modelElement);
        }

        public CustomBorder2TableRenderer(Table modelElement, Table.RowRange rowRange) {
            super(modelElement, rowRange);
        }

        @Override
        public CustomBorder2TableRenderer getNextRenderer() {
            return new CustomBorder2TableRenderer((Table) modelElement);
        }

        @Override
        protected TableRenderer[] split(int row, boolean hasContent) {
            TableRenderer[] results = super.split(row, hasContent);
            CustomBorder2TableRenderer splitRenderer = (CustomBorder2TableRenderer) results[0];
            splitRenderer.wasSplitted = true;
            return results;
        }

        @Override
        protected void drawBorders(DrawContext drawContext) {
            Rectangle area = occupiedArea.getBBox();
            PdfCanvas canvas = drawContext.getCanvas();

            canvas
                    .saveState()
                    .moveTo(area.getLeft(), area.getBottom())
                    .lineTo(area.getLeft(), area.getTop())
                    .moveTo(area.getRight(), area.getTop())
                    .lineTo(area.getRight(), area.getBottom());
            if (wasSplitted) {
                if (1 == drawContext.getDocument().getNumberOfPages()) {
                    canvas
                            .moveTo(area.getLeft(), area.getTop())
                            .lineTo(area.getRight(), area.getTop());
                }
            } else {
                canvas
                        .moveTo(area.getLeft(), area.getBottom())
                        .lineTo(area.getRight(), area.getBottom());
            }
            canvas
                    .stroke()
                    .restoreState();
        }
    }
}