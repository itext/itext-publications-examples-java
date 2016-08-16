/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/23730886/row-span-not-working-for-writeselectedrows-method
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class RowspanAbsolutePosition extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/rowspan_absolute_position.pdf";
    public static final String IMG = "./src/test/resources/img/berlin2013.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RowspanAbsolutePosition().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table1 = new Table(new float[]{15, 20, 20});
        table1.setWidth(555);
        Cell cell = new Cell(1, 2).add(new Paragraph("{Month}"));
        cell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        Image img = new Image(ImageDataFactory.create(IMG));
        img.scaleToFit(555f * 20f / 55f, 10000);
        Cell cell2 = new Cell(2, 1).add(img.setAutoScale(true));
        Cell cell3 = new Cell(1, 2).add(new Paragraph("Mr Fname Lname"));
        cell3.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table1.addCell(cell);
        table1.addCell(cell2);
        table1.addCell(cell3);
        doc.add(table1);

        doc.close();
    }
}
