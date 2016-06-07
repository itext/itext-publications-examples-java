/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie and Nishanthi Grashia in answer to the following question:
 * http://stackoverflow.com/questions/33440294/create-table-in-itext-pdf-in-java
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class RightCornerTable extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/right_corner_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RightCornerTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(300, 300));
        doc.setMargins(0, 0, 0, 0);

        Table table = new Table(1);
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        table.setWidth(90);
        Cell cell = new Cell().add(new Paragraph(" Date").setFontColor(Color.WHITE));
        cell.setBackgroundColor(Color.BLACK);
        cell.setBorder(new SolidBorder(Color.GRAY, 2));
        table.addCell(cell);
        Cell cellTwo = new Cell().add(("10/01/2015"));
        cellTwo.setBorder(new SolidBorder(2));
        table.addCell(cellTwo);
        doc.add(table);
        doc.add(new AreaBreak());
        doc.add(table);

        doc.close();
    }
}
