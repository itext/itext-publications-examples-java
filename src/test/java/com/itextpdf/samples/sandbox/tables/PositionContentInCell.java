/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/31169268/cell-background-image-with-text-itextsharp
 */

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Leading;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class PositionContentInCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/position_content_in_cell.pdf";
    public static final String IMG = "./src/test/resources/img/info.png";

    public enum POSITION {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT}

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PositionContentInCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        // 1. Create a Document which contains a table:
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(2);
        Cell cell1 = new Cell();
        Cell cell2 = new Cell();
        Cell cell3 = new Cell();
        Cell cell4 = new Cell();
        // 2. Inside that table, make each cell with specific height:
        cell1.setHeight(50);
        cell2.setHeight(50);
        cell3.setHeight(50);
        cell4.setHeight(50);
        // 3. Each cell has the same background image
        // 4. Add text in front of the image at specific position
        cell1.setNextRenderer(new ImageAndPositionRenderer(cell1,
                new Image(ImageDataFactory.create(IMG)), "Top left", POSITION.TOP_LEFT));
        cell2.setNextRenderer(new ImageAndPositionRenderer(cell2,
                new Image(ImageDataFactory.create(IMG)), "Top right", POSITION.TOP_RIGHT));
        cell3.setNextRenderer(new ImageAndPositionRenderer(cell3,
                new Image(ImageDataFactory.create(IMG)), "Bottom left", POSITION.BOTTOM_LEFT));
        cell4.setNextRenderer(new ImageAndPositionRenderer(cell4,
                new Image(ImageDataFactory.create(IMG)), "Bottom right", POSITION.BOTTOM_RIGHT));
        // Wrap it all up!
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        doc.add(table);

        doc.close();
    }


    private class ImageAndPositionRenderer extends CellRenderer {
        private Image img;
        private String content;
        private POSITION position;

        public ImageAndPositionRenderer(Cell modelElement, Image img, String content, POSITION position) {
            super(modelElement);
            this.img = img;
            this.content = content;
            this.position = position;
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            img.scaleToFit(getOccupiedAreaBBox().getWidth(), getOccupiedAreaBBox().getHeight());

            drawContext.getCanvas().addXObject(img.getXObject(),
                    getOccupiedAreaBBox().getX() +
                            (getOccupiedAreaBBox().getWidth()
                                    - img.getImageWidth() * (float) img.getProperty(Property.HORIZONTAL_SCALING)) / 2,
                    getOccupiedAreaBBox().getY() +
                            (getOccupiedAreaBBox().getHeight()
                                    - img.getImageHeight() * (float) img.getProperty(Property.VERTICAL_SCALING)) / 2,
                    img.getImageWidth() * (float) img.getProperty(Property.HORIZONTAL_SCALING));
            drawContext.getCanvas().stroke();

            Paragraph p = new Paragraph(content);
            Leading leading = p.getDefaultProperty(Property.LEADING);
            Float defaultFontSize = new DocumentRenderer(new Document(drawContext.getDocument())).getPropertyAsFloat(Property.FONT_SIZE);

            float x;
            float y;
            TextAlignment alignment;
            switch (position) {
                case TOP_LEFT:
                    x = getOccupiedAreaBBox().getLeft() + 3;
                    y = getOccupiedAreaBBox().getTop() - defaultFontSize * leading.getValue();
                    alignment = TextAlignment.LEFT;
                    break;
                case TOP_RIGHT:
                    x = getOccupiedAreaBBox().getRight() - 3;
                    y = getOccupiedAreaBBox().getTop() - defaultFontSize * leading.getValue();
                    alignment = TextAlignment.RIGHT;
                    break;
                case BOTTOM_LEFT:
                    x = getOccupiedAreaBBox().getLeft() + 3;
                    y = getOccupiedAreaBBox().getBottom() + 3;
                    alignment = TextAlignment.LEFT;
                    break;
                case BOTTOM_RIGHT:
                    x = getOccupiedAreaBBox().getRight() - 3;
                    y = getOccupiedAreaBBox().getBottom() + 3;
                    alignment = TextAlignment.RIGHT;
                    break;
                default:
                    x = 0;
                    y = 0;
                    alignment = TextAlignment.CENTER;
            }
            new Canvas(drawContext.getCanvas(), drawContext.getDocument(), getOccupiedAreaBBox()).showTextAligned(p, x, y, alignment);
        }
    }
}
