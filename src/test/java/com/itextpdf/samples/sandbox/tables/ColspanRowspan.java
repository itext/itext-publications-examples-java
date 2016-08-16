/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23989852/itext-combining-rowspan-and-colspan-pdfptable
 */
package com.itextpdf.samples.sandbox.tables;

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
public class ColspanRowspan extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/colspan_rowspan.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColspanRowspan().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(4);
        Cell cell = new Cell().add(" 1,1 ");
        table.addCell(cell);
        cell = new Cell().add(" 1,2 ");
        table.addCell(cell);
        Cell cell23 = new Cell(2, 2).add("multi 1,3 and 1,4");
        table.addCell(cell23);
        cell = new Cell().add(" 2,1 ");
        table.addCell(cell);
        cell = new Cell().add(" 2,2 ");
        table.addCell(cell);
        doc.add(table);

        doc.close();
    }
}
