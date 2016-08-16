/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/28610545/get-page-number-in-itext
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class SimpleTable5 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/simple_table5.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleTable5().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        Table table = new Table(5);
        Cell cell = new Cell(1, 5).add("Table XYZ (Continued)");
        table.addHeaderCell(cell);
        cell = new Cell(1, 5).add("Continue on next page");
        table.addFooterCell(cell);
        table.setSkipFirstHeader(true);
        table.setSkipLastFooter(true);
        for (int i = 0; i < 350; i++) {
            table.addCell(String.valueOf(i + 1));
        }

        doc.add(table);

        doc.close();
    }
}
