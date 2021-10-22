package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
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
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class WatermarkedImages2 {
    public static final String DEST = "./target/sandbox/images/watermarked_images2.pdf";

    public static final String IMAGE1 = "./src/main/resources/img/bruno.jpg";
    public static final String IMAGE2 = "./src/main/resources/img/dog.bmp";
    public static final String IMAGE3 = "./src/main/resources/img/fox.bmp";
    public static final String IMAGE4 = "./src/main/resources/img/bruno_ingeborg.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new WatermarkedImages2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(1).useAllAvailableWidth();

        Image image = new Image(ImageDataFactory.create(IMAGE1)).setAutoScaleWidth(true);
        Cell cell = new Cell().add(image);
        cell.setNextRenderer(new WatermarkedCellRenderer(cell, "Bruno"));
        table.addCell(cell);

        image = new Image(ImageDataFactory.create(IMAGE2)).setAutoScaleWidth(true);
        cell = new Cell().add(image);
        cell.setNextRenderer(new WatermarkedCellRenderer(cell, "Dog"));
        table.addCell(cell);

        image = new Image(ImageDataFactory.create(IMAGE3)).setAutoScaleWidth(true);
        cell = new Cell().add(image);
        cell.setNextRenderer(new WatermarkedCellRenderer(cell, "Fox"));
        table.addCell(cell);

        image = new Image(ImageDataFactory.create(IMAGE4)).setAutoScaleWidth(true);
        cell = new Cell().add(image);
        cell.setNextRenderer(new WatermarkedCellRenderer(cell, "Bruno and Ingeborg"));
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }


    private static class WatermarkedCellRenderer extends CellRenderer {
        private String content;

        public WatermarkedCellRenderer(Cell modelElement, String content) {
            super(modelElement);
            this.content = content;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new WatermarkedCellRenderer((Cell) modelElement, content);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Paragraph p = new Paragraph(content).setFontColor(ColorConstants.WHITE);
            Rectangle rect = getOccupiedAreaBBox();
            float coordX = (rect.getLeft() + rect.getRight()) / 2;
            float coordY = (rect.getBottom() + rect.getTop()) / 2;
            float angle = (float) Math.PI / 6;
            new Canvas(drawContext.getCanvas(), rect)
                    .showTextAligned(p, coordX, coordY, getOccupiedArea().getPageNumber(),
                            TextAlignment.CENTER, VerticalAlignment.MIDDLE, angle)
                    .close();
        }
    }
}
