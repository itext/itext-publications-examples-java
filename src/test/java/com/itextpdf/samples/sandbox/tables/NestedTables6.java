/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/37548146
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class NestedTables6 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/nested_tables6.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new NestedTables6().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(1200, 800));

        // Header part
        Table mainTable = new Table(1);
        mainTable.setWidth(1000);

        // Notice that in itext7 there is no getDefaultCell method
        // and you should set paddings, margins and other properties exactly on the element
        // you want to handle them

        Table subTable2 = new Table(new float[]{200, 100, 200, 200, 300});
        subTable2.addCell("test 1");
        subTable2.addCell("test 2");
        subTable2.addCell("test 3");
        subTable2.addCell("test 4");
        subTable2.addCell("test 5");
        Cell cell2 = new Cell().add(subTable2);
        cell2.setPadding(0);
        mainTable.addCell(cell2);

        doc.add(mainTable);

        doc.close();
    }
}
