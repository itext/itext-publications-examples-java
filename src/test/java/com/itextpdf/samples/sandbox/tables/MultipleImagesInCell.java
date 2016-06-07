/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/33603296/how-to-add-two-images-in-one-cell-in-itext
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class MultipleImagesInCell extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/tables/multiple_images_in_cell.pdf";
    public static final String IMG1
            = "./src/test/resources/img/brasil.png";
    public static final String IMG2
            = "./src/test/resources/img/dog.bmp";
    public static final String IMG3
            = "./src/test/resources/img/fox.bmp";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MultipleImagesInCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image img1 = new Image(ImageDataFactory.create(IMG1));
        Image img2 = new Image(ImageDataFactory.create(IMG2));
        Image img3 = new Image(ImageDataFactory.create(IMG3));

        Table table = new Table(1);
        table.setWidthPercent(50);
        table.addCell("Different images, one after the other vertically:");
        Cell cell = new Cell();

        // There's no image autoscaling by default
        cell.add(img1.setAutoScale(true));
        cell.add(img2.setAutoScale(true));
        cell.add(img3.setAutoScale(true));
        table.addCell(cell);
        doc.add(table);
        doc.add(new AreaBreak());

        // In the snippet after this autoscaling is not needed
        // Notice that we do not need to create new Image instances since the images had been already flushed
        img1.setAutoScale(false);
        img2.setAutoScale(false);
        img3.setAutoScale(false);

        table = new Table(1);
        table.addCell("Different images, one after the other vertically, but scaled:");
        cell = new Cell();
        img1.setWidthPercent(20);
        cell.add(img1);
        img2.setWidthPercent(20);
        cell.add(img2);
        img3.setWidthPercent(20);
        cell.add(img3);
        table.addCell(cell);
        table.addCell("Different images, one after the other horizontally:");

        // Notice that the table is not flushed yet so it's strictly forbidden to change image properties yet
        img1 = new Image(ImageDataFactory.create(IMG1));
        img2 = new Image(ImageDataFactory.create(IMG2));
        img3 = new Image(ImageDataFactory.create(IMG3));

        Paragraph p = new Paragraph();
        img1.scale(0.3f, 0.3f);
        p.add(img1);
        p.add(img2);
        p.add(img3);
        table.addCell(p);
        table.addCell("Text and images (mixed):");

        img2 = new Image(ImageDataFactory.create(IMG2));
        img3 = new Image(ImageDataFactory.create(IMG3));

        p = new Paragraph("The quick brown ");
        p.add(img3);
        p.add(" jumps over the lazy ");
        p.add(img2);
        cell = new Cell();
        cell.add(p);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
