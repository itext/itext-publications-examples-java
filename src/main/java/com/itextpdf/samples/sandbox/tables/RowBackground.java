package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class RowBackground {
    public static final String DEST = "./target/sandbox/tables/row_background.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RowBackground().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(UnitValue.createPercentArray(7)).useAllAvailableWidth();
        table.setNextRenderer(new RowBackgroundTableRenderer(table, new Table.RowRange(0, 9), 2));

        for (int i = 0; i < 10; i++) {
            for (int j = 1; j < 8; j++) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(j))).setBorder(Border.NO_BORDER));
            }
        }

        doc.add(table);

        doc.close();
    }


    private static class RowBackgroundTableRenderer extends TableRenderer {
        protected int row;

        public RowBackgroundTableRenderer(Table modelElement, Table.RowRange rowRange, int row) {
            super(modelElement, rowRange);

            // the row number of the row that needs a background
            this.row = row;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new RowBackgroundTableRenderer((Table) modelElement, rowRange, row);
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
            canvas.setFillColor(ColorConstants.LIGHT_GRAY);
            canvas.fill();
            canvas.restoreState();

            super.draw(drawContext);
        }
    }
}
