/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/34177025
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.PatternColor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfPatternCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class TiledBackgroundColor extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/tiled_background_color.pdf";
    public static final String IMG = "./src/test/resources/img/bulb.gif";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TiledBackgroundColor().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        ImageData img = ImageDataFactory.create(IMG);
        Image image = new Image(img);
        PdfPattern.Tiling img_pattern = new PdfPattern.Tiling(image.getImageScaledWidth(), image.getImageScaledHeight());
        new PdfPatternCanvas(img_pattern, pdfDoc).addImage(img, 0, 0, false);
        Color color = new PatternColor(img_pattern);
        Table table = new Table(2);
        table.addCell(new Cell().add("Behold a cell with an image pattern:"));
        Cell cell = new Cell();
        cell.setHeight(60);
        cell.setBackgroundColor(color);
        table.addCell(cell);
        doc.add(table);
        doc.close();
    }
}
