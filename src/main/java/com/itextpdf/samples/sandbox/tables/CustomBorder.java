package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class CustomBorder {
    public static final String DEST = "./target/sandbox/tables/custom_border.pdf";

    public static final String TEXT = "This is some long paragraph\n" +
            "that will be added over and over\n" +
            "again to prove a point.\n" +
            "It should result in rows\n" +
            "that are split\n" +
            " and rows that aren't.";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CustomBorder().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // By default column width is calculated automatically for the best fit.
        // useAllAvailableWidth() method makes table use the whole page's width while placing the content.
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        // Fill a table with cells.
        // Be aware that by default all the cells will not have ay bottom borders
        for (int i = 1; i <= 60; i++) {
            table.addCell(new Cell().add(new Paragraph("Cell " + i)).setBorderBottom(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph(TEXT)).setBorderBottom(Border.NO_BORDER));
        }

        // We do not need a smart collapse logic: on the contrary, we want to override
        // processing of some cells, that's why we set SEPARATE value here
        table.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);

        // Use a custom renderer in which borders drawing is overridden
        table.setNextRenderer(new CustomBorderTableRenderer(table));

        doc.add(table);

        doc.close();
    }


    private static class CustomBorderTableRenderer extends TableRenderer {
        private boolean bottom = true;
        private boolean top = true;

        public CustomBorderTableRenderer(Table modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new CustomBorderTableRenderer((Table) modelElement);
        }

        @Override
        protected TableRenderer[] split(int row, boolean hasContent, boolean cellWithBigRowspanAdded) {
            TableRenderer[] renderers = super.split(row, hasContent, cellWithBigRowspanAdded);

            // The first row of the split renderer represent the first rows of the current renderer
            ((CustomBorderTableRenderer) renderers[0]).top = top;

            // If there are some split cells, we should draw top borders of the overflow renderer
            // and bottom borders of the split renderer
            if (hasContent) {
                ((CustomBorderTableRenderer) renderers[0]).bottom = false;
                ((CustomBorderTableRenderer) renderers[1]).top = false;
            }
            return renderers;
        }

        @Override
        public void draw(DrawContext drawContext) {

            // If not set, iText will omit drawing of top borders
            if (!top) {
                for (CellRenderer cellRenderer : rows.get(0)) {
                    cellRenderer.setProperty(Property.BORDER_TOP, Border.NO_BORDER);
                }
            }

            // If set, iText will draw bottom borders
            if (bottom) {
                for (CellRenderer cellRenderer : rows.get(rows.size() - 1)) {
                    cellRenderer.setProperty(Property.BORDER_BOTTOM, new SolidBorder(0.5f));
                }
            }

            super.draw(drawContext);
        }
    }

}
