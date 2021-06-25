package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class AddOverlappingImage {
    public static final String DEST = "./target/sandbox/tables/add_overlapping_image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddOverlappingImage().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // By default column width is calculated automatically for the best fit.
        // useAllAvailableWidth() method makes table use the whole page's width while placing the content.
        Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();

        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 5; c++) {
                Cell cell = new Cell();
                cell.add(new Paragraph(((char) r) + String.valueOf(c)));
                table.addCell(cell);
            }
        }

        // Adds drawn on a canvas image to the table
        table.setNextRenderer(new OverlappingImageTableRenderer(table,
                ImageDataFactory.create("./src/main/resources/img/hero.jpg")));

        doc.add(table);

        doc.close();
    }


    private static class OverlappingImageTableRenderer extends TableRenderer {
        private ImageData image;

        public OverlappingImageTableRenderer(Table modelElement, ImageData img) {
            super(modelElement);
            this.image = img;
        }

        @Override
        public void drawChildren(DrawContext drawContext) {

            // Use the coordinates of the cell in the fourth row and the second column to draw the image
            Rectangle rect = rows.get(3)[1].getOccupiedAreaBBox();
            super.drawChildren(drawContext);

            drawContext.getCanvas().addImageAt(image, rect.getLeft() + 10, rect.getTop() - image.getHeight(), false);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new OverlappingImageTableRenderer((Table) modelElement, image);
        }
    }
}
