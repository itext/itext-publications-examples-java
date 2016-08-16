/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/26628678/manipulating-witdh-and-height-of-rows-and-cells-in-itextsharp
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ColumnWidthExample extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/column_width_example.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColumnWidthExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        float[] columnWidths = {1, 5, 5};
        Table table = new Table(columnWidths);
        table.setWidthPercent(100);
        PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
        Cell cell = new Cell(1, 3)
                .add(new Paragraph("This is a header"))
                .setFont(f)
                .setFontSize(13)
                .setFontColor(DeviceGray.WHITE)
                .setBackgroundColor(DeviceGray.BLACK)
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(cell);
        for (int i = 0; i < 2; i++) {
            Cell[] headerFooter = new Cell[]{
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add("#"),
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add("Key"),
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add("Value")
            };
            for (Cell hfCell : headerFooter) {
                if (i == 0) {
                    table.addHeaderCell(hfCell);
                } else {
                    table.addFooterCell(hfCell);
                }
            }
        }
        for (int counter = 1; counter < 101; counter++) {
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(String.valueOf(counter)));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add("key " + counter));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add("value " + counter));
        }
        doc.add(table);
        doc.close();
    }
}
