/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ClippedCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/clipped_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ClippedCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(1);
        // a long phrase with newlines
        Paragraph p = new Paragraph("Dr. iText or:\nHow I Learned to Stop Worrying\nand Love PDF.");
        Cell cell = new Cell().add(p);

        // the phrase doesn't fits the height
        cell.setHeight(50f);
        cell.setNextRenderer(new ClipContentRenderer(cell));
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }


    private class ClipContentRenderer extends CellRenderer {
        public ClipContentRenderer(Cell modelElement) {
            super(modelElement);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            Rectangle area = layoutContext.getArea().getBBox();
            LayoutContext context = new LayoutContext(new LayoutArea(layoutContext.getArea().getPageNumber(),
                    new Rectangle(area.getLeft(), area.getTop() - retrieveHeight(), area.getWidth(), retrieveHeight())));
            LayoutResult result = super.layout(context);
            if (LayoutResult.FULL != result.getStatus()) {
                return new LayoutResult(LayoutResult.FULL, result.getOccupiedArea(), null, null);
            } else {
                return result;
            }
        }
    }
}
