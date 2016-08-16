/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23374557/itextsharp-why-cell-background-image-is-rotated-90-degress-clockwise
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.PatternColor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfPatternCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class TiledBackground extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/tiled_background.pdf";
    public static final String IMG1 = "./src/test/resources/img/ALxRF.png";
    public static final String IMG2 = "./src/test/resources/img/bulb.gif";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TiledBackground().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(2);
        Cell cell = new Cell();
        ImageData image = ImageDataFactory.create(IMG1);
        cell.setNextRenderer(new TiledImageBackgroundCellRenderer(cell, image));
        cell.setHeight(770);
        table.addCell(cell);
        cell = new Cell();
        image = ImageDataFactory.create(IMG2);
        cell.setNextRenderer(new TiledImageBackgroundCellRenderer(cell, image));
        cell.setHeight(770);
        table.addCell(cell);
        doc.add(table);

        doc.close();
    }


    private class TiledImageBackgroundCellRenderer extends CellRenderer {
        protected ImageData img;

        public TiledImageBackgroundCellRenderer(Cell modelElement, ImageData img) {
            super(modelElement);
            this.img = img;
        }

        public void colorRectangle(PdfCanvas canvas, Color color, float x, float y, float width, float height) {
            canvas.saveState().setFillColor(color).rectangle(x, y, width, height).fillStroke().restoreState();
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfPattern.Tiling img_pattern = new PdfPattern.Tiling(img.getWidth(), img.getHeight(), img.getWidth(),
                    img.getHeight());
            new PdfPatternCanvas(img_pattern, drawContext.getDocument()).addImage(img, 0, 0, false);
            PdfCanvas canvas = drawContext.getCanvas();
            colorRectangle(canvas, new PatternColor(img_pattern), getOccupiedAreaBBox().getX(),
                    getOccupiedAreaBBox().getY(), getOccupiedAreaBBox().getWidth(), getOccupiedAreaBBox().getHeight());
            canvas.setFillColor(new PatternColor(img_pattern));
            canvas.rectangle(getOccupiedAreaBBox());
            canvas.stroke();
        }
    }
}
