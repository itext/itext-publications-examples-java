/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written in answer to the SO question:
 * http://stackoverflow.com/questions/39154089
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.color.Color;
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
public class RowBackground extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/row_background.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RowBackground().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(7);
        table.setWidthPercent(100);
        table.setNextRenderer(new RowBackgroundTableRenderer(table, new Table.RowRange(0, 9), 2));
        for (int i = 0; i < 10; i++) {
            for (int j = 1; j < 8; j++) {
                table.addCell(new Cell().add(String.valueOf(j)).setBorder(Border.NO_BORDER));
            }
        }
        doc.add(table);

        doc.close();
    }


    private class RowBackgroundTableRenderer extends TableRenderer {
        // the row number of the row that needs a background
        protected int row;

        public RowBackgroundTableRenderer(Table modelElement, Table.RowRange rowRange, int row) {
            super(modelElement, rowRange);
            this.row = row;
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfCanvas canvas;
            float llx = rows.get(row)[0].getOccupiedAreaBBox().getLeft();
            float lly = rows.get(row)[0].getOccupiedAreaBBox().getBottom();
            float urx = rows.get(row)[rows.get(row).length - 1].getOccupiedAreaBBox().getRight();
            float ury = rows.get(row)[0].getOccupiedAreaBBox().getTop();
            float h = ury - lly;
            canvas = drawContext.getCanvas();
            canvas.saveState();
            canvas.arc(llx - h / 2, lly, llx + h / 2, ury, 90, 180);
            canvas.lineTo(urx, lly);
            canvas.arc(urx - h / 2, lly, urx + h / 2, ury, 270, 180);
            canvas.lineTo(llx, ury);
            canvas.setFillColor(Color.LIGHT_GRAY);
            canvas.fill();
            canvas.restoreState();
            super.draw(drawContext);
        }
    }
}
