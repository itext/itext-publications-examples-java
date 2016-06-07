/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/34303448
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class MultipleImagesInTable extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/tables/multiple_images_in_table.pdf";
    public static final String IMG1
            = "./src/test/resources/img/brasil.png";
    public static final String IMG2
            = "./src/test/resources/img/dog.bmp";
    public static final String IMG3
            = "./src/test/resources/img/fox.bmp";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MultipleImagesInTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image img1 = new Image(ImageDataFactory.create(IMG1));
        Image img2 = new Image(ImageDataFactory.create(IMG2));
        Image img3 = new Image(ImageDataFactory.create(IMG3));

        Table table = new Table(1);
        table.setWidthPercent(20);

        img1.setAutoScale(true);
        img2.setAutoScale(true);
        img3.setAutoScale(true);

        table.addCell(img1);
        table.addCell("Brazil");
        table.addCell(img2);
        table.addCell("Dog");
        table.addCell(img3);
        table.addCell("Fox");

        doc.add(table);

        doc.close();
    }
}
