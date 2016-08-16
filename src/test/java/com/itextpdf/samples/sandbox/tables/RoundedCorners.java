/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following questions:
 * http://stackoverflow.com/questions/30106862/left-and-right-top-round-corner-for-rectangelroundrectangle
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class RoundedCorners extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/rounded_corners.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RoundedCorners().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(3);
        Cell cell = getCell("These cells have rounded borders at the top.");
        table.addCell(cell);
        cell = getCell("These cells aren't rounded at the bottom.");
        table.addCell(cell);
        cell = getCell("A custom cell event was used to achieve this.");
        table.addCell(cell);
        doc.add(table);

        doc.close();
    }

    public Cell getCell(String content) {
        Cell cell = new Cell().add(new Paragraph(content));
        cell.setNextRenderer(new RoundedCornersCellRenderer(cell));
        cell.setPadding(5);
        cell.setBorder(null);
        return cell;
    }


    private class RoundedCornersCellRenderer extends CellRenderer {
        public RoundedCornersCellRenderer(Cell modelElement) {
            super(modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            float llx = getOccupiedAreaBBox().getX() + 2;
            float lly = getOccupiedAreaBBox().getY() + 2;
            float urx = getOccupiedAreaBBox().getX() + getOccupiedAreaBBox().getWidth() - 2;
            float ury = getOccupiedAreaBBox().getY() + getOccupiedAreaBBox().getHeight() - 2;
            float r = 4;
            float b = 0.4477f;
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.moveTo(llx, lly);
            canvas.lineTo(urx, lly);
            canvas.lineTo(urx, ury - r);
            canvas.curveTo(urx, ury - r * b, urx - r * b, ury, urx - r, ury);
            canvas.lineTo(llx + r, ury);
            canvas.curveTo(llx + r * b, ury, llx, ury - r * b, llx, ury - r);
            canvas.lineTo(llx, lly);
            canvas.stroke();
            super.draw(drawContext);
        }
    }
}
