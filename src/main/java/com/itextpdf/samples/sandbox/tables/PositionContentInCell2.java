package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class PositionContentInCell2 {
    public static final String DEST = "./target/sandbox/tables/position_content_in_cell2.pdf";

    public static final String IMG = "./src/main/resources/img/info.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PositionContentInCell2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {

        // 1. Create a Document which contains a table:
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        Cell cell1 = new Cell();
        Cell cell2 = new Cell();
        Cell cell3 = new Cell();
        Cell cell4 = new Cell();
        Cell cell5 = new Cell();
        Cell cell6 = new Cell();
        Cell cell7 = new Cell();
        Cell cell8 = new Cell();

        // 2. Inside that table, make each cell with specific height:
        cell1.setHeight(50);
        cell2.setHeight(50);
        cell3.setHeight(50);
        cell4.setHeight(50);
        cell5.setHeight(50);
        cell6.setHeight(50);
        cell7.setHeight(50);
        cell8.setHeight(50);

        Image img = new Image(ImageDataFactory.create(IMG));

        // 3. Each cell has the same background image
        // 4. Add text in front of the image at specific position
        cell1.setNextRenderer(new ImageAndPositionRenderer(cell1, 0, 1,
                img, "Top left", TextAlignment.LEFT));
        cell2.setNextRenderer(new ImageAndPositionRenderer(cell2, 1, 1,
                img, "Top right", TextAlignment.RIGHT));
        cell3.setNextRenderer(new ImageAndPositionRenderer(cell3, 0.5f, 1,
                img, "Top center", TextAlignment.CENTER));
        cell4.setNextRenderer(new ImageAndPositionRenderer(cell4, 0.5f, 0,
                img, "Bottom center", TextAlignment.CENTER));
        cell5.setNextRenderer(new ImageAndPositionRenderer(cell5, 0.5f, 0.5f,
                img, "Middle center", TextAlignment.CENTER));
        cell6.setNextRenderer(new ImageAndPositionRenderer(cell6, 0.5f, 0.5f,
                img, "Middle center", TextAlignment.CENTER));
        cell7.setNextRenderer(new ImageAndPositionRenderer(cell7, 0, 0,
                img, "Bottom left", TextAlignment.LEFT));
        cell8.setNextRenderer(new ImageAndPositionRenderer(cell8, 1, 0,
                img, "Bottom right", TextAlignment.RIGHT));

        // Wrap it all up!
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        table.addCell(cell7);
        table.addCell(cell8);
        doc.add(table);

        doc.close();
    }


    private static class ImageAndPositionRenderer extends CellRenderer {
        private Image img;
        private String content;
        private TextAlignment alignment;
        private float wPct;
        private float hPct;

        public ImageAndPositionRenderer(Cell modelElement, float wPct, float hPct,
                                        Image img, String content, TextAlignment alignment) {
            super(modelElement);
            this.img = img;
            this.content = content;
            this.alignment = alignment;
            this.wPct = wPct;
            this.hPct = hPct;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new ImageAndPositionRenderer((Cell) modelElement, wPct, hPct, img, content, alignment);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            drawContext.getCanvas().addXObjectFittedIntoRectangle(img.getXObject(), getOccupiedAreaBBox());
            drawContext.getCanvas().stroke();

            UnitValue fontSizeUV = getPropertyAsUnitValue(Property.FONT_SIZE);
            float x = getOccupiedAreaBBox().getX() + wPct * getOccupiedAreaBBox().getWidth();
            float y = getOccupiedAreaBBox().getY() + hPct * (getOccupiedAreaBBox().getHeight()
                    - (fontSizeUV.isPointValue() ? fontSizeUV.getValue() : 12f) * 1.5f);
            new Canvas(drawContext.getDocument().getFirstPage(), drawContext.getDocument().getDefaultPageSize())
                    .showTextAligned(content, x, y, alignment);
        }
    }
}
