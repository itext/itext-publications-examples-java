/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class SimpleTable8 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/simple_table8.pdf";
    public static final String SRC = "./src/test/resources/pdfs/";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleTable8().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(3);
        PdfReader reader = new PdfReader(SRC + "header_test_file.pdf");
        PdfDocument srcDoc = new PdfDocument(reader);
        PdfFormXObject header = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);
        Cell cell = new Cell(1, 3).add(new Image(header).setAutoScale(true));
        table.addCell(cell);
        for (int row = 1; row <= 50; row++) {
            for (int column = 1; column <= 3; column++) {
                table.addCell(String.format("row %s, column %s", row, column));
            }
        }
        reader = new PdfReader(SRC + "footer_test_file.pdf");
        srcDoc = new PdfDocument(reader);
        PdfFormXObject footer = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);
        cell = new Cell(1, 3).add(new Image(footer).setAutoScale(true));
        table.addCell(cell);
        doc.add(table);

        doc.close();
    }
}
