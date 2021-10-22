package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.PatternColor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfPatternCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.BoxSizingPropertyValue;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class TiledBackground {
    public static final String DEST = "./target/sandbox/tables/tiled_background.pdf";

    public static final String IMG1 = "./src/main/resources/img/ALxRF.png";

    public static final String IMG2 = "./src/main/resources/img/bulb.gif";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TiledBackground().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        Cell cell = new Cell();
        ImageData image = ImageDataFactory.create(IMG1);
        cell.setNextRenderer(new TiledImageBackgroundCellRenderer(cell, image));
        cell.setProperty(Property.BOX_SIZING, BoxSizingPropertyValue.BORDER_BOX);
        cell.setHeight(770).setBorder(Border.NO_BORDER);
        table.addCell(cell);

        cell = new Cell();
        image = ImageDataFactory.create(IMG2);
        cell.setNextRenderer(new TiledImageBackgroundCellRenderer(cell, image));
        cell.setProperty(Property.BOX_SIZING, BoxSizingPropertyValue.BORDER_BOX);
        cell.setHeight(770).setBorder(Border.NO_BORDER);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }


    private static class TiledImageBackgroundCellRenderer extends CellRenderer {
        protected ImageData img;

        public TiledImageBackgroundCellRenderer(Cell modelElement, ImageData img) {
            super(modelElement);
            this.img = img;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TiledImageBackgroundCellRenderer((Cell) modelElement, img);
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfPattern.Tiling imgPattern = new PdfPattern.Tiling(img.getWidth(), img.getHeight(), img.getWidth(),
                    img.getHeight());

            PdfPatternCanvas patternCanvas = new PdfPatternCanvas(imgPattern, drawContext.getDocument());
            patternCanvas.addImageAt(img, 0, 0, false);

            PdfCanvas canvas = drawContext.getCanvas();

            canvas.saveState();

            colorRectangle(canvas, new PatternColor(imgPattern), getOccupiedAreaBBox().getX(),
                    getOccupiedAreaBBox().getY(), getOccupiedAreaBBox().getWidth(), getOccupiedAreaBBox().getHeight());

            canvas.setFillColor(new PatternColor(imgPattern));
            canvas.stroke();

            canvas.restoreState();
        }

        private static void colorRectangle(PdfCanvas canvas, Color color, float x, float y, float width, float height) {
            canvas
                    .saveState()
                    .setFillColor(color)
                    .rectangle(x, y, width, height)
                    .fillStroke()
                    .restoreState();
        }
    }
}
