/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/35356847
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class SplittingNestedTable1 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/splitting_nested_table1.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SplittingNestedTable1().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(300, 170));

        // Note that the sense of KEEP_TOGETHER property is slightly different from late splitting in itext5
        doc.add(new Paragraph("Table with setKeepTogether(true):"));
        Table table = new Table(2);
        table.setKeepTogether(true);
        table.setMarginTop(10);
        Cell cell = new Cell();
        cell.add("G");
        cell.add("R");
        cell.add("O");
        cell.add("U");
        cell.add("P");
        table.addCell(cell);
        Table inner = new Table(1);
        inner.addCell("row 1");
        inner.addCell("row 2");
        inner.addCell("row 3");
        inner.addCell("row 4");
        inner.addCell("row 5");
        cell = new Cell().add(inner);
        cell.setPadding(0);
        table.addCell(cell);
        doc.add(table);

        doc.add(new AreaBreak());

        doc.add(new Paragraph("Table with setKeepTogether(false):"));
        table.setKeepTogether(false);
        doc.add(table);

        doc.close();
    }
}