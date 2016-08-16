/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following questions:
 * http://stackoverflow.com/questions/28503491/large-table-in-table-cell-invoke-page-break
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class NestedTables2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/nested_tables2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new NestedTables2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(new float[]{1, 15});
        table.setWidthPercent(100);
        for (int i = 1; i <= 20; i++) {
            table.addCell(String.valueOf(i));
            table.addCell("It is not smart to use iText 2.1.7!");
        }
        Table innertable = new Table(new float[]{1, 15});
        innertable.setWidthPercent(100);
        for (int i = 0; i < 90; i++) {
            innertable.addCell(String.valueOf(i + 1));
            innertable.addCell("Upgrade if you're a professional developer!");
        }
        table.addCell("21");
        table.addCell(innertable);
        for (int i = 22; i <= 40; i++) {
            table.addCell(String.valueOf(i));
            table.addCell("It is not smart to use iText 2.1.7!");
        }
        doc.add(table);

        doc.close();
    }
}
