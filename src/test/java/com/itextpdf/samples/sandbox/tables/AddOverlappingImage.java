/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22094289/itext-precisely-position-an-image-on-top-of-a-pdfptable
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TableRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class AddOverlappingImage extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/add_overlapping_image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddOverlappingImage().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(5);
        table.setNextRenderer(new OverlappingImageTableRenderer(table, new Table.RowRange(0, 25),
                ImageDataFactory.create("./src/test/resources/img/hero.jpg")));
        Cell cell;
        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 5; c++) {
                cell = new Cell();
                cell.add(new Paragraph(String.valueOf((char) r) + String.valueOf(c)));
                table.addCell(cell);
            }
        }
        doc.add(table);

        doc.close();
    }


    private class OverlappingImageTableRenderer extends TableRenderer {
        private ImageData image;

        public OverlappingImageTableRenderer(Table modelElement, Table.RowRange rowRange, ImageData img) {
            super(modelElement, rowRange);
            this.image = img;
        }

        public OverlappingImageTableRenderer(Table modelElement, ImageData img) {
            super(modelElement);
            this.image = img;
        }

        @Override
        public void drawChildren(DrawContext drawContext) {
            super.drawChildren(drawContext);
            float x = Math.max(this.getOccupiedAreaBBox().getX() +
                    this.getOccupiedAreaBBox().getWidth() / 3 - image.getWidth(), 0);
            float y = Math.max(this.getOccupiedAreaBBox().getY() +
                    this.getOccupiedAreaBBox().getHeight() / 3 - image.getHeight(), 0);
            drawContext.getCanvas().addImage(image, x, y, false);
        }

        @Override
        public OverlappingImageTableRenderer getNextRenderer() {
            return new OverlappingImageTableRenderer((Table) modelElement, image);
        }
    }
}
