/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/32130219/itext-list-of-images-in-a-cell
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ImagesInChunkInCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/images_in_chunk_in_cell.pdf";
    public static final String IMG = "./src/test/resources/img/bulb.gif";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ImagesInChunkInCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Image image = new Image(ImageDataFactory.create(IMG));
        Table table = new Table(new float[]{120});
        Paragraph listOfDots = new Paragraph();
        for (int i = 0; i < 40; i++) {
            listOfDots.add(image);
            listOfDots.add(new Text(" "));
        }
        table.addCell(listOfDots);
        doc.add(table);
        doc.close();
    }
}
