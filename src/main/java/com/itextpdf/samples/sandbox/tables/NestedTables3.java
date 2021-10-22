package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.AbstractRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class NestedTables3 {
    public static final String DEST = "./target/sandbox/tables/nested_tables3.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new NestedTables3().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        // Creates outer table
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        // Draws header for every nested table.
        // That header will be repeated on every page.
        table.setNextRenderer(new InnerTableRenderer(table, new Table.RowRange(0, 0)));

        Cell cell = new Cell(1, 2).add(new Paragraph("This outer header is repeated on every page"));
        table.addHeaderCell(cell);

        // Creates the first inner table
        Table inner1 = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        // Creates an empty header cell for the header content drawn by outer table renderer
        cell = new Cell();
        cell.setHeight(20);
        inner1.addHeaderCell(cell);

        // Creates header cell that will be repeated only on pages, where that table has content
        cell = new Cell().add(new Paragraph("This inner header won't be repeated on every page"));
        inner1.addHeaderCell(cell);

        for (int i = 0; i < 10; i++) {
            inner1.addCell(new Cell().add(new Paragraph("test")));
        }

        cell = new Cell().add(inner1);
        table.addCell(cell.setPadding(0));

        // Creates the second inner table
        Table inner2 = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        // Creates an empty header cell for the header content drawn by outer table renderer
        cell = new Cell();
        cell.setHeight(20);
        inner2.addHeaderCell(cell);

        // Creates header cell that will be repeated only on pages, where that table has content
        cell = new Cell().add(new Paragraph("This inner header may be repeated on every page"));
        inner2.addHeaderCell(cell);

        for (int i = 0; i < 35; i++) {
            inner2.addCell("test");
        }

        cell = new Cell().add(inner2);
        table.addCell(cell.setPadding(0));

        doc.add(table);

        doc.close();
    }

    private static class InnerTableRenderer extends TableRenderer {
        public InnerTableRenderer(Table modelElement, Table.RowRange rowRange) {
            super(modelElement, rowRange);
        }

        protected InnerTableRenderer(Table modelElement) {
            super(modelElement);
        }

        @Override
        public void drawChildren(DrawContext drawContext) {
            super.drawChildren(drawContext);

            for (IRenderer renderer : childRenderers) {
                PdfCanvas canvas = drawContext.getCanvas();
                canvas.beginText();
                Rectangle box = ((AbstractRenderer) renderer).getInnerAreaBBox();
                UnitValue fontSize = this.getPropertyAsUnitValue(Property.FONT_SIZE);
                canvas.moveText(box.getLeft(), box.getTop() - (fontSize.isPointValue() ? fontSize.getValue() : 12f));
                canvas.setFontAndSize(this.getPropertyAsFont(Property.FONT),
                        fontSize.isPointValue() ? fontSize.getValue() : 12f);
                canvas.showText("This inner table header will always be repeated");
                canvas.endText();
                canvas.stroke();
            }
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new InnerTableRenderer((Table) modelElement);
        }
    }
}
