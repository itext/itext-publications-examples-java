/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/34177025
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class TiledBackgroundColor2 extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/tables/tiled_background_color2.pdf";
    public static final String IMG
            = "./src/test/resources/img/bulb.gif";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TiledBackgroundColor2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(2);
        table.addCell("Behold a cell with an image pattern:");
        Cell cell = new Cell();
        ImageData img = ImageDataFactory.create(IMG);
        cell.setNextRenderer(new TiledImageBackgroundRenderer(cell, img));
        cell.setHeight(60);
        table.addCell(cell);
        doc.add(table);
        doc.close();
    }


    class TiledImageBackgroundRenderer extends CellRenderer {
        protected ImageData img;

        public TiledImageBackgroundRenderer(Cell modelElement, ImageData img) {
            super(modelElement);
            this.img = img;
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfCanvas canvas = drawContext.getCanvas();
            Rectangle position = getOccupiedAreaBBox();
            com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(img);
            image.scaleToFit(10000000, position.getHeight());
            float x = position.getLeft();
            float y = position.getBottom();
            while (x + image.getImageScaledWidth() < position.getRight()) {
                image.setFixedPosition(x, y);
                canvas.addImage(img, x, y, image.getImageScaledWidth(), false);
                x += image.getImageScaledWidth();
            }
        }
    }
}
