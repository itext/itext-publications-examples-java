/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written in answer to:
 * http://stackoverflow.com/questions/42440133/itext-cell-with-image-doesnt-apply-rowspan
 */

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ImageRowspan extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/image_rowspan.pdf";
    public static final String IMG = "./src/test/resources/img/bruno.jpg";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ImageRowspan().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        table.addCell(new Cell(2, 1).add(new Image(ImageDataFactory.create(IMG)).setWidth(UnitValue.createPercentValue(100))));
        table.addCell("1");
        table.addCell("2");
        doc.add(table);

        doc.close();
    }
}
