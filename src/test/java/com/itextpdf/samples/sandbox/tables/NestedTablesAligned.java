/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class NestedTablesAligned extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/nested_tables_aligned.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new NestedTablesAligned().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        float[] columnWidths = {200f, 200f, 200f};
        Table table = new Table(columnWidths);
        buildNestedTables(table);
        doc.add(table);

        doc.close();
    }

    private void buildNestedTables(Table outerTable) {
        Table innerTable1 = new Table(1);
        innerTable1.setWidth(100f);
        innerTable1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        innerTable1.addCell("Cell 1");
        innerTable1.addCell("Cell 2");
        outerTable.addCell(innerTable1);
        Table innerTable2 = new Table(2);
        innerTable2.setWidth(100f);
        innerTable2.setHorizontalAlignment(HorizontalAlignment.CENTER);
        innerTable2.addCell("Cell 3");
        innerTable2.addCell("Cell 4");
        outerTable.addCell(innerTable2);
        Table innerTable3 = new Table(2);
        innerTable3.setWidth(100f);
        innerTable3.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        innerTable3.addCell("Cell 5");
        innerTable3.addCell("Cell 6");
        outerTable.addCell(innerTable3);
    }
}