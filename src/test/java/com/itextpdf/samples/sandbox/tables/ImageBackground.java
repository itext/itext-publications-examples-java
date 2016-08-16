/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24162974/how-to-set-background-image-in-pdfpcell-in-itext
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ImageBackground extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/image_background.pdf";
    public static final String IMG = "./src/test/resources/img/bruno.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ImageBackground().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(1);
        table.setWidth(400);
        Cell cell = new Cell();
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        Paragraph p = new Paragraph("A cell with an image as background color.")
                .setFont(font)
                .setFontColor(DeviceGray.WHITE);
        cell.add(p);
        Image img = new Image(ImageDataFactory.create(IMG));
        cell.setNextRenderer(new ImageBackgroundCellRenderer(cell, img));
        cell.setHeight(600 * img.getImageHeight() / img.getImageWidth());
        table.addCell(cell);
        doc.add(table);

        doc.close();
    }


    private class ImageBackgroundCellRenderer extends CellRenderer {
        protected Image img;

        public ImageBackgroundCellRenderer(Cell modelElement, Image img) {
            super(modelElement);
            this.img = img;
        }

        @Override
        public void draw(DrawContext drawContext) {
            img.scaleToFit(getOccupiedAreaBBox().getWidth(), getOccupiedAreaBBox().getHeight());
            drawContext.getCanvas().addXObject(img.getXObject(), getOccupiedAreaBBox());
            super.draw(drawContext);
        }
    }
}
