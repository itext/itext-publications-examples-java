/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/33286841/stop-itext-table-from-spliting-on-new-page
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Splitting extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/splitting.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Splitting().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Paragraph p = new Paragraph("Test");
        Table table = new Table(2);
        for (int i = 1; i < 6; i++) {
            table.addCell("key " + i);
            table.addCell("value " + i);
        }
        for (int i = 0; i < 27; i++) {
            doc.add(p);
        }
        doc.add(table);
        for (int i = 0; i < 23; i++) {
            doc.add(p);
        }
        table = new Table(2);
        for (int i = 1; i < 6; i++) {
            table.addCell("key " + i);
            table.addCell("value " + i);
        }

        Table nesting = new Table(1);
        Cell cell = new Cell().add(table);
        cell.setBorder(Border.NO_BORDER);
        nesting.addCell(cell.setKeepTogether(true));
        doc.add(nesting);
        doc.close();
    }
}