/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

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
public class CellHeights extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/cell_heights.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CellHeights().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A5).rotate());

        Table table = new Table(2);
        // a long phrase with newlines
        Paragraph p = new Paragraph("Dr. iText or:\nHow I Learned to Stop Worrying\nand Love PDF.");
        Cell cell = new Cell().add(p);
        // the phrase fits the fixed height
        table.addCell("fixed height (more than sufficient)");
        cell.setHeight(72f);
        table.addCell(cell.clone(true));
        // the phrase doesn't fit the fixed height
        table.addCell("fixed height (not sufficient)");
        // TODO DEVSIX-555
        cell.setHeight(36f);
        table.addCell(cell.clone(true));
        // The minimum height is exceeded
        table.addCell("minimum height");
        cell = new Cell().add("Dr. iText");
        cell.setHeight(36f);
        table.addCell(cell);

        doc.add(table);
        doc.close();
    }
}
