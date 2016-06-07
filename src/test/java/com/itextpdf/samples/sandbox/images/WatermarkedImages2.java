/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/


/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/26814958/pdf-vertical-postion-method-gives-the-next-page-position-instead-of-current-page
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class WatermarkedImages2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/images/watermarked_images2.pdf";
    public static final String IMAGE1 = "./src/test/resources/img/bruno.jpg";
    public static final String IMAGE2 = "./src/test/resources/img/dog.bmp";
    public static final String IMAGE3 = "./src/test/resources/img/fox.bmp";
    public static final String IMAGE4 = "./src/test/resources/img/bruno_ingeborg.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new WatermarkedImages2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(1);
        Cell cell;

        cell = new Cell().add(new Image(ImageDataFactory.create(IMAGE1)).setAutoScaleWidth(true));
        cell.setNextRenderer(new WatermarkedCellRenderer(cell, "Bruno"));
        table.addCell(cell);

        cell = new Cell().add(new Image(ImageDataFactory.create(IMAGE2)).setAutoScaleWidth(true));
        cell.setNextRenderer(new WatermarkedCellRenderer(cell, "Dog"));
        table.addCell(cell);

        cell = new Cell().add(new Image(ImageDataFactory.create(IMAGE3)).setAutoScaleWidth(true));
        cell.setNextRenderer(new WatermarkedCellRenderer(cell, "Fox"));
        table.addCell(cell);

        cell = new Cell().add(new Image(ImageDataFactory.create(IMAGE4)).setAutoScaleWidth(true));
        cell.setNextRenderer(new WatermarkedCellRenderer(cell, "Bruno and Ingeborg"));
        table.addCell(cell);

        doc.add(table);
        doc.close();
    }


    private class WatermarkedCellRenderer extends CellRenderer {
        private String content;

        public WatermarkedCellRenderer(Cell modelElement, String content) {
            super(modelElement);
            this.content = content;
        }

        @Override
        public CellRenderer getNextRenderer() {
            return new WatermarkedCellRenderer((Cell) modelElement, content);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Paragraph p = new Paragraph(content).setFontColor(DeviceRgb.WHITE);
            Rectangle rect = getOccupiedAreaBBox();
            new Canvas(drawContext.getCanvas(), drawContext.getDocument(), getOccupiedAreaBBox())
                    .showTextAligned(p, (rect.getLeft() + rect.getRight()) / 2, (rect.getBottom() + rect.getTop()) / 2,
                            getOccupiedArea().getPageNumber(), TextAlignment.CENTER, VerticalAlignment.MIDDLE, (float) Math.PI / 6);
        }
    }
}
