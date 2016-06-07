/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27834540/generating-pdf-structure-on-dynamic-inputs
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class CenterVertically extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/center_vertically.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CenterVertically().manipulatePdf(DEST);
    }

    public Rectangle addTable(Document doc, Table table) {
        Rectangle pageDimension = new Rectangle(36, 36, 523, 770);
        Rectangle rect;
        IRenderer tableRenderer = table.createRendererSubTree().setParent(doc.getRenderer());
        LayoutResult tableLayoutResult = tableRenderer.layout(new LayoutContext(new LayoutArea(0, pageDimension)));
        if (LayoutResult.PARTIAL == tableLayoutResult.getStatus()) {
            rect = pageDimension;
        } else {
            rect = new Rectangle(36, ((tableLayoutResult.getOccupiedArea().getBBox().getBottom() + 36) / 2),
                    523, tableLayoutResult.getOccupiedArea().getBBox().getHeight());
        }
        return rect;
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table;
        Cell cell = new Cell();
        for (int i = 1; i <= 5; i++) {
            cell.add("Line " + i);
        }

        table = new Table(1);
        table.addCell(cell);
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.setNextRenderer(new CustomTableRenderer(table, addTable(doc, table)));
        doc.add(table);
        doc.add(new AreaBreak());

        table = new Table(1);
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.setNextRenderer(new CustomTableRenderer(table, addTable(doc, table)));
        doc.add(table);

        doc.close();
    }


    class CustomTableRenderer extends TableRenderer {
        protected Rectangle rect;

        public CustomTableRenderer(Table modelElement, Rectangle rect) {
            super(modelElement);
            this.rect = new Rectangle(rect);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            return super.layout(new LayoutContext(new LayoutArea(layoutContext.getArea().getPageNumber(), rect)));
        }
    }
}
