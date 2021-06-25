package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class TiledBackgroundColor2 {
    public static final String DEST
            = "./target/sandbox/tables/tiled_background_color2.pdf";

    public static final String IMG
            = "./src/main/resources/img/bulb.gif";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TiledBackgroundColor2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.addCell("Behold a cell with an image pattern:");

        Cell cell = new Cell();
        ImageData img = ImageDataFactory.create(IMG);
        cell.setNextRenderer(new TiledImageBackgroundRenderer(cell, img));
        cell.setHeight(60);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }


    private static class TiledImageBackgroundRenderer extends CellRenderer {
        protected ImageData img;

        public TiledImageBackgroundRenderer(Cell modelElement, ImageData img) {
            super(modelElement);
            this.img = img;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TiledImageBackgroundRenderer((Cell) modelElement, img);
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfCanvas canvas = drawContext.getCanvas();
            Rectangle position = getOccupiedAreaBBox();

            Image image = new Image(img);
            image.scaleToFit(10000000, position.getHeight());

            float x = position.getLeft();
            float y = position.getBottom();

            while (x + image.getImageScaledWidth() < position.getRight()) {
                image.setFixedPosition(x, y);
                canvas.addImageFittedIntoRectangle(img, new Rectangle(x, y, image.getImageScaledWidth(), image.getImageScaledHeight()), false);
                x += image.getImageScaledWidth();
            }
        }
    }
}
