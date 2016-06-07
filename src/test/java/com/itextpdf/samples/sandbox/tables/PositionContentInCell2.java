/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class PositionContentInCell2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/position_content_in_cell2.pdf";
    public static final String IMG = "./src/test/resources/img/info.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PositionContentInCell2().manipulatePdf(DEST);
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
        // 3. Each cell has the same background image
        // 4. Add text in front of the image at specific position
        cell1.setNextRenderer(new ImageAndPositionRenderer(cell1, 0, 1,
                new Image(ImageDataFactory.create(IMG)), "Top left", TextAlignment.LEFT));
        cell2.setNextRenderer(new ImageAndPositionRenderer(cell2, 1, 1,
                new Image(ImageDataFactory.create(IMG)), "Top right", TextAlignment.RIGHT));
        cell3.setNextRenderer(new ImageAndPositionRenderer(cell3, 0.5f, 1,
                new Image(ImageDataFactory.create(IMG)), "Top center", TextAlignment.CENTER));
        cell4.setNextRenderer(new ImageAndPositionRenderer(cell4, 0.5f, 0,
                new Image(ImageDataFactory.create(IMG)), "Bottom center", TextAlignment.CENTER));
        cell5.setNextRenderer(new ImageAndPositionRenderer(cell5, 0.5f, 0.5f,
                new Image(ImageDataFactory.create(IMG)), "Middle center", TextAlignment.CENTER));
        cell6.setNextRenderer(new ImageAndPositionRenderer(cell6, 0.5f, 0.5f,
                new Image(ImageDataFactory.create(IMG)), "Middle center", TextAlignment.CENTER));
        cell7.setNextRenderer(new ImageAndPositionRenderer(cell7, 0, 0,
                new Image(ImageDataFactory.create(IMG)), "Bottom left", TextAlignment.LEFT));
        cell8.setNextRenderer(new ImageAndPositionRenderer(cell8, 1, 0,
                new Image(ImageDataFactory.create(IMG)), "Bottom right", TextAlignment.RIGHT));
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


    private class ImageAndPositionRenderer extends CellRenderer {
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

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);

            drawContext.getCanvas().addXObject(img.getXObject(), getOccupiedAreaBBox());
            drawContext.getCanvas().stroke();

            float x = getOccupiedAreaBBox().getX() + wPct * getOccupiedAreaBBox().getWidth();
            float y = getOccupiedAreaBBox().getY() + hPct * (getOccupiedAreaBBox().getHeight() - drawContext.getCanvas().getGraphicsState().getLeading());
            new Document(drawContext.getDocument()).showTextAligned(content, x, y, alignment);

        }
    }
}
