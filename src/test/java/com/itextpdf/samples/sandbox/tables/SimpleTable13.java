/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/34480476
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class SimpleTable13 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/simple_table13.pdf";
    public static final String[][] DATA = {
            {"John Edward Jr.", "AAA"},
            {"Pascal Einstein W. Alfi", "BBB"},
            {"St. John", "CCC"}
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleTable13().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(new float[]{5, 1});
        table.setWidthPercent(50);
        table.setTextAlignment(TextAlignment.LEFT);
        table.addCell(new Cell().add("Name: " + DATA[0][0]).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(DATA[0][1]).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("Surname: " + DATA[1][0]).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(DATA[1][1]).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("School: " + DATA[2][0]).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(DATA[1][1]).setBorder(Border.NO_BORDER));
        doc.add(table);
        doc.close();
    }
}
