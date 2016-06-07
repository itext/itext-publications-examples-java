/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class ColumnTextChunkImage extends GenericTest {
    public static final String DOG = "src/test/resources/img/dog.bmp";
    public static final String FOX = "src/test/resources/img/fox.bmp";
    public static final String DEST = "./target/test/resources/sandbox/objects/column_text_chunk_image.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColumnTextChunkImage().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfImageXObject dog = new PdfImageXObject(ImageDataFactory.create(DOG));
        PdfImageXObject fox = new PdfImageXObject(ImageDataFactory.create(FOX));
        Paragraph p = new Paragraph("quick brown fox jumps over the lazy dog.").
                add("Or, to say it in a more colorful way: quick brown ").
                add(new Image(fox)).
                add(" jumps over the lazy ").
                add(new Image(dog)).
                add(".").
                setMultipliedLeading(1);
        doc.add(p);

        doc.close();
    }

}
